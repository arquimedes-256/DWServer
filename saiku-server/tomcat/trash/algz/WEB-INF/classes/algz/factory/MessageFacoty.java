package algz.factory;

import java.util.Calendar;

import root.etc.MsgCode;
import root.etc.Notificable;
import root.etc.ReasonCode;
import algz.model.Locale;
import algz.model.Message;
import algz.model.Notification;
import algz.model.Reason;
import core.base.ServiceBus;

public class MessageFacoty {
	
	
	private static MessageFacoty instance;
	
	public Notification buildNotication(MsgCode msgCode,Locale locale, Notificable notificable)
	{
		
		Object[] params = {msgCode.toString(),locale.getSign()}; 
		
		Message m = (Message) ServiceBus.getInstace().getHibernateTemplate()
			.find("Select m from Message as m where m.msgCode = ? and m.locale.sign = ?",params).get(0);
		
		String body;
		if(notificable instanceof ReasonCode)
		{
			Object[] params2 = {notificable.getNotificationText(),locale.getSign()};
			
			Reason r = (Reason) ServiceBus.getInstace().getHibernateTemplate()
					.find("Select r from Reason r where r.reasonCode = ? and r.locale.sign = ?",params2).get(0);
			body = r.getText();
		}
		else if(notificable != null)
		{
			body = notificable.getNotificationText();
		}
		else
			body = "";
		Notification n = new Notification();
		n.setBody(body);
		n.setDate(Calendar.getInstance());
		n.setMessage(m);
		
		return n;
	}
	public static MessageFacoty getInstance()
	{
		if(instance == null)
		{
			instance = new MessageFacoty();
		}
		return instance;
	}
	
}
