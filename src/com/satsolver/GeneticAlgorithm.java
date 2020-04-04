package com.satsolver;

import com.sat.SAT;
import com.sat.Solution;
import com.sat.Variable;

import java.util.*;

public class GeneticAlgorithm {

    private SAT sat;
    private double mutationRate = 0.05;
    private double crossoverRate = 0.15;
    private int initialPop = 50;
    private double crossoverLength = 0.5;
    private double suffFitness = 1;

    private class Score implements Comparable<Score>{
        int id;
        double score;

        Score(int a, double score){
            this.id = a;
            this.score = score;
        }

        @Override
        public int compareTo(Score o) {
            return Double.compare(score, o.score);
        }
    }

    public GeneticAlgorithm(SAT sat){
        this.sat = sat;
    }

    public Solution solve(int numGen){
        ArrayList<Solution> population = initPop();
        System.out.println("Initpop : " + population.size());
        ArrayList<Double> fitness = calculateFitness(population);
        System.out.println("INIT FITNESS : " + fitness);
        int posBestSolution;
        for(int i=0;i<numGen;i++){
            posBestSolution = getPosBestSolution(fitness);
            if(posBestSolution != -1){
                System.out.println(String.valueOf(posBestSolution) + " " + fitness.get(posBestSolution));
            }
            if(posBestSolution != -1 && fitness.get(posBestSolution) >= suffFitness){
                System.out.println("Number of gens : " + i);
                return population.get(posBestSolution);
            }
            population = selection(population, fitness);
            System.out.println("selection : " + population.size());
            population = crossover(population);
            System.out.println("crossover : " + population.size());
            mutation(population);
            System.out.println("mutation : " + population.size());
            fitness = calculateFitness(population);
            System.out.println("FITNESS : " + fitness);
        }
        return population.get(getPosBestSolution(fitness));
    }

    private int getPosBestSolution(ArrayList<Double> fitness) {
        int posMax = -1;
        double value = -Double.MAX_VALUE;
        for(int i=0;i<fitness.size();i++){
            if(fitness.get(i) > value){
                value = fitness.get(i);
                posMax = i;
            }
        }
        return posMax;
    }

    private void mutation(ArrayList<Solution> population) {
        Random rand = new Random();
        for(Solution s : population){
            for(int i=0;i<s.getConfig().length;i++){
                if(rand.nextDouble() < mutationRate){
                    s.getConfig()[i] = !s.getConfig()[i];
                }
            }
        }
    }

    public Solution samplePop(){
        Random rand = new Random();
        Solution s = new Solution(sat.getNumVar());
        for(int i=0;i<s.getConfig().length;i++){
            s.getConfig()[i] = rand.nextBoolean();
        }
        return s;
    }

    public ArrayList<Solution> initPop(){
        ArrayList<Solution> population = new ArrayList<>();
        for(int i=0;i<initialPop;i++){
            population.add(samplePop());
        }
        return population;
    }

    public ArrayList<Double> calculateFitness(ArrayList<Solution> population){
        ArrayList<Double> fitness = new ArrayList<>();
        for(Solution solution : population){
            fitness.add(sat.clauseScore(solution));
        }
        return fitness;
    }

    public ArrayList<Solution> selection(ArrayList<Solution> population, ArrayList<Double> fitness){
        ArrayList<Score> scores = new ArrayList<>();
        ArrayList<Double> rank = new ArrayList<>(fitness);
        for(int i=0;i<fitness.size();i++){
            scores.add(new Score(i, fitness.get(i)));
        }
        Collections.sort(scores);
        for(int i=0;i<scores.size();i++){
            rank.set(scores.get(i).id, (double) (i + 1));
        }
        Random rand = new Random();
        ArrayList<Double> cumulative = new ArrayList<>();

        int sum = 0;
        for(int i = 0;i < rank.size();i++){
            cumulative.add(rank.get(i) + sum);
            sum += rank.get(i);
        }
        for(int i = 0;i < cumulative.size();i++){
            cumulative.set(i, cumulative.get(i)/sum);
        }

        ArrayList<Solution> newPop = new ArrayList<>();


        for(int i=0;i<initialPop/2;i++){
            double d = rand.nextDouble();
            for(int j=0;j<cumulative.size();j++){
                if(d<cumulative.get(j)){
                    newPop.add(population.get(j));
                    break;
                }
            }
        }
        return newPop;
    }

    public ArrayList<Solution> crossover(ArrayList<Solution> population){
        Random rand = new Random();
        ArrayList<Solution> newPop = new ArrayList<>(population);
        while(newPop.size() < initialPop){
            if(rand.nextDouble() < crossoverRate){
                int pos = Math.abs(rand.nextInt()%population.size());
                Solution s1 = new Solution(population.get(pos));
                pos = Math.abs(rand.nextInt()%population.size());
                Solution s2 = new Solution(population.get(pos));
                crossSolutions(s1, s2);
                newPop.addAll(Arrays.asList(s1, s2));
            }
            else{
                int pos = Math.abs(rand.nextInt()%population.size());
                Solution s1 = new Solution(population.get(pos));
                newPop.add(s1);
                s1 = new Solution(population.get(pos));
                newPop.add(s1);
            }
        }
        return newPop;
    }

    public void crossSolutions(Solution s1, Solution s2){
        Random rand = new Random();
        for(int i=0;i<s1.getConfig().length;i++){
            if(rand.nextDouble() < crossoverLength){
                Boolean temp = s2.getConfig()[i];
                s2.getConfig()[i] = s1.getConfig()[i];
                s1.getConfig()[i] = temp;
            }
        }
    }
}
