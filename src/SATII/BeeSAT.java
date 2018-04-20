package SATII;

import BSO.Bee;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ressay on 29/03/18.
 */
public class BeeSAT extends Bee<SATSolution>
{
    private SATInstance instance;
    private int maxIterations = 10;
    private int maxListSize = 0;
    private boolean first = true;

    public BeeSAT(SATInstance instance,SATSolution searchZone, int maxIterations) {
        this.instance = instance;
        this.searchZone = searchZone;
        this.maxIterations = maxIterations;
    }

    @Override
    protected SATSolution init() {
//
        if(first)
            return searchZone;
        first = false;
        return getNextSearchZone();
    }

    protected SATSolution getNextSearchZone()
    {
        for (int i = 0; i < searchZone.length(); i++)
        {
            if(ThreadLocalRandom.current().nextInt(0,3 ) == 1)
                searchZone.flip(i);
        }
        return searchZone;
    }



    @Override
    protected SATSolution search() {
        SATTabuSearch searcher = new SATTabuSearch(maxIterations,maxListSize,instance);
        return searcher.search(searchZone);
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }


    public void setMaxListSize(int maxListSize) {
        this.maxListSize = maxListSize;
    }
}
