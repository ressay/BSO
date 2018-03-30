package MetaHeuristics;

import java.util.List;

/**
 * Created by ressay on 29/03/18.
 */
public abstract class TabuSearchAbstract<T>
{
    TabuList<T> tabuList;

    public TabuSearchAbstract(TabuList<T> tabuList) {
        this.tabuList = tabuList;
    }

    // get neighbors of parameter solution
    abstract protected List<T> getNeighbors(T solution);
    // fitness function to be minimized
    abstract protected double evaluate(T solution);

    // if stopping condition is met e.g: optimal solution found, time limit or maximum number of iterations reached
    abstract protected boolean end(T solution);

    public T search(T start)
    {
        T sBest = start;
        T currentBest = start;
        tabuList.add(start);
        while(!end(currentBest))
        {
            List<T> neighbors = getNeighbors(currentBest);

            for (T neighbor : neighbors)
                if(!tabuList.contains(neighbor) &&
                        (currentBest == null || evaluate(neighbor) < evaluate(currentBest)))
                    currentBest = neighbor;

            if(evaluate(currentBest) < evaluate(sBest))
                sBest = currentBest;

            tabuList.add(currentBest);
        }
        return sBest;
    }
}
