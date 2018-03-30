package com.company;

import SATII.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
//        SATInstance instance = SATInstance.loadClausesFromDimacs("uf20-01.cnf");
        SATInstance instance = SATInstance.loadClausesFromDimacs("UF75.325.100/uf75-04.cnf");
//        SATInstance instance = SATInstance.loadClausesFromDimacs("uf100-430/uf100-02.cnf");
//        SATInstance instance = SATInstance.loadClausesFromDimacs("UF250.1065.100/uf250-02.cnf");
        int flip = instance.getNumberOfVariables()/10;
        SATSolution start = SATSolution.generateRandomSolution(instance);
        SATSolution sol = BSOSat.searchBSOSAT(instance,300,flip,30,
                6,30,start);
        System.out.println("best solution: " + sol);
        System.out.println("number of clauses satisfied: " + instance.getNumberOfClausesSatisfied(sol));
        double heat = instance.getNumberOfVariables()/2;
        sol = BSOSatDynamic.searchBSOSATDynamic(instance,300,flip,30,
                6,30,heat,10,start);
        System.out.println("best solution: " + sol);
        System.out.println("number of clauses satisfied: " + instance.getNumberOfClausesSatisfied(sol));

    }
}
