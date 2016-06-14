package root.dao;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import algz.model.Customer;
import algz.model.Session;
import algz.model.ui.Component;
import algz.model.ui.UIApp;


public class UIAppDAO 
{
	private static UIAppDAO instance;
	public boolean exists(String uiAppName,HibernateTemplate template,Customer customer)
	{
		List<?> rs = template.find("Select count(uapp) from UIApp uapp "
				+ "where uapp.name = ? and uapp.customer.id = ? ",
				new Object[]{uiAppName,customer.getId()});
		
		return ((Long)rs.get(0)) > 0;
	}
	public static UIAppDAO getInstance()
	{
		return instance == null ? instance = new UIAppDAO() : instance;
	}
	public UIApp get(String uiAppName, HibernateTemplate template,
			Session sesion) 
	{
		List<?> rs = template.find("Select uapp from UIApp uapp "
				+ "where uapp.name  = ? and uapp.customer.id = ? ",
				new Object[]{uiAppName,sesion.getUser().getId()});
		
		return (UIApp)(rs.size() > 0 ? rs.get(0) : null);
		
	}
	@SuppressWarnings("unchecked")
	public List<Component> getComponents(HibernateTemplate hibernateTemplate) {
		
		return hibernateTemplate.find("Select c from Component c");
	}
	public List<?> getUIApps(HibernateTemplate template, Session session) 
	{
		List<?> rs = template.find("Select uapp.name from UIApp uapp where uapp.customer.id = ? ",
				new Object[]{session.getUser().getCustomer().getId()});
		return (List<?>)rs;
	}
	
}
