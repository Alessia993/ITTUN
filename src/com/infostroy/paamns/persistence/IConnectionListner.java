/**
 * 
 */
package com.infostroy.paamns.persistence;

/**
 *
 * @author Vladimir Zrazhevskiy
 * InfoStroy Co., 2012.
 *
 */
public interface IConnectionListner
{
    public void fireConnetionEstablished();
    
    public void fireConnetionResufed();
}
