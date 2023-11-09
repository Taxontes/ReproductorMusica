package com.example.reproductordemusica.Vistas;


import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.reproductordemusica.BaseDatos.Conexion;
import com.example.reproductordemusica.BaseDatos.Consultas;

import com.example.reproductordemusica.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class form_registro extends AppCompatActivity {
    Button btnGuardar;
    Button btnVolver;
    RadioGroup rg;
    RadioButton foto_hombre;
    RadioButton foto_mujer;
    ImageView image;
    FloatingActionButton btn_flotante;
    EditText txtNombre, txtFecha, txtPassword;
    Context context;
    Uri eleccionIMG;
    String fechaIntroducida;
    byte[] byteArray;
    private static final int GALLERY_REQUEST_CODE = 100;
    Conexion conexion = new Conexion(form_registro.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_registro);


        btnGuardar = findViewById(R.id.btnguardar);
        btnVolver = findViewById(R.id.btn_volver);
        rg = findViewById(R.id.radio_group);
        foto_hombre = findViewById(R.id.radioButton);
        foto_mujer = findViewById(R.id.radioButton2);
        image = findViewById(R.id.avatar);
        txtNombre = findViewById(R.id.usuario);
        txtFecha = findViewById(R.id.fecha);
        txtPassword = findViewById(R.id.password);
        btnGuardar.setEnabled(false);



        btnVolver.setOnClickListener(v -> {
            finish();
            Intent form_login = new Intent(form_registro.this, MainActivity.class);
            startActivity(form_login);



        });

        btn_flotante = findViewById(R.id.btn_agregar);


        btn_flotante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageFromGallery();
            }
        });





        Integer[] fotos = {R.drawable.deku, R.drawable.uraraka};
        this.rg.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton radioButton = (RadioButton) rg.findViewById(i);
            int index = rg.indexOfChild(radioButton);
            image.setImageResource(fotos[index]);
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sexo = compruebaSexo();


                if(!compararUsuario(txtNombre.getText().toString().trim())) {

                    if(compruebaFecha()){

                        if (valida_nombre() && valida_pass()) {

                            if (eleccionIMG == null) {
                                if (sexo.equals("Mujer")) {

                                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.uraraka);
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                    byteArray = stream.toByteArray();

                                } else if (sexo.equals("Hombre")) {

                                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.deku);
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                    byteArray = stream.toByteArray();
                                }
                            }


                            Consultas consultas = new Consultas(form_registro.this);
                            long id = consultas.insertarContacto(txtNombre.getText().toString().trim(), sexo,
                                    txtFecha.getText().toString(), txtPassword.getText().toString().trim(), byteArray);

                            if (id > 0) {
                                Toast.makeText(form_registro.this, "Usuario registrado", Toast.LENGTH_LONG).show();
                                limpiar();
                                finish();
                            } else {
                                Toast.makeText(form_registro.this, "Error al guardar registro", Toast.LENGTH_LONG).show();
                            }
                            Intent form_login = new Intent(form_registro.this, MainActivity.class);
                            startActivity(form_login);


                        } else {
                            Toast.makeText(form_registro.this, "Nombre y contraseña 8 caractares min", Toast.LENGTH_LONG).show();
                        }


                    }else{
                        Toast.makeText(form_registro.this, "Campo fehca no puede quedar vacio", Toast.LENGTH_LONG).show();
                    }


                    } else {
                        Toast.makeText(form_registro.this, "Usuario existente", Toast.LENGTH_LONG).show();
                    }

            }
        });

        txtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fecha:
                        showDatePickerDialog1();
                        break;
                }
            }
        });

    }




    public String compruebaSexo() {
        String sexo;
        if (foto_hombre.isChecked()) {
            sexo = "Mujer";
        } else {
            sexo = "Hombre";
        }
        return sexo;
    }
    public boolean compruebaFecha(){
        if(txtFecha.getText().toString().isEmpty()){
            return false;
        }else{
            return true;
        }
    }

    private void showDatePickerDialog1() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + " / " + (month + 1) + " / " + year;
                txtFecha.setText(selectedDate);
                fechaIntroducida=selectedDate;

                Calendar fechaActual = Calendar.getInstance();
                Calendar fechaNacimiento = Calendar.getInstance();

                // Establecer la fecha de nacimiento en base a la fecha introducida
                fechaNacimiento.set(Calendar.YEAR, year);
                fechaNacimiento.set(Calendar.MONTH, month);
                fechaNacimiento.set(Calendar.DAY_OF_MONTH, day);

                // Calcular la diferencia en años
                int diffYears = fechaActual.get(Calendar.YEAR) - fechaNacimiento.get(Calendar.YEAR);
                if (fechaActual.get(Calendar.MONTH) < fechaNacimiento.get(Calendar.MONTH) ||
                        (fechaActual.get(Calendar.MONTH) == fechaNacimiento.get(Calendar.MONTH) &&
                                fechaActual.get(Calendar.DAY_OF_MONTH) < fechaNacimiento.get(Calendar.DAY_OF_MONTH))) {
                    diffYears--;

                }

                // Verificar si la diferencia en años es menor a 16
                if (diffYears < 16) {
                    Toast.makeText(form_registro.this, "No puedes registrarte, debes tener al menos 16 años.", Toast.LENGTH_LONG).show();

                }else{
                    btnGuardar.setEnabled(true);
                }
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    private void limpiar() {
        txtNombre.setText("");
        txtFecha.setText("");
    }

    public boolean valida_nombre() {
        if (txtNombre.getText().toString().isEmpty() || txtNombre.getText().toString().length() < 8) {

            return false;
        } else {
            return true;
        }
    }
    public boolean valida_pass() {
        if (txtPassword.getText().toString().isEmpty() || txtPassword.getText().toString().length() < 8) {

            return false;
        } else {
            return true;
        }
    }

    public boolean compararUsuario(String usuarioBuscado) {

        SQLiteDatabase db = conexion.getReadableDatabase();
        Cursor cursor = null;
        String nombreUsuario;
        boolean usuarioExiste = false;

        try {
            cursor = db.rawQuery("SELECT nombre FROM usuario_tb where nombre = ?", new String[]{usuarioBuscado});
            if (cursor != null && cursor.moveToFirst()) {
                nombreUsuario = cursor.getString(0);

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





    private void chooseImageFromGallery() {

            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImage = data.getData();
            eleccionIMG= data.getData();
            image.setImageURI(eleccionIMG);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byteArray = stream.toByteArray();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }
    @Override
    public void onBackPressed() {
        // Cerrar la actividad actual
        finish();
    }

}

