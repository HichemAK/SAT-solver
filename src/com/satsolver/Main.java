package com.satsolver;

import com.sat.Clause;
import com.sat.SAT;
import com.sat.Solution;

public class Main {
    public static void main(String[] args){
        Clause c1 = new Clause(new Integer[]{0, 1, 2}, new Boolean[]{true, false, true});
        Clause c2 = new Clause(new Integer[]{0, 1, 2}, new Boolean[]{false, false, true});
        Clause c3 = new Clause(new Integer[]{0, 2}, new Boolean[]{false, false});

        Clause[] clauses = new Clause[]{c1,c2,c3};
        SAT sat = new SAT(clauses, 3);


        GeneticAlgorithm GA = new GeneticAlgorithm(sat);
        Solution s = GA.solve(100);
        System.out.println(s.toString() + " Score : " + sat.clauseScore(s));
    }
}
