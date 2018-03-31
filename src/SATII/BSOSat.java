package SATII;

import BSO.BSOBasic;
import BSO.Bee;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ressay on 29/03/18.
 */
public class BSOSat extends BSOBasic<SATSolution>
{
    SATInstance instance;
    protected int flip = 6;
    protected int numberOfBees = 10;
    protected int limitIterationsBees = 10;

    public BSOSat(SATInstance ins, int maxChances) {
        super(new SATDances(ins,maxChances));
        this.instance = ins;

    }

    public BSOSat(SATInstance ins, long maximumIteration,int maxChances) {
        super(new SATDances(ins,maxChances),maximumIteration);
        this.instance = ins;
    }

    @Override
    protected List<Bee<SATSolution>> determineSearchPoints(SATSolution solution) {
        LinkedList<Bee<SATSolution>> list = new LinkedList<>();
        list.add(new BeeSAT(instance,solution, limitIterationsBees));
        for (int i = 0; i < flip && i < numberOfBees; i++) {
            SATSolution newSearchPoint = solution.copy();
            int p = 0;
            do {
                newSearchPoint.flip(p*flip+i);
                p++;
            }while(p*flip+i < newSearchPoint.length());
            list.add(new BeeSAT(instance,newSearchPoint, limitIterationsBees));
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
        BSOSat bsoSat = new BSOSat(instance,maxIter,maxCh);
        bsoSat.flip = flip;
        bsoSat.numberOfBees = numBees;
        bsoSat.limitIterationsBees = maxLocal;
        BeeSAT bee = new BeeSAT(instance,SATSolution.generateRandomSolution(instance),
                maxLocal);
        return bsoSat.search(bee);
    }

    public static SATSolution searchBSOSAT(SATInstance instance,int maxIter,int flip,int numBees,
                                           int maxCh, int maxLocal,SATSolution sol)
    {
        BSOSat bsoSat = new BSOSat(instance,maxIter,maxCh);
        bsoSat.flip = flip;
        bsoSat.numberOfBees = numBees;
        bsoSat.limitIterationsBees = maxLocal;
        BeeSAT bee = new BeeSAT(instance,sol,
                maxLocal);
        return bsoSat.search(bee);
    }
}
