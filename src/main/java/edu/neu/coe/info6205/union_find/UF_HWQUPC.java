/**
 * Original code:
 * Copyright © 2000–2017, Robert Sedgewick and Kevin Wayne.
 * <p>
 * Modifications:
 * Copyright (c) 2017. Phasmid Software
 */
package edu.neu.coe.info6205.union_find;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * Height-weighted Quick Union with Path Compression
 */
public class UF_HWQUPC implements UF {
    /**
     * Ensure that site p is connected to site q,
     *
     * @param p the integer representing one site
     * @param q the integer representing the other site
     */
    public void connect(int p, int q) {
        if (!isConnected(p, q)) union(p, q);
    }

    /**
     * Initializes an empty union–find data structure with {@code n} sites
     * {@code 0} through {@code n-1}. Each site is initially in its own
     * component.
     *
     * @param n               the number of sites
     * @param pathCompression whether to use path compression
     * @throws IllegalArgumentException if {@code n < 0}
     */
    public UF_HWQUPC(int n, boolean pathCompression) {
        count = n;
        parent = new int[n];
        height = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            height[i] = 1;
        }
        this.pathCompression = pathCompression;
    }

    /**
     * Initializes an empty union–find data structure with {@code n} sites
     * {@code 0} through {@code n-1}. Each site is initially in its own
     * component.
     * This data structure uses path compression
     *
     * @param n the number of sites
     * @throws IllegalArgumentException if {@code n < 0}
     */
    public UF_HWQUPC(int n) {
        this(n, true);
    }

    public void show() {
        for (int i = 0; i < parent.length; i++) {
            System.out.printf("site %d: root %d, height %d\n", i, parent[i], height[i]);
        }
    }

    /**
     * Returns the number of components.
     *
     * @return the number of components (between {@code 1} and {@code n})
     */
    public int components() {
        return count;
    }

    /**
     * Returns the component identifier for the component containing site {@code p}.
     *
     * @param p the integer representing one site
     * @return the component identifier for the component containing site {@code p}
     * @throws IllegalArgumentException unless {@code 0 <= p < n}
     */
    public int find(int p) {
        validate(p);
        int root = p;
        // TO BE IMPLEMENTED

        if (pathCompression){
            doPathCompression(p);
        }

        // find the root
        while(root != parent[root]){
            root = parent[root];
        }

//        System.out.println("the root of " + p + " is "+ root);
        return root;

        //throw new RuntimeException("implementation missing");
    }

    /**
     * Returns true if the two sites are in the same component.
     *
     * @param p the integer representing one site
     * @param q the integer representing the other site
     * @return {@code true} if the two sites {@code p} and {@code q} are in the same component;
     * {@code false} otherwise
     * @throws IllegalArgumentException unless
     *                                  both {@code 0 <= p < n} and {@code 0 <= q < n}
     */
    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    /**
     * Merges the component containing site {@code p} with the
     * the component containing site {@code q}.
     *
     * @param p the integer representing one site
     * @param q the integer representing the other site
     * @throws IllegalArgumentException unless
     *                                  both {@code 0 <= p < n} and {@code 0 <= q < n}
     */
    public void union(int p, int q) {
        // CONSIDER can we avoid doing find again?
        mergeComponents(find(p), find(q));
        count--;
    }

    @Override
    public int size() {
        return parent.length;
    }

    /**
     * Used only by testing code
     *
     * @param pathCompression true if you want path compression
     */
    public void setPathCompression(boolean pathCompression) {
        this.pathCompression = pathCompression;
    }

    @Override
    public String toString() {
        return "UF_HWQUPC:" + "\n  count: " + count +
                "\n  path compression? " + pathCompression +
                "\n  parents: " + Arrays.toString(parent) +
                "\n  heights: " + Arrays.toString(height);
    }

    // validate that p is a valid index
    private void validate(int p) {
        int n = parent.length;
        if (p < 0 || p >= n) {
            throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n - 1));
        }
    }

    private void updateParent(int p, int x) {
        parent[p] = x;
    }

    private void updateHeight(int p, int x) {
        height[p] += height[x];
    }

    /**
     * Used only by testing code
     *
     * @param i the component
     * @return the parent of the component
     */
    private int getParent(int i) {
        return parent[i];
    }

    private final int[] parent;   // parent[i] = parent of i
    private final int[] height;   // height[i] = height of subtree rooted at i
    private int count;  // number of components
    private boolean pathCompression;

    private void mergeComponents(int i, int j) {
        // TO BE IMPLEMENTED  make shorter root point to taller one

        if (height[i] < height[j]) {
            parent[i] = j;
            height[j] += height[i];

        } else if (height[i] > height[j]){
            parent[j] = i;
            height[i] += height[j];
        }
        // if two trees have same height
        else{
            parent[j] = i;
            height[i] += 1;
        }

        // END SOLUTION
    }

    /**
     * This implements the single-pass path-halving mechanism of path compression
     */
    private void doPathCompression(int i) {
        // TO BE IMPLEMENTED  update parent to value of grandparent

        while(i != parent[i]){
            parent[i] = parent[parent[i]];
            i = parent[i];
        }


        // END SOLUTION
    }

    /**
     * Return the number of connections that it takes to connect n sites
     *
     * @param n the number of sites
     * @return the number of connections
     */
    public static int count(int n){
        // Initializes an empty union–find data structure with n site.
        // Each site is initially in its own component.
        UF_HWQUPC h = new UF_HWQUPC(n);

        // generate random pairs of int between 0 and n-1
        Random random = new Random();
        int numOfConnections = 0;
        int numOfTries = 0;

        // loop over until all components are connected
        while(h.count > 1){
            int p = random.nextInt(n);
            int q = random.nextInt(n);

            // check if the pair of component is connected
            if(!h.connected(p, q)){
                h.union(p, q);
                numOfConnections += 1;

            }

            numOfTries += 1;

        }

        System.out.println("The number of connections: " + numOfConnections + " The number of pairs: " + numOfTries);

        return numOfConnections;

    }

    public static void main(String[] args) {

        // set up the testing inputs
        int a = 1000;

        for(int i = 1; i < 100; i++){
            int n = 2 * a * i;
            System.out.print("n = " + n + " " );
            count(n);
        }

    }
}