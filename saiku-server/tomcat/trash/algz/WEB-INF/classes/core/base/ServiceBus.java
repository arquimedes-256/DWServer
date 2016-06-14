package core.base;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;

import orion.channel.app.OrionService;

public class ServiceBus 
{
	private static ServiceBus instance;
	
	public static ServiceBus getInstace()
	{
		if(instance == null)
		{
			instance = new ServiceBus();
		}
		return instance;
	}
	
	private OrionService service = SingletonsPool.getService();
	
	@SuppressWarnings("unchecked")
	public List<Object> getEntities(String clazzName,int status,int from,int to)
	{
		return service.getHibernateTemplate()
		.findByCriteria
		(
				DetachedCriteria.forEntityName(clazzName)
					.add(Restrictions.eq("status", status))
					,from
					,to
		);
	}
	public HibernateTemplate getHibernateTemplate()
	{
		return service.getHibernateTemplate();
	}
}
