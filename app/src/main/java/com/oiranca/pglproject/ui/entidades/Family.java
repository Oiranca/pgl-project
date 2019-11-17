package com.oiranca.pglproject.ui.entidades;

public class Family {



    String nameF;
    String surnameF;
    String emailF;
    String passF;
    String rangeF;
    String idFam;
    String emailAdm;

    public Family() {
    }

    public String getNameF() {
        return nameF;
    }

    public void setNameF(String nameF) {
        this.nameF = nameF;
    }

    public String getSurnameF() {
        return surnameF;
    }

    public void setSurnameF(String surnameF) {
        this.surnameF = surnameF;
    }

    public String getEmailF() {
        return emailF;
    }

    public void setEmailF(String emailF) {
        this.emailF = emailF;
    }

    public String getPassF() {
        return passF;
    }

    public void setPassF(String passF) {
        this.passF = passF;
    }

    public String getRangeF() {
        return rangeF;
    }

    public void setRangeF(String rangeF) {
        this.rangeF = rangeF;
    }

    public String getIdFam() {
        return idFam;
    }

    public void setIdFam(String idFam) {
        this.idFam = idFam;
    }

    public String getEmailAdm() {
        return emailAdm;
    }

    public void setEmailAdm(String emailAdm) {
        this.emailAdm = emailAdm;
    }

    @Override
    public String toString() {
        return "Family{" +
                "nameF='" + nameF + '\'' +
                ", surnameF='" + surnameF + '\'' +
                ", emailF='" + emailF + '\'' +
                ", passF='" + passF + '\'' +
                ", rangeF='" + rangeF + '\'' +
                ", idFam='" + idFam + '\'' +
                ", emailAdm='" + emailAdm + '\'' +
                '}';
    }
}
