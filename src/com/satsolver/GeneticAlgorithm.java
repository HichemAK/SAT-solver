package com.satsolver;

import com.sat.SAT;
import com.sat.Solution;

import java.util.*;

import static com.satsolver.Utils.mean;
import static com.satsolver.Utils.variance;

public class GeneticAlgorithm {

    private SAT sat;
    private double mutationRate = 0.05;
    private double mutationChance = 0.2;
    private double crossoverRate = 1;
    private int initialPop = 100;
    private double crossoverLength = 0.3;
    private double suffFitness;
    private double survivalOfGen = 0.5;
    private double[] clauses_weights;
    private boolean firstTimeCalFitness;
    private double alpha = 0.95;
    private double[] frequency;

    private static class Score implements Comparable<Score>{
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
        this.suffFitness = sat.getClauses().length;
        clauses_weights = new double[sat.getClauses().length];
        Arrays.fill(clauses_weights, 0);
        frequency = new double[clauses_weights.length];
        Arrays.fill(frequency, 0);
    }

    public Solution solve(int numGen){
        Arrays.fill(frequency, 0);
        firstTimeCalFitness = true;
        ArrayList<Solution> population = initPop();
        //System.out.println("Initpop : " + population.size());
        double[][] fitnesses = calculateWeightedFitness(population);
        //System.out.println("INIT FITNESS : " + fitness);
        int posBestSolution;
        for(int i=0;i<numGen;i++){
            posBestSolution = getPosBestSolution(fitnesses[0]);
            //System.out.println(i + " , " + mean(fitnesses[0]));
            //System.out.println(i + ", " + fitnesses[0][posBestSolution]);
            if(posBestSolution != -1 && fitnesses[0][posBestSolution] >= suffFitness){
                return population.get(posBestSolution);
            }
            population = selection(population, fitnesses[1]);
            population = crossover(population);
            mutation(population);
            fitnesses = calculateWeightedFitness(population);
        }
        return population.get(getPosBestSolution(fitnesses[0]));
    }

    public Solution solveWithoutWeights(int numGen){
        Arrays.fill(frequency, 0);
        firstTimeCalFitness = true;
        ArrayList<Solution> population = initPop();
        //System.out.println("Initpop : " + population.size());
        double[] fitness = calculateFitness(population);
        //System.out.println("INIT FITNESS : " + fitness);
        int posBestSolution;
        for(int i=0;i<numGen;i++){
            posBestSolution = getPosBestSolution(fitness);
            //System.out.println(i + " , " + mean(fitnesses[0]));
            //System.out.println(i + ", " + fitnesses[0][posBestSolution]);
            if(posBestSolution != -1 && fitness[posBestSolution] >= suffFitness){
                return population.get(posBestSolution);
            }
            population = selection(population, fitness);
            population = crossover(population);
            mutation(population);
            fitness = calculateFitness(population);
        }
        return population.get(getPosBestSolution(fitness));
    }

    private int getPosBestSolution(double[] fitness) {
        int posMax = -1;
        double value = -Double.MAX_VALUE;
        for(int i=0;i<fitness.length;i++){
            if(fitness[i] > value){
                value = fitness[i];
                posMax = i;
            }
        }
        return posMax;
    }

