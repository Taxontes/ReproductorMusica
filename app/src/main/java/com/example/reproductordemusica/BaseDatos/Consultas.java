package com.example.reproductordemusica.BaseDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.reproductordemusica.Vistas.ListaCanciones;
import com.example.reproductordemusica.clases_objetos.AdudioModel;

import java.util.ArrayList;
import java.util.List;

public class Consultas extends Conexion {

    Context context;
    public Consultas(@Nullable Context context) {
        super(context);
        this.context=context;
    }

    public long insertarContacto(String nombre, String sexo, String fecha_nacimiento,String pass, byte[] avatar) {

        long id = 0;

        try {
            Conexion conexion = new Conexion(context);
            SQLiteDatabase db = conexion.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("contrasena", pass);
            values.put("sexo", sexo);
            values.put("fecha_nacimiento", fecha_nacimiento);
            values.put("avatar", avatar);

            id = db.insert(tabla_usuario, null, values);
        } catch (Exception ex) {
            ex.toString();
        }

        return id;
    }
    public long insertarCancion(String titulo, String path,String duracion) {

        long id = 0;

        try {
            Conexion conexion = new Conexion(context);
            SQLiteDatabase db = conexion.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("titulo", titulo);
            values.put("duracion", duracion);
            values.put("path", path);


            id = db.insert(tabla_cancion, null, values);
        } catch (Exception ex) {
            ex.toString();
        }

        return id;
    }
    public long insertarPlaylist(int idUser, String titulo,String duracion, String path) {

        long id = 0;

        try {
            Conexion conexion = new Conexion(context);
            SQLiteDatabase db = conexion.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("id_Usuario", idUser);
            values.put("titulo", titulo);
            values.put("duracion", duracion);
            values.put("path", path);


            id = db.insert(tabla_usuario_cancion, null, values);
        } catch (Exception ex) {
            ex.toString();
        }

        return id;
    }
    public List<AdudioModel>infoCancion(String idUsuario) {

        String query = "SELECT idCancionUser, id_Usuario, path, duracion, titulo FROM user_song_tb WHERE id_Usuario = '" + idUsuario + "'";
        Conexion conexion = new Conexion(context);
        SQLiteDatabase db = conexion.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        List<AdudioModel> canciones = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int idCancionUser = cursor.getInt(0);
            String titulo = cursor.getString(4);
            String duracion = cursor.getString(3);
            String path = cursor.getString(2);

            canciones.add(new AdudioModel(idCancionUser, titulo, path, duracion));
            cursor.moveToNext();
        }


        return canciones;
    }




}
