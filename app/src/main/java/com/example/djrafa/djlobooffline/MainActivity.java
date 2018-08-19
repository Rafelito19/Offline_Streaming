package com.example.djrafa.djlobooffline;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.*;
import android.widget.TextView;

import java.io.IOException;
import java.net.URI;

public class MainActivity extends Activity {
    private Button btn;
    private Button download_btn;
    private String song_url;

    private DownloadManager downloadManager ;
    /**
     *
     * help to toggle between play and pause.
     */
    private boolean playPause;
    private MediaPlayer mediaPlayer;
    /**
     * remain false till media is not completed, inside OnCompletionListener make it true.
     */
    private boolean intialStage = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Bundle bundle = getIntent().getExtras();
        song_url = bundle.getString("message");
        TextView tv = findViewById(R.id.textView);
        tv.setText(song_url.substring(song_url.lastIndexOf("/")+1).replaceAll("%"," ").replace(".mp3",""));
         btn = (Button) findViewById(R.id.button);
download_btn = (Button) findViewById(R.id.button2); /// what a great naming scheme button 1 and 2
        Log.d("Son url",song_url);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        btn.setOnClickListener(pausePlay);

        download_btn.setOnClickListener(download_Listener);

         downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        new Player()
                .execute(song_url);  //start as soon as it loads
  playPause=false;
        btn.setBackgroundResource(android.R.drawable.ic_media_pause);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private View.OnClickListener pausePlay = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            // TODO Auto-generated method stub

            if (!playPause) {
                btn.setBackgroundResource(android.R.drawable.ic_media_pause);
                if (intialStage)
                    new Player()
                            .execute(song_url);
                else {
                    if (!mediaPlayer.isPlaying())
                        mediaPlayer.start();
                }
                playPause = true;
            } else {
                btn.setBackgroundResource(R.drawable.ic_play_arrow_black_36dp);
                if (mediaPlayer.isPlaying())
                    mediaPlayer.pause();
                playPause = false;
            }
        }
    };



    private long DownloadData (Uri uri) {

        long downloadReference;
String file_name = song_url.substring(song_url.lastIndexOf("/")+1);
        // Create request for android download manager
        downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        //Setting title of request
        request.setTitle("Music Download");

        //Setting description of request
        request.setDescription(file_name);

        //Set the local destination for the downloaded file to a path within the application's external files directory

            request.setDestinationInExternalFilesDir(MainActivity.this, Environment.DIRECTORY_DOWNLOADS,file_name);



        //Enqueue download and save into referenceId
        downloadReference = downloadManager.enqueue(request);


        return downloadReference;
    }





private long download_Song(String ul){




   Uri Download_Uri = Uri.parse(song_url);

    DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
    request.setAllowedOverRoaming(false);
    request.setTitle(song_url.substring(song_url.lastIndexOf("/")+1));
    request.setDescription("Downloading " + "Sample" + ".mp3");
    request.setVisibleInDownloadsUi(true);
    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/GadgetSaint/"  + "/" + "Sample" + ".png");


       return downloadManager.enqueue(request);


}



    private View.OnClickListener download_Listener = new View.OnClickListener() {

        @Override
        public  void onClick(View v) {

            // petform download

download_btn.setBackgroundResource(R.drawable.ic_close_black_24dp);

Log.e("download id is",song_url);  //jajaja ""+int == string




Uri song_ur= Uri.parse(song_url);


DownloadData(song_ur);
}












    };












    /**
     * preparing mediaplayer will take sometime to buffer the content so prepare it inside the background thread and starting it on UI thread.
     *
     * @author piyush
     */

    class Player extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog progress;

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            Boolean prepared;
            try {

                mediaPlayer.setDataSource(params[0]);

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // TODO Auto-generated method stub
                        intialStage = true;
                        playPause = false;
                        //btn.setBackgroundResource(R.drawable.ic_play_arrow_black_36dp);
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });
                mediaPlayer.prepare();
                prepared = true;
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                Log.d("IllegarArgument", e.getMessage());
                prepared = false;
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (progress.isShowing()) {
                progress.cancel();
            }
            Log.d("Prepared", "//" + result);
            mediaPlayer.start();

            intialStage = false;
        }

        public Player() {
            progress = new ProgressDialog(MainActivity.this);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            this.progress.setMessage("Buffering...");
            this.progress.show();

        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}