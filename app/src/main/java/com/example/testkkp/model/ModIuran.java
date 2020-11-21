package com.example.testkkp.model;

public class ModIuran {
    String waktu, jlh_iuran, status, nik;

    public ModIuran() {

    }

    public ModIuran(String waktu, String jlh_iuran, String status, String nik) {
        this.waktu = waktu;
        this.jlh_iuran = jlh_iuran;
        this.status = status;
        this.nik = nik;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getJlh_iuran() {
        return jlh_iuran;
    }

    public void setJlh_iuran(String jlh_iuran) {
        this.jlh_iuran = jlh_iuran;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }
}
