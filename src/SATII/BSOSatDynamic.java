package SATII;

import BSO.Bee;

import java.util.LinkedList;
import java.util.List;

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
        list.add(new BeeSAT(instance,solution,maxIterationsBees));

        int step = (int)(heat - instance.getNumberOfVariables()/flip)/(numberOfBees-1);
        int p = 0,k = 0;
        for (int i = 0; i < numberOfBees; i++) {

            SATSolution newSearchPoint = solution.copy();
            // flip heat bits for each new solution
            for (int j = i*step; j < heat ; j++) {
                newSearchPoint.flip(p*flip+k);
                p++;
                if(p*flip+k >= solution.length())
                {
                    k = (k+1)%flip;
                    p=0;
                    flip = (flip+1)%solution.length();
                    if(flip == 0) flip = 1;
                }
            }
            list.add(new BeeSAT(instance,newSearchPoint,maxIterationsBees));
        }
        coolDown();
        return list;
    }

    protected void coolDown()
    {
//        heat = maxHeat-maxHeat*((double)numberOfIterations/(double)maximumIteration)+1;
//        numberOfBees = (int)(maxNumberOfBees-(double)maxNumberOfBees*((double)numberOfIterations/(double)maximumIteration)+5);
        heat*= (maximumIteration-coolingRate)/(double)maximumIteration;
        coolingRate = maxCoolingRate-maxCoolingRate*((double)numberOfIterations/(double)maximumIteration);
//        coolingRate *= 0.99;
        if(heat < instance.getNumberOfVariables()/flip)
            heat = instance.getNumberOfVariables()/flip;
//        numberOfBees*= (maximumIteration-1)/(double)maximumIteration;
        if(numberOfIterations%50 == 0)
        System.out.println("heat: "+heat+" number of bees: "+numberOfBees);

        if(((SATDances)dances).didChoseDiversity())
        {
            heat = maxHeat;
            numberOfBees = maxNumberOfBees;
            coolingRate = maxCoolingRate = maxCoolingRate*1.2;
            System.out.println("entered");
        }
    }

    public static SATSolution searchBSOSATDynamic(SATInstance instance,int maxIter,int flip,int numBees,
                                                  int maxCh, int maxLocal,double heat,int coolingRate)
    {
        BSOSatDynamic bsoSat = new BSOSatDynamic(instance,maxIter,maxCh,heat,coolingRate);
        bsoSat.flip = flip;
        bsoSat.numberOfBees = numBees;
        bsoSat.maxIterationsBees = maxLocal;
        bsoSat.maxNumberOfBees = numBees;
        BeeSAT bee = new BeeSAT(instance,SATSolution.generateRandomSolution(instance),
                maxLocal);
        return bsoSat.search(bee);
    }

    public static SATSolution searchBSOSATDynamic(SATInstance instance,int maxIter,int flip,int numBees,
    int maxCh, int maxLocal,double heat,int coolingRate,SATSolution sol)
    {
        BSOSatDynamic bsoSat = new BSOSatDynamic(instance,maxIter,maxCh,heat,coolingRate);
        bsoSat.flip = flip;
        bsoSat.numberOfBees = numBees;
        bsoSat.maxIterationsBees = maxLocal;
        bsoSat.maxNumberOfBees = numBees;
        BeeSAT bee = new BeeSAT(instance,sol,
                maxLocal);
        return bsoSat.search(bee);
    }
}
