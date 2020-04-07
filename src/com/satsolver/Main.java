package com.satsolver;

import com.sat.Clause;
import com.sat.SAT;
import com.sat.Solution;

import static java.lang.System.nanoTime;

public class Main {
    public static void main(String[] args){
        SAT[] sats = SATLoader.loads("benchmark");
        int count = 0;
        double t1, t2;
        t1 = nanoTime();
        for(int i=0;i<sats.length;i++){
            GeneticAlgorithm GA = new GeneticAlgorithm(sats[i]);
            Solution s = GA.solve(500);
            System.out.println(i + ", " + sats[i].clauseScore(s));
            if(sats[i].clauseScore(s) == sats[i].getClauses().length){
                count++;
            }
        }

        t2 = nanoTime();
        System.out.println("Done. Accuracy = " + (double)count/sats.length);
        System.out.println("Execution time = " + (t2-t1)/1000000000);
    }

    public static void testSimpleExample(){
        Clause c1 = new Clause(new int[]{0, 1, 2}, new boolean[]{true, false, true});
        Clause c2 = new Clause(new int[]{0, 1, 2}, new boolean[]{false, false, true});
        Clause c3 = new Clause(new int[]{0, 2}, new boolean[]{false, false});

        Clause[] clauses = new Clause[]{c1,c2,c3};
        SAT sat = new SAT(clauses, 3);


        GeneticAlgorithm GA = new GeneticAlgorithm(sat);
        Solution s = GA.solve(100);
        System.out.println(s.toString() + " \nScore : " + sat.clauseScore(s));
    }

    public static void testBenchmarks(){
        SAT[] sats = SATLoader.loads("benchmark");
        for(int i=0;i<sats.length;i++){
            GeneticAlgorithm GA = new GeneticAlgorithm(sats[i]);
            Solution s = GA.solve(2000);
            System.out.println(i + ", " + sats[i].clauseScore(s));
        }

        System.out.println("Done");
    }

    public static void testOneBenchmark(){
        SAT sat = SATLoader.load("benchmark/uf75-01.cnf");
        int num_test = 20;
        for(int i=0;i<num_test;i++){
            GeneticAlgorithm GA = new GeneticAlgorithm(sat);
            Solution s = GA.solve(1000);
            System.out.println(i + ", " + sat.clauseScore(s));
        }

        System.out.println("Done");
    }
}
