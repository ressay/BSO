package MetaHeuristics;

/**
 * Created by ressay on 29/03/18.
 */
public abstract class TabuList<T>
{
    long maxListSize;

    public TabuList(long maxListSize) {
        this.maxListSize = maxListSize;
    }

    abstract public boolean contains(T solution);
    abstract public void add(T solution);
}
