package com.sat;


public class Clause {
    private Integer[] variables;
    private Boolean[] notOp;

    public Clause(Integer[] variables, Boolean[] notOp) {
        this.variables = variables;
        this.notOp = notOp;
    }

    public boolean evaluate(Solution s){
        for(int i=0; i<variables.length; i++){
            if(s.getConfig()[variables[i]].equals(notOp[i])){
                return true;
            }
        }
        return false;
    }
}
