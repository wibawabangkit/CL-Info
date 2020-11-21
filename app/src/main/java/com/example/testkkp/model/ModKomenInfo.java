package com.example.testkkp.model;

public class ModKomenInfo {
    String id, id_info, isi_komen, nik, waktu;

    public ModKomenInfo() {

    }

    public ModKomenInfo(String id, String id_info, String isi_komen, String nik, String waktu) {
        this.id = id;
        this.id_info = id_info;
        this.isi_komen = isi_komen;
        this.nik = nik;
        this.waktu = waktu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_info() {
        return id_info;
    }

    public void setId_info(String id_info) {
        this.id_info = id_info;
    }

    public String getIsi_komen() {
        return isi_komen;
    }

    public void setIsi_komen(String isi_komen) {
        this.isi_komen = isi_komen;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }
}
