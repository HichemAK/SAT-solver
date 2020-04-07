package com.sat;


public class Solution {
    boolean[] config;

    public Solution(boolean[] config) {
        this.config = config;
    }

    public Solution(Solution s) {
        this.config = s.getConfig().clone();
    }

    public Solution(int numVar) {
        this.config = new boolean[numVar];
    }

    public boolean[] getConfig() {
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
