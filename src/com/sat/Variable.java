package com.sat;

public class Variable {
    private static int id = 0;
    private String name;

    public Variable(String name){
        this.name = name;
    }

    public Variable(){
        this.name = "V" + String.valueOf(id);
        id++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
