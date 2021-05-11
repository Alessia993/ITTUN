/**
 * 
 */
package com.infostroy.paamns.web.beans.wrappers;

/**
 *
 * @author Vladimir Zrazhevskiy
 * InfoStroy Co., 2013.
 *
 */
public class Pair<A, B>
{
    private A first;
    
    private B second;
    
    public Pair(A first, B second)
    {
        super();
        this.first = first;
        this.second = second;
    }
    
    /**
     * Gets first
     * @return first the first
     */
    public A getFirst()
    {
        return first;
    }
    
    /**
     * Sets first
     * @param first the first to set
     */
    public void setFirst(A first)
    {
        this.first = first;
    }
    
    /**
     * Gets second
     * @return second the second
     */
    public B getSecond()
    {
        return second;
    }
    
    /**
     * Sets second
     * @param second the second to set
     */
    public void setSecond(B second)
    {
        this.second = second;
    }
    
}
