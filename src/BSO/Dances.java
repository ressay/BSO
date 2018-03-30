package BSO;

/**
 * Created by ressay on 29/03/18.
 */
public abstract class Dances <T>
{
    abstract public void add(T solution);
    abstract public T getBest();
    abstract public double evaluate(T solution);
}
