package com.company;

/**
 * Created by ressay on 29/03/18.
 */
public abstract class Heap<T>
{
    public static final int SMALL = 1<<10;
    public static final int MEDIUM = 1<<15;
    public static final int BIG = 1<<20;

    private Object[] heap;
    private int currentSize = 0;

    public Heap()
    {
        heap = new Object[MEDIUM];
    }

    public Heap(int size)
    {
        heap = new Object[size];
    }

    /**
     *
     * @param n1
     * @param n2
     * @return 1 if n1 > n2 -1 if n1 < n2 and 0 if n1 == n2
     */
    abstract public int compare(T n1, T n2);

    public void add(T node)
    {
        if(currentSize == heap.length) {
            doubleSize();
        }
        heap[currentSize++] = node;

        for (int i = currentSize-1; i != 0 && compare(get(i),get(parent(i))) < 0; i=parent(i))
            swap(i,parent(i));

    }

    public T get(int index)
    {
        return (T) heap[index];
    }

    public boolean isEmpty() {
        return currentSize == 0;
    }

    public T getRoot() {
        if(currentSize == 0)
            return null;
        if (currentSize == 1)
            return get(--currentSize);
        T node = get(0);
        heap[0] = heap[--currentSize];
        heapify(0);
        return node;
    }

    public T removeLast()
    {
        if(currentSize == 0)
            return null;
        return get(--currentSize);
    }

    public void clear()
    {
        currentSize = 0;
    }

    private void heapify(int i)
    {
        int l = left(i),r = right(i);
        int smallest = i;
        if(l < currentSize && compare(get(l),get(i)) < 0) // heap[l] < heap[i]
            smallest = l;
        if(r < currentSize && compare(get(r),get(smallest)) < 0) // heap[r] < heap[smallest]
            r = smallest;
        if(smallest != i)
        {
            swap(i,smallest);
            heapify(smallest);
        }
    }



    private int parent(int i)
    {
        return (i-1)/2;
    }

    private int left(int i)
    {
        return 2*i+1;
    }

    private int right(int i)
    {
        return 2*i+2;
    }

    private void swap(int i,int j)
    {
        T node = get(i);
        heap[i] = heap[j];
        heap[j] = node;
    }


    private void doubleSize()
    {
        Object[] newHeap = new Object[heap.length*2];
        for (int i = 0; i < currentSize; i++) {
            newHeap[i] = heap[i];
        }
        heap = newHeap;
    }

    public Object[] getHeap() {
        return heap;
    }

    public int getCurrentSize() {
        return currentSize;
    }
}
