package BSO;

import MetaHeuristics.TabuList;

import java.util.LinkedList;
import java.util.List;

/**
 *  T is the solution type
 *  S is the searchPoint
 * Created by ressay on 29/03/18.
 */
public abstract class BSOAbstract<T>
{
    protected TabuList<T> tabuList;
    protected Dances<T> dances;

    public BSOAbstract(TabuList<T> tabuList, Dances<T> dances) {
        this.tabuList = tabuList;
        System.out.println(tabuList.size());
        this.dances = dances;
    }

    abstract protected boolean end(T solution);
    abstract protected List<Bee<T>> determineSearchPoints(T solution);

    public T search(Bee<T> beeInit)
    {
        T sRef = beeInit.init();
        T bestSolution = sRef;
        double evaluationBest = dances.evaluate(sRef);
        while (!end(sRef))
        {
            tabuList.add(sRef);
            List<Bee<T>> bees = determineSearchPoints(sRef);

            for(Bee<T> bee : bees)
                dances.add( bee.search() );

            do {
                sRef = dances.getBest();
            }while (sRef != null && tabuList.contains(sRef));

            if(sRef == null) {
                sRef = beeInit.init();
                System.out.println("sref is null!!!");
            }

            double evaluation = dances.evaluate(sRef);
            if(evaluation < evaluationBest) {
                bestSolution = sRef;
                evaluationBest = evaluation;
            }
        }
        return bestSolution;
    }
}
