package org.thingswithworth.metamusic.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import org.thingswithworth.metamusic.R;
import org.thingswithworth.metamusic.activities.adapters.SearchListAdapter;

/**
 * Activity to allow the user to search their collection. Displays an EditText at the bottom an ExpandableList below with the results.
 * @author Andrey Kurenkov
 * @see org.thingswithworth.metamusic.activities.adapters.SearchListAdapter
 */
public class SearchActivity extends android.app.Activity {
    /**
     * Gives values to instance variables and sets up view of Activity.
     * @param savedInstanceState the last shown tab is stored in this
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        EditText searchText=(EditText)findViewById(R.id.searchText);
        ExpandableListView resultsLists =(ExpandableListView)findViewById(R.id.searchResultsView);


        TextView header=new TextView(this);
        header.setText("Search Results");
        header.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
        header.setGravity(Gravity.CENTER);
        resultsLists.addHeaderView(header, null, false);

        resultsLists.setAdapter(new SearchListAdapter(this,searchText));
    }
}