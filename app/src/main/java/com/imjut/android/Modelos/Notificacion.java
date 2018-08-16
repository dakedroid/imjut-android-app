package com.imjut.android.Modelos;

public class Notificacion{
    private String titulo;
    private String descripcion;
    private long timeCreated;
    private String postId;

    public Notificacion(){

    }

    public Notificacion(String titulo, String descripcion, long timeCreated, String postId) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.timeCreated = timeCreated;
        this.postId = postId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
