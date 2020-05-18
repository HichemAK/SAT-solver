package com.satsolver;

import com.sat.Solution;

public class Particle {
    private Solution position;
    private Solution best_sol;
    private Solution velocity;
    private double score;

    public Particle(Solution position, Solution best_sol, Solution velocity){
        this.position = position;
        this.best_sol = best_sol;
        this.velocity = velocity;
        this.score = 0;
    }
    public Particle(Solution position){
        this(position, position, null);
    }

    public Solution getPosition() {
        return position;
    }

    public Solution getBest_sol() {
        return best_sol;
    }

    public Solution getVelocity() {
        return velocity;
    }

    public void setPosition(Solution position) {
        this.position = position;
    }

    public void setBest_sol(Solution best_sol) {
        this.best_sol = best_sol;
    }

    public void setVelocity(Solution velocity) {
        this.velocity = velocity;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
