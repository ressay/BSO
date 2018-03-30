package BSO;

import com.company.Heap;

/**
 * organize dances in a heap
 * Created by ressay on 29/03/18.
 */
public abstract class DancesHeap<T> extends Dances<T>
{
    protected Heap<T> heap = new Heap<T>() {
        @Override
        public int compare(T n1, T n2) {
            return compareSolutions(n1,n2);
        }
    };


    @Override
    public void add(T solution)
    {
        heap.add(solution);
    }

    @Override
    public T getBest() {
        return heap.getRoot();
    }

    protected int compareSolutions(T sol1,T sol2)
    {
        double e1 = evaluate(sol1),e2 = evaluate(sol2);
        if(e1 > e2)
            return 1;
        if(e1 < e2)
            return -1;
        return 0;
    }
}
