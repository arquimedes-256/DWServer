package root.dao;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import algz.model.Class;
import algz.model.User;

public class BackpackDAO {

	private static BackpackDAO instance = new BackpackDAO();
	
	public BackpackDAO()
	{
		if(instance != null)
			throw new Error("use getinstance");
	}
	
	public static BackpackDAO getInstance() 
	{
		return instance;
	}

	@SuppressWarnings("unchecked")
	public List<Class> getBackpacks(HibernateTemplate hibernateTemplate,
			User user) 
	{
		String sql = "Select c from Class c join c.attributes a " +
				"	where a.typer.id  = all(select a.id from Attribute a where a.name in( 'Integer','User') ) ";
		
		return (List<Class>)hibernateTemplate.find(sql);
	}

}
