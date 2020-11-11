package com.sholeh.monitorig.mrecycler;

public class ModelK {
    String idkeluhan;
    String idmaintenance;
    String iduser;
    String namauser;
    String namalab;
    String namabarang;
    String tanggal;
    String status_barang;
    String foto;
    String foto_kerusakan;



    public ModelK(String idkeluhan, String idmaintenance, String iduser, String namauser, String namalab, String namabarang, String tanggal, String status_barang, String foto, String foto_kerusakan) {
        this.idkeluhan = idkeluhan;
        this.idmaintenance = idmaintenance;
        this.iduser = iduser;
        this.namauser = namauser;
        this.namalab = namalab;
        this.namabarang = namabarang;
        this.tanggal = tanggal;
        this.foto = foto;
        this.foto_kerusakan = foto_kerusakan;
        this.status_barang = status_barang;

    }
    public String getIdkeluhan() {
        return idkeluhan;
    }

    public String getNamauser() {
        return namauser;
    }


    public String getIduser() {
        return iduser;
    }

    public String getNamalab() {
        return namalab;
    }

    public String getNamabarang() {
        return namabarang;
    }

    public String getTanggal() {
        return tanggal;
    }
    public String getFoto() {
        return foto;
    }

    public String getFoto_kerusakan() {
        return foto_kerusakan;
    }

    public String getStatus_barang() {
        return status_barang;
    }

    public String getIdmaintenance() {
        return idmaintenance;
    }

}
