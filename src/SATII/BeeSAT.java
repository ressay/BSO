package SATII;

import BSO.Bee;

/**
 * Created by ressay on 29/03/18.
 */
public class BeeSAT extends Bee<SATSolution>
{
    private SATInstance instance;
    private int maxIterations = 10;
    private int maxListSize = 0;

    public BeeSAT(SATInstance instance,SATSolution searchZone, int maxIterations) {
        this.instance = instance;
        this.searchZone = searchZone;
        this.maxIterations = maxIterations;
    }

    @Override
    protected SATSolution init() {
//        return searchZone = SATSolution.generateRandomSolution(instance);
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
