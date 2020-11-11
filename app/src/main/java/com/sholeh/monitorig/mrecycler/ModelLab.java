package com.sholeh.monitorig.mrecycler;

public class ModelLab {
    private String idlab;
    private String namalab;
    private String image;


    public ModelLab(String idlab, String namalab, String image){
        super();
        this.idlab = idlab;
        this.namalab = namalab;
        this.image = image;
    }

    public String getIdlab() {
        return idlab;
    }

    public String getNamalab() {
        return namalab;
    }

    public String getImage() {
        return image;
    }
}
