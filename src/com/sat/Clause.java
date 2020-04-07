package com.sat;


public class Clause {
    private int[] variables;
    private boolean[] notOp;

    public Clause(int[] variables, boolean[] notOp) {
        this.variables = variables;
        this.notOp = notOp;
    }

    public boolean evaluate(Solution s){
        for(int i=0; i<variables.length; i++){
            if(s.getConfig()[variables[i]] == notOp[i]){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        for(int i=0;i<variables.length;i++){
            if(!notOp[i]){
                str.append("-");
            }
            str.append(variables[i]);
            str.append(" ");
        }
        return str.toString();
    }
}
