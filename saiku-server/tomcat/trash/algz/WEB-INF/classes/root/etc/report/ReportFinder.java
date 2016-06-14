package root.etc.report;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import root.etc.ClassDAO;
import algz.model.Application;
import algz.model.Attribute;
import algz.model.Class;
import algz.model.Customer;

public class ReportFinder {

	public static ResultFind findAttribute(Class clazz, String keyword,
			ResultFind lastResultFind, Attribute attribute) {
		if (lastResultFind == null)
			lastResultFind = new ResultFind();

		for (Attribute a : clazz.getAttributes()) 
		{
			if (a.getName().toLowerCase().contains(keyword.toLowerCase())) 
			{
				if (attribute != null)
					lastResultFind.push(attribute);
				lastResultFind.setAttribute(a);
				return lastResultFind;
			}
		}

		for (Attribute a : clazz.getAttributes()) {
			lastResultFind = findAttribute(a.getTyper(), keyword,
					lastResultFind, a);
		}
		return lastResultFind;
	}
	public static Class findFactClass(ArrayList<String> classifiedAtrs,
			ArrayList<String> classifiedClass, HibernateTemplate template, Customer customer, Application application) 
	{
		List<?> rs = ClassDAO.getInstance().findClassByAttributes(template,customer,
				classifiedAtrs,classifiedClass,null,application);

		return (Class) (rs.size() > 0 ? rs.get(0) : null);
	}
	public static Class findFactClass(ArrayList<String> attributes,HibernateTemplate template, Customer customer, Application application) 
	{
		List<?> rs = ClassDAO.getInstance().findClassByAttributes(template,customer,
				attributes,null,null, application);

		return (Class) (rs.size() > 0 ? (rs.get(0)) : null);
	}
	public static Class findFactClass(ArrayList<String> classifiedAtrs,
			ArrayList<String> classifiedClass,
			ArrayList<String> classifiedVals, HibernateTemplate template,Customer customer, Application application) 
	{
		List<?> rs = ClassDAO.getInstance().findClassByAttributes(template,
				customer, classifiedAtrs,classifiedClass,classifiedVals,application);
		return (Class) (rs.size() > 0 ? ((FactProduct)rs.get(0)).getFactClass() : null);
	}

	
}
