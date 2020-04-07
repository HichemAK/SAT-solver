package com.sat;

import java.util.ArrayList;

public class SAT {
    private Clause[] clauses;
    private int numVar;

    public SAT(Clause[] clauses, int numVar){
        this.clauses = clauses;
        this.numVar = numVar;
    }

    public boolean evaluate(Solution s){
        for(Clause c : clauses){
            if(!c.evaluate(s)){
                return false;
            }
        }
        return true;
    }

    public int getNumVar() {
        return numVar;
    }

    public int[]  clauseEvaluationMask(Solution s){
        int[] clauseMask = new int[clauses.length];
        for(int i=0;i<clauses.length;i++){
            clauseMask[i] = (clauses[i].evaluate(s) ? 1 : 0);
        }
        return clauseMask;
    }

    public double clauseScore(Solution s){
        double score = 0;
        for(int b : clauseEvaluationMask(s)){
            score += b;
        }
        //score /= clauses.length;
        return score;
    }

    public double clauseScore(int[] mask){
        double score = 0;
        for(int b : mask){
            score += b;
        }
        //score /= clauses.length;
        return score;
    }

    public double clauseScore(int[] mask, double[] weights){
        double score = 0;
        double current_score = 0;
        for(int i=0;i<mask.length;i++){
            current_score = mask[i]*weights[i];
            score += current_score;
        }
        //score /= clauses.length;
        return score;
    }


    public Clause[] getClauses() {
        return clauses;
    }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        for(Clause c : clauses){
            str.append(c.toString());
            str.append("\n");
        }
        return str.toString();
    }
}
