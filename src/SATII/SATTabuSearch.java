package SATII;

import MetaHeuristics.TabuSearch;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ressay on 29/03/18.
 */
public class SATTabuSearch extends TabuSearch<SATSolution>
{
    SATInstance instance;
    int limitNeighbors = 100;
    int minVal = 1000000;

    public SATTabuSearch(long maximumIteration, long maxListSize, SATInstance instance) {
        super(maximumIteration, maxListSize);
        this.instance = instance;
    }

    public SATTabuSearch(SATInstance instance) {
        this.instance = instance;
    }

    @Override
    protected List<SATSolution> getNeighbors(SATSolution solution)
    {
        LinkedList<SATSolution> neighbors = new LinkedList<>();
        LinkedList<Integer> indexes = new LinkedList<>();
        for (int i = 0; i < instance.getNumberOfVariables(); i++) {
            indexes.add(i);
        }
        // the number of neighbors generated is equal to the limit of number or number of variables
        // a neighbor solution has the same values of variables except for one which is flipped
        for (int i = 0; i < limitNeighbors && i < instance.getNumberOfVariables(); i++) {
            int index = indexes.remove(ThreadLocalRandom.current().nextInt(0, indexes.size()));
            neighbors.add(solution.copy().flip(index));
        }

        return neighbors;
    }

    @Override
    protected double evaluate(SATSolution solution) {
        double val = solution.getEvaluation();
//        if(val < minVal)
//        {
//            minVal = val;
//            System.out.println("minimum so far: "+minVal);
//        }
        if(val == 0)
            endSearch();
        return val;
    }

    @Override
    protected boolean equal(SATSolution sol1, SATSolution sol2) {
        return sol1.equals(sol2);
    }
}
