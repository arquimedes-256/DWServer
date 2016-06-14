package root.dao;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import algz.model.Session;
import algz.model.User;

public class UserDAO 
{
	private static UserDAO instance = new UserDAO();
	
	public UserDAO()
	{
		if(instance != null)
			throw new Error("use getInstance");
	}
	
	public static UserDAO getInstance()
	{
		return instance;
	}
	public User find(String login,HibernateTemplate template,Session session)
	{
		User user = null;
		List<?> users = template.find("select u from User u where u.login = ?",new Object[]{login});
		user = (User)users.get(0);
		return user;
	}
}
