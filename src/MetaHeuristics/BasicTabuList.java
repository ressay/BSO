package MetaHeuristics;

import java.util.LinkedList;

/**
 * Created by ressay on 29/03/18.
 */
public class BasicTabuList<T> extends TabuList<T>
{
    LinkedList<T> tabuList = new LinkedList<>();
    long maxListSize;

    public BasicTabuList(long maxListSize) {
        super(maxListSize);
    }

    @Override
    public boolean contains(T solution) {
        for (T visited : tabuList)
            if(solution.equals(visited))
                return true;
        return false;
    }

    @Override
    public void add(T solution) {
        tabuList.add(solution);
        if(maxListSize >= 0 && tabuList.size() > maxListSize)
            tabuList.removeFirst();
    }
}
