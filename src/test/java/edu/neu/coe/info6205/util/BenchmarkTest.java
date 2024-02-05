/*
 * Copyright (c) 2017. Phasmid Software
 */

package edu.neu.coe.info6205.util;

import edu.neu.coe.info6205.sort.BaseHelper;
import edu.neu.coe.info6205.sort.GenericSort;
import edu.neu.coe.info6205.sort.elementary.InsertionSort;
import edu.neu.coe.info6205.sort.elementary.InsertionSortTest;
import org.junit.Test;
import org.junit.runner.manipulation.Sorter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("ALL")
public class BenchmarkTest {

    int pre = 0;
    int run = 0;
    int post = 0;

    @Test // Slow
    public void testWaitPeriods() throws Exception {
        int nRuns = 2;
        int warmups = 2;
        Benchmark<Boolean> bm = new Benchmark_Timer<>(
                "testWaitPeriods", b -> {
            GoToSleep(100L, -1);
            return null;
        },
                b -> {
                    GoToSleep(200L, 0);
                },
                b -> {
                    GoToSleep(50L, 1);
                });
        double x = bm.run(true, nRuns);
        assertEquals(nRuns, post);
        assertEquals(nRuns + warmups, run);
        assertEquals(nRuns + warmups, pre);
        assertEquals(200, x, 10);
    }

    private void GoToSleep(long mSecs, int which) {
        try {
            Thread.sleep(mSecs);
            if (which == 0) run++;
            else if (which > 0) post++;
            else pre++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getWarmupRuns() {
        assertEquals(2, Benchmark_Timer.getWarmupRuns(0));
        assertEquals(2, Benchmark_Timer.getWarmupRuns(20));
        assertEquals(3, Benchmark_Timer.getWarmupRuns(45));
        assertEquals(6, Benchmark_Timer.getWarmupRuns(100));
        assertEquals(6, Benchmark_Timer.getWarmupRuns(1000));
    }

    @Test
    public void insertionSortRandomArrays() throws Exception {

        int nRuns = 50;

        // use doubling method to test different size of random arrays
        int[] testSizes = {100, 200, 400, 800, 1600, 3200};

        for (int i = 0; i < testSizes.length; i++){

            int size = testSizes[i];
            int[] array = generateRandomArray(size);

            // Converting int[] to Integer[]
            Integer[] unsorted = Arrays.stream(array).boxed().toArray(Integer[]::new);
            // System.out.println("input array: " + Arrays.toString(unsorted));

            // fRun: insertion sort, fPost: checkSorted
            Benchmark<Integer []> bm = new Benchmark_Timer<>(
                    "Insertion sort: random array with [" + unsorted.length + "] elements" ,
                    (Integer[] arr) -> InsertionSort.sort(Arrays.copyOf(arr, arr.length)),
                    (Integer[] arr) -> {
                        try {
                            checkSorted(arr);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

            // run nRuns times
            double x = bm.run(unsorted, nRuns);
            assertEquals(50, nRuns);
            assertEquals(50, post);

            // reset post value for next loop
            post = 0;
        }
    }

    @Test
    public void insertionSortOrderedArrays() throws Exception {

        int nRuns = 100;
        int[] testSizes = {100, 200, 400, 800, 1600, 3200};

        for (int i = 0; i < testSizes.length; i++){

            int size = testSizes[i];
            int[] array = generateOrderedArray(size);
            Integer[] unsorted = Arrays.stream(array).boxed().toArray(Integer[]::new);

            Benchmark<Integer []> bm = new Benchmark_Timer<>(
                    "Insertion sort: sorted array with [" + unsorted.length + "] elements" ,
                    (Integer[] arr) -> InsertionSort.sort(Arrays.copyOf(arr, arr.length)),
                    (Integer[] arr) -> {
                        try {
                            checkSorted(arr);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

            double x = bm.run(unsorted, nRuns);
            assertEquals(100, nRuns);
            assertEquals(100, post);

            post = 0;
        }
    }

    @Test
    public void insertionSortPartiallyOrderedArrays() throws Exception {

        int nRuns = 250;
        int[] testSizes = {100, 200, 400, 800, 1600, 3200};

        for (int i = 0; i < testSizes.length; i++){

            int size = testSizes[i];
            int[] array = generatePartiallyOrderedArray(size);
            Integer[] unsorted = Arrays.stream(array).boxed().toArray(Integer[]::new);

            Benchmark<Integer []> bm = new Benchmark_Timer<>(
                    "Insertion sort: partially sorted array with [" + unsorted.length + "] elements" ,
                    (Integer[] arr) -> InsertionSort.sort(Arrays.copyOf(arr, arr.length)),
                    (Integer[] arr) -> {
                        try {
                            checkSorted(arr);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

            double x = bm.run(unsorted, nRuns);
            assertEquals(250, nRuns);
            assertEquals(250, post);

            post = 0;
        }
    }

    @Test
    public void insertionSortReversedArrays() throws Exception {

        int nRuns = 500;
        int[] testSizes = {100, 200, 400, 800, 1600, 3200};

        for (int i = 0; i < testSizes.length; i++){

            int size = testSizes[i];
            int[] array = generateReversedArray(size);
            Integer[] unsorted = Arrays.stream(array).boxed().toArray(Integer[]::new);

            Benchmark<Integer []> bm = new Benchmark_Timer<>(
                    "Insertion sort: reversed sorted array with [" + unsorted.length + "] elements" ,
                    (Integer[] arr) -> InsertionSort.sort(Arrays.copyOf(arr, arr.length)),
                    (Integer[] arr) -> {
                        try {
                            checkSorted(arr);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

            double x = bm.run(unsorted, nRuns);
            assertEquals(500, nRuns);
            assertEquals(500, post);

            post = 0;
        }
    }

    public void checkSorted(Integer[] input) throws IOException {
        BaseHelper<Integer> helper = new BaseHelper<>("randomArray", input.length, Config.load(InsertionSortTest.class));
        GenericSort<Integer> sorter = new InsertionSort<Integer>(helper);
        Integer[] ys = sorter.sort(input);
        if (!helper.sorted(ys)) {
            // handling the case when the array is not properly sorted
            System.out.println("Array is not sorted properly");
        } else {
            post++;
//            System.out.println("Array is sorted: " + Arrays.toString(ys));
        }
    }

    public int[] generateRandomArray(int size){
        int[] arr = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++){
            arr[i] = random.nextInt(100);
        }
        return arr;
    }

    public int[] generateOrderedArray(int size){
        int[] arr = generateRandomArray(size);
        Arrays.sort(arr);
        return arr;
    }

    public int[] generatePartiallyOrderedArray(int size){
        int[] arr = new int[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            if(i < size/2)
                arr[i] = i;
            else
                arr[i] = rand.nextInt(1000);
        }
        return arr;
    }

    public int[] generateReversedArray(int size){
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = size - i;
        }
        return arr;
    }

}

