/*
 * Copyright (c) 2017. Phasmid Software
 */

package edu.neu.coe.info6205.randomwalk;

import java.util.Random;

public class RandomWalk {

    private int x = 0;
    private int y = 0;

    private final Random random = new Random();

    /**
     * Private method to move the current position, that's to say the drunkard moves
     *
     * @param dx the distance he moves in the x direction
     * @param dy the distance he moves in the y direction
     */
    private void move(int dx, int dy) {
        // TO BE IMPLEMENTED  do move
        if (dx != 0 || dy != 0) {
            x += dx;
            y += dy;
        }
        else {
        // SKELETON
            throw new RuntimeException("Not implemented");
        // END SOLUTION
        }
    }

    /**
     * Perform a random walk of m steps
     *
     * @param m the number of steps the drunkard takes
     */
    private void randomWalk(int m) {
        // TO BE IMPLEMENTED
        if (m > 0){
            for (int i = m; i > 0; i--){
                randomMove();
            }
        } else {
            throw new RuntimeException("implementation missing");
        }

    }

    /**
     * Private method to generate a random move according to the rules of the situation.
     * That's to say, moves can be (+-1, 0) or (0, +-1).
     */
    private void randomMove() {
        boolean ns = random.nextBoolean(); // Either true or false
        int step = random.nextBoolean() ? 1 : -1; // If true, then step = 1
        move(ns ? step : 0, ns ? 0 : step); // If ns is true, then move(step,0), otherwise move(0,step)
    }

    /**
     * Method to compute the distance from the origin (the lamp-post where the drunkard starts) to his current position.
     *
     * @return the (Euclidean) distance from the origin to the current position.
     */
    public double distance() {
        // TO BE IMPLEMENTED
        if (x != 0 || y != 0){
             double d = Math.sqrt(x * x + y * y);
             return d;
        } else {
            // SKELETON
            return 0.0;
            // END SOLUTION
        }
    }

    /**
     * Perform multiple random walk experiments, returning the mean distance.
     *
     * @param m the number of steps for each experiment
     * @param n the number of experiments to run
     * @return the mean distance
     */
    public static double randomWalkMulti(int m, int n) {
        double totalDistance = 0;
        for (int i = 0; i < n; i++) {
            RandomWalk walk = new RandomWalk();
            walk.randomWalk(m);
            totalDistance = totalDistance + walk.distance();
        }
        return totalDistance / n;
    }

    public static void main(String[] args) {
        int n = 100;
        int count = 500;
        double x;
        double totalX = 1;

        for (int i = count; i > 1; i--){
            double meanDistance = randomWalkMulti(i, n);
            System.out.println(i + " steps: " + meanDistance + " over " + n + " experiments");

            x = Math.log(i) / Math.log(meanDistance);
            System.out.println("X" + i + " = "+ x);

            totalX += x;
        }

        // calculate the average of x
        double meanX = totalX / count;
        System.out.println("-------------------");
        System.out.println("The mean of x when m from 1 to " + count + " = " + meanX);


    }

}