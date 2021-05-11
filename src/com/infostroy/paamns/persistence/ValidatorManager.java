package com.infostroy.paamns.persistence;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class ValidatorManager
{
    
    private static final Validator validator;
    static
    {
        try
        {
            ValidatorFactory factory = Validation
                    .buildDefaultValidatorFactory();
            validator = factory.getValidator();
        }
        catch(Throwable ex)
        {
            // Make sure you log the exception, as it might be swallowed
            System.err
                    .println("Initial ValidatorFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    /**
     * Gets session
     * @return session the session
     */
    public static Validator getValidator()
    {
        return validator;
    }
    
    /*private static Validator CreateSession()
    {
    	if(validator==null)
    	{
    		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    		validator = factory.getValidator();
    	}
        return validator;
    }
    
    public static void CloseSession()
    {
        if(session!=null)
        {
            session.flush();
            session.close();
        }
    }*/
    
}
