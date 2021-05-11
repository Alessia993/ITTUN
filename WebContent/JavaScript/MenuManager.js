function SetCookie (name) 
{
    var coo=new Cookie(name);    
    var oldValue=coo.Load();
    var newValue;
    if(oldValue==null)
    {
        newValue = "yes";
    }
    else
    {
        if(oldValue == "yes")
        {
            newValue = "none";
        }
        else
        {
            newValue = "yes";
        }
    }
    var exp=new Date();
    exp.setFullYear(exp.getFullYear()+1);    
    var coo=new Cookie(name, newValue, "/", exp.toGMTString());    
    coo.Save();
}

function GetCookie (name) 
{
    var coo=new Cookie(name);    
    var oldValue=coo.Load();
    if(oldValue == null)
    {
    	oldValue = 'none';
    }
    if(oldValue == "yes")
    {
    	oldValue = '';
    }

    var el = document.getElementById(name);
    if(el!=null)
    {
    	el.style.display = oldValue;
    }
}