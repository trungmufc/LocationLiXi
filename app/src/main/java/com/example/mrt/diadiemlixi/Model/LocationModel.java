package com.example.mrt.diadiemlixi.Model;

/**
 * Created by trungpham on 20/10/2017.
 */

public class LocationModel {
    private int id;
    private String name;
    private int parent_id;


    public LocationModel(int id, String name, int parent_id) {
        this.id = id;
        this.name = name;
        this.parent_id = parent_id;
    }


    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
