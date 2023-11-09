package com.example.reproductordemusica.Vistas;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.reproductordemusica.R;
import com.example.reproductordemusica.clases_objetos.AdudioModel;
import com.example.reproductordemusica.BaseDatos.Consultas;
import com.example.reproductordemusica.Adapters.MusicaAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;


public class ListaCanciones extends AppCompatActivity {


    private static final int REPRODUCTOR_REQUEST_CODE = 1;
    RecyclerView recyclerView;
    SearchView searchView;

    private FloatingActionButton fab;
    private FloatingActionButton goPlayList;


    ArrayList<AdudioModel> listaCanciones = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_canciones);


        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab_main);
        goPlayList = findViewById(R.id.fabPLayList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListaCanciones.this);
                builder.setMessage("¿Estás seguro de que deseas cerrar sesión?")
                        .setCancelable(false)
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent form_registro = new Intent(ListaCanciones.this, MainActivity.class);
                                startActivity(form_registro);
                                finish();


                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }


                        });
                AlertDialog alert = builder.create();
                alert.show();
            }

        });

        goPlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playlist = new Intent(ListaCanciones.this, PlayList.class);
               startActivity(playlist);
            }
        });


        if (revisaPermiso() == false) {
            requestPermission();
            return;
        }

        Consultas consultas = new Consultas(ListaCanciones.this);


        /*El argumento projection es un arreglo que especifica las columnas que se quieren recuperar.

            El argumento selection es una cláusula WHERE que permite filtrar los resultados de la consulta.*/
        String[] projection = {

                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + " !=0 " + " or " + MediaStore.Audio.Media.MIME_TYPE + "= 'audio/mp4'";

        /*Este código se utiliza para recuperar datos de una fuente de datos en este caso la almacenamiento externo del dispositivo.*/
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);

        while (cursor.moveToNext()) {
            AdudioModel cancion = new AdudioModel(cursor.getString(1), cursor.getString(0), cursor.getString(2));
            if (new File(cancion.getPath()).exists()) {
                listaCanciones.add(cancion);

                long idCancion = consultas.insertarCancion(cancion.getTitle(), cancion.getDuration(),
                        cancion.getPath());
            }
        }


        if (listaCanciones.size() == 0) {
            Toast.makeText(ListaCanciones.this, "no hay canciones", Toast.LENGTH_SHORT).show();
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MusicaAdapter(listaCanciones, getApplicationContext()));
        }


    }


    public boolean revisaPermiso() {

        //Controlas que se importo android manifest para poder usar esta opcion
        int result = ContextCompat.checkSelfPermission(ListaCanciones.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(ListaCanciones.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(ListaCanciones.this, "Permisos necesarios para acceder a la lista de canciones", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(ListaCanciones.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
        }


    }




 @Override
    protected void onResume() {
     super.onResume();
     if (recyclerView != null) {
         recyclerView.setAdapter(new MusicaAdapter(listaCanciones, getApplicationContext()));
     }
 }

}














