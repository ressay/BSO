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
    public BSOSatDynamic(SATInstance ins, long maximumIteration, int maxChances
    ,double heat,int coolingRate) {
        super(ins, maximumIteration, maxChances);
        this.heat = heat;
        maxHeat = heat;
        this.coolingRate = coolingRate;
        this.maxCoolingRate = coolingRate;
    }

    @Override
    protected List<Bee<SATSolution>> determineSearchPoints(SATSolution solution) {

        LinkedList<Bee<SATSolution>> list = new LinkedList<>();
        list.add(new BeeSAT(instance,solution, limitIterationsBees));

        int distance = (int)heat;
        for (int i = 0; i < numberOfBees; ) {

            SATSolution newSearchPoint = solution.copy();


            LinkedList<Integer> indexes = new LinkedList<>();
            for (int j = 0; j < instance.getNumberOfVariables(); j++) {
                indexes.add(j);
            }


            while (indexes.size() >= distance) {
                // flip distance bits for each new solution
                for (int j = 0; j < distance; j++) {
                    int index = indexes.remove(ThreadLocalRandom.current().nextInt(0, indexes.size()));
                    newSearchPoint.flip(index);
                }
                i++;
                list.add(new BeeSAT(instance, newSearchPoint, limitIterationsBees));
                if(numberOfIterations %50 == 0)
                {
                    System.out.println("distance: "+ distance);
                }
            }

            distance /= 2;
        }
        coolDown();
        return list;
    }

    protected void coolDown()
    {
        double minimumHeat = maxHeat/5;
        heat = (maxHeat-minimumHeat)*(numberOfIterations-coolingRate)/maximumIteration+minimumHeat;
        numberOfBees = (int)(maxNumberOfBees*heat/maxHeat);
        limitIterationsBees = (int)(maxIterationsBees*maxHeat/heat);
        if(((SATDances)dances).didChoseDiversity())
        {
            heat = maxHeat;
            numberOfBees = maxNumberOfBees;
            coolingRate = maxCoolingRate = maxCoolingRate*1.2;
        }

        if(numberOfIterations %50 == 0)
        {
            System.out.println("heat: " + heat + " number of bees: "+numberOfBees+" local iterations: "+ limitIterationsBees);
        }
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

    public static SATSolution searchBSOSATDynamic(SATInstance instance,int maxIter,int flip,int numBees,
    int maxCh, int maxLocal,double heat,int coolingRate,SATSolution sol)
    {
        BSOSatDynamic bsoSat = new BSOSatDynamic(instance,maxIter,maxCh,heat,coolingRate);
        bsoSat.flip = flip;
        bsoSat.numberOfBees = numBees;
        bsoSat.limitIterationsBees = maxLocal;
        bsoSat.maxNumberOfBees = numBees;
        bsoSat.maxIterationsBees = maxLocal;
        BeeSAT bee = new BeeSAT(instance,sol,
                maxLocal);
        return bsoSat.search(bee);
    }
}
