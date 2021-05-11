/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain.enums;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.beans.entities.LocalizableEnumeration;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
@Entity
@Table(name = "ateco")
public class Ateco extends LocalizableEnumeration
{
    /**
     * Stores code value of entity.
     */
    private String      code;
    
    @OneToMany(mappedBy = "parent")
    private List<Ateco> childs;
    
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = true)
    private Ateco       parent;
    
    @Column(name = "level", nullable = false)
    private Integer     level;
    
    /**
     * Gets childs
     * @return childs the childs
     */
    public List<Ateco> getChilds()
    {
        return this.childs;
    }
    
    /**
     * Sets childs
     * @param childs the childs to set
     */
    public void setChilds(List<Ateco> childs)
    {
        this.childs = childs;
    }
    
    /**
     * Gets parent
     * @return parent the parent
     */
    public Ateco getParent()
    {
        return this.parent;
    }
    
    /**
     * Sets parent
     * @param parent the parent to set
     */
    public void setParent(Ateco parent)
    {
        this.parent = parent;
    }
    
    /**
     * Gets level
     * @return level the level
     */
    public Integer getLevel()
    {
        return this.level;
    }
    
    /**
     * Sets level
     * @param level the level to set
     */
    public void setLevel(Integer level)
    {
        this.level = level;
    }
    
    /**
     * Sets code
     * @param code the code to set
     */
    public void setCode(String code)
    {
        this.code = code;
    }
    
    /**
     * Gets code
     * @return code the code
     */
    public String getCode()
    {
        return code;
    }
}
