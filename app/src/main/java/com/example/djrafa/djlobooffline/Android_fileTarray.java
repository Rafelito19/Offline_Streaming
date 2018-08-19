package com.example.djrafa.djlobooffline;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
public class Android_fileTarray {








        public  final static String path = "";
        final static String TAG = "file reader  ";






        public static   ArrayList<String> ReadFile( Context context,String fileName){
            String line = null;
            int cureent=0;
            ArrayList<String> stuff = new ArrayList<String>();
            try {
                Log.d(TAG,fileName);
               // FileInputStream fileInputStream =
                InputStream inputStreamReader =(context.getResources().openRawResource(R.raw.songs));
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStreamReader));
                StringBuilder stringBuilder = new StringBuilder();

                while ( (line = bufferedReader.readLine()) != null )
                {
                 stuff.add(line.substring(line.lastIndexOf("/")+1,line.lastIndexOf(".")));  //line by line add stuff
                   cureent++;

                    Log.d("Line added ", line);
                }



                bufferedReader.close();

                Log.d("","THINGS ADDED "+String.valueOf(cureent));
            }
            catch(FileNotFoundException ex) {
                Log.d(TAG, "file could not been found");
            }
            catch(IOException ex) {
                Log.d(TAG, ex.getMessage());
            }


            return stuff;
        }

        public static boolean saveToFile( String data,String fileName){
            try {
                new File(path  ).mkdir();
                File file = new File( fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file,true);
                fileOutputStream.write((data + System.getProperty("line.separator")).getBytes());
                Log.e("someting went good", "saveToFile: "+path+fileName );
                return true;
            }  catch(FileNotFoundException ex) {
                Log.d(TAG,"something bad happend" +ex.getMessage());
            }  catch(IOException ex) {
                Log.d(TAG, "ssoemthing bad happehd"+ex.getMessage());
            }
            return  false;


        }

    }















