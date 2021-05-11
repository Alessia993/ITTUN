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

import com.infostroy.paamns.persistence.beans.entities.domain.enums.ResponsibilityTypes;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "responsibilities")
public class Responsibilities extends
        com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long        serialVersionUID = -4126547204351884407L;
    
    /**
     * Stores cfPartnerAnagraph value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn(name = "cf_partner_anagraph_id")
    private CFPartnerAnagraphProject cfPartnerAnagraph;
    
    /**
     * Stores fiscalCode value of entity.
     */
    @Column(name = "fiscal_code")
    private String                   fiscalCode;
    
    /**
     * Stores surname value of entity.
     */
    @Column
    private String                   surname;
    
    /**
     * Store name value of entity
     */
    @Column
    private String                   name;
    
    /**
     * Stores role value of entity.
     */
    @Column
    private String                   role;
    
    /**
     * Stores address value of entity.
     */
    @Column
    private String                   address;
    
    /**
     * Stores email value of entity.
     */
    @Column
    private String                   email;
    
    /**
     * Stores phone value of entity.
     */
    @Column
    private String                   phone;
    
    /**
     * Stores fax value of entity.
     */
    @Column
    private String                   fax;
    
    /**
     * Stores type value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn
    private ResponsibilityTypes      type;
    
    /**
     * Sets cfPartnerAnagraph
     * @param cfPartnerAnagraph the cfPartnerAnagraph to set
     */
    public void setCfPartnerAnagraph(CFPartnerAnagraphProject cfPartnerAnagraph)
    {
        this.cfPartnerAnagraph = cfPartnerAnagraph;
    }
    
    /**
     * Gets cfPartnerAnagraph
     * @return cfPartnerAnagraph the cfPartnerAnagraph
     */
    public CFPartnerAnagraphProject getCfPartnerAnagraph()
    {
        return cfPartnerAnagraph;
    }
    
    /**
     * Sets fiscalCode
     * @param fiscalCode the fiscalCode to set
     */
    public void setFiscalCode(String fiscalCode)
    {
        this.fiscalCode = fiscalCode;
    }
    
    /**
     * Gets fiscalCode
     * @return fiscalCode the fiscalCode
     */
    public String getFiscalCode()
    {
        return fiscalCode;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * Sets surname
     * @param surname the surname to set
     */
    public void setSurname(String surname)
    {
        this.surname = surname;
    }
    
    /**
     * Gets surname
     * @return surname the surname
     */
    public String getSurname()
    {
        return surname;
    }
    
    /**
     * Sets role
     * @param role the role to set
     */
    public void setRole(String role)
    {
        this.role = role;
    }
    
    /**
     * Gets role
     * @return role the role
     */
    public String getRole()
    {
        return role;
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
     * Sets type
     * @param type the type to set
     */
    public void setType(ResponsibilityTypes type)
    {
        this.type = type;
    }
    
    /**
     * Gets type
     * @return type the type
     */
    public ResponsibilityTypes getType()
    {
        return type;
    }
}
