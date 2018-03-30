package com.company;

import SATII.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
//        SATInstance instance = SATInstance.loadClausesFromDimacs("uf20-01.cnf");
        SATInstance instance = SATInstance.loadClausesFromDimacs("UF75.325.100/uf75-01.cnf");
        SATSolution sol = BSOSat.searchBSOSAT(instance,1000,8,100,6,10);
        System.out.println("best solution: " + sol);
        System.out.println("number of clauses satisfied: " + instance.getNumberOfClausesSatisfied(sol));

    }
}
