package org.thingswithworth.metamusic.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.android.gm.api.model.Song;
import org.thingswithworth.metamusic.Callback;
import org.thingswithworth.metamusic.MusicApplication;
import org.thingswithworth.metamusic.R;
import org.thingswithworth.metamusic.activities.factories.DialogFactory;
import org.thingswithworth.metamusic.model.ChartGenerator;
import org.thingswithworth.metamusic.net.GoogleMusic;
import org.thingswithworth.metamusic.net.SocialShare;
import org.achartengine.GraphicalView;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * The home activity of the app. Display a tab widget to select some charts or transition to listening to music. Also handles sharing chart images.
 * @author Andrey Kurenkov, Alex Bettadapur
 * @Version 2.1.0
 * TODO: consider moving to ViewPager+Fragments
 * TODO: clean up code
 */
public class BrowseChartsActivity extends AppActivity implements View.OnClickListener, Callback, AdapterView.OnItemClickListener {
    private TextView collectionInfo;
    private AlertDialog dialog;
    private GraphicalView mChart;
    private Button gMusicButton;
    private static boolean gotWeb, showedToast;
    private ListView listView;
    private ListView statsView;
    private ArrayAdapter<String> statsAdapter;
    private TabHost tabHost;
    private final ParamCallback<String> queryCallback;

    {
        queryCallback=new ParamCallback<String>() {
            @Override
            public void onCompletion(String... param) {
                startQuery(param[0],param[1]);
            }
        };
    }

    /**
     * Gives values to instance variables and sets up view of Activity.
     * @param savedInstanceState the last shown tab is stored in this
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_charts_activity);

        gMusicButton = (Button)findViewById(R.id.googleMusicButton);
        collectionInfo=(TextView) findViewById(R.id.collection_info);
        gMusicButton.setOnClickListener(this);

        listView = new ListView(this);
        listView.setOnItemClickListener(this);
        populateListView();

        statsView = new ListView(this);

        tabHost=(TabHost)findViewById(android.R.id.tabhost);
        tabHost.setup();
        //TODO: constants?
        String[] tabTags={"Pie Graph","Bar Graph","Line Graph","Most Played","All Artists","Stats"};
        String[] tabNames={"Genre %","Genre #","Song Years","Most Played","All Artists","Statistics"};
        TabFactory tabFactory=new TabFactory();
        for(int i=0;i<tabTags.length;i++){
            TabHost.TabSpec tab=tabHost.newTabSpec(tabTags[i]);
            //TODO: its possible to set content to be intents instead of a Factory
            //In the long term it is better/more flexible to have these be activities
            tab.setContent(tabFactory);
            tab.setIndicator(tabNames[i]);
            tabHost.addTab(tab);
        }
        if (savedInstanceState != null) {
            tabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }
        collectionInfo.setText("Displaying info from "+MusicApplication.getGlobalCollection().getSongs().size()+" songs.");

        if(!showedToast){
            showedToast=true;
            //TODO make into legit "intro" dialogs
            Toast.makeText(this,"Click on any genre for more detail.", Toast.LENGTH_LONG);
        }


        boolean intro= MusicApplication.preferences.getBoolean("showIntro", true);
        if(intro){
            DialogFactory.getIntroDialog(this).show();
            MusicApplication.preferences.edit().putBoolean("showIntro",false).commit();
        }
    }

    /**
     * If preferences were changed at some point, recreate to recreate tabs.
     */
    @Override
    public void onStart(){
        super.onStart();
        if(PreferencesActivity.wasLaunched()){
            PreferencesActivity.resetLaunched();
            recreate();
        }
    }

