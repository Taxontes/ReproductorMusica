package com.example.reproductordemusica.Vistas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.reproductordemusica.Adapters.MusicaAdapter;
import com.example.reproductordemusica.BaseDatos.Conexion;
import com.example.reproductordemusica.BaseDatos.Consultas;
import com.example.reproductordemusica.R;
import com.example.reproductordemusica.clases_objetos.AdudioModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;

public class PlayList extends AppCompatActivity {
    ImageView image;
    FloatingActionButton back, video;
    RecyclerView recyclerView;
    ArrayList<AdudioModel> listaPlayList = new ArrayList<>();
    int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);

        image=findViewById(R.id.avatar);
        recyclerView = findViewById(R.id.recyclerViewplayList);
        back=findViewById(R.id.fab_back);
        video=findViewById(R.id.fabVideo);

        /* Obtenemos el avatar del usuario */


        Conexion conexion = new Conexion(this);
        idUsuario=revisaUsuario(MainActivity.nombreUsuario);

        byte[] imagen = conexion.getImage(idUsuario);
        if (imagen != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imagen, 0, imagen.length);
            image.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this, "No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
        }


        /*Mostramos la lista de las canciones de la playlist*/

        Consultas consultas = new Consultas(PlayList.this);

        String [] projection={

                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
        };

        String selection= MediaStore.Audio.Media.IS_MUSIC+" !=0 " + " or " + MediaStore.Audio.Media.MIME_TYPE + "= 'audio/mp4'";


        Cursor cursor =getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,null);

        while(cursor.moveToNext()){

            AdudioModel cancion= new AdudioModel(cursor.getString(1),cursor.getString(0),cursor.getString(2));
            if(new File(cancion.getPath()).exists() ) {

                if(comprobarfav(cancion)){
                    listaPlayList.add(cancion);
                }

            }
        }


        if(listaPlayList.size()==0){
            Toast.makeText(PlayList.this, "no hay canciones", Toast.LENGTH_SHORT).show();
        }else{
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MusicaAdapter(listaPlayList,getApplicationContext()));
        }


        /*Salimos de la activity playlist*/

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              Intent intent= new Intent(PlayList.this,ListaCanciones.class);
                startActivity(intent);
                finish();
            }
        });


        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(PlayList.this,ReproductorVideo.class);
                startActivity(intent);
                finish();
            }
        });


    }

    public int revisaUsuario(String usuarioBuscado) {
        Conexion conexion=new Conexion(PlayList.this);
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

        String sql = "Select titulo from user_song_tb where id_usuario like " + idUsuario + " and titulo like '" + song.getTitle()+"'";
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




    @Override
    public void onBackPressed() {
        // Cerrar la actividad actual
        finish();
    }

}
