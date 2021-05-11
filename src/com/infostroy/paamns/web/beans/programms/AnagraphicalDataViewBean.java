/**
 * 
 */
package com.infostroy.paamns.web.beans.programms;

import org.apache.myfaces.custom.tree.model.TreeModel;
import org.apache.myfaces.custom.tree2.TreeNode;
import org.apache.myfaces.custom.tree2.TreeNodeBase;
import org.hibernate.HibernateException;

import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.utils.Utils;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.enums.SpecificGoals;
import com.infostroy.paamns.persistence.beans.entities.domain.statics.AnagraphicalData;
import com.infostroy.paamns.web.beans.EntityViewBean;
import com.infostroy.paamns.web.beans.validator.ValidatorBean;

/**
 *
 * @author Alexander Chelombitko
 * InfoStroy Co., 2010.
 *
 */
public class AnagraphicalDataViewBean extends EntityViewBean<AnagraphicalData>
{
    private TreeModel treeModel;
    
    private TreeNode  treeData;
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityViewBean#Page_Load()
     */
    @SuppressWarnings("unchecked")
    @Override
    public void Page_Load() throws NumberFormatException, HibernateException,
            PersistenceBeanException, PersistenceBeanException
    {
        setList(BeansFactory.AnagraphicalData().Load());
        
        setTreeData(new TreeNodeBase("person", "Inbox", false));
        TreeNodeBase asseNode;
        for (AnagraphicalData item : getList())
        {
            if (ValidatorBean.IsNumber(item.getAsse()))
            {
                asseNode = new TreeNodeBase("person", Utils.getString(
                        "Resources", "asse", null) + " " + item.getAsse(),
                        false);
            }
            else
            {
                asseNode = new TreeNodeBase("person", item.getAsse(), false);
            }
            
            boolean isDescription = item.getDescription() != null
                    && !item.getDescription().isEmpty();
            TreeNodeBase description = new TreeNodeBase("person",
                    Utils.getString("Resources", "description", null),
                    !isDescription);
            if (isDescription)
            {
                description.getChildren().add(
                        new TreeNodeBase("document", item.getDescription(),
                                true));
            }
            
            asseNode.getChildren().add(description);
            
            boolean isImportFESR = item.getImportFESR() != null
                    && !item.getImportFESR().isEmpty();
            TreeNodeBase importFESR = new TreeNodeBase("person",
                    Utils.getString("Resources", "importFESR", null),
                    !isImportFESR);
            if (isImportFESR)
            {
                importFESR.getChildren()
                        .add(new TreeNodeBase("document", item.getImportFESR(),
                                true));
            }
            
            asseNode.getChildren().add(importFESR);
            
            /*boolean isImportCn = item.getImportCN() != null
                    && !item.getImportCN().isEmpty();
            TreeNodeBase importCN = new TreeNodeBase("person", Utils.getString(
                    "Resources", "importCN", null), !isImportCn);
            if (isImportCn)
            {
                importCN.getChildren().add(
                        new TreeNodeBase("document", item.getImportCN(), true));
            }
            
            asseNode.getChildren().add(importCN); */
            
            
            TreeNodeBase specificGoal = new TreeNodeBase("person",
                    Utils.getString("Resources", "specificGoal", null),
                    (item.getSpecificGoals() != null && item.getSpecificGoals()
                            .size() == 0));
            
            for (SpecificGoals goal : item.getSpecificGoals())
            {
                if (goal.getValue() != null && !goal.getValue().isEmpty())
                {
                	TreeNodeBase itemGoal = new TreeNodeBase("person", goal.getCode(), false);
                	               	
                	TreeNodeBase goalDescription = new TreeNodeBase("person", Utils.getString(
                            "Resources", "goalDescription", null), false);
                	
                	goalDescription.getChildren()
                            .add(new TreeNodeBase("document", goal.getValue(),
                                    true));
                	itemGoal.getChildren().add(goalDescription);
                	
                	if (goal.getThematic_object() != null && !goal.getThematic_object().isEmpty()){
                		TreeNodeBase goalThematic = new TreeNodeBase("person", Utils.getString(
                                "Resources", "goalThematic", null), false);
                    	
                    	goalThematic.getChildren()
                                .add(new TreeNodeBase("document", goal.getThematic_object(),
                                        true));
                    	itemGoal.getChildren().add(goalThematic);
                	}
                	
                	if (goal.getPriority_investment() != null && !goal.getPriority_investment().isEmpty()){
                		TreeNodeBase goalPriority = new TreeNodeBase("person", Utils.getString(
                                "Resources", "goalPriority", null), false);
                    	
                    	goalPriority.getChildren()
                                .add(new TreeNodeBase("document", goal.getPriority_investment(),
                                        true));
                    	itemGoal.getChildren().add(goalPriority);
                	}
                	
                	specificGoal.getChildren().add(itemGoal);
                
                }
            }
            
            asseNode.getChildren().add(specificGoal);
     
            getTreeData().getChildren().add(asseNode);
        }
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.beans.EntityViewBean#Page_Load_Static()
     */
    @Override
    public void Page_Load_Static() throws HibernateException,
            PersistenceBeanException
    {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * Sets treeModel
     * @param treeModel the treeModel to set
     */
    public void setTreeModel(TreeModel treeModel)
    {
        this.treeModel = treeModel;
    }
    
    /**
     * Gets treeModel
     * @return treeModel the treeModel
     */
    public TreeModel getTreeModel()
    {
        return treeModel;
    }
    
    /**
     * Sets treeData
     * @param treeData the treeData to set
     */
    public void setTreeData(TreeNode treeData)
    {
        this.treeData = treeData;
    }
    
    /**
     * Gets treeData
     * @return treeData the treeData
     */
    public TreeNode getTreeData()
    {
        return treeData;
    }
}
