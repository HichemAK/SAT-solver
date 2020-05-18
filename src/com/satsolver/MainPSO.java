package com.satsolver;

import com.sat.Clause;
import com.sat.SAT;
import com.sat.Solution;

import java.util.ArrayList;

import static com.satsolver.Utils.*;
import static com.satsolver.Utils.mean;
import static java.lang.System.nanoTime;

public class MainPSO {
    public static void main(String[] args){
        calculateStatsOnBenchmark(1);
    }

    public static void testSimpleExample(){
        Clause c1 = new Clause(new int[]{0, 1, 2}, new boolean[]{true, false, true});
        Clause c2 = new Clause(new int[]{0, 1, 2}, new boolean[]{false, false, true});
        Clause c3 = new Clause(new int[]{0, 2}, new boolean[]{false, false});

        Clause[] clauses = new Clause[]{c1,c2,c3};
        SAT sat = new SAT(clauses, 3);


        PSO pso = new PSO(sat);
        Solution s = pso.solve(100);
        System.out.println(s.toString() + " \nScore : " + sat.clauseScore(s));
    }

    public static void testBenchmarks(){
        SAT[] sats = SATLoader.loads("benchmark");
        for(int i=0;i<sats.length;i++){
            PSO pso = new PSO(sats[i]);
            Solution s = pso.solve(2000);
            System.out.println(i + ", " + sats[i].clauseScore(s));
        }

        System.out.println("Done");
    }

    public static void testOneBenchmark(){
        SAT sat = SATLoader.load("benchmark/uf75-01.cnf");
        int num_test = 20;
        for(int i=0;i<num_test;i++){
            PSO pso = new PSO(sat);
            Solution s = pso.solve(1000);
            System.out.println(i + ", " + sat.clauseScore(s));
        }

        System.out.println("Done");
    }

    public static void calculateStatsOnBenchmark(int numTry) {
        SAT[] sats = SATLoader.loads("benchmark");
        ArrayList<Integer> TR = new ArrayList<>();
        ArrayList<Double> SM = new ArrayList<>();
        ArrayList<Double> TEMG = new ArrayList<>();
        ArrayList<Double> TEMR = new ArrayList<>();
        ArrayList<Double> TEME = new ArrayList<>();
        int count = 0;
        double t1, t2, t, score;
        int success;

        for (int j = 0; j < numTry; j++) {
            for (int i = 0; i < sats.length; i++) {
                PSO pso = new PSO(sats[i]);
                t1 = nanoTime();
                Solution s = pso.solve(500);
                t2 = nanoTime();
                t = (t2 - t1) / 1000000000;
                System.out.println((j * sats.length + i + 1) + "/" + (numTry * sats.length));
                score = sats[i].clauseScore(s);
                success = (score == sats[i].getClauses().length ? 1 : 0);
                SM.add(score);
                TR.add(success);
                TEMG.add(t);
                if (success == 1) {
                    TEMR.add(t);
                } else {
                    TEME.add(t);
                }
            }
        }
        System.out.println("TR = " + mean2(TR));
        double mean = mean(SM);
        System.out.println("SM = " + mean);
        System.out.println("SET = " + Math.sqrt(variance(SM, mean)));
        System.out.println("TEMG = " + mean(TEMG));
        System.out.println("TEMR = " + mean(TEMR));
        System.out.println("TEME = " + mean(TEME));
    }
}
