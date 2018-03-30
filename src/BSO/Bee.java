package BSO;

/**
 *  T is the solution type
 *  S is the searchPoint
 * Created by ressay on 29/03/18.
 */
public abstract class Bee<T>
{
    protected T searchZone;
    protected abstract T init();
    protected abstract T search();
}
