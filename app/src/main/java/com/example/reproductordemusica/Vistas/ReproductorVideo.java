package com.example.reproductordemusica.Vistas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.reproductordemusica.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ReproductorVideo extends AppCompatActivity {

    FloatingActionButton back;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor_video);

        back=findViewById(R.id.fab_salir);
        videoView=findViewById(R.id.videoView);

        MediaPlayer mediaPlayer= MediaPlayer.create(this,R.raw.deku_vs_muscular);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ReproductorVideo.this,PlayList.class);
                startActivity(intent);
                mediaPlayer.stop();
                finish();
            }
        });




        String path="android.resource://"+ getPackageName() + "/" + R.raw.deku_vs_muscular;
        Uri uri= Uri.parse(path);
        videoView.setVideoURI(uri);


        MediaController controls= new MediaController(this);
        videoView.setMediaController(controls);
        controls.setAnchorView(videoView);




    }
    @Override
    public void onBackPressed() {
        finish();
    }
}