package com.example.reproductordemusica.Vistas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.reproductordemusica.BaseDatos.Conexion;
import com.example.reproductordemusica.R;

public class MainActivity extends AppCompatActivity {

        EditText usuario;
        EditText password;
        Button logear;
        Button registrar;
        Conexion conexion = new Conexion(MainActivity.this);
       public static String nombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuario=findViewById(R.id.usuario);
        password=findViewById(R.id.password);

        logear=findViewById(R.id.loginbutton);
        registrar=findViewById(R.id.boton_registro);

            abre_conexion();

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              abre_formulario();
              finish();
            }
        });
        logear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               if(revisaUsuario(usuario.getText().toString().trim(),password.getText().toString().trim())){
                    nombreUsuario=(usuario.getText().toString().trim());

                    Intent form_registro= new Intent(MainActivity.this, ListaCanciones.class);
                    form_registro.putExtra("nombre_usuario", nombreUsuario);
                    startActivity(form_registro);
                    limpiar();
                   Toast.makeText(MainActivity.this,"Bienvenido "+MainActivity.nombreUsuario,Toast.LENGTH_LONG).show();
                    finish();


                }else{
                    Toast.makeText(MainActivity.this,"Datos invalidos",Toast.LENGTH_LONG).show();
                }

            }
        });

    }
    private void limpiar() {
        usuario.setText("");
        password.setText("");
    }

    public void abre_formulario(){
        Intent form_registro= new Intent(MainActivity.this, com.example.reproductordemusica.Vistas.form_registro.class);
        startActivity(form_registro);
    }
    public void abre_conexion(){
        Conexion conexion= new Conexion(MainActivity.this);
        SQLiteDatabase db= conexion.getWritableDatabase();

        if(db!=null){
            //Toast.makeText(MainActivity.this,"Conexion establecida",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(MainActivity.this,"Error de conexion",Toast.LENGTH_LONG).show();
        }
    }

    public boolean revisaUsuario(String usuarioBuscado,String contrasena) {

        SQLiteDatabase db = conexion.getReadableDatabase();
        Cursor cursor = null;
        String nombreUsuario;
        String contrasenaUsuario;
        boolean usuarioExiste = false;

        try {
            cursor = db.rawQuery("SELECT nombre,contrasena FROM usuario_tb where nombre = ? and contrasena= ?", new String[]{usuarioBuscado,contrasena});
            if (cursor != null && cursor.moveToFirst()) {
                nombreUsuario = cursor.getString(0).trim();
                contrasenaUsuario = cursor.getString(1).trim();

                usuarioExiste = true;
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        return usuarioExiste;
    }


}