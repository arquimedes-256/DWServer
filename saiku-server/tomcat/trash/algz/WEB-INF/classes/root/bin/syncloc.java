package root.bin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import root.etc.Binary;
import algz.model.Location;
import algz.model.Object;
import algz.model.Value;

public class syncloc extends Binary 
{
	private static class SMS
	{
		public String body;
		public String address;
	}
	private static class SMSFactory
	{
		
		public ArrayList<SMS> build(String requestBody)
		{
			ArrayList<SMS> list = new ArrayList<SMS>();
			String[] lines = requestBody.replace("syncloc", "").split("\n");
			
			for(String line : lines)
			{
				String[] attrs = line.split("\\|");
				if(attrs.length == 4)
				{
					SMS sms = new SMS();
					sms.address = attrs[1];
					sms.body = attrs[3];
					list.add(sms);	
				}
			}
			return list;
		}
	}
	@Override
	protected void init() 
	{
		String requestBody = getRequest().getParameter("shell");
		
		SMSFactory smsFactory = new SMSFactory();
		
		ArrayList<SMS> list = smsFactory.build(requestBody);
		System.out.println(requestBody);
		syncEach(list);
		
		System.out.println(requestBody);
		return;
	}

	private void syncEach(ArrayList<SMS> list) 
	{
		for(SMS sms : list)
		{
			if(isValidLocation(sms))
			{
				sync(sms);
			}
		}
	}

	private boolean isValidLocation(SMS sms) 
	{
		return sms.body.contains("lat") && (sms.body.contains("lng") || sms.body.contains("long"));
	}

	private void sync(SMS sms) 
	{
		Object o = getObjectByAddress(sms.address);
		Value locationValue = o.getVal("Localizacao");
		
		if(locationValue != null)
		{
			updateLocation(locationValue,sms.body);
		}
	}
	/*
	lat:-16.341266 long:-048.969971 speed:000.00
	t:09/09/11 14:10
	 */
	private String getField(String body, final String field) 
	{
		Matcher matcher = Pattern.compile(field+"\\s*?\\S\\s*?(\\-?[0-9]+\\.[0-9]+)").matcher(body);

		while ( matcher.find() ) 
		{
		    return matcher.group(2);
		}
		return null;
	} 
	private void updateLocation(Value locationValue, String body) 
	{
		String lat = getField(body,"(lat|latitude)");
		String lng = getField(body,"(lng|long|longitude)");
		
		Calendar ctime = buildTime(body);
		List<Location> locationHistory = locationValue.getLocationHistory();
		Location location = locationValue.getLocation();
		Location newLocation = new Location();
		
		newLocation.setLatString(lat);
		newLocation.setLngString(lng);
		
		newLocation.setTime(ctime);
		
		if(location != null)
		{
			locationHistory.add(location);
		}
		locationValue.setLocation(newLocation);
		getHibernateTemplate().saveOrUpdate(Value.class.getName(), locationValue);
	}
	/*
	 * t:09/09/11 14:10
	 */
	private Calendar buildTime(String body) 
	{
		Calendar cal = Calendar.getInstance();
		Matcher matcher = Pattern.compile("(\\d{2})/(\\d{2})/(\\d{2,4}) (\\d{2}):(\\d{2})").matcher(body);
		
		ArrayList<String> list = new ArrayList<String>();
		
		if ( matcher.find() ) 
		{
			list.add(matcher.group(1));
			list.add(matcher.group(2));
			list.add(matcher.group(3));
			list.add(matcher.group(4));
			list.add(matcher.group(5));
		}
		
		int day_of_month = Integer.parseInt(list.get(0));
		int month = Integer.parseInt(list.get(1));
		int year = Integer.parseInt(list.get(2));
		
		int hours = Integer.parseInt(list.get(3));
		int minutes = Integer.parseInt(list.get(4));
		
		cal.set(Calendar.DAY_OF_MONTH, day_of_month);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.YEAR,year < 2000 ? 2000 + year : year);
		cal.set(Calendar.HOUR, hours);
		cal.set(Calendar.MINUTE, minutes);
		cal.set(Calendar.SECOND, 0);
		
		return cal;
	}


	private Object getObjectByAddress(String address) 
	{
		String sql = "select o from Object o " +
				"join o.valores v " +
				"join v.attribute a " +
				"where a.name = 'CHIP' and v.stringData = ? ";
		List<?> list = getHibernateTemplate().find(sql, address);
		return (Object) (list.size() > 0 ? list.get(0) : null);
	}

	@Override
	public boolean needLogin() 
	{
		return false;
	}

}
