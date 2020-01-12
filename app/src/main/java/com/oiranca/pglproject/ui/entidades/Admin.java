package com.oiranca.pglproject.ui.entidades;

public class Admin {

    String name;
    String surname;
    String email;
    String pass;
    String range;
    String idAdm;



    public Admin(String name, String surname, String email, String pass, String range, String idAdm) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.pass = pass;
        this.range = range;
        this.idAdm = idAdm;
    }

    public Admin() {
    }

    public String getIdAdm() {
        return idAdm;
    }

    public void setIdAdm(String idAdm) {
        this.idAdm = idAdm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }


}
