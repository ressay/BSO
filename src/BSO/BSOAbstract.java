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
    protected boolean didInit = false;

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
            didInit = false;
            if(sRef == null) {
                sRef = beeInit.init();
                didInit = true;
            }

            double evaluation = dances.evaluate(sRef);
            if(evaluation < evaluationBest) {
                bestSolution = sRef;
                evaluationBest = evaluation;
            }
        }
        return bestSolution;
    }

    LinkedList<Thread> beeThreads = new LinkedList<>();
    public T searchMultiThread(Bee<T> beeInit)
    {
        T sRef = beeInit.init();
        T bestSolution = sRef;
        double evaluationBest = dances.evaluate(sRef);
        while (!end(sRef))
        {
            tabuList.add(sRef);
            List<Bee<T>> bees = determineSearchPoints(sRef);

            launchBees(bees);
            waitForBees();
            do {
                sRef = dances.getBest();
            }while (sRef != null && tabuList.contains(sRef));
            didInit = false;
            if(sRef == null) {
                sRef = beeInit.init();
                didInit = true;
            }

            double evaluation = dances.evaluate(sRef);
            if(evaluation < evaluationBest) {
                bestSolution = sRef;
                evaluationBest = evaluation;
            }
        }
        return bestSolution;
    }

    LinkedList<T> list = new LinkedList<>();
    protected void launchBees(List<Bee<T>> bees)
    {
        for(Bee<T> bee : bees)
        {
            Thread thread = new Thread(() -> addToList(bee.search()) );
            beeThreads.add(thread);
            thread.start();
        }

    }

    synchronized protected void searchBee(Bee<T> bee)
    {
        list.add( bee.search());
    }

    synchronized protected void addToList(T sol)
    {
        list.add( sol);
    }

    protected void waitForBees()
    {
        for(Thread thread : beeThreads)
        {
            while (thread.isAlive())
                try
                {
                    thread.join();
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
        }
//        try
        {
            for(T sol : list)
                dances.add(sol);
        }
//        catch (NullPointerException ex)
//        {
//            System.out.println(list.size());
//        }

        list.clear();
        beeThreads.clear();
    }

}
