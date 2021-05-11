package com.infostroy.paamns.web.beans.support;

import java.util.ArrayList;
import java.util.List;

import com.infostroy.paamns.common.enums.DurStateTypes;
import com.infostroy.paamns.common.enums.LocationForCostDef;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.persistence.beans.BeansFactory;
import com.infostroy.paamns.persistence.beans.entities.domain.CostDefinitions;
import com.infostroy.paamns.persistence.beans.entities.domain.DurCompilations;

public class DurCompilationSupport
{
    
    public void fixOldDurs() throws PersistenceBeanException
    {
        List<DurCompilations> durCompilations = BeansFactory.DurCompilations()
                .Load();
        for (DurCompilations durCompilation : durCompilations)
        {
            try
            {
                double zoneCurrentTotalValue = 0d;
                List<CostDefinitions> listCD = new ArrayList<CostDefinitions>();
                listCD.addAll(BeansFactory.CostDefinitions()
                        .GetByDurCompilation(durCompilation.getId()));
                if (durCompilation.getDurState() == null
                        || DurStateTypes.Create.getValue()
                                .equals(durCompilation.getDurState().getId())
                        || DurStateTypes.STCEvaluation.getValue()
                                .equals(durCompilation.getDurState().getId()))
                {
                    for (CostDefinitions cost : listCD)
                    {
                        
                        if (Boolean.TRUE.equals(cost.getAdditionalFinansing()))
                        {
                            continue;
                        }
                        
                        if (LocationForCostDef.PALERMO
                                .equals(cost.getLocation())
                                || LocationForCostDef.CATANIA
                                        .equals(cost.getLocation()))
                        {
                            
                            if (cost.getCfCheck() != null)
                            {
                                zoneCurrentTotalValue += cost.getCfCheck();
                            }
                            else
                            {
                                zoneCurrentTotalValue += cost
                                        .getCilCheck() != null
                                                ? cost.getCilCheck() : 0d;
                            }
                            
                        }
                    }
                    
                }
                else if (DurStateTypes.AGUEvaluation.getValue()
                        .equals(durCompilation.getDurState().getId()))
                {
                    for (CostDefinitions cost : listCD)
                    {
                        
                        if (Boolean.TRUE.equals(cost.getAdditionalFinansing()))
                        {
                            continue;
                        }
                        
                        if (LocationForCostDef.PALERMO
                                .equals(cost.getLocation())
                                || LocationForCostDef.CATANIA
                                        .equals(cost.getLocation()))
                        {
                            
                            zoneCurrentTotalValue += cost
                                    .getStcCertification() != null
                                            ? cost.getStcCertification()
                                                    .doubleValue()
                                            : 0d;
                            
                        }
                    }
                    
                }
                else if (DurStateTypes.ACUEvaluation.getValue()
                        .equals(durCompilation.getDurState().getId()))
                {
                    for (CostDefinitions cost : listCD)
                    {
                        
                        if (Boolean.TRUE.equals(cost.getAdditionalFinansing()))
                        {
                            continue;
                        }
                        
                        if (LocationForCostDef.PALERMO
                                .equals(cost.getLocation())
                                || LocationForCostDef.CATANIA
                                        .equals(cost.getLocation()))
                        {
                            
                            zoneCurrentTotalValue += cost
                                    .getAguCertification() != null
                                            ? cost.getAguCertification()
                                                    .doubleValue()
                                            : 0d;
                            
                        }
                    }
                    
                }
                else if (DurStateTypes.ACUEvaluation.getValue()
                        .doubleValue() < durCompilation.getDurState().getId()
                                .doubleValue())
                
                {
                    for (CostDefinitions cost : listCD)
                    {
                        
                        if (Boolean.TRUE.equals(cost.getAdditionalFinansing()))
                        {
                            continue;
                        }
                        
                        if (LocationForCostDef.PALERMO
                                .equals(cost.getLocation())
                                || LocationForCostDef.CATANIA
                                        .equals(cost.getLocation()))
                        {
                            
                            zoneCurrentTotalValue += cost
                                    .getAguCertification() != null
                                            ? cost.getAguCertification()
                                                    .doubleValue()
                                            : 0d;
                            
                        }
                    }
                    
                }
                durCompilation
                        .setZoneCurrentTotal(zoneCurrentTotalValue * 0.85);
                BeansFactory.DurCompilations().Save(durCompilation);
            }
            catch (Exception ex)
            {
            }
        }
    }
    
}
