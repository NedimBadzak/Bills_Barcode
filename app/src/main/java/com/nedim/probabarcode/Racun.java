package com.nedim.probabarcode;

public class Racun {
    private String ime;
    private String referenca;
    private String lokacija;
    private double iznos;
    private String skenirano;

    public Racun(String ime, String referenca, double iznos) {
        this.ime = ime;
        this.referenca = referenca;
        this.iznos = iznos;
    }

    public Racun(String ime, String referenca) {
        this.ime = ime;
        this.referenca = referenca;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getReferenca() {
        return referenca;
    }

    public void setReferenca(String referenca) {
        this.referenca = referenca;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public double getIznos() {
        return iznos;
    }

    public void setIznos(double iznos) {
        this.iznos = iznos;
    }

    public String getSkenirano() {
        return skenirano;
    }

    public void setSkenirano(String skenirano) {
        this.skenirano = skenirano;
    }
}
