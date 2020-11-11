package com.sholeh.monitorig.mrecycler.model;

public class ChildModel {

    String nama_barang;
    String status;

    public ChildModel(String nama_barang, String status) {
        this.nama_barang = nama_barang;
        this.status = status;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }
}
