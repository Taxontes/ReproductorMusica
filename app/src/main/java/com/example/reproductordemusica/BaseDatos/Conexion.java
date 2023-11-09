package com.example.reproductordemusica.BaseDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Conexion extends SQLiteOpenHelper {

    private static final int version_DB=11;
    public static final String nombre_DB="reproductor_musica.db";
    public static final String tabla_usuario="usuario_tb";
    public static final String tabla_cancion="cancion_tb";
    public static final String tabla_usuario_cancion="user_song_tb";



    public Conexion(@Nullable Context context) {
        super(context,nombre_DB,null,version_DB);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ tabla_usuario + "(" +
                "idUsuario INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT not null unique," +
                "contrasena TEXT not null," +
                "sexo TEXT not null," +
                "avatar BLOB not null," +
                "fecha_nacimiento TEXT not null)");


        db.execSQL("CREATE TABLE "+ tabla_cancion + "(" +
                "idCancion INTEGER PRIMARY KEY AUTOINCREMENT," +
                "titulo TEXT not null," +
                "duracion TEXT not null," +
                "path TEXT not null)");


        db.execSQL("CREATE TABLE "+ tabla_usuario_cancion + "(" +
                "idCancionUser INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_Usuario TEXT not null," +
                "path TEXT not null," +
                "duracion TEXT not null," +
                "titulo TEXT not null," +
                "FOREIGN KEY(id_Usuario) REFERENCES tabla_usuario(idUsuario));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE "+tabla_usuario);
        db.execSQL("DROP TABLE "+tabla_cancion);
        db.execSQL("DROP TABLE "+tabla_usuario_cancion);
        onCreate(db);
    }

    public byte[] getImage(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT avatar FROM " + tabla_usuario + " WHERE idUsuario=?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            byte[] image = cursor.getBlob(0);
            cursor.close();
            return image;
        }
        return null;
    }
}
