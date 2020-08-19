package com.satsolver;

import com.sat.SAT;
import com.sat.Solution;

import java.util.*;

public class BSO {
    private final double suffFitness;
    private SAT sat;
    private int num_bees = 20;
    private int flip = 2;

    public BSO(SAT sat){
        this.sat = sat;
        this.suffFitness = sat.getClauses().length;
    }

    public Solution solve(int numGen){
        HashSet<Solution> tabu_list = new HashSet<>();
        int count = 0;
        Solution ref = samplePop();
        tabu_list.add(ref);
        while(sat.clauseScore(ref) < suffFitness && count < numGen){
            ArrayList<Solution> bees = local_search(ref);
            bees = local_searches(bees);
            bees = tabu_remove(bees, tabu_list);
            if(bees.size() == 0){
                break;
            }
            ref = bestScore(bees);
            tabu_list.add(ref);
            count++;
        }
        return bestScore(tabu_list);
    }

    private ArrayList<Solution> local_searches(ArrayList<Solution> bees) {
        ArrayList<Solution> result = new ArrayList<>();
        for(int i=0;i<bees.size();i++){
            result.addAll(local_search(bees.get(i)));
        }
        bees.addAll(result);
        return bees;
    }

    public Solution samplePop() {
        Random rand = new Random();
        Solution s = new Solution(sat.getNumVar());
        for (int i = 0; i < s.getConfig().length; i++) {
            s.getConfig()[i] = rand.nextBoolean();
        }
        return s;
    }

    public ArrayList<Solution> local_search(Solution ref){
        ArrayList<Solution> result = new ArrayList<>();
        ArrayList<Integer> sample = new ArrayList<>();
        for(int i = 0; i < ref.getConfig().length; i++) {
            sample.add(i);
        }
        for(int i=0;i<num_bees;i++){
            boolean[] s = ref.getConfig().clone();
            Collections.shuffle(sample);
            for(int j=0;j<flip;j++){
                s[sample.get(j)] = !s[sample.get(j)];
            }
            result.add(new Solution(s));
        }
        return result;
    }

    public Solution bestScore(ArrayList<Solution> bees){
        Solution bestSol = null;
        double bestScore = -1;
        for(int i = 0;i<bees.size();i++){
            double clause_score = sat.clauseScore(bees.get(i));
            if(clause_score > bestScore){
                bestScore = clause_score;
                bestSol = bees.get(i);
            }
        }
        return bestSol;
    }

    public Solution bestScore(HashSet<Solution> bees){

        Solution bestSol = null;
        double bestScore = -1;
        for (Solution s : bees){
            double clause_score = sat.clauseScore(s);
            if(clause_score > bestScore){
                bestScore = clause_score;
                bestSol = s;
            }
        }
        return bestSol;
    }

    public ArrayList<Solution> tabu_remove(ArrayList<Solution> bees, HashSet<Solution> tabu_list){
        bees.removeAll(tabu_list);
        return bees;
    }
}
