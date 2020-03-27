package com.satsolver;

import com.sat.Clause;
import com.sat.SAT;
import com.sat.Solution;
import com.sat.Variable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Main {
    public static void main(String[] args){
        HashSet<Variable> variables = new HashSet<>();
        Variable a = new Variable();
        Variable b = new Variable();
        Variable c = new Variable();
        variables.add(a);
        variables.add(b);
        variables.add(c);

        HashMap<Variable, Boolean> VS1 = new HashMap<>();
        VS1.put(a, true);
        VS1.put(b, false);
        VS1.put(c, true);

        HashMap<Variable, Boolean> VS2 = new HashMap<>();
        VS2.put(a, false);
        VS2.put(b, false);
        VS2.put(c, true);

        HashMap<Variable, Boolean> VS3 = new HashMap<>();
        VS3.put(a, false);
        VS3.put(c, false);
        Clause c1 = new Clause(VS1);
        Clause c2 = new Clause(VS2);
        Clause c3 = new Clause(VS3);

        HashSet<Clause> clauses = new HashSet<>(Arrays.asList(c1,c2,c3));
        SAT sat = new SAT(clauses);

        GeneticAlgorithm GA = new GeneticAlgorithm(sat);
        Solution s = GA.solve(100);
        System.out.println(s.toString() + " Score : " + sat.clauseScore(s));
    }
}
