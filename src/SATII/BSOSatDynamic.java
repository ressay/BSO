package SATII;

import BSO.Bee;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ressay on 30/03/18.
 */
public class BSOSatDynamic extends BSOSat
{
    protected double coolingRate;
    protected double heat;
    protected double maxHeat;
    protected int maxNumberOfBees;
    protected double maxCoolingRate;
    protected int maxIterationsBees;
    protected int maxDistance;

    public BSOSatDynamic(SATInstance ins, long maximumIteration, int maxChances
            , double heat, int coolingRate)
    {
        super(ins, maximumIteration, maxChances);
        this.heat = heat;
        maxHeat = heat;
        this.coolingRate = coolingRate;
        this.maxCoolingRate = coolingRate;
    }

    @Override
    protected List<Bee<SATSolution>> determineSearchPoints(SATSolution solution)
    {
        int distance = (int) ((heat/maxHeat)*(double)maxDistance);
        limitIterationsBees =(int) ((heat/maxHeat)*(double) maxIterationsBees);

        LinkedList<Bee<SATSolution>> list = new LinkedList<>();
        list.add(new BeeSAT(instance, solution, limitIterationsBees));
        int f = 1;
        int shift = 0;
        int p = 0;
        for (int i = 0;i < numberOfBees; i++) {
            SATSolution newSearchPoint = solution.copy();
            int k = 0;
            do {
                newSearchPoint.flip(p*f+shift);
                p++;k++;
                if(p*f+shift >= newSearchPoint.length())
                {
                    shift++;
                    if(shift == f)
                    {
                        f = (f+1)%newSearchPoint.length();
                        shift = 0;
                    }
                    p = 0;
                }

            }while(k < distance);
            list.add(new BeeSAT(instance,newSearchPoint, limitIterationsBees));
        }
        coolDown();
        return list;
    }

    protected void coolDown()
    {
        double minimumHeat = maxHeat / 5;
        double rate = maximumIteration-coolingRate;
        if(rate < 0 ) rate = 0;
        heat = (heat - minimumHeat) * (rate) / maximumIteration + minimumHeat;

        if (((SATDances) dances).didChoseDiversity() || didInit)
        {
            heat = maxHeat;
            coolingRate = maxCoolingRate = maxCoolingRate * 1.1;
        }

//        if (numberOfIterations % 50 == 0)
//        {
//            System.out.println("max chances is : " + ((SATDances) dances).maxChances);
//            System.out.println("heat: " + heat + " number of bees: " + numberOfBees + " local iterations: " + limitIterationsBees);
//        }
    }

//    public static SATSolution searchBSOSATDynamic(SATInstance instance,int maxIter,int flip,int numBees,
//                                                  int maxCh, int maxLocal,double heat,int coolingRate)
//    {
//        BSOSatDynamic bsoSat = new BSOSatDynamic(instance,maxIter,maxCh,heat,coolingRate);
//        bsoSat.flip = flip;
//        bsoSat.numberOfBees = numBees;
//        bsoSat.limitIterationsBees = maxLocal;
//        bsoSat.maxNumberOfBees = numBees;
//        bsoSat.maxIterationsBees = maxLocal;
//        BeeSAT bee = new BeeSAT(instance,SATSolution.generateRandomSolution(instance),
//                maxLocal);
//        return bsoSat.search(bee);
//    }

    public static SATSolution searchBSOSATDynamic(SATInstance instance, int maxIter, int numBees,
                                                  int maxCh, int maxDistance, double heat, int coolingRate, SATSolution sol)
    {
        BSOSatDynamic bsoSat = new BSOSatDynamic(instance, maxIter, maxCh, heat, coolingRate);
        bsoSat.numberOfBees = numBees;
        bsoSat.limitIterationsBees = maxDistance;
        bsoSat.maxNumberOfBees = numBees;
        bsoSat.maxIterationsBees = maxDistance;
        bsoSat.maxDistance = maxDistance;
        BeeSAT bee = new BeeSAT(instance, sol,
                maxDistance);
        return bsoSat.searchMultiThread(bee);
    }
}
