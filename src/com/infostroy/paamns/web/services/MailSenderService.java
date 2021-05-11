/**
 * 
 */
package com.infostroy.paamns.web.services;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.infostroy.paamns.common.enums.MailStatuses;
import com.infostroy.paamns.common.exceptions.PersistenceBeanException;
import com.infostroy.paamns.common.helpers.mail.MailHelper;
import com.infostroy.paamns.persistence.beans.entities.domain.Mails;
import com.infostroy.paamns.web.services.base.BaseDBService;

/**
 *
 * @author Vladimir Zrazhevskiy
 * InfoStroy Co., 2012.
 *
 */
public class MailSenderService extends BaseDBService
{
    
    /**
     * @param name
     */
    public MailSenderService(String name)
    {
        super(name);
        // TODO Auto-generated constructor stub
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.services.base.BaseService#routineFuncInternal()
     */
    @Override
    protected void routineFuncInternal()
    {
        try
        {
            
            List<Mails> mails = loadMails();
            
            if (mails != null && !mails.isEmpty())
            {
                for (Mails mail : mails)
                {
                    MailHelper.getInstance().sendMail(mail, getSession());
                }
            }
            
        }
        catch(Exception ex)
        {
            log.error(ex);
        }
        
    }
    
    @SuppressWarnings("unchecked")
    private List<Mails> loadMails() throws HibernateException,
            PersistenceBeanException
    {
        List<Mails> list = this.getSession().createCriteria(Mails.class)
                .add(Restrictions.eq("deleted", false))
                .add(Restrictions.eq("status", MailStatuses.Completed.getId()))
                .addOrder(Order.desc("createDate")).list();
        return list;
    }
    
    /* (non-Javadoc)
     * @see com.infostroy.paamns.web.services.base.BaseService#getPollTimeKey()
     */
    @Override
    protected int getPollTimeKey()
    {
        // TODO Auto-generated method stub
        return 2;
    }
    
}
