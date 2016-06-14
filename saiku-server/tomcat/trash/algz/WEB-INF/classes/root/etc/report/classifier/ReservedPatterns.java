package root.etc.report.classifier;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

import root.etc.report.classifier.annotations.ReservedKey;


public class ReservedPatterns 
{
	private static ReservedPatterns instance;
	@ReservedKey
	public Pattern IN = Pattern.compile("^(in|em|dentro|inner)$");
	
	@ReservedKey
	(
			isFilter=true,isTemporal=true,
			preColumnValue	=	"year(x.dateTimeData)||'-'||month(x.dateTimeData)||'-'||day(x.dateTimeData),",
			filterValue		=	"and year(x.dateTimeData) = year(sysdate()) "+ 
					"and month(x.dateTimeData) = month(sysdate())"+ 
					"and day(x.dateTimeData) = day(sysdate())"
	)
	public Pattern TODAY = Pattern.compile("^(hoje|today)$");
	
	@ReservedKey(isFilter=true,isTemporal=true,preColumnValue="year(x.dateTimeData)||' / '||month(x.dateTimeData),")
	public Pattern MONTHLY = Pattern.compile("^(mes|mensal|month)"); 
	
	@ReservedKey(
			isFilter=true,
			isTemporal=true,
			preColumnValue="hour(x.dateTimeData)||':'||minute(x.dateTimeData),"
	)
	public Pattern HOURLY = Pattern.compile("^(hora|ora|hour|di[aáàâ]ria)");
	
	@ReservedKey
	public Pattern OF = Pattern.compile("^(of|de)$");
	
	@ReservedKey(isAgregation=true,postColumnValue="count(*)")
	public Pattern COUNT = Pattern.compile("^(count|conte|num|quant)");
	
	@ReservedKey(isAgregation=true,postColumnValue="sum(%s)")
	public Pattern SUM = Pattern.compile("^(soma|some|sum|total|calc)");
	
	public ReservedPatterns() throws Exception
	{
		if(instance != null)
			throw new Error("use getInstance");
		
	}
	Pattern getPatternOf(String p)
	{
		if(!p.startsWith("*"))
		{
			for(Field f:ReservedPatterns.class.getFields())
			{
				if(f.getType().equals(Pattern.class))
				{
					try
					{
						Pattern pattern = (Pattern)f.get(this);
						if(pattern.matcher(p).find())
							return pattern;
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}	
		}
		return null;
	}

	private Field getField(Pattern pattern)
	{
		for(Field f: ReservedPatterns.class.getFields())
		{
			Pattern p2;
			try 
			{
				p2 = (Pattern)f.get(this);
				
				if(p2.equals(pattern))
				{
					return f;
				}
			} 
			catch (IllegalArgumentException e) 
			{
				e.printStackTrace();
			} 
			catch (IllegalAccessException e) 
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	private String getPostColumnValue(Pattern pattern,String... cols)
	{
		String val = null;
		String buf = getField(pattern).getAnnotation(ReservedKey.class).postColumnValue();
		if(cols == null)
			val = buf;
		else
			val = String.format(buf,(Object)cols);
		return val;
	}
	private String getPreColumnValue(Pattern pattern,String... cols) 
	{
		String val = null;
		String buf = getField(pattern).getAnnotation(ReservedKey.class).preColumnValue();
		if(cols == null)
			val = buf;
		else
			val = String.format(buf,(Object)cols);
		return val;
	}
	private String getFilterValue(Pattern pattern,String... cols) 
	{
		String val = null;
		String buf = getField(pattern).getAnnotation(ReservedKey.class).filterValue();
		if(cols == null)
			val = buf;
		else
			val = String.format(buf,(Object)cols);
		return val;
	}
	public boolean isAgregation(Pattern pattern) 
	{	
		Field f = getField(pattern);
		ReservedKey annotation = f.getAnnotation(ReservedKey.class);
		return annotation.isAgregation();
	}
	
	public boolean isFilter(Pattern pattern)
	{
		Field f = getField(pattern);
		ReservedKey annotation = f.getAnnotation(ReservedKey.class);
		return annotation.isFilter();
	}
	
	public boolean isTemporal(Pattern pattern) 
	{
		Field f = getField(pattern);
		ReservedKey annotation = f.getAnnotation(ReservedKey.class);
		return annotation.isTemporal();
	}
	public String getPostColumn(String p)
	{
		Pattern pattern = getPatternOf(p);
		return getPostColumnValue(pattern);
	}
	public String getPreColumn(String p)
	{
		Pattern pattern = getPatternOf(p);
		return getPreColumnValue(pattern);
		
	}
	public String getFilter(String p) 
	{
		Pattern pattern = getPatternOf(p);
		return getFilterValue(pattern);
	}
	public static ReservedPatterns getInstance() {
		if(instance == null)
			try 
			{
				instance = new ReservedPatterns();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		return instance ;
	}
	
}
