package com.example.angel.imjut.Modelos;

/**
 * Created by Angel on 12/01/2018.
 */

public class Foto {
    private String imageUrl;
    private String postId;
    private String descripcion;

    public Foto(){

    }

    public Foto(String imageUrl, String postId, String descripcion) {
        this.imageUrl = imageUrl;
        this.postId = postId;
        this.descripcion = descripcion;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
