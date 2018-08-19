package com.example.djrafa.djlobooffline;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.djrafa.djlobooffline.MyAdapter.*;

public class Parsewer extends AppCompatActivity  implements MyAdapter.ListItemClickListener {
    ArrayList<Text_Link> dir_list = new ArrayList<Text_Link>();
    ArrayList<String> songlist = new ArrayList<String>();
    private Button getBtn;
    private TextView result;
    private RecyclerView rv;
    private boolean done;
   private ProgressBar pbar;
   private int progress_status=0;


    String base_url="http://losmoncionero.com/musica/fiestas/";

    private MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);


//declare rec view

        //adapter

        populatelist_from_URL(base_url);


    }







 // reads data from an url // paess and generates link list
    private  void populatelist_from_URL(String uri)
    {
        songlist = new ArrayList<String>();
base_url=uri;

        rv = (RecyclerView) findViewById(R.id.rv);


        rv.setLayoutManager(new LinearLayoutManager(this));


        pbar = (ProgressBar) findViewById(R.id.pbHeaderProgress);
        getWebsite();








    }




private void play_music ( String url){



    MediaPlayer mediaPlayer = new MediaPlayer();
    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
   try {
       mediaPlayer.setDataSource(url);
       mediaPlayer.prepare(); // might take long! (for buffering, etc)
   }


   catch (IOException IO){

   }
    mediaPlayer.start();

}
    private void getWebsite() {

done= false;
        pbar.setVisibility(View.VISIBLE);



        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try {
                    Document doc = Jsoup.connect(base_url).get();
                    String title = doc.title();
                    Log.e("parsing",title );
                    Elements links = doc.select("a[href]");

                    builder.append(title).append("\n");

                    for (Element link : links) {



                        builder.append("\n").append("Link : ").append(link.attr("href"))
                                .append("\n").append("Text : ").append(link.text());

                    dir_list.add(new Text_Link(link.text(),link.attr("href"))); // adds the atribute and link pair
                       if (link.text().contains("Parent")==false )
                        songlist.add(link.text());

                    }

                    done =true; //signal the main thread that we are ready to display the items
Log.d(" doner   ___>>>>>", base_url);
                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {




                        pbar.setVisibility(View.GONE); // hide the spinner
                        set();

          // /adapter





                    }
                });
            }
        }).start();
    }


private boolean isSong(String song){


if (song.contains(".mp3")|| song.endsWith("3/")) {

    Log.d(" STRING A SONG---->", song);

    return true;
}
        return false;
}
private void set()
{    adapter=new MyAdapter(this,songlist, (ListItemClickListener) this);
    rv.setAdapter(adapter);


}//


    @Override
    public void onListItemClick(int clickedItemIndex) {
        String url= base_url+songlist.get(clickedItemIndex); //FULL URL




      //  Log.d(" base url is ", base_url);

        String msg= "clicked "+songlist.get(clickedItemIndex);
        Toast tost= Toast.makeText(this , msg,Toast.LENGTH_SHORT);
String link_clicked= dir_list.get(clickedItemIndex).getLink();






if (isSong(url)){
Log.e("playing this son----->", url);

    tost= Toast.makeText(this , "np3 song  "+url,Toast.LENGTH_SHORT);
    tost.show();

    Intent intent = new Intent(Parsewer.this, MainActivity.class);
    intent.putExtra("message", url);
    startActivity(intent);













}


if (url.contains(".zip")){


    tost= Toast.makeText(this , "Zip FILE  "+url,Toast.LENGTH_SHORT);
    tost.show();


}

else if (isDirectory(link_clicked)){


    songlist = new ArrayList<String>();


    tost= Toast.makeText(this , "click directory " +url,Toast.LENGTH_SHORT);
    tost.show();


populatelist_from_URL(url);






}

        else {


    tost= Toast.makeText(this , "is  neither" +url,Toast.LENGTH_SHORT);



}
        tost.show();








    }











public static  boolean  isDirectory(String dir){



        if ((dir.endsWith("/"))&&(dir.contains(".mp3")==false)) {


            Log.d(" STRING A DIRECORY---->",dir);
            return true;

        }

return false;


}


    class Text_Link{  // a simple class that stores the text link object
        private String text;
        private String link;

        public Text_Link(String t, String l){
            text =  t;
            link = l;

        }


        public  void set_text(String txt  ){
            text = txt;
        }
        public  String getLink(){

            return link;
        }


    }

}


