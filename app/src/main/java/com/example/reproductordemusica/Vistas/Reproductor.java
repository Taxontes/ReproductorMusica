package com.example.reproductordemusica.Vistas;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reproductordemusica.R;
import com.example.reproductordemusica.clases_objetos.AdudioModel;
import com.example.reproductordemusica.BaseDatos.Conexion;
import com.example.reproductordemusica.BaseDatos.Consultas;
import com.example.reproductordemusica.clases_objetos.MediaPlayerI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Reproductor extends AppCompatActivity {


    TextView titulo,tiempoinicial,tiempofinal;
    SeekBar seekBar;
    ImageView anterior,siguiente,play_pause,logo,stop,aleatorio,repetir,btn_ultimo,btn_primero,btn_like;
    ArrayList<AdudioModel> listaCanciones= new ArrayList<>();
    AdudioModel cancion;
    private boolean isRandomMode = false;
    private boolean isRepeat = false;


    int posicionActual=0;
    int idUsuarioRep;
    int x=0;
    MediaPlayer mediaPlayer= MediaPlayerI.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);

        titulo= findViewById(R.id.tituloReproductor);
        tiempoinicial=findViewById(R.id.tiempoInicial);
        tiempofinal=findViewById(R.id.tiempoTotal);
        seekBar=findViewById(R.id.seek_bar);
        anterior=findViewById(R.id.anterior);
        siguiente=findViewById(R.id.siguiente);
        logo=findViewById(R.id.logoAplicacion);
        play_pause=findViewById(R.id.play_pause);
        stop=findViewById(R.id.btn_stop);
        repetir=findViewById(R.id.repetir);
        aleatorio=findViewById(R.id.btn_Aleatorio);
        btn_primero=findViewById(R.id.primary_song);
        btn_ultimo=findViewById(R.id.final_song);
        btn_like=findViewById(R.id.meGusta);

        titulo.setSelected(true);


        idUsuarioRep=revisaUsuario(MainActivity.nombreUsuario);


        listaCanciones= (ArrayList<AdudioModel>) getIntent().getSerializableExtra("Lista");

        Consultas consultas = new Consultas(Reproductor.this);

        setResourceWithMusic();

        btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(comprobarfav(cancion)){
                    borrarFav(cancion);
                    Toast.makeText(Reproductor.this, "Eliminada favoritos ",Toast.LENGTH_LONG).show();
                    btn_like.clearColorFilter();
                }else{
                    consultas.insertarPlaylist(revisaUsuario(MainActivity.nombreUsuario), cancion.getTitle(),cancion.getDuration(), cancion.getPath());
                    Toast.makeText(Reproductor.this, "Agregado a favoritos ",Toast.LENGTH_LONG).show();
                    btn_like.setColorFilter(Color.RED);


                }

            }
        });



        Reproductor.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    tiempoinicial.setText(convertirTiempo(mediaPlayer.getCurrentPosition()+""));
                }

                if(mediaPlayer.isPlaying()){
                    play_pause.setImageResource(R.drawable.baseline_pause_24);
                    logo.setRotation(x++);
                }else{
                    play_pause.setImageResource(R.drawable.baseline_play_arrow_24);
                    logo.setRotation(0);
                }

                if(convertirTiempo(mediaPlayer.getCurrentPosition()+"").equals(convertirTiempo(cancion.getDuration()))){
                    siguiente();
                }

                new Handler().postDelayed(this,100);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {



            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    public void setResourceWithMusic(){


        cancion=listaCanciones.get(MediaPlayerI.currentIndex);
        titulo.setText(cancion.getTitle());
        tiempofinal.setText(convertirTiempo(cancion.getDuration()));

        play_pause.setOnClickListener(v -> pausePlay());
        anterior.setOnClickListener(v -> anterior());
        siguiente.setOnClickListener(v -> siguiente());
        stop.setOnClickListener(v -> stopi());

        btn_primero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Reproductor.this,"Primera cancion",Toast.LENGTH_LONG).show();
                MediaPlayerI.currentIndex = 0;
                // Asigna la primera canción a la variable canción
                cancion = listaCanciones.get(MediaPlayerI.currentIndex);
                titulo.setText(cancion.getTitle());
                tiempofinal.setText(convertirTiempo(cancion.getDuration()));
                // Reproduce la canción
                play();
            }
        });

        btn_ultimo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Reproductor.this,"Ultima cancion",Toast.LENGTH_LONG).show();
                MediaPlayerI.currentIndex = listaCanciones.size()-1;
                // Asigna la primera canción a la variable canción
                cancion = listaCanciones.get(MediaPlayerI.currentIndex);
                titulo.setText(cancion.getTitle());
                tiempofinal.setText(convertirTiempo(cancion.getDuration()));
                // Reproduce la canción
                play();
            }
        });


     aleatorio.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             isRandomMode = !isRandomMode;
             if (isRandomMode) {
                 Toast.makeText(Reproductor.this,"Aleatorio On",Toast.LENGTH_LONG).show();
                 aleatorio.setColorFilter(Color.GREEN);
             } else {
                 Toast.makeText(Reproductor.this,"Aleatorio Off",Toast.LENGTH_LONG).show();
                 aleatorio.clearColorFilter();
             }
         }
     });
      repetir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRepeat = !isRepeat;
                if (isRepeat) {
                    Toast.makeText(Reproductor.this,"Repetir On",Toast.LENGTH_LONG).show();
                    repetir.setColorFilter(Color.GREEN);
                } else {
                    Toast.makeText(Reproductor.this,"Repetir Off",Toast.LENGTH_LONG).show();
                    repetir.clearColorFilter();
                }
            }

        });


        play();



        if(comprobarfav(cancion)){
            btn_like.setColorFilter(Color.RED);
        }else{
            btn_like.clearColorFilter();
        }


    }


    /*   Metodos para reproducir la musica    */

    private void play(){
        mediaPlayer.reset();

        try {
            posicionActual= MediaPlayerI.currentIndex;
            mediaPlayer.setDataSource(cancion.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());

        }catch (IOException e){
            System.out.println(e.getMessage());
        }



    }

    private void siguiente(){

        if (isRepeat) {
            mediaPlayer.reset();
            setResourceWithMusic();
        } else {
            if(isRandomMode){
                // Obtiene un índice aleatorio
                int randomIndex = getRandomIndex();

                // Actualiza el índice actual
                posicionActual = randomIndex;

                // Actualiza la canción actual
                MediaPlayerI.currentIndex=randomIndex;
                mediaPlayer.reset();
                setResourceWithMusic();

            }else{
                if(MediaPlayerI.currentIndex==listaCanciones.size()-1){
                    MediaPlayerI.currentIndex=0;
                    mediaPlayer.reset();
                    setResourceWithMusic();
                }else{

                    MediaPlayerI.currentIndex+=1;
                    mediaPlayer.reset();
                    setResourceWithMusic();
                }
            }

        }





    }
    private int getRandomIndex() {
        Random random = new Random();
        int randomIndex = random.nextInt(listaCanciones.size());
        return randomIndex;
    }
    private void pausePlay() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }


    protected void stopi() {

       if(mediaPlayer.isPlaying()){
           mediaPlayer.pause();
           mediaPlayer.seekTo(0);
       }


    }

    private void anterior(){
        if(isRandomMode){
            // Obtiene un índice aleatorio
            int randomIndex = getRandomIndex();

            // Actualiza el índice actual
            posicionActual = randomIndex;

            // Actualiza la canción actual
            MediaPlayerI.currentIndex=randomIndex;
            mediaPlayer.reset();
            setResourceWithMusic();

        }else{
            if(MediaPlayerI.currentIndex==0){
                MediaPlayerI.currentIndex=listaCanciones.size()-1;
                mediaPlayer.reset();
                setResourceWithMusic();
            }else{

                MediaPlayerI.currentIndex-=1;
                mediaPlayer.reset();
                setResourceWithMusic();
            }
        }

    }

    public int revisaUsuario(String usuarioBuscado) {
        Conexion conexion=new Conexion(Reproductor.this);
        SQLiteDatabase db = conexion.getReadableDatabase();
        Cursor cursor = null;
        String nombreUsuario = null;


        try {
            cursor = db.rawQuery("SELECT idUsuario FROM usuario_tb where nombre = ?", new String[]{usuarioBuscado});
            if (cursor != null && cursor.moveToFirst()) {
                nombreUsuario = cursor.getString(0).trim();


                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        return Integer.parseInt(nombreUsuario);
    }

    public boolean comprobarfav(AdudioModel song) {


        Conexion admin = new Conexion(this);
        SQLiteDatabase bbdd = admin.getReadableDatabase();

        String sql = "Select titulo from user_song_tb where id_usuario like " + idUsuarioRep + " and titulo like '" + song.getTitle()+"'";
        Cursor fila = bbdd.rawQuery(sql, null);

        if (!fila.moveToFirst()) {
            fila.close();
            bbdd.close();
            return false;

        } else {
            fila.close();
            bbdd.close();
            return true;
        }
    }
    public void borrarFav(AdudioModel song){

        Conexion admin = new Conexion(this);
        SQLiteDatabase bbdd = admin.getReadableDatabase();

        String sql = "Delete from user_song_tb where id_usuario like " + idUsuarioRep + " and titulo like '" + song.getTitle()+"'";
        bbdd.execSQL(sql);
        bbdd.close();

    }


    public static String convertirTiempo(String duration){
        Long milis=Long.parseLong(duration);
      return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milis)%TimeUnit.HOURS.toMinutes(1),
        TimeUnit.MILLISECONDS.toSeconds(milis)%TimeUnit.MINUTES.toSeconds(1));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopi();

    }
}