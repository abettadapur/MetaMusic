package org.thingswithworth.metamusic.net;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * Class with static methods for sharing to facebook/other networks
 */
public class SocialShare {

    public static void sendImage(Activity from, Uri path){
        Intent shareImageIntent =new Intent(Intent.ACTION_SEND);
        shareImageIntent.setType("image/*");

        //set photo
        shareImageIntent.putExtra(Intent.EXTRA_STREAM, path);


        from.startActivity(Intent.createChooser(shareImageIntent, "Share with"));
    }

    public static void sendImage(Activity from, Bitmap image){
        String path = MediaStore.Images.Media.insertImage(from.getContentResolver(),
                image, "title", null);
        Uri imageUri = Uri.parse(path);
        sendImage(from,imageUri);
        File toDelete=new File(path);
        toDelete.deleteOnExit();
    }

    public static void sendText(Activity from, String text)
    {
        Intent shareTextIntent = new Intent(Intent.ACTION_SEND);
        shareTextIntent.setType("text/*");
        shareTextIntent.putExtra(Intent.EXTRA_SUBJECT,"My Music Statistics");
        shareTextIntent.putExtra(Intent.EXTRA_TEXT, text);
        from.startActivity(Intent.createChooser(shareTextIntent, "Share with"));
    }

    // private ShareActionProvider mShareActionProvider;

    /*
    public void doShare() {
        // When you want to share set the share intent.
        mShareActionProvider.setShareIntent(getImageShareIntent());
    }

    /** Returns a share intent
    private Intent getImageShareIntent(){
        Bitmap bitmap=mChart.toBitmap();

        boolean storeImage = true;
       /**
        FileOutputStream fileOutputStream = null;
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(path, "temp.jpg");
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        try {
            bos.flush();
            bos.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri  imageUri=  Uri.parse("file://" + file.getAbsolutePath());
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "temp", null);
        Uri imageUri = Uri.parse(path);
        Log.d("Share Uri", imageUri.toString());


        Log.e("Share Uri",imageUri.toString());

        Intent shareImageIntent =new Intent(Intent.ACTION_SEND);
        shareImageIntent.setType("image/jpg");

        //set photo
        shareImageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareImageIntent.putExtra(Intent.EXTRA_TEXT, path);

        return Intent.createChooser(shareImageIntent, "Share with");
    }
    */
}
