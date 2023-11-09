package com.example.reproductordemusica.Adapters;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.recyclerview.widget.RecyclerView;


import com.example.reproductordemusica.Vistas.MainActivity;
import com.example.reproductordemusica.R;
import com.example.reproductordemusica.Vistas.Reproductor;
import com.example.reproductordemusica.clases_objetos.AdudioModel;
import com.example.reproductordemusica.clases_objetos.MediaPlayerI;

import java.util.ArrayList;

public class MusicaAdapter extends RecyclerView.Adapter<MusicaAdapter.ViewHolder>{

    ArrayList<AdudioModel>listaCanciones;
    Context context;

    public MusicaAdapter(ArrayList<AdudioModel> listaCanciones, Context context) {
        this.listaCanciones = listaCanciones;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false);
       return new MusicaAdapter.ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(MusicaAdapter.ViewHolder holder,
                                 int position) {

        AdudioModel datosCancion= listaCanciones.get(holder.getAdapterPosition());
        holder.titulo.setText(datosCancion.getTitle());



        holder.titulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                MediaPlayerI.getInstance().reset();
                MediaPlayerI.currentIndex=(holder.getAdapterPosition());

                Intent intent =new Intent(context, Reproductor.class);
                intent.putExtra("Lista",listaCanciones);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }


    @Override
    public int getItemCount() {
        return listaCanciones.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView titulo;
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);

            titulo=itemView.findViewById(R.id.tituloCancion);
            img=itemView.findViewById(R.id.icono);

        }
    }
}
