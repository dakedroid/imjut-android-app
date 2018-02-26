package com.imjut.android.Modelos;

/**
 * Created by Angel on 17/01/2018.
 */

public class BolsaTrabajo {

    private String marca;
    private String postImageUrl;
    private String postId;
    private long timeCreated;
    private String educacion;
    private String descripcionGeneral;
    private String tipoPuesto;
    private String sueldo;
    private String experiencia;
    private String habilidades;
    private String requisitosGenerales;

    public BolsaTrabajo(){

    }

    public BolsaTrabajo(String marca, String postImageUrl, String postId, long timeCreated, String educacion, String descripcionGeneral, String tipoPuesto, String sueldo, String experiencia, String habilidades, String requisitosGenerales) {
        this.marca = marca;
        this.postImageUrl = postImageUrl;
        this.postId = postId;
        this.timeCreated = timeCreated;
        this.educacion = educacion;
        this.descripcionGeneral = descripcionGeneral;
        this.tipoPuesto = tipoPuesto;
        this.sueldo = sueldo;
        this.experiencia = experiencia;
        this.habilidades = habilidades;
        this.requisitosGenerales = requisitosGenerales;
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

    public String getEducacion() {
        return educacion;
    }

    public void setEducacion(String educacion) {
        this.educacion = educacion;
    }

    public String getDescripcionGeneral() {
        return descripcionGeneral;
    }

    public void setDescripcionGeneral(String descripcionGeneral) {
        this.descripcionGeneral = descripcionGeneral;
    }

    public String getTipoPuesto() {
        return tipoPuesto;
    }

    public void setTipoPuesto(String tipoPuesto) {
        this.tipoPuesto = tipoPuesto;
    }

    public String getSueldo() {
        return sueldo;
    }

    public void setSueldo(String sueldo) {
        this.sueldo = sueldo;
    }

    public String getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(String experiencia) {
        this.experiencia = experiencia;
    }

    public String getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(String habilidades) {
        this.habilidades = habilidades;
    }

    public String getRequisitosGenerales() {
        return requisitosGenerales;
    }

    public void setRequisitosGenerales(String requisitosGenerales) {
        this.requisitosGenerales = requisitosGenerales;
    }
}
