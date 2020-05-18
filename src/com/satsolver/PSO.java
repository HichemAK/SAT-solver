package com.satsolver;

import com.sat.SAT;
import com.sat.Solution;

import java.util.ArrayList;
import java.util.Random;

public class PSO {
    private final double suffFitness;
    private int initialPop = 50;
    private SAT sat;
    private double omega = 0.1;
    private double phi_p = 0.3;
    private double phi_g = 0.5;

    public PSO(SAT sat){
        this.sat = sat;
        this.suffFitness = sat.getClauses().length;
    }

    public Solution solve(int numIter){
        Random rand = new Random();
        int num_dims = sat.getNumVar();
        ArrayList<Particle> population = initPop();
        Particle bestSol = bestScore(population);
        double bestScore = bestSol.getScore();
        int count=0;
        while(bestSol.getScore() < suffFitness && count < numIter){
            for(int i=0;i<population.size();i++){
                Particle par = population.get(i);
                boolean[] p = par.getBest_sol().getConfig();
                boolean[] v = par.getVelocity().getConfig();
                boolean[] x = par.getPosition().getConfig();
                boolean[] g = bestSol.getPosition().getConfig();
                for(int j=0;j<num_dims;j++){
                    double r_p = rand.nextDouble(), r_g = rand.nextDouble();
                    v[j] = (Math.round(omega * (v[j] ? 1 : 0) + phi_p * r_p * ((p[j] ? 1 : 0) - (x[j] ? 1 : 0)) + phi_g * r_g * ((g[j] ? 1 : 0) - (x[j] ? 1 : 0))) % 2) == 1;
                    x[j] = x[j] ^ v[j];
                }
                par.setScore(sat.clauseScore(par.getPosition()));
                if(par.getScore() > sat.clauseScore(par.getBest_sol())){
                    par.setBest_sol(par.getPosition());
                    if(par.getScore() > bestScore){
                        bestSol = par;
                        bestScore = bestSol.getScore();
                    }
                }

            }
            count++;
        }
        return bestSol.getPosition();
    }

    public Particle samplePop(){
        Random rand = new Random();
        Solution s = new Solution(sat.getNumVar());
        Solution v = new Solution(sat.getNumVar());
        Particle p  = new Particle(s,s,v);
        for(int i=0;i<s.getConfig().length;i++){
            p.getPosition().getConfig()[i] = rand.nextBoolean();
            p.getVelocity().getConfig()[i] = rand.nextBoolean();
        }
        return p;
    }

    public ArrayList<Particle> initPop(){
        ArrayList<Particle> population = new ArrayList<>();
        for(int i=0;i<initialPop;i++){
            population.add(samplePop());
        }
        return population;
    }

    public Particle bestScore(ArrayList<Particle> population){
        Particle bestSol = null;
        double bestScore = -1;
        for(int i = 0;i<population.size();i++){
            double clause_score = sat.clauseScore(population.get(i).getPosition());
            population.get(i).setScore(clause_score);
            if(clause_score > bestScore){
                bestScore = clause_score;
                bestSol = population.get(i);
            }
        }
        return bestSol;
    }
}
