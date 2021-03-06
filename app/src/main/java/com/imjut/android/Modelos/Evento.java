package com.imjut.android.Modelos;

/**
 * Created by Angel on 11/01/2018.
 */

public class Evento {
    private String postImageUrl;
    private String titulo;
    private String descripcion;
    private String postId;
    private String direccion;
    private long timeCreated;
    private long timeEnd;
    private long date;
    private long hour;


    public Evento(){

    }

    public Evento(String postImageUrl, String titulo, String descripcion, String postId, String direccion, long timeCreated, long timeEnd, long date, long hour) {
        this.postImageUrl = postImageUrl;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.postId = postId;
        this.direccion = direccion;
        this.timeCreated = timeCreated;
        this.timeEnd = timeEnd;
        this.date = date;
        this.hour = hour;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
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

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getHour() {
        return hour;
    }

    public void setHour(long hour) {
        this.hour = hour;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
