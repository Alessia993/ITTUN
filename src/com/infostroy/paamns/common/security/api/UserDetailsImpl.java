/**
 * 
 */
package com.infostroy.paamns.common.security.api;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

/**
 * @author sman
 * 
 */
public class UserDetailsImpl implements UserDetails
{
    /**
     * 
     */
    private static final long  serialVersionUID  = -1275986526924009634L;
    
    private String             _userName         = null;
    
    private String             _password         = null;
    
    private GrantedAuthority[] _grantedAuthority = null;
    
    public UserDetailsImpl(String userName, String password,
            GrantedAuthority[] grantedAuthority)
    {
        _userName = userName;
        
        _password = password;
        
        _grantedAuthority = grantedAuthority;
    }
    
    public GrantedAuthority[] getAuthorities()
    {
        return _grantedAuthority;
    }
    
    public String getPassword()
    {
        return _password;
    }
    
    public String getUsername()
    {
        return _userName;
    }
    
    public boolean isAccountNonExpired()
    {
        return true;
    }
    
    public boolean isAccountNonLocked()
    {
        return true;
    }
    
    public boolean isCredentialsNonExpired()
    {
        return true;
    }
    
    public boolean isEnabled()
    {
        return true;
    }
    
}
