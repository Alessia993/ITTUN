/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.domain.enums.ControllerTypes;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Countries;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
@Entity
@Table(name = "controller_user_anagraph")
public class ControllerUserAnagraph extends
        com.infostroy.paamns.persistence.beans.entities.PersistentEntity
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long serialVersionUID = -786211746816832967L;
    
    /**
     * Stores controllerType value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn(name = "controller_type_id")
    private ControllerTypes   controllerType;
    
    /**
     * Stores country value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn(name = "country_id")
    private Countries         country;
    
    /**
     * Stores fiscalCode value of entity.
     */
    @Column(name = "fiscal_code")
    private String            fiscalCode;
    
    /**
     * Stores vatNumber value of entity.
     */
    @Column(name = "vat_number")
    private String            vatNumber;
    
    /**
     * Stores nation value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn(name = "nation_id")
    private Countries         nation;
    
    /**
     * Stores placeOfLive value of entity.
     */
    @Column(name = "place_of_live")
    private String            placeOfLive;
    
    /**
     * Stores address value of entity.
     */
    @Column(name = "address")
    private String            address;
    
    /**
     * Stores phone value of entity.
     */
    @Column(name = "phone")
    private String            phone;
    
    /**
     * Stores fax value of entity.
     */
    @Column(name = "fax")
    private String            fax;
    
    /**
     * Stores legalPhone value of entity.
     */
    @Column(name = "legal_phone")
    private String            legalPhone;
    
    /**
     * Stores legalFax value of entity.
     */
    @Column(name = "legal_fax")
    private String            legalFax;
    
    /**
     * Stores user value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn(name = "user_id")
    private Users             user;
    
    /**
     * Stores denomination value of entity.
     */
    @Column(name = "denomination")
    private String            denomination;
    
    /**
     * Stores giuridicalType value of entity.
     */
    @Column(name = "giuridical_type")
    private String            giuridicalType;
    
    /**
     * Stores lawAddress value of entity.
     */
    @Column(name = "law_address")
    private String            lawAddress;
    
    /**
     * Stores birthDay value of entity.
     */
    @Column(name = "birth_day")
    private Date              birthDay;
    
    /**
     * Sets controllerType
     * 
     * @param controllerType
     *            the controllerType to set
     */
    public void setControllerType(ControllerTypes controllerType)
    {
        this.controllerType = controllerType;
    }
    
    /**
     * Gets controllerType
     * 
     * @return controllerType the controllerType
     */
    public ControllerTypes getControllerType()
    {
        return controllerType;
    }
    
    /**
     * Sets country
     * 
     * @param country
     *            the country to set
     */
    public void setCountry(Countries country)
    {
        this.country = country;
    }
    
    /**
     * Gets country
     * 
     * @return country the country
     */
    public Countries getCountry()
    {
        return country;
    }
    
    /**
     * Sets fiscalCode
     * 
     * @param fiscalCode
     *            the fiscalCode to set
     */
    public void setFiscalCode(String fiscalCode)
    {
        this.fiscalCode = fiscalCode;
    }
    
    /**
     * Gets fiscalCode
     * 
     * @return fiscalCode the fiscalCode
     */
    public String getFiscalCode()
    {
        return fiscalCode;
    }
    
    /**
     * Sets vatNumber
     * 
     * @param vatNumber
     *            the vatNumber to set
     */
    public void setVatNumber(String vatNumber)
    {
        this.vatNumber = vatNumber;
    }
    
    /**
     * Gets vatNumber
     * 
     * @return vatNumber the vatNumber
     */
    public String getVatNumber()
    {
        return vatNumber;
    }
    
    /**
     * Sets nation
     * 
     * @param nation
     *            the nation to set
     */
    public void setNation(Countries nation)
    {
        this.nation = nation;
    }
    
    /**
     * Gets nation
     * 
     * @return nation the nation
     */
    public Countries getNation()
    {
        return nation;
    }
    
    /**
     * Sets placeOfLive
     * 
     * @param placeOfLive
     *            the placeOfLive to set
     */
    public void setPlaceOfLive(String placeOfLive)
    {
        this.placeOfLive = placeOfLive;
    }
    
    /**
     * Gets placeOfLive
     * 
     * @return placeOfLive the placeOfLive
     */
    public String getPlaceOfLive()
    {
        return placeOfLive;
    }
    
    /**
     * Sets address
     * 
     * @param address
     *            the address to set
     */
    public void setAddress(String address)
    {
        this.address = address;
    }
    
    /**
     * Gets address
     * 
     * @return address the address
     */
    public String getAddress()
    {
        return address;
    }
    
    /**
     * Sets phone
     * 
     * @param phone
     *            the phone to set
     */
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    
    /**
     * Gets phone
     * 
     * @return phone the phone
     */
    public String getPhone()
    {
        return phone;
    }
    
    /**
     * Sets fax
     * 
     * @param fax
     *            the fax to set
     */
    public void setFax(String fax)
    {
        this.fax = fax;
    }
    
    /**
     * Gets fax
     * 
     * @return fax the fax
     */
    public String getFax()
    {
        return fax;
    }
    
    /**
     * Sets user
     * 
     * @param user
     *            the user to set
     */
    public void setUser(Users user)
    {
        this.user = user;
    }
    
    /**
     * Gets user
     * 
     * @return user the user
     */
    public Users getUser()
    {
        return user;
    }
    
    /**
     * Sets denomination
     * 
     * @param denomination
     *            the denomination to set
     */
    public void setDenomination(String denomination)
    {
        this.denomination = denomination;
    }
    
    /**
     * Gets denomination
     * 
     * @return denomination the denomination
     */
    public String getDenomination()
    {
        return denomination;
    }
    
    /**
     * Sets giuridicalType
     * 
     * @param giuridicalType
     *            the giuridicalType to set
     */
    public void setGiuridicalType(String giuridicalType)
    {
        this.giuridicalType = giuridicalType;
    }
    
    /**
     * Gets giuridicalType
     * 
     * @return giuridicalType the giuridicalType
     */
    public String getGiuridicalType()
    {
        return giuridicalType;
    }
    
    /**
     * Sets lawAddress
     * 
     * @param lawAddress
     *            the lawAddress to set
     */
    public void setLawAddress(String lawAddress)
    {
        this.lawAddress = lawAddress;
    }
    
    /**
     * Gets lawAddress
     * 
     * @return lawAddress the lawAddress
     */
    public String getLawAddress()
    {
        return lawAddress;
    }
    
    /**
     * Sets birthDay
     * 
     * @param birthDay
     *            the birthDay to set
     */
    public void setBirthDay(Date birthDay)
    {
        this.birthDay = birthDay;
    }
    
    /**
     * Gets birthDay
     * 
     * @return birthDay the birthDay
     */
    public Date getBirthDay()
    {
        return birthDay;
    }
    
    /**
     * Sets legalPhone
     * 
     * @param legalPhone
     *            the legalPhone to set
     */
    public void setLegalPhone(String legalPhone)
    {
        this.legalPhone = legalPhone;
    }
    
    /**
     * Gets legalPhone
     * 
     * @return legalPhone the legalPhone
     */
    public String getLegalPhone()
    {
        return legalPhone;
    }
    
    /**
     * Sets legalFax
     * 
     * @param legalFax
     *            the legalFax to set
     */
    public void setLegalFax(String legalFax)
    {
        this.legalFax = legalFax;
    }
    
    /**
     * Gets legalFax
     * 
     * @return legalFax the legalFax
     */
    public String getLegalFax()
    {
        return legalFax;
    }
}
