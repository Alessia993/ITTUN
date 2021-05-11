/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain.enums;

import java.util.List;

import javax.persistence.CascadeType;
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
@Table(name = "not_regular_types")
public class NotRegularTypes extends LocalizableEnumeration
{
    
    /**
     * Stores serialVersionUID value of entity.
     */
    private static final long     serialVersionUID = 1197580444263806507L;
    
    @OneToMany(mappedBy = "parent")
    private List<NotRegularTypes> childs;
    
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = true)
    private NotRegularTypes       parent;
    
    /**
     * Sets childs
     * @param childs the childs to set
     */
    public void setChilds(List<NotRegularTypes> childs)
    {
        this.childs = childs;
    }
    
    /**
     * Gets childs
     * @return childs the childs
     */
    public List<NotRegularTypes> getChilds()
    {
        return childs;
    }
    
    /**
     * Sets parent
     * @param parent the parent to set
     */
    public void setParent(NotRegularTypes parent)
    {
        this.parent = parent;
    }
    
    /**
     * Gets parent
     * @return parent the parent
     */
    public NotRegularTypes getParent()
    {
        return parent;
    }
}
