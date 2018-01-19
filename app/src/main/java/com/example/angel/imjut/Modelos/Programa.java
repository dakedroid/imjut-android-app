package com.example.angel.imjut.Modelos;

/**
 * Created by Angel on 07/01/2018.
 */

public class Programa {
    private String postImageUrl;
    private String titulo;
    private String objetivos;
    private String uid;
    private long timeCreated;

    public Programa(){

    }

    public Programa(String postImageUrl, String titulo, String objetivos, String uid, long timeCreated) {
        this.postImageUrl = postImageUrl;
        this.titulo = titulo;
        this.objetivos = objetivos;
        this.uid = uid;
        this.timeCreated = timeCreated;
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

    public String getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }
}
