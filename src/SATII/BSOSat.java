package SATII;

import BSO.BSOBasic;
import BSO.Bee;
import BSO.Dances;
import BSO.DancesHeap;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ressay on 29/03/18.
 */
public class BSOSat extends BSOBasic<SATSolution>
{
    SATInstance instance;
    private int flip = 6;
    private int numberOfBees = 10;
    private int maxChances = 6;
    private int maxIterationsBees = 10;

    public BSOSat(SATInstance ins) {
        super(new SATDances(ins,6));
        this.instance = ins;
    }

    public BSOSat(SATInstance ins, long maximumIteration) {
        super(new SATDances(ins,6),maximumIteration);
        this.instance = ins;
    }

    @Override
    protected List<Bee<SATSolution>> determineSearchPoints(SATSolution solution) {
        LinkedList<Bee<SATSolution>> list = new LinkedList<>();
        list.add(new BeeSAT(instance,solution,maxIterationsBees));
        for (int i = 0; i < flip && i < numberOfBees; i++) {
            SATSolution newSearchPoint = solution.copy();
            int p = 0;
            do {
                newSearchPoint.flip(p*flip+i);
                p++;
            }while(p*flip+i < newSearchPoint.length());
            list.add(new BeeSAT(instance,newSearchPoint,maxIterationsBees));
        }
        return list;
    }

    @Override
    public boolean end(SATSolution solution)
    {
        return super.end(solution) || optimalSolution(solution);
    }

    protected boolean optimalSolution(SATSolution solution)
    {
        return solution.getEvaluation() == 0;
    }


    public static SATSolution searchBSOSAT(SATInstance instance,int maxIter,int flip,int numBees, int maxCh, int maxLocal)
    {
        BSOSat bsoSat = new BSOSat(instance,maxIter);
        bsoSat.flip = flip;
        bsoSat.numberOfBees = numBees;
        bsoSat.maxChances = maxCh;
        bsoSat.maxIterationsBees = maxLocal;
        BeeSAT bee = new BeeSAT(instance,SATSolution.generateRandomSolution(instance),
                maxLocal);
        return bsoSat.search(bee);
    }
}