    /**
     * Saves the shown tab to "tab" key
     * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
     */
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", tabHost.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
    }

    /**
     * The factory that generates the charts and lists for each tab as appropriate
     * @author Andrey Kurenkov
     */
    private class TabFactory implements TabHost.TabContentFactory {


        /** (non-Javadoc)
         * @see android.widget.TabHost.TabContentFactory#createTabContent(String)
         */
        public View createTabContent(String tag) {
            if(MusicApplication.getGlobalCollection().getSongs().size()==0){
                TextView view=new TextView(BrowseChartsActivity.this);
                view.setText("No songs found.");
                view.setGravity(Gravity.CENTER);
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP,30);
                return view;
            }
            if(tag.equals("Pie Graph")) {
                mChart=ChartGenerator.getPieChart(BrowseChartsActivity.this);
            }  else  if(tag.equals("Bar Graph")) {
                mChart=ChartGenerator.getBarChart(BrowseChartsActivity.this, false);
            }  else  if(tag.equals("Line Graph")) {
                mChart=ChartGenerator.getYearLineChart(BrowseChartsActivity.this);
            }  else  if(tag.equals("Most Played")) {
                if(gotWeb)
                    mChart=ChartGenerator.getBarChart(BrowseChartsActivity.this, true);
                else{
                    TextView response=new TextView(BrowseChartsActivity.this);
                    response.setTextSize(30);
                    response.setText("This requires information from Google Music.");
                    response.setGravity(Gravity.CENTER);
                    return response;
                }
            }
            if(tag.equals("All Artists"))
            {
                populateListView();
                return (listView);
            }
            else if(tag.equals("Stats"))
            {
                populateStatsView();
                return statsView;
            }
            else
            {
                return mChart;
            }
        }
    }

    /**
     * Fills up the list view of all artists.
     * //TODO: makes fancier or expandable list view as in search?
     * @author Alex Bettadapur
     */
    private void populateListView() {
        Collection<String> artists = MusicApplication.getGlobalCollection().getArtistNames();
        String[] stringarr = Arrays.copyOf(artists.toArray(), artists.size(), String[].class);
        Arrays.sort(stringarr);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringarr);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }

    /**
     * Handles launching the ArtistActivity for the Artists ListView
     * (non-Javadoc)
     * @see android.widget.ListView.OnItemClickListener#onItemClick(AdapterView, View, int, long)
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        String artist = (String)adapterView.getItemAtPosition(i);
        Intent intent = new Intent(this, ArtistActivity.class);
        intent.putExtra("artist", artist);
        startActivity(intent);
    }

    /**
     * Fills up the list view of statistics tab
     * //TODO: more statistics?
     * @author Alex Bettadapur
     */
    private void populateStatsView()
    {
        long totalmilllis=0;
        int millisCounter=0;
        List<Song> songs = MusicApplication.getGlobalCollection().getSongs();
        HashSet<String> filteredArtists = new HashSet<String>();
        HashSet<String> filteredAlbums = new HashSet<String>();
        HashSet<String> filteredGenres = new HashSet<String>();
        for(Song s:songs)
        {
            filteredAlbums.add(s.getAlbum());
            filteredArtists.add(s.getArtist());
            filteredGenres.add(s.getGenre());

            long millis = s.getDurationMillis();
            if(millis>0)
            {
                totalmilllis+=millis;
                millisCounter++;
            }

        }
        long averagemillis = totalmilllis/millisCounter;
        long seconds = averagemillis/1000;
        int minutes =  (int)seconds/60;
        int modSeconds = (int)seconds%60;
        String[] stringarr = {"Number of Artists: "+filteredArtists.size(),"Number of Albums "+filteredAlbums.size(),"Number of Genres: "+filteredGenres.size(), "Number of Songs: "+songs.size(),"Average Song Length: "+minutes+":"+modSeconds};
        //Arrays.sort(stringarr);
        statsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringarr);
        statsAdapter.notifyDataSetChanged();
        statsView.setAdapter(statsAdapter);

    }

    /**
     * Handles the Google Music button click - checks for wifi, and if connected allows to retrieve data
     * @see View.OnClickListener#onClick(View)
     */
    @Override
    public void onClick(View view)
    {
        if(MusicApplication.isConnectedToWifi())
            DialogFactory.createLoginDialog(this,queryCallback).show();
        else{
            Toast toast=Toast.makeText(this, "Must be connected to wifi.", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /**
     * Starts Google Music query
     * @param email The email of the Google Music account
     * @param password The password for the Google Music Account
     */
    public void startQuery(String email, String password)
    {
        dialog = DialogFactory.getProgressDialog("Loading", "Querying Google Music", this, true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        GoogleMusic.retrieveAllSongs(this, email, password,this);
    }

    /**
     * Callback from Google Music query. Checks for wrong input or if successful adds new Songs to global collection.
     */
    @Override
    public void onCompletion() {
        if(GoogleMusic.getHadError()) {
            String log = GoogleMusic.getErrorLog();

            Toast toast=Toast.makeText(this, log, Toast.LENGTH_LONG);
            toast.show();
            //TODO: constants?
            if(log.equals("The input login information is invalid."))
            {
                dialog.hide();
                DialogFactory.createLoginDialog(this,queryCallback).show();

            }
            else if(log.equals("We could not find any google music songs on this account"))
            {
                dialog.hide();
            }
        }
        else {
            gotWeb = true;
            dialog.hide();
            dialog.cancel();
            gMusicButton.setEnabled(false);
            MusicApplication.getGlobalCollection().addSongs(GoogleMusic.getSongsLastRetrieved());
            MusicApplication.getGlobalCollection().cleanGenreCount();
            collectionInfo.setText("Displaying info from "+MusicApplication.getGlobalCollection().getSongs().size()+" songs.");
            recreate(); //recreate to remake tabs
        }
    }

    /**
     * Additional handling for share button, otherwise same as all other activities.
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.menu_item_share){
            if(tabHost.getCurrentTabTag().equals("Stats"))
            {
                StringBuilder text = new StringBuilder();
                text.append(statsAdapter.getItem(0)+"\n");
                text.append(statsAdapter.getItem(1)+"\n");
                text.append(statsAdapter.getItem(2)+"\n");
                text.append(statsAdapter.getItem(3)+"\n");
                text.append(statsAdapter.getItem(4)+"\n");
                SocialShare.sendText(this, text.toString());
            }
            else
            {
                if(mChart!=null){
                    Bitmap toShare=mChart.toBitmap();
                    Canvas c = new Canvas (toShare);
                    Paint paint=new Paint();
                    paint.setColor(Color.WHITE);
                    paint.setTextSize(25);
                    paint.setUnderlineText(true);
                    paint.setTextAlign(Paint.Align.CENTER);
                    c.drawText("MetaMusic",toShare.getWidth()/2,toShare.getHeight()-25,paint);


                    paint=new Paint();
                    paint.setColor(Color.GRAY);
                    paint.setTextSize(15);
                    paint.setTextAlign(Paint.Align.CENTER);
                    c.drawText("Android App by ThingsWithWorth",toShare.getWidth()/2,toShare.getHeight()-8,paint);
                    SocialShare.sendImage(this,toShare);
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflating the current activity's menu with res/menu/items.xml
        getMenuInflater().inflate(R.menu.main_opt_menu, menu);
        return true;

    }
}
