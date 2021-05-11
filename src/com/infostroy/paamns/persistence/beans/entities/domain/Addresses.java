/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.domain.enums.Cities;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Departments;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Provinces;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Regions;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Zones;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "addresses")
public class Addresses extends
        com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long serialVersionUID = -1339493274261852765L;
    
    /**
     * Stores region value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn
    private Regions           region;
    
    /**
     * Stores province value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn
    private Provinces         province;
    
    /**
     * Stores city value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn
    private Cities            city;
    
    /**
     * Stores province value of entity.
     */
    @ManyToOne
    @JoinColumn
    private Departments       department;
    
    /**
     * Stores city value of entity.
     */
    @ManyToOne
    @JoinColumn
    private Zones             zone;
    
    /**
     * Stores email value of entity.
     */
    @Column
    private String            email;
    
    /**
     * Stores phone value of entity.
     */
    @Column
    private String            phone;
    
    /**
     * Stores fax value of entity.
     */
    @Column
    private String            fax;
    
    @Column
    private String            address;
    
    /**
     * Sets fax
     * @param fax the fax to set
     */
    public void setFax(String fax)
    {
        this.fax = fax;
    }
    
    /**
     * Gets fax
     * @return fax the fax
     */
    public String getFax()
    {
        return fax;
    }
    
    /**
     * Sets region
     * @param region the region to set
     */
    public void setRegion(Regions region)
    {
        this.region = region;
    }
    
    /**
     * Gets region
     * @return region the region
     */
    public Regions getRegion()
    {
        return region;
    }
    
    /**
     * Sets province
     * @param province the province to set
     */
    public void setProvince(Provinces province)
    {
        this.province = province;
    }
    
    /**
     * Gets province
     * @return province the province
     */
    public Provinces getProvince()
    {
        return province;
    }
    
    /**
     * Sets city
     * @param city the city to set
     */
    public void setCity(Cities city)
    {
        this.city = city;
    }
    
    /**
     * Gets city
     * @return city the city
     */
    public Cities getCity()
    {
        return city;
    }
    
    /**
     * Sets email
     * @param email the email to set
     */
    public void setEmail(String email)
    {
        this.email = email;
    }
    
    /**
     * Gets email
     * @return email the email
     */
    public String getEmail()
    {
        return email;
    }
    
    /**
     * Sets phone
     * @param phone the phone to set
     */
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    
    /**
     * Gets phone
     * @return phone the phone
     */
    public String getPhone()
    {
        return phone;
    }
    
    /**
     * Sets address
     * @param address the address to set
     */
    public void setAddress(String address)
    {
        this.address = address;
    }
    
    /**
     * Gets address
     * @return address the address
     */
    public String getAddress()
    {
        return address;
    }
    
    /**
     * Sets zone
     * @param zone the zone to set
     */
    public void setZone(Zones zone)
    {
        this.zone = zone;
    }
    
    /**
     * Gets zone
     * @return zone the zone
     */
    public Zones getZone()
    {
        return zone;
    }
    
    /**
     * Sets department
     * @param department the department to set
     */
    public void setDepartment(Departments department)
    {
        this.department = department;
    }
    
    /**
     * Gets department
     * @return department the department
     */
    public Departments getDepartment()
    {
        return department;
    }
}
