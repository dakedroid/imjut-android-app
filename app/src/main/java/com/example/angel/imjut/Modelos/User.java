package com.example.angel.imjut.Modelos;

/**
 * Created by Angel on 09/12/2017.
 */

public class User {
    private String name;
    private String apellido;
    private String edad;
    private String email;
    private String uid;

    public User(){

    }

    public User(String name, String apellido, String edad, String email, String uid) {
        this.name = name;
        this.apellido = apellido;
        this.edad = edad;
        this.email = email;
        this.uid = uid;
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
}
