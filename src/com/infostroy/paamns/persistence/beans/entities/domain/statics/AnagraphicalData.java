/**
 * 
 */
package com.infostroy.paamns.persistence.beans.entities.domain.statics;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.infostroy.paamns.persistence.LocaleSessionManager;
import com.infostroy.paamns.persistence.beans.entities.LocalizableStaticEntity;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.SpecificGoals;

/**
 * 
 * @author Alexander Chelombitko InfoStroy Co., 2010.
 * 
 */
@Entity
@Table(name = "anagraphical_data")
public class AnagraphicalData extends LocalizableStaticEntity
{
    /**
     * Stores description value of entity.
     */
    @Column(name = "description")
    private String              description;
    
    @Column(name = "asse_id")
    private String              asse;
    
    /**
     * Stores importFESR value of entity.
     */
    @Column(name = "import_fesr")
    private String              importFESR;
    
    /**
     * Stores importCN value of entity.
     */
    //@Column(name = "import_cn")
    //private String              importCN;
     
    /**
     * Stores specificGoal value of entity.
     */
    @OneToMany(mappedBy = "anagraphicalData")
    // / @OrderBy("value")
    private List<SpecificGoals> specificGoals;
    
    /**
     * Sets description
     * 
     * @param description
     *            the description to set
     */
    public void setDescription(String description)
    {
        this.description = setLocalValue(getLocalParams(LocaleSessionManager
                .getInstance().getInstanceBean().getCurrLocale().toString(),
                this.description, description));
    }
    
    /**
     * Gets description
     * 
     * @return description the description
     */
    public String getDescription()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(),
                this.description, null));
    }
    
    /**
     * Sets specificGoals
     * 
     * @param specificGoals
     *            the specificGoals to set
     */
    public void setSpecificGoals(List<SpecificGoals> specificGoals)
    {
        this.specificGoals = specificGoals;
    }
    
    /**
     * Gets specificGoals
     * 
     * @return specificGoals the specificGoals
     */
    public List<SpecificGoals> getSpecificGoals()
    {
        return specificGoals;
    }
    
    /**
     * Sets importFESR
     * 
     * @param importFESR
     *            the importFESR to set
     */
    public void setImportFESR(String importFESR)
    {
        this.importFESR = setLocalValue(getLocalParams(LocaleSessionManager
                .getInstance().getInstanceBean().getCurrLocale().toString(),
                this.importFESR, importFESR));
    }
    
    /**
     * Gets importFESR
     * 
     * @return importFESR the importFESR
     */
    public String getImportFESR()
    {
        return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                .getInstanceBean().getCurrLocale().toString(), this.importFESR,
                null));
    }
    
    /**
     * Sets importCN
     * 
     * @param importCN
     *            the importCN to set
     */
    //public void setImportCN(String importCN)
    //{
        //this.importCN = setLocalValue(getLocalParams(LocaleSessionManager
                //.getInstance().getInstanceBean().getCurrLocale().toString(),
                //this.importCN, importCN));
    //}
    
    /**
     * Gets importCN
     * 
     * @return importCN the importCN
     */
    //public String getImportCN()
    //{
        //return getLocalValue(getLocalParams(LocaleSessionManager.getInstance()
                //.getInstanceBean().getCurrLocale().toString(), this.importCN,
                //null));
    //}
    
    
    /**
     * Sets asse
     * 
     * @param asse
     *            the asse to set
     */
    public void setAsse(String asse)
    {
        this.asse = asse;
    }
    
    /**
     * Gets asse
     * 
     * @return asse the asse
     */
    public String getAsse()
    {
        return this.asse;
    }
}
