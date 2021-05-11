/**
 * 
 */
package com.infostroy.paamns.web.tags;

import java.io.IOException;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;

import com.sun.facelets.FaceletContext;
import com.sun.facelets.FaceletException;
import com.sun.facelets.tag.TagHandler;
import com.sun.facelets.tag.jsf.ComponentConfig;

/**
 *
 * @author Sergey Manoylo
 * InfoStroy Co., 2010.
 *
 */
public class ShowNotArchived extends TagHandler
{
    /**
     * 
     */
    public void apply(FaceletContext faceletContext, UIComponent uiComponent)
            throws IOException, FacesException, FaceletException, ELException
    {
        this.nextHandler.apply(faceletContext, uiComponent);
    }
    
    /**
     * 
     * @param componentConfig
     */
    public ShowNotArchived(ComponentConfig componentConfig)
    {
        super(componentConfig);
    }
}
