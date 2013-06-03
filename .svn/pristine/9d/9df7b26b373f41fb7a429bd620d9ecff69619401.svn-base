package org.thingswithworth.metamusic.activities;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import org.thingswithworth.metamusic.R;
/**
 * This is a top-level class to handle commonalities between most AppActivities. It currently set up the action bar items for most activities (palyer, search, and settings)
 * @author: Andrey Kurenkov
 */
public abstract class AppActivity extends android.app.Activity {

    /**
     *  Sets up opt_menus, which has the player, search, and settings buttons
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflating the current activity's menu with res/menu/items.xml
        getMenuInflater().inflate(R.menu.opt_menus, menu);
        return super.onCreateOptionsMenu(menu);

    }

    /**
     *  Launches correct activities for each options icon
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_item_settings:
                Intent prefs=new Intent(this,PreferencesActivity.class);
                startActivity(prefs);
                break;

            case R.id.menu_item_search:
                Intent search=new Intent(this,SearchActivity.class);
                startActivity(search);
                break;

            case R.id.menu_item_player:
                Intent intent = new Intent(this, SongPlayActivity.class);
                startActivity(intent);
                break;

        }
        return true;
    }
}
