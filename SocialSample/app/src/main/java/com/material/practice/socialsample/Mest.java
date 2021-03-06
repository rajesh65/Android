package com.material.practice.socialsample;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.io.ByteArrayOutputStream;

/**
 * An {@link Activity} showing a tuggable "Hello World!" card.
 * <p/>
 * The main content view is composed of a one-card {@link CardScrollView} that provides tugging
 * feedback to the user when swipe gestures are detected.
 * If your Glassware intends to intercept swipe gestures, you should set the content view directly
 * and use a {@link com.google.android.glass.touchpad.GestureDetector}.
 *
 * @see <a href="https://developers.google.com/glass/develop/gdk/touch">GDK Developer Guide</a>
 */
public class Mest extends Activity {

    /**
     * {@link CardScrollView} to use as the main content view.
     */
    private CardScrollView mCardScroller;

    /**
     * "Hello World!" {@link View} generated by {@link #buildView()}.
     */
    private View mView;
    private void saveChanges(){

    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri Selectionuri = data.getData();
            String[] pathCols = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(Selectionuri, pathCols, null, null, null);
            cursor.moveToFirst();
            final String path = cursor.getString(cursor.getColumnIndex(pathCols[0]));
            cursor.close();
            String imgStr= getStringImage(BitmapFactory.decodeFile(path));
            Bitmap bmp=null;

            // cross-test for decode and encode
            byte[] barr= Base64.decode(imgStr,Base64.DEFAULT);
            bmp= BitmapFactory.decodeByteArray(barr, 0,barr.length);
            // end


            proImage.setImageBitmap(bmp);
            // proImage.setImageBitmap(BitmapFactory.decodeFile(path));

            if(InternetManager.getInstance(this).isConnectedToNet()){
                Log.e("EP", "trying upload");
                Handler handler= new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("EP", "thread dispatched");
                        ImageUploader.getInstance(new Contexter().getContext()).upload(BitmapFactory.decodeFile(path));
                    }
                });

            }

        }

    }
}.onPause();
    }

    /**
     * Builds a Glass styled "Hello World!" view using the {@link CardBuilder} class.
     */
    private View buildView() {
        CardBuilder card = new CardBuilder(this, CardBuilder.Layout.TEXT);

        card.setText(R.string.hello_world);
        return card.getView();
    }

}
