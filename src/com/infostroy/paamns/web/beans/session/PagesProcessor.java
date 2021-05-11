/**
 * 
 */
package com.infostroy.paamns.web.beans.session;

import com.infostroy.paamns.common.enums.PagesTypes;
import com.infostroy.paamns.common.exceptions.PagesProcessingException;

/**
 * 
 * @author Sergey Manoylo InfoStroy Co., 2009.
 * 
 */
public interface PagesProcessor
{
    /**
     * 
     * @param pages
     * @return
     */
    boolean isGrantForPage(PagesTypes page) throws PagesProcessingException;
}
