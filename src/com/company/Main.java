package com.company;

import SATII.*;

import java.io.File;
import java.io.PrintWriter;

public class Main {

    static String rootPath = "statistics/";

    static class parameters {
        int flip = 5;
        int maxIterations = 100;
        int numberOfBees = 20;
        int maxChances = 6;
        int maxLocalSearch = 30;
        String path;

        public parameters(int flip, int maxIterations, int numberOfBees, int maxChances, int maxLocalSearch) {
            this.flip = flip;
            this.maxIterations = maxIterations;
            this.numberOfBees = numberOfBees;
            this.maxChances = maxChances;
            this.maxLocalSearch = maxLocalSearch;
            path = rootPath + "params" + maxIterations + "." + flip + "." + maxLocalSearch + "." + maxChances;
        }

        void createDirectory() {
            new File(path).mkdirs();
        }
    }

    static class Result {
        double averageSat;
        double averageRate;
        double averageTime;

        public Result(double averageSat, double averageRate, double averageTime) {
            this.averageSat = averageSat;
            this.averageRate = averageRate;
            this.averageTime = averageTime;
        }
    }

    public static void main(String[] args) throws Exception {
//        SATInstance instance = SATInstance.loadClausesFromDimacs("uf20-01.cnf");
//        SATInstance instance = SATInstance.loadClausesFromDimacs("UF75.325.100/uf75-04.cnf");
//        SATInstance instance = SATInstance.loadClausesFromDimacs("uf100-430/uf100-02.cnf");
//        SATInstance instance = SATInstance.loadClausesFromDimacs("UF250.1065.100/uf250-02.cnf");
//        int flip = instance.getNumberOfVariables()/10;
//        SATSolution start = SATSolution.generateRandomSolution(instance);
//        SATSolution sol = BSOSat.searchBSOSAT(instance,300,flip,30,
//                6,30,start);
//        System.out.println("best solution: " + sol);
//        System.out.println("number of clauses satisfied: " + instance.getNumberOfClausesSatisfied(sol));
//        double heat = instance.getNumberOfVariables()/2;
//        sol = BSOSatDynamic.searchBSOSATDynamic(instance,300,flip,30,
//                6,30,heat,10,start);
//        System.out.println("best solution: " + sol);
//        System.out.println("number of clauses satisfied: " + instance.getNumberOfClausesSatisfied(sol));
        new File(rootPath).mkdirs();
        int numberOfInstances = 10;
        int tryPerInstance = 10;
        PrintWriter writer = new PrintWriter(rootPath + "stats.csv");
        writer.write("number of instances per parameter;" + numberOfInstances + "\n");
        writer.write("try per instance;" + tryPerInstance + "\n\n\n");
        writer.write("maxIterations;flip;number of bees;maxChances;local searches;average sat;average rate;average time (s)\n");
        int numberOfBees = 30;
        for (int flip = 5; flip <= 9; flip += 2) {
            for (int maxIterations = 400; maxIterations <= 1000; maxIterations += 300) {
                for (int maxChances = 7; maxChances <= 12; maxChances += 2) {
                    for (int maxLocalSearch = 15; maxLocalSearch <= 30; maxLocalSearch += 5) {

                        parameters params = new parameters(flip, maxIterations, numberOfBees, maxChances, maxLocalSearch);
                        Result res = createCSV(params, numberOfInstances, tryPerInstance);
                        writer.write(maxIterations + ";" + flip + ";" + numberOfBees + ";" + maxChances + ";" + maxLocalSearch
                                + ";" + res.averageSat + ";" + res.averageRate + ";" + res.averageTime + "\n");
                    }
                }
            }
        }
        writer.close();
    }

    static Result createCSV(parameters params, int numberOfInstances, int tryPerInstance) throws Exception {
        int flip = params.flip;
        int maxIterations = params.maxIterations;
        int numberOfBees = params.numberOfBees;
        int maxChances = params.maxChances;
        int maxLocalSearch = params.maxLocalSearch;
        double avgSat = 0;
        double avgRate = 0;
        double avgTime = 0;
        params.createDirectory();
        String path = params.path;
        int numberOfFiles = numberOfInstances;
        PrintWriter total = new PrintWriter(path + "/" + "allDetails.csv");
        total.write("try per instance;" + tryPerInstance + "\n\n");
        total.write("maxIterations;flip;number of bees;maxChances;local searches\n");
        total.write(maxIterations + ";" + flip + ";" + numberOfBees + ";" + maxChances + ";" + maxLocalSearch + "\n\n\n");
        total.write(";;instance;clauses satisfied;rate;time\n");
        for (int j = 0; j < numberOfFiles; j++) {
            String fileName = "uf75-0" + (j + 1) + ".cnf";
            String file = "uf75-0" + (j + 1);
            PrintWriter writer = new PrintWriter(path + "/" + file + "details.csv");

            SATInstance instance = SATInstance.loadClausesFromDimacs("Benchmarks/UF75.325.100/" + fileName);

            writer.write("instance;maxIterations;flip;number of bees;maxChances;local searches\n");
            writer.write(file + ";" + maxIterations + ";" + flip + ";" + numberOfBees + ";" + maxChances + ";" + maxLocalSearch + "\n\n\n");
            writer.write(";;attempt;clauses satisfied;rate;time\n");
            double sumSat = 0;
            double sumRate = 0;
            double sumTime = 0;
            int numberOfAttempts = tryPerInstance;
            for (int i = 1; i <= numberOfAttempts; i++) {
                SATSolution start = SATSolution.generateRandomSolution(instance);
                long currentTime = System.currentTimeMillis();
                SATSolution sol = BSOSat.searchBSOSAT(instance, maxIterations, flip, numberOfBees,
                        maxChances, maxLocalSearch, start);
                long exeTime = System.currentTimeMillis() - currentTime;
                double t = (double) exeTime / 1000;
                int sat = instance.getNumberOfClausesSatisfied(sol);
                double rate = (double) sat / instance.getNumberOfClauses();
                writer.write(";;" + i + ";" + sat + ";" + rate + ";" + t + "\n");
                sumRate += rate;
                sumSat += sat;
                sumTime += t;
            }
            sumRate /= numberOfAttempts;
            sumSat /= numberOfAttempts;
            sumTime /= numberOfAttempts;
            writer.write(";;average;" + sumSat + ";" + sumRate + ";" + sumTime + "\n");
            writer.close();
            total.write(";;" + file + ";" + sumSat + ";" + sumRate + ";" + sumTime + "\n");
            avgSat += sumSat;
            avgRate += sumRate;
            avgTime += sumTime;
        }
        avgSat /= numberOfFiles;
        avgRate /= numberOfFiles;
        avgTime /= numberOfFiles;
        total.close();
        return new Result(avgSat, avgRate, avgTime);
    }
}
