package root.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import root.etc.AlgoozDAO;
import root.etc.SearchBuilder;
import algz.lang.AlgoozList;
import algz.model.Attribute;
import algz.model.Class;
import algz.model.Session;
import core.base.BaseEntity;

public class ObjectDAO extends AlgoozDAO
{
	
	private static final String FIND_OBJECT_BY_CLASS = "Select o from "
			+ "Application app\n"
			+ "join app.modules m\n"
			+ "join m.classes c,\n"
			+ "Object o \n"
			+ "where o.clazz.id = c.id and o.clazz.id = ? and \n"
			+ "app.customer.id = %d ";
	
	private static String OBJECT_FIND_ALL_QUERY 
		= "Select distinct o from Object o ";
	private static String OBJECT_FIND_QUERY 	
		= OBJECT_FIND_ALL_QUERY
			+ "where o.clazz.id = ? ";
	
	private static String OBJECT_FIND_UNIQUE_QUERY = OBJECT_FIND_QUERY
			+ "and o.id =  ? and o.fileStatus.id in(2,3)";


	private static ObjectDAO instance;

	public static ObjectDAO getInstance()
	{
		if (instance == null)
			instance = new ObjectDAO();
		return instance;
	}

	@Override
	public List<?> getAll(HibernateTemplate hibernateTemplate,
			String folderName, BaseEntity baseEntityAux)
	{
		hibernateTemplate.setMaxResults(30);
		return hibernateTemplate.find(OBJECT_FIND_QUERY + " and o.fileStatus.id in(2,3)",
				new Object[] { baseEntityAux.getId() });
	}

	public void sendObjectToTrash(HibernateTemplate hibernateTemplate,
			String fileList,Class connectedClass)
	{
		hibernateTemplate
				.bulkUpdate("update Object o set o.fileStatus.id = 1 where o.id in("+fileList+")\n " +
						"and o.clazz.id = ?",new Object[]{connectedClass.getId()});
	}

	public algz.model.Object getUnique(HibernateTemplate hibernateTemplate,
			Class clazz, String path)
	{
		List<?> l = hibernateTemplate.find(OBJECT_FIND_UNIQUE_QUERY,
				new Object[] { clazz.getId(), Long.valueOf(path) });
		return (algz.model.Object) (l.size() > 0 ? l.get(0) : null);
	}
	
	public AlgoozList<?> findObject(HibernateTemplate template,
			long m, String[] keywords)
	{
		AlgoozList<?> l = SearchBuilder.buildSearch(template,m,keywords);
		
		return l;
	}

	public algz.model.Object getUniqueObject(HibernateTemplate hibernateTemplate,
			Long objectId, Attribute attribute)
	{
		Object[] o = new Object[]{objectId,attribute.getTyper().getId()};
		List<?> l = hibernateTemplate.find("select o from Object o where o.id = ? and o.clazz.id = ?",o);
		return (algz.model.Object) (l.size() > 0 ? l.get(0) : null);
	}

	public List<?> getEvents(HibernateTemplate hibernateTemplate, String path,
			Class connectedClass,int mounth, int year, int classID, int objectID) {
		
		 final String EVENT_SEARCH_QUERY = "Select distinct new root.etc.Event(o"+(objectID > 0 ? (","+objectID):"")+") from Object o \n"
					+ "join o.valores v "
					+ "where o.fileStatus.id in(2,3) ";
		
		final String YEAR_FILTER 	= " and YEAR(v.dateTimeData) = ?";
		final String MOUNTH_FILTER 	= " and MONTH(v.dateTimeData) = ?";
		final String CLASS_FILTER  	= " and o.clazz.id = ?";
		
		if(classID == 0)
		{
			if(mounth > 0 && year == 0)
				return hibernateTemplate.find(EVENT_SEARCH_QUERY + MOUNTH_FILTER,
						new Object[]{mounth});
			else if(mounth > 0 && year > 0)
				return hibernateTemplate.find(EVENT_SEARCH_QUERY + MOUNTH_FILTER + YEAR_FILTER,
						new Object[]{mounth,year});
		}
		else if(classID > 0)
		{
			if(mounth > 0 && year == 0)
				return hibernateTemplate.find(EVENT_SEARCH_QUERY + MOUNTH_FILTER + CLASS_FILTER,
						new Object[]{mounth,classID});
			else if(mounth > 0 && year > 0)
				return hibernateTemplate.find(EVENT_SEARCH_QUERY + MOUNTH_FILTER + YEAR_FILTER + CLASS_FILTER,
						new Object[]{mounth,year,classID});
		}
		return null;
	}


	public List<?> findAttributeByValue(
			ArrayList<String> classifiedVals, HibernateTemplate template,
			Session session) {
		
		StringBuilder paramBuilder= new StringBuilder();
		ArrayList<String> argsList = new ArrayList<String>();
		
		Iterator<String> cursorVals = classifiedVals.iterator();
		while(cursorVals.hasNext())
		{
			paramBuilder.append("regexp(v.stringData,?) = 1 ");
			argsList.add(cursorVals.next());
			
			if(cursorVals.hasNext())
				paramBuilder.append(" or ");
			
		}
		
		String sql = String.format("Select distinct v from "
				+ "Application app\n"
				+ "join app.modules m\n"
				+ "join m.classes c,\n"
				+ "Object o \n"
				+ "join o.valores v\n"
				+ "where o.clazz.id = c.id and \n"
				+ "app.customer.id = %d and ("+paramBuilder.toString()+")",
					session
						.getUser()
						.getCustomer()
						.getId()
				);
			List<?> l = template.find(sql,argsList.toArray());
			return l;
	}

	public long getLabelsCount(HibernateTemplate template, Session session,
			String p) {
		String sql = String.format("Select count(a) from "
				+ "Application app\n"
				+ "join app.modules m\n"
				+ "join m.classes c,\n"
				+ "Object o \n"
				+ "join o.valores v\n"
				+ "join v.attribute a\n"
				+ "where o.clazz.id = c.id and \n"
				+ "app.customer.id = %d and regexp(a.name,?) = 1 and a.typer.id = 10 ",
					session
						.getUser()
						.getCustomer()
						.getId()
				);
			List<?> l = template.find(sql,p);
			return l.size() > 0 ? (Long)l.get(0) : 0;
	}
	public Long getValuesCount(HibernateTemplate template, Session session, String p) 
	{
		String sql = 
				("Select count(v) from Value v "
			+ " where regexp(v.stringData,?) = 1 ");
		List<?> l = template.find(sql,p);
		return l.size() > 0 ? (Long)l.get(0) : 0;
	}
	@SuppressWarnings("unchecked")
	public List<algz.model.Object> findObjects(HibernateTemplate template,
			Session session, long clazzId) 
	{
		String sql = String.format(FIND_OBJECT_BY_CLASS,
				session
					.getUser()
					.getCustomer()
					.getId()
			);
		List<?> l = template.find(sql,clazzId);
		return ((List<algz.model.Object>)(l.size() > 0 ? l : null));
	}
	
	public algz.model.Object findObject(HibernateTemplate template,
			Session session, long clazzId, long objectId) {
		String sql = String.format(FIND_OBJECT_BY_CLASS+" and o.id = ? ",
				session
					.getUser()
					.getCustomer()
					.getId()
			);
		List<?> l = template.find(sql,clazzId,objectId);
		return (algz.model.Object)(l.size() > 0 ? l.get(0) : null);
		
	}
}
