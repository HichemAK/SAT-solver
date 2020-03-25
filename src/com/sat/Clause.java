package com.sat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Clause {
    private HashMap<Variable, Boolean> variablesNot;

    public Clause(HashMap<Variable, Boolean> variablesNot){
        this.variablesNot = variablesNot;
    }

    public HashMap<Variable, Boolean> getVariablesNot() {
        return variablesNot;
    }

    public void setVariablesNot(HashMap<Variable, Boolean> variables_not) {
        this.variablesNot = variables_not;
    }

    public boolean evaluate(HashMap<Variable, Boolean> variableStates){
        for(Map.Entry m : variablesNot.entrySet()){
            if((Boolean)(m.getValue()) && variableStates.get(m.getKey()) ||
                    !(Boolean)(m.getValue()) && !variableStates.get(m.getKey())){
                return true;
            }
        }
        return false;
    }
}
