package com.example.angel.imjut.Modelos;

/**
 * Created by Angel on 17/01/2018.
 */

public class BolsaTrabajo {

    private String marca;
    private String postImageUrl;
    private String requisitos;
    private String postId;

    public BolsaTrabajo(){

    }

    public BolsaTrabajo(String marca, String postImageUrl, String requisitos, String postId) {
        this.marca = marca;
        this.postImageUrl = postImageUrl;
        this.requisitos = requisitos;
        this.postId = postId;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public String getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
