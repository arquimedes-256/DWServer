package root.etc;

import java.util.Calendar;
import java.util.regex.Pattern;

import algz.model.Attribute;
import algz.model.Object;
import algz.model.Value;
import algz.utils.StringUtils;

public class Event implements Notificable
{	
	private abstract static class Validator
	{
		private static final Pattern pStart = Pattern.compile("(start|[i�]nici|come[�c]o)");
		private static final Pattern pEnd = Pattern.compile("(end|fim|final)");
		
		public static boolean isStartDate(Attribute attribute)
		{
			if(pStart.matcher(attribute.getName().toLowerCase()).find())
				return true;
			return false;
		}
		public static boolean isEndDate(Attribute attribute)
		{
			if(pEnd.matcher(attribute.getName().toLowerCase()).find())
				return true;
			return false;
		}
	}
	private float id;
	private Calendar start;
	private Calendar end;
	private Attribute startAttr;

	private Attribute endAttr;
	private boolean allDay;
	private String title;
	private String className;
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	private boolean editable = true;
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Calendar getStart() {
		return start;
	}

	public void setStart(Calendar start) {
		this.start = start;
	}

	public Calendar getEnd() {
		return end;
	}

	public void setEnd(Calendar end) {
		this.end = end;
	}

	public float getId() {
		return id;
	}

	public void setId(float id) {
		this.id = id;
	}

	public boolean isAllDay() {
		return allDay;
	}

	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}

	public Event(Object object) 
	{
		if(object != null)
		{
			this.setId(object.getId());
			buildDates(object);
			buildTitle(object);	
		}
	}
	private void buildTitle(Object object) 
	{
		StringBuilder titleBuilder = new StringBuilder("");
		for (Value value : object.getValores()) 
		{
			if(value.getAttribute().getTyper().getName().equals("Date"))
				continue;
			titleBuilder.append(StringUtils.getRealValue(value)+";\n");
		}
		setTitle(titleBuilder.toString()); 
	}
	public Event(Object object,int objectID)
	{
		this(object);
		if(this.getId() != objectID)
		{
			this.setEditable(false);
			this.setClassName("c1");
		}
		else
			this.setClassName("c0");
			
	}
	
	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	

	private void buildDates(Object object)
	{
		boolean flag_start 	= false;
		boolean flag_end 	= false;
		for (Value value : object.getValores()) 
		{
			Attribute attribute = value.getAttribute();
			
			if(attribute.getTyper().getName().equals("Date"))
			{
				if(Validator.isStartDate(attribute))
				{
					this.setStart(value.getDateTimeData());
					this.setStartAttr(value.getAttribute());
					flag_start = true;
				}
				else if(Validator.isEndDate(attribute))
				{
					this.setEnd(value.getDateTimeData());
					this.setEndAttr(value.getAttribute());
					flag_end = true;
				}
			}		
		}
		boolean xor = (flag_start && !flag_end) || (!flag_end && !flag_start);
	
		if(xor)
		{
			for (Value value : object.getValores()) 
			{
				Attribute attribute = value.getAttribute();
				
				if(attribute.getTyper().getName().equals("Date"))
				{
					this.setStart(value.getDateTimeData());
					this.setStartAttr(value.getAttribute());
					this.setAllDay(true);
					this.setEnd(null);
			
				}	
			}
		}
	}

	public Attribute getStartAttr() {
		return startAttr;
	}

	public void setStartAttr(Attribute attribute) {
		this.startAttr = attribute;
	}

	public Attribute getEndAttr() {
		return endAttr;
	}

	public void setEndAttr(Attribute attribute) {
		this.endAttr = attribute;
	}
	@Override
	public String getNotificationText() 
	{
		return getTitle();
	}
}
                                              