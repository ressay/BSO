package SATII;

import java.util.BitSet;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ressay on 29/03/18.
 */
public class SATSolution
{
    private BitSet values;
    private int numberOfVariables;

    public SATSolution(int numberOfVariables)
    {
        this.numberOfVariables = numberOfVariables;
        values = new BitSet(numberOfVariables);
    }

    public static SATSolution generateRandomSolution(int size)
    {
        SATSolution solution = new SATSolution(size);

        for (int i = 0; i < size; i++)
            if(ThreadLocalRandom.current().nextInt(0,2 ) == 1)
                solution.set(i);
            else
                solution.clear(i);

        return solution;
    }

    public SATSolution copy()
    {
        BitSet copyBitset = new BitSet(values.length());
        copyBitset.clear();
        copyBitset.or(values);
        SATSolution solution = new SATSolution(length());
        solution.values = copyBitset;
        return solution;
    }

    public SATSolution flip(int index)
    {
        values.flip(index);
        return this;
    }

    public SATSolution set(int index)
    {
        values.set(index);
        return this;
    }

    public SATSolution clear(int index)
    {
        values.clear(index);
        return this;
    }

    public SATSolution setIndexValue(int index, boolean value)
    {
        if(value)
            values.set(index);
        else
            values.clear(index);
        return this;
    }

    public boolean get(int index)
    {
        return values.get(index);
    }

    public int length()
    {
        return numberOfVariables;
    }

    @Override
    public boolean equals(Object solution)
    {
        return values.equals(((SATSolution)solution).values);
    }

    @Override
    public String toString()
    {
        String output = "";
        for (int i = 0; i < length(); i++) {
            if(get(i))
                output += (i+1)+" ";
            else
                output += "-"+(i+1)+" ";
        }
        return output;
    }

    public int distance(SATSolution solution)
    {
        BitSet dis = copy().values;
        dis.xor(solution.values);
        return dis.cardinality();
    }
}
