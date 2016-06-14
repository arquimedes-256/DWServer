package core.services;

import java.util.Iterator;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

@SuppressWarnings({ "unchecked", "hiding" })
public abstract class DefaultDAO<Object> extends HibernateDaoSupport implements ICoreDao<Object>{

	protected HibernateTemplate hibernateTemplate = getHibernateTemplate();
	
	private Class<?> entityClass;
	
	@Override
	public List<Object> find(String className) 
	{
		turnEntityClass(className);
		return (List<Object>)getHibernateTemplate().loadAll(getEntityClass());
	}

	@Override
	public List<Object> find(String className, int from, int to) {
		
		turnEntityClass(className);
		HibernateTemplate ht = getHibernateTemplate();
		return ht.findByCriteria(DetachedCriteria.forClass(getEntityClass()), from, to);
	}

	@Override
	public Object find(String className,int id) 
	{
		turnEntityClass(className);
		return (Object) getHibernateTemplate().load(getEntityClass(), id);
	}

	@Override
	public void save(String className,Object object) 
	{
		turnEntityClass(className);
		HibernateTemplate hiTemplate = getHibernateTemplate();
		hiTemplate.save(className, object);
	}

	@Override
	public void save(String className,List<Object> object) 
	{
		turnEntityClass(className);
		getSession().close();
		for (Iterator<Object> iterator = object.iterator(); iterator.hasNext();) 
		{
			Object t = (Object) iterator.next();
			getHibernateTemplate().save(className, t);
		}
	}

	@Override
	public void update(String className,Object object)
	{
		turnEntityClass(className);
		getSession().close();
		getHibernateTemplate().update(object);
	}
	@Override
	public void update(String className,List<Object> object) 
	{
		turnEntityClass(className);
		getSession().close();
		for (Iterator<Object> iterator = object.iterator(); iterator.hasNext();) 
		{
			Object t = (Object) iterator.next();
			getHibernateTemplate().update(t);
		}
	}

	@Override
	public void remove(String className,Object object) 
	{
		turnEntityClass(className);
		getSession().close();
		getHibernateTemplate().delete(object);
	}
	@Override
	public void remove(String className,List<Object> object) 
	{
		turnEntityClass(className);
		getSession().close();
		for (Iterator<Object> iterator = object.iterator(); iterator.hasNext();) 
		{
			Object t = (Object) iterator.next();
			getHibernateTemplate().delete(t);
			getSession().close();
		}
	}

	@Override
	public void merge(String className,Object object) 
	{
		turnEntityClass(className);
		getHibernateTemplate().merge(object);
	}

	@Override
	public void merge(String className,List<Object> object) 
	{
		turnEntityClass(className);
		for (Iterator<Object> iterator = object.iterator(); iterator.hasNext();) 
		{
			getSession().close();
			Object t = (Object) iterator.next();
			getHibernateTemplate().merge(t);
			getSession().close();
		}
	}
	/**
	 * Entidade Relacionada ao DAO
	 * @return the entityClass
	 * @exemple <code><b>return</b> Pessoa.class</code>
	 */
	public Class<?> getEntityClass()
	{
		return entityClass;
	}
	public void setEntityClass(Class<?> c)
	{
		this.entityClass = c;
	}
	private void turnEntityClass(String className)
	{
		try 
		{
			setEntityClass(Class.forName(className.replace("::", ".")));
		} 
		catch (Exception e) 
		{
			logger.debug("classe n��o encontrada: "+className);
		}
	}
	
}
