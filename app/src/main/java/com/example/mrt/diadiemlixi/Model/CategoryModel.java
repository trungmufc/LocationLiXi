package com.example.mrt.diadiemlixi.Model;

/**
 * Created by trungpham on 19/10/2017.
 */

public class CategoryModel {
    private String name;
    private  int id;
    private boolean check;

    public CategoryModel(String name, int id) {
        this.name = name;
        this.id = id;
        this.check = false;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
