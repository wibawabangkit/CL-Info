package com.example.testkkp.model;

public class ModInfo {
    String id, isi_info, nik, waktu, url_photo_info;

    public ModInfo() {

    }

    public ModInfo(String id, String isi_info, String nik, String waktu, String url_photo_info) {
        this.id = id;
        this.isi_info = isi_info;
        this.nik = nik;
        this.waktu = waktu;
        this.url_photo_info = url_photo_info;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsi_info() {
        return isi_info;
    }

    public void setIsi_info(String isi_info) {
        this.isi_info = isi_info;
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

    public String getUrl_photo_info() {
        return url_photo_info;
    }

    public void setUrl_photo_info(String url_photo_info) {
        this.url_photo_info = url_photo_info;
    }
}
