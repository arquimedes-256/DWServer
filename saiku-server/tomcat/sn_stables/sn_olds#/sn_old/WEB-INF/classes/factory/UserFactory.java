package factory;

import model.User;
import dao.LoginDAO;

public class UserFactory 
{
	public static UserFactory instance = new UserFactory();
	
	public UserFactory()
	{
		if(instance != null)
			throw new Error("use getinstance");
	}
	public static UserFactory getInstance()
	{
		return instance;
	}
	public User buildUser(String user, String pass) 
	{
		boolean isSuccess = LoginDAO.login(user, pass);
		
		return new User(user, buildName(user), PerfilFactory.buildPerfil(user), isSuccess);
	}
	private String buildName (String user)
	{
		String[] ws = user.split("\\.");
		String nome = "";
		String sobrenome = "";
		
		for(int i=0;i<ws[0].length();i++)
		{
			if(i==0)
				nome += (ws[0].charAt(i)+"").toUpperCase();
			else
				nome += ws[0].charAt(i);
		}
		if(ws.length > 1)
		{
			for(int i=0;i<ws[1].length();i++)
			{
				if(i==0)
					sobrenome += (ws[1].charAt(i)+"").toUpperCase();
				else
					sobrenome += ws[1].charAt(i);
			}
		}
		return nome+" "+sobrenome;
	};
	

}
