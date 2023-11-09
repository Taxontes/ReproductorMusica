package com.example.reproductordemusica.clases_objetos;

import android.media.MediaPlayer;

public class MediaPlayerI {
    static MediaPlayer instance;


    public static MediaPlayer getInstance(){
        if(instance==null){
            instance= new MediaPlayer();
        }
        return instance;
    }
    public static int currentIndex = -1;
}
