package root.dao;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import core.base.BaseEntity;
import root.etc.AlgoozDAO;
import algz.model.Application;
import algz.model.Customer;
import algz.model.Session;

public class ApplicationDAO extends AlgoozDAO
{
	private static final String APP_FIND_BY_NAME = "Select a from Application as a "
				+ "where a.name = ? "
				+ "and a.customer.id = ? "
				+ "and a.fileStatus.id in(2,3)";
	
	private static final String APP_FIND_ALL_= "Select a from Application as a "
			+ "where  a.customer.id = ? "
			+ "and a.fileStatus.id in(2,3)";	
	
	
	private static ApplicationDAO instance;
	public static ApplicationDAO getInstance()
	{
		if(instance == null)
			instance = new ApplicationDAO();
		return instance;
	}
	
	public Application getApplication(HibernateTemplate template,String path,Session session)
	{
		return (Application) getApplications(template, path, session).get(0);
		
	}
	public List<?> getApplications(HibernateTemplate template,String path,Session session)
	{
		return template.find(APP_FIND_BY_NAME,
				new Object[]{path,session.getUser().getCustomer().getId()});
		
	}
	public List<?> getAll(HibernateTemplate template,
			String folderName, BaseEntity baseEntityAux)
	{
		return template.find(APP_FIND_BY_NAME,new Object[]{folderName,baseEntityAux.getId()});
	}
	public List<?> getAll(HibernateTemplate template,Customer customer)
	{
		return template.find(APP_FIND_ALL_,new Object[]{customer.getId()});
	}
}
