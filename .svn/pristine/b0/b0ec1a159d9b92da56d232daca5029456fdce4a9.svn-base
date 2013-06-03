package org.thingswithworth.metamusic.activities.factories;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.thingswithworth.metamusic.Callback;
import org.thingswithworth.metamusic.MusicApplication;
import org.thingswithworth.metamusic.R;
import org.thingswithworth.metamusic.activities.SearchActivity;
import org.thingswithworth.metamusic.net.GoogleMusic;

/**
 * Simple (monostate) factory class to handle creation of Dialogs.
 * @author Alex Bettadapur
 */
public class DialogFactory
{
    /**
     * Creates a progress dialog, with an option to have a cancel button that cancels Google Music queries.
     * @param title The title at the top
     * @param message The detailed message
     * @param caller SearchActivity in which to show this
     * @param cancelButtton whether to have a cancel button in the bottom that calls GooogleMusic.stopQuery
     * @return The desired AlertDialog, not yet shown (need to call show())
     */
    public static AlertDialog getProgressDialog(String title, String message, final Activity caller, boolean cancelButtton)
    {
        AlertDialog.Builder aDB = new AlertDialog.Builder(caller);
        aDB.setTitle(title);
        final ProgressBar bar = new ProgressBar(caller);
        bar.setIndeterminate(true);
        aDB.setView(bar);
        aDB.setMessage(message);
        if(cancelButtton)
            aDB.setNegativeButton("Cancel Operation", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    GoogleMusic.stopQuery();
                }
            });

        return aDB.create();
    }

    /**
     * Creates the email/password input dialog with OK/Cancel buttons.
     * @param caller the activity to show this in
     * @param call the method to call when user preses Okay (username will be string 0 and password string 1)
     * @return The dialog
     */
    public static AlertDialog createLoginDialog(final Activity caller, final Callback.ParamCallback<String> call)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(caller);
        LayoutInflater li = LayoutInflater.from(caller);
        View promptsView = li.inflate(R.layout.gmusiclogin_activity, null);
        final TextView emailView = (TextView)promptsView.findViewById(R.id.gMusicUser);
        final TextView passwordView = (TextView)promptsView.findViewById(R.id.gMusicPassword);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setTitle("Enter Email");
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which)
                    {
                        call.onCompletion(emailView.getEditableText().toString(), passwordView.getEditableText().toString());
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                    }
                });
        alertDialogBuilder.setMessage("Enter your google credentials");
        alertDialogBuilder.setCancelable(true);
        return alertDialogBuilder.create();
    }

    /**
     * Creates the into dialog with some info for the user.
     * @param context Context for showing. Used in Applciation stuff and possibly later for info.
     * @return the dialog, not shown, with an OK button to exist
     */
    public static AlertDialog getIntroDialog(Activity context){
        return new AlertDialog.Builder(context)
                .setTitle("Welcome to MetaMusic")
                .setMessage("The metadata of your music collection has been processed and graphed. "+
                        "Zoom and pan the graphs, share them with your friends, "+
                        "and even explore your music by touching genres on the pie chart. Enjoy!")
                .setIcon(android.R.drawable.ic_dialog_info).setPositiveButton("OK",new
                AlertDialog.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
    }


}
