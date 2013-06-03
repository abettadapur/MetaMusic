package org.thingswithworth.metamusic.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.Button;
import org.thingswithworth.metamusic.R;

/**
 * Allows for configuration options. Currently created in deprecated way to allow for button at the bottom and single page of preferences.
 */
public class PreferencesActivity extends PreferenceActivity  {
    private static boolean launched;
    /**
     * Gives values to instance variables and sets up view of Activity.
     * Inflates view and sets up done button. Sets launched to true.
     * @param savedInstanceState not used
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        setContentView(R.layout.prefs);
        findViewById(R.id.propsDoneButton).setEnabled(true);
        findViewById(R.id.propsDoneButton).setClickable(true);
        findViewById(R.id.propsDoneButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        launched=true;
    }

    /**
     * Gets launched boolean. Used to check for possible configuration changes.
     * @return launched
     */
    public static boolean wasLaunched(){
        return launched;
    }

    /**
     * Sets launched to false.
     */
    public static void resetLaunched(){
        launched=false;
    }
}
