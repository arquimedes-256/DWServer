package algz.factory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import algz.model.Locale;
import algz.model.Session;
import algz.model.TTY;
import algz.model.User;
import core.base.ServiceBus;

public class SessionFactory 
{
	private static SessionFactory instance;
	
	public SessionFactory()
	{
		if(instance != null)
			try 
			{
				throw new Exception("use getInstance");
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
	}
	
	public static SessionFactory getInstance()
	{
		if(instance == null)
		{
			instance = new SessionFactory(); 
		}
		return instance;
	}
	public Session getSession(List<User> userList,String remoteAddr,String locale)
	{
		if(userList.size() > 0)
		{
			User user = userList.get(0);
			Session openedSession = getSessionOpened(user);
			
			if(openedSession == null)
			{
				HibernateTemplate hibernateTemplate = ServiceBus.getInstace().getHibernateTemplate();
				Locale loc = (Locale) hibernateTemplate.find("Select l from Locale as l where l.sign = ?",locale).get(0);
				Session session = new Session();	
				session.setIp(remoteAddr);
				session.setBornDate(Calendar.getInstance());
				session.setUser(user);
				session.setLocale(loc);
				session.setTtys(new ArrayList<TTY>());
				TTY tty = new TTY();
				session.getTtys().add(tty);
				session.setTty(tty);
				simpleSave(TTY.class,tty);
				saveSession(session);	
				openedSession = session;
			}
			return openedSession;
		}
		return null;
		
	}
	public void saveSession(Session session)
	{
		ServiceBus.getInstace()
			.getHibernateTemplate()
			.saveOrUpdate(Session.class.getName(), session);
	}
	public Session getSessionOpened(User user)
	{
		@SuppressWarnings("unchecked")
		List<Session> sessions = ServiceBus.getInstace()
			.getHibernateTemplate()
			.find("Select s from Session as s where s.user.login = ? and s.deathDate is null",user.getLogin());
		return sessions.size() == 0 ? null : sessions.get(0);
	}
	public void simpleSave(Class<?> Entity,Object object)
	{
		ServiceBus.getInstace().getHibernateTemplate().saveOrUpdate(Entity.getName(), object);
	}
	public void killSession(Session session,String ip) 
	{
		session.setDeathDate(Calendar.getInstance());
		ServiceBus.getInstace().getHibernateTemplate().update(Session.class.getName(), session);
		sessionMap.put(ip, null);
	}
	public Session getSessionByIp(String ipAddress)
	{
		Session session = null;
		if(sessionMap.get(ipAddress) != null)
		{
			session = sessionMap.get(ipAddress);
		}
		else
		{
			@SuppressWarnings("unchecked")
			List<Session> sessions = ServiceBus.getInstace()
					.getHibernateTemplate()
					.find("Select s from Session s where s.ip = ? and s.deathDate is null",ipAddress);	
			session =  sessions.size() == 0 ? null : sessions.get(0);
			sessionMap.put(ipAddress, session);
		}
		return session;
		
	}
	private HashMap<String, Session> sessionMap = new HashMap<String, Session>();
}
