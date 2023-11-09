package com.example.reproductordemusica.clases_objetos;

import java.io.Serializable;

public class AdudioModel implements Serializable {

    int idUsuario;
    String path;
    String title;
    String duration;

    public AdudioModel(int idUsuario, String path, String title, String duration) {
        this.idUsuario = idUsuario;
        this.path = path;
        this.title = title;
        this.duration = duration;
    }

    public AdudioModel(String path, String title, String duration) {
        this.path = path;
        this.title = title;
        this.duration = duration;
    }

    public int getId() {
        return idUsuario;
    }

    public void setId(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