    private void mutation(ArrayList<Solution> population) {
        Random rand = new Random();
        for(Solution s : population){
            if(rand.nextDouble() < mutationChance){
                for(int i=0;i<s.getConfig().length;i++){
                    if(rand.nextDouble() < mutationRate){
                        s.getConfig()[i] = !s.getConfig()[i];
                    }
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

    public double[] calculateFitness(ArrayList<Solution> population){
        double[] fitness = new double[population.size()];
        for(int i=0;i<population.size();i++){
            fitness[i] = sat.clauseScore(population.get(i));
        }
        return fitness;
    }

    public double[][] calculateWeightedFitness(ArrayList<Solution> population){
        double[][] fitnesses = new double[2][population.size()];
        double[] current_frequency = new double[clauses_weights.length];
        Arrays.fill(current_frequency, 0);
        int[][] masks = new int[population.size()][];
        for(int i=0;i<population.size();i++){
            int[] mask = sat.clauseEvaluationMask(population.get(i));
            masks[i] = mask;
            for(int j=0 ; j<clauses_weights.length ; j++){
                current_frequency[j] += mask[j];
            }
        }
        //System.out.println(arrayToString(current_clauses_weights));
        if(firstTimeCalFitness){
            frequency = current_frequency.clone();
            firstTimeCalFitness = false;
        }
        else{
            // Exponential smoothing
            //System.out.println("OLD : " + arrayToString(clauses_weights));
            //System.out.println("NEW : " + arrayToString(current_clauses_weights));
            for(int i=0;i<frequency.length;i++){
                frequency[i] = alpha*frequency[i] + (1-alpha)*current_frequency[i];
            }
            //System.out.println("RESULT : " + arrayToString(frequency));
        }
        for(int i=0;i<clauses_weights.length;i++){
            clauses_weights[i] = 1-frequency[i]/population.size();
        }
        //System.out.println(arrayToString(frequency));
        //System.out.println(arrayToString(clauses_weights));
        for(int i=0;i<population.size();i++){
            fitnesses[0][i] = sat.clauseScore(masks[i]);
            fitnesses[1][i] = sat.clauseScore(masks[i], clauses_weights);
        }
        //System.out.println(arrayToString(fitnesses[1]));
        return fitnesses;
    }

    public ArrayList<Solution> selection(ArrayList<Solution> population, double[] fitness){
        //addNoise(fitness);
        ArrayList<Score> scores = new ArrayList<>();
        double[] rank = new double[fitness.length];
        for(int i=0;i<fitness.length;i++){
            scores.add(new Score(i, fitness[i]));
        }
        Collections.sort(scores);
        for(int i=0;i<scores.size();i++){
            rank[scores.get(i).id] =  (i + 1);
        }
        Random rand = new Random();
        ArrayList<Double> cumulative = new ArrayList<>();

        int sum = 0;
        for(int i = 0;i < rank.length;i++){
            cumulative.add(rank[i] + sum);
            sum += rank[i];
        }
        for(int i = 0;i < cumulative.size();i++){
            cumulative.set(i, cumulative.get(i)/sum);
        }

        ArrayList<Solution> newPop = new ArrayList<>();


        for(int i=0;i<initialPop*survivalOfGen;i++){
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

    public ArrayList<Solution> fitnessUniformSelection(ArrayList<Solution> population, ArrayList<Double> fitness){
        Random rand = new Random();
        ArrayList<Solution> newPop = new ArrayList<>();
        while(newPop.size() < initialPop*survivalOfGen){
            double fmin = Collections.min(fitness), fmax = Collections.max(fitness);
            double randFit = rand.nextDouble() * (fmax - fmin) + fmin;
            double minDistance = Double.MAX_VALUE;
            int pos = -1;
            for(int i=0;i<population.size();i++){
                double distance = Math.abs(fitness.get(i) - randFit);
                if(distance < minDistance){
                    minDistance = distance;
                    pos = i;
                }
            }
            newPop.add(population.get(pos));
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


    private double[] addDiversity(double[] fitness){
        double mean = 0;
        for(double d : fitness){
            mean += d;
        }
        mean /= fitness.length;
        double[] result = new double[fitness.length];
        double variance = 0;
        for(int i=0;i<fitness.length;i++){
            variance += Math.pow(fitness[i] - mean, 2);
        }
        variance /= fitness.length;
        variance = Math.sqrt(variance);

        for(int i=0;i<fitness.length;i++){
            result[i] = (fitness[i] + Math.abs(fitness[i] - mean) * (mean / variance));
        }

        return result;
    }

    private void addNoise(double[] fitness){
        double mean = mean(fitness);
        double variance = variance(fitness, mean);
        //System.out.println("Variance " + variance + " MEAN " + mean);
        Random rand = new Random();
        for(int i=0;i<fitness.length;i++){
            //System.out.println("BEFORE " + fitness[i]);
            fitness[i] += rand.nextDouble() * Math.sqrt(variance) + mean;
            //System.out.println("AFTER " + fitness[i]);
        }
    }

    public void crossSolutions(Solution s1, Solution s2){
        Random rand = new Random();
        int pivot = (int)(crossoverLength*s1.getConfig().length);
        for(int i=0;i<s1.getConfig().length;i++){
            if(rand.nextDouble() < crossoverLength){
                boolean temp = s2.getConfig()[i];
                s2.getConfig()[i] = s1.getConfig()[i];
                s1.getConfig()[i] = temp;
            }
        }
    }






}
