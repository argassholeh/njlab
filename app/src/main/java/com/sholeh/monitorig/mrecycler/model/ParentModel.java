package com.sholeh.monitorig.mrecycler.model;

import org.json.JSONArray;

public class ParentModel {

    String nama_barang, total_barang, total_normal, total_rusak;
    JSONArray barang;

    public ParentModel(String nama_barang, String total_barang, String total_normal, String total_rusak, JSONArray barang) {
        this.nama_barang = nama_barang;
        this.total_barang = total_barang;
        this.total_normal = total_normal;
        this.total_rusak = total_rusak;
        this.barang = barang;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public JSONArray getBarang() {
        return barang;
    }

    public void setBarang(JSONArray barang) {
        this.barang = barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getTotal_barang() {
        return total_barang;
    }

    public void setTotal_barang(String total_barang) {
        this.total_barang = total_barang;
    }

    public String getTotal_normal() {
        return total_normal;
    }

    public void setTotal_normal(String total_normal) {
        this.total_normal = total_normal;
    }

    public String getTotal_rusak() {
        return total_rusak;
    }

    public void setTotal_rusak(String total_rusak) {
        this.total_rusak = total_rusak;
    }
}
