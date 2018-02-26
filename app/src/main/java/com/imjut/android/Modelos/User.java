package com.imjut.android.Modelos;

/**
 * Created by Angel on 09/12/2017.
 */

public class User {
    private String name;
    private String apellido;
    private String edad;
    private String email;
    private String uid;
    private boolean permisos_admin;

    public User(){

    }

    public User(String name, String apellido, String edad, String email, String uid, boolean permisos_admin) {
        this.name = name;
        this.apellido = apellido;
        this.edad = edad;
        this.email = email;
        this.uid = uid;
        this.permisos_admin = permisos_admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public boolean isPermisos_admin() {
        return permisos_admin;
    }

    public void setPermisos_admin(boolean permisos_admin) {
        this.permisos_admin = permisos_admin;
    }
}
