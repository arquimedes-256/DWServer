package root.bin;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import root.etc.Binary;
import algz.factory.SessionFactory;
import algz.model.Session;
import algz.model.User;
import core.base.ServiceBus;

public class login extends Binary
{

	@Override
	protected void init() 
	{
		Session session = SessionFactory.getInstance().
		 	getSession(findUserList(),getRequest().getRemoteAddr(),testParam(3, "-l") ? getArgs()[4]: "en");
		
		if(testParam(3,"logout"))
		{
			SessionFactory.getInstance().killSession(session,getRequest().getRemoteAddr());
		}

		this.getPipeline().setOutput(session);
	}
	@SuppressWarnings("unchecked")
	private List<User> findUserList()
	{
		return ServiceBus.getInstace().getHibernateTemplate()
				.findByCriteria
				(
						DetachedCriteria
						.forClass(User.class)
							.add(
									Restrictions.eq("login", getArgs()[1])
								)
							.add(
									Restrictions.eq("passw", getArgs()[2])
								)
				);
		
	}
	@Override
	public boolean needLogin() {
		return false;
	}
}
