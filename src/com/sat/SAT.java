package com.sat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class SAT {
    private HashSet<Clause> clauses;
    private HashSet<Variable> variables;

    public SAT(HashSet<Clause> clauses){
        this.clauses = clauses;
        variables = new HashSet<>();
        for(Clause c : clauses){
            for(Map.Entry m : c.getVariablesNot().entrySet()){
                variables.add((Variable) m.getKey());
            }
        }
    }

    public HashSet<Clause> getClauses() {
        return clauses;
    }

    public void setClauses(HashSet<Clause> clauses) {
        this.clauses = clauses;
    }

    public HashSet<Variable> getVariables(){
        return variables;
    }

    public boolean evaluate(Solution s){
        for(Clause c : clauses){
            if(!c.evaluate(s)){
                return false;
            }
        }
        return true;
    }

    public HashMap<Clause, Boolean>  clauseEvaluationMask(Solution s){
        HashMap<Clause, Boolean> clauseMask = new HashMap<>();
        for(Clause c : clauses){
            clauseMask.put(c, c.evaluate(s));
        }
        return clauseMask;
    }

    public double clauseScore(Solution s){
        int score = 0;
        for(Map.Entry m : clauseEvaluationMask(s).entrySet()){
            score += (Boolean)m.getValue() ? 1 : 0;
        }
        score /= clauses.size();
        return score;
    }
}
