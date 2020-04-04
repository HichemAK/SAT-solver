package com.sat;


public class Solution {
    Boolean[] config;

    public Solution(Boolean[] config) {
        this.config = config;
    }

    public Solution(Solution s) {
        this.config = s.getConfig().clone();
    }

    public Solution(int numVar) {
        this.config = new Boolean[numVar];
    }

    public Boolean[] getConfig() {
        return config;
    }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer("[");
        for(int i=0;i<config.length;i++){
            str.append((config[i] ? 1 : 0) + ", ");
        }
        str.delete(str.length()-2, str.length());
        str.append("]");
        return str.toString();
    }
}
