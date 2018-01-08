package com.example.angel.imjut.Modelos;

/**
 * Created by Angel on 07/01/2018.
 */

public class Programa {
    private String titulo;
    private String objetivos;
    private String uid;

    public Programa(){

    }
    public Programa(String titulo, String objetivos, String uid) {
        this.titulo = titulo;
        this.objetivos = objetivos;
        this.uid = uid;
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
}
