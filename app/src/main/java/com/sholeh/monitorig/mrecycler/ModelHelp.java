package com.sholeh.monitorig.mrecycler;

public class ModelHelp {
    String nama;
    String nohp;
    String status;
    String fotopop;

    public ModelHelp(String nama, String nohp, String status, String fotopop) {
        this.nama = nama;
        this.nohp = nohp;
        this.status = status;
        this.fotopop = fotopop;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getFotopop() {
        return fotopop;
    }

    public void setFotopop(String foto) {
        this.fotopop = fotopop;
    }


}
