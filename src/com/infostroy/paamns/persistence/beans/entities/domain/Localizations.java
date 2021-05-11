package com.infostroy.paamns.persistence.beans.entities.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.PersistentEntity;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Cities;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Countries;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Departments;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Provinces;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Regions;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.Zones;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 * modified by Ingloba360
 *
 */
@Entity
@Table(name = "localizations")
public class Localizations extends PersistentEntity {
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long serialVersionUID = -5682870168035950971L;
    
    /**
     * Stores project value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn
    private Projects          project;
    
    /**
     * Stores isMain value of entity.
     */
    @Column(name = "is_main")
    private boolean           isMain;
    
    /**
     * Stores country value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn
    private Countries         country;
    
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
     * Stores coordinateX value of entity.
     */
    @Column(name = "coordinate_x")
    private String            coordinateX;
    
    /**
     * Stores coordinateY value of entity.
     */
    @Column(name = "coordinate_y")
    private String            coordinateY;
    
    /**
     * Stores department value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn
    private Departments       department;
    
    /**
     * Stores zone value of entity.
     */
    @ManyToOne(cascade = {
            CascadeType.ALL, CascadeType.ALL
    })
    @JoinColumn
    private Zones             zone;
    
    /**
     * Stores address value of entity.
     */
    @Column
    private String            address;
    
    @Column(name = "cod_cap", columnDefinition="char(5)")
    private String codCap;
    
    /**
     * Sets project
     * @param project the project to set
     */
    public void setProject(Projects project)
    {
        this.project = project;
    }
    
    /**
     * Gets project
     * @return project the project
     */
    public Projects getProject()
    {
        return project;
    }
    
    /**
     * Sets isMain
     * @param isMain the isMain to set
     */
    public void setMain(boolean isMain)
    {
        this.isMain = isMain;
    }
    
    /**
     * Gets isMain
     * @return isMain the isMain
     */
    public boolean isMain()
    {
        return isMain;
    }
    
    /**
     * Sets country
     * @param country the country to set
     */
    public void setCountry(Countries country)
    {
        this.country = country;
    }
    
    /**
     * Gets country
     * @return country the country
     */
    public Countries getCountry()
    {
        return country;
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
     * Sets coordinateX
     * @param coordinateX the coordinateX to set
     */
    public void setCoordinateX(String coordinateX)
    {
        this.coordinateX = coordinateX;
    }
    
    /**
     * Gets coordinateX
     * @return coordinateX the coordinateX
     */
    public String getCoordinateX()
    {
        return coordinateX;
    }
    
    /**
     * Sets coordinateY
     * @param coordinateY the coordinateY to set
     */
    public void setCoordinateY(String coordinateY)
    {
        this.coordinateY = coordinateY;
    }
    
    /**
     * Gets coordinateY
     * @return coordinateY the coordinateY
     */
    public String getCoordinateY()
    {
        return coordinateY;
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
     * Sets codCap.
     * @param codCap
     */
    public void setCodCap(String codCap) {
        this.codCap = codCap;
    }
    
    /**
     * Gets codCap.
     * @return codCap
     */
    public String getCodCap() {
        return codCap;
    }
}
