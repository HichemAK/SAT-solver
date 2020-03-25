package com.sat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class SAT {
    private HashSet<Clause> clauses;

    public SAT(HashSet<Clause> clauses){
        this.clauses = clauses;
    }

    public HashSet<Clause> getClauses() {
        return clauses;
    }

    public void setClauses(HashSet<Clause> clauses) {
        this.clauses = clauses;
    }

    public boolean evaluate(HashMap<Variable, Boolean> variableStates){
        for(Clause c : clauses){
            if(!c.evaluate(variableStates)){
                return false;
            }
        }
        return true;
    }

    public HashMap<Clause, Boolean>  clauseEvaluationMask(HashMap<Variable, Boolean> variableStates){
        HashMap<Clause, Boolean> clauseMask = new HashMap<>();
        for(Clause c : clauses){
            clauseMask.put(c, c.evaluate(variableStates));
        }
        return clauseMask;
    }
}
