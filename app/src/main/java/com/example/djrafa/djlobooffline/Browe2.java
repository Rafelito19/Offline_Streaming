package com.example.djrafa.djlobooffline;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/*  To do
 * Figure out whats wrong here
 * come up with a smart way of dealing with the music brower
 * Investigate the music objects
 * Implement file download class given url
 * ADD Background play
  *
  * */



public class Browe2 extends AppCompatActivity implements MyAdapter.ListItemClickListener {
    ArrayList<Browe2.Text_Link> dir_list = new ArrayList<>();
    ArrayList<String> songlist = new ArrayList<String>();
    private Button getBtn;
    private TextView result;
    private RecyclerView rv;
    private boolean done;


    String base_url = "http://losmoncionero.com/musica/fiestas/";
    String base = "http://losmoncionero.com/musica/fiestas/Junio%202018/";
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);


//declare rec view
        rv = (RecyclerView) findViewById(R.id.rv);


        rv.setLayoutManager(new LinearLayoutManager(this));
        ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        getWebsite();


        while (!done) {
        }
        progress.dismiss();

        adapter = new MyAdapter(this, songlist, (MyAdapter.ListItemClickListener) this);
        rv.setAdapter(adapter);
        //adapter


    }


    private void play_music(String url) {


        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
        } catch (IOException IO) {

        }
        mediaPlayer.start();

    }

    private void getWebsite() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try {
                    Document doc = Jsoup.connect(base_url).get();
                    String title = doc.title();
                    Log.e("parsing", title);
                    Elements links = doc.select("a[href]");

                    builder.append(title).append("\n");

                    for (Element link : links) {
                        builder.append("\n").append("Link : ").append(link.attr("href"))
                                .append("\n").append("Text : ").append(link.text());

                        dir_list.add(new Text_Link(link.text(), link.attr("href"))); // adds the atribute and link pair
                        songlist.add(link.text());

                    }
                    done = true; //signal the main thread that we are ready to display the items

                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        done = true;


                        // /adapter


                    }
                });
            }
        }).start();
    }


    @Override
    public void onListItemClick(int clickedItemIndex) {
        String url = base + dir_list.get(clickedItemIndex).getLink();
        String msg = "clicked " + songlist.get(clickedItemIndex);
        Toast tost = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        String link_clicked = dir_list.get(clickedItemIndex).getLink();
        if (link_clicked.contains("mp3")) {


            Intent intent = new Intent(Browe2.this, MainActivity.class);
            intent.putExtra("message", url);
            startActivity(intent);


        } else if (isDirectory(link_clicked)) {

            base_url = link_clicked;


            tost = Toast.makeText(this, "is directory" + url, Toast.LENGTH_LONG);


        } else {


        }
        tost.show();

    }


    public static boolean isDirectory(String dir) {


        if (dir.endsWith("/"))
            return true;


        return false;


    }

    class Text_Link {  // a simple class that stores the text link object
        private String text;
        private String link;

        public Text_Link(String t, String l) {
            text = t;
            link = l;

        }


        public void set_text(String txt) {
            text = txt;
        }

        public String getLink() {

            return link;
        }


    }
}
