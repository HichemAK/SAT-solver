package com.sat;

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

    public Boolean[]  clauseEvaluationMask(Solution s){
        Boolean[] clauseMask = new Boolean[clauses.length];
        for(int i=0;i<clauses.length;i++){
            clauseMask[i] = clauses[i].evaluate(s);
        }
        return clauseMask;
    }

    public double clauseScore(Solution s){
        int score = 0;
        for(Boolean b : clauseEvaluationMask(s)){
            score += b ? 1 : 0;
        }
        score /= clauses.length;
        return score;
    }
}
