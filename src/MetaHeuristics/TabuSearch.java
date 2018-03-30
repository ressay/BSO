package MetaHeuristics;

import java.util.LinkedList;
import java.util.List;

/**
 * implementation of Tabu search with LinkedList
 * Created by ressay on 29/03/18.
 */
public abstract class TabuSearch<T> extends TabuSearchAbstract<T> {

    long maximumIteration;
    long numberOfIterations = 0;
    boolean endS = false;

    public TabuSearch()
    {
        // maxListSize = 100
        super(new BasicTabuList<>(100));
        // default number of iterations
        this.maximumIteration = 100000;
    }

    public TabuSearch(long maximumIteration) {
        super(new BasicTabuList<>(100));
        this.maximumIteration = maximumIteration;
    }

    public TabuSearch(long maximumIteration, long maxListSize) {
        super(new BasicTabuList<>(maxListSize));

        this.maximumIteration = maximumIteration;
    }

    // tests if two solutions are equal
    protected abstract boolean equal(T sol1, T sol2);


    @Override
    protected boolean end(T solution) {
        return (numberOfIterations++ >= maximumIteration) || endS;
    }

    protected void endSearch()
    {
        endS = true;
    }

    public void setMaximumIteration(long maximumIteration) {
        this.maximumIteration = maximumIteration;
    }
}
