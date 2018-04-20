package com.company;

import SATII.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

public class Main {

    static String rootPath = "statistics2/";
    static class parameters
    {
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
            path = rootPath+"params"+maxIterations+"."+flip+"."+maxLocalSearch+"."+maxChances;
        }

        void createDirectory()
        {
            new File(path).mkdirs();
        }
    }

    static class Result
    {
        double averageSat;
        double averageRate;
        double averageTime;

        public Result(double averageSat, double averageRate, double averageTime) {
            this.averageSat = averageSat;
            this.averageRate = averageRate;
            this.averageTime = averageTime;
        }
    }

    public static void main(String[] args) throws Exception
    {
//        SATInstance instance = SATInstance.loadClausesFromDimacs("uf20-01.cnf");
//        SATInstance instance = SATInstance.loadClausesFromDimacs("UF75.325.100/uf75-04.cnf");
//        SATInstance instance = SATInstance.loadClausesFromDimacs("uf100-430/uf100-01.cnf");
//        SATInstance instance = SATInstance.loadClausesFromDimacs("UF250.1065.100/uf250-02.cnf");
//        int flip = instance.getNumberOfVariables()/10;
//        int localSearch = 30;
//        SATSolution start = SATSolution.generateRandomSolution(instance);
//        SATSolution sol = BSOSat.searchBSOSAT(instance,1000,flip,20,
//                10,localSearch,start);
//        System.out.println("best solution: " + sol);
//        System.out.println("number of clauses satisfied: " + instance.getNumberOfClausesSatisfied(sol));
//        double heat = instance.getNumberOfVariables()/2;
//        localSearch = instance.getNumberOfVariables()/2;
//        int count = 0;
//        for (int i = 0; i < 10; i++)
//        {
//            sol = BSOSatDynamic.searchBSOSATDynamic(instance, 1000, 30, 5,
//                    (int) (localSearch), heat, 40, start);
//            System.out.println("best solution: " + sol);
//            System.out.println("number of clauses satisfied: " + instance.getNumberOfClausesSatisfied(sol));
//            start = SATSolution.generateRandomSolution(instance);
//            if(instance.getNumberOfClausesSatisfied(sol) == instance.getNumberOfClauses())
//                count++;
//        }
//        System.out.println("got : "+count+"/10");

        new File(rootPath).mkdirs();
        int numberOfInstances = 5;
        int tryPerInstance = 5;
        Result res = createCSV(numberOfInstances, tryPerInstance);

    }

    static String fileInst = "uf250-0";
    //    static String fileInstPath = "UF75.325.100/";
    static String fileInstPath = "UF250.1065.100/";
    static Result createCSV(int numberOfInstances,int tryPerInstance) throws Exception {
        double avgSat = 0;
        double avgRate = 0;
        double avgTime = 0;
        int numberOfFiles = numberOfInstances;
        PrintWriter total = new PrintWriter(rootPath+"/"+"allDetails"+fileInst+".csv");
        total.write("try per instance;"+tryPerInstance+"\n\n");
        total.write(";;instance;clauses satisfied;rate;time\n");
        for (int j = 0; j < numberOfFiles; j++) {
            String fileName = fileInst+(j+1)+".cnf";
            String file = fileInst+(j+1);
            SATInstance instance = SATInstance.loadClausesFromDimacs(fileInstPath + fileName);
            double sumSat = 0;
            double sumRate = 0;
            double sumTime = 0;
            int numberOfAttempts = tryPerInstance;
            for (int i = 1; i <= numberOfAttempts; i++) {
                SATSolution start = SATSolution.generateRandomSolution(instance);
                double heat = instance.getNumberOfVariables()/2;
                double localSearch = instance.getNumberOfVariables()/2;
                long currentTime = System.currentTimeMillis();
                SATSolution sol = BSOSatDynamic.searchBSOSATDynamic(instance, 1000, 30, 5,
                    (int) (localSearch), heat, 40, start);
                long exeTime = System.currentTimeMillis()-currentTime;
                double t = (double)exeTime/1000;
                int sat = instance.getNumberOfClausesSatisfied(sol);
                double rate = (double)sat/instance.getNumberOfClauses();
                sumRate += rate;
                sumSat += sat;
                sumTime += t;
            }
            sumRate /= numberOfAttempts;
            sumSat /= numberOfAttempts;
            sumTime /=numberOfAttempts;
            total.write(";;"+file+";"+sumSat+";"+sumRate+";"+sumTime+"\n");
            avgSat += sumSat;
            avgRate += sumRate;
            avgTime += sumTime;
        }
        avgSat /= numberOfFiles;
        avgRate /= numberOfFiles;
        avgTime /= numberOfFiles;
        total.write(";;average;"+avgSat+";"+avgRate+";"+avgTime+"\n");
        total.close();
        return new Result(avgSat,avgRate,avgTime);
    }
}
