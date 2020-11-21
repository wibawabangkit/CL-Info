package com.example.testkkp.model;

public class ModConfirm {
    String jlh_iuran, nama_bank, nama_peng, nik, no_rek, status, waktu;

    public ModConfirm() {

    }

    public ModConfirm(String jlh_iuran, String nama_bank, String nama_peng, String nik, String no_rek, String status, String waktu) {
        this.jlh_iuran = jlh_iuran;
        this.nama_bank = nama_bank;
        this.nama_peng = nama_peng;
        this.nik = nik;
        this.no_rek = no_rek;
        this.status = status;
        this.waktu = waktu;
    }

    public String getJlh_iuran() {
        return jlh_iuran;
    }

    public void setJlh_iuran(String jlh_iuran) {
        this.jlh_iuran = jlh_iuran;
    }

    public String getNama_bank() {
        return nama_bank;
    }

    public void setNama_bank(String nama_bank) {
        this.nama_bank = nama_bank;
    }

    public String getNama_peng() {
        return nama_peng;
    }

    public void setNama_peng(String nama_peng) {
        this.nama_peng = nama_peng;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNo_rek() {
        return no_rek;
    }

    public void setNo_rek(String no_rek) {
        this.no_rek = no_rek;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }
}
