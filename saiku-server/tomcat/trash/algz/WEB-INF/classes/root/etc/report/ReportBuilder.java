package root.etc.report;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.springframework.orm.hibernate3.HibernateTemplate;

import root.etc.ClassDAO;
import root.etc.report.classifier.ClassifierType;
import root.etc.report.classifier.LinearProduct;
import root.etc.report.classifier.ParamClassifier;
import root.etc.report.classifier.ReservedPatterns;
import algz.model.Attribute;
import algz.model.Class;
import algz.model.Customer;
import algz.model.Session;

public class ReportBuilder {

	private Pool pooling;
	private ParamClassifier paramClassifier;
	
	private StringBuilder selectClausule;
	private StringBuilder joinClausule;
	private StringBuilder whereClasule;
	private StringBuilder filterClasule;
	private StringBuilder groupByClasule;
	private StringBuilder orderByClasule;
	
	private StringBuilder finalQuery;
	
	private HibernateTemplate template;
	private Session session;
	
	private Iterator<String> clazzCursor;
	
	public ParamClassifier getParamClassifier() 
	{
		return paramClassifier;
	}

	private Class factClass;
	private Customer customer;
	
	public void initBuilders()
	{
		pooling = new Pool();
		finalQuery 			= new StringBuilder();
		selectClausule 		= new StringBuilder();
		joinClausule 		= new StringBuilder();
		whereClasule 		= new StringBuilder(" where ");
		filterClasule		= new StringBuilder();
		groupByClasule 		= new StringBuilder();
		orderByClasule		= new StringBuilder();
		System.gc();
	}
	public ArrayList<String> build(String[] splitedText,HibernateTemplate template,Session session) 
	{	
		this.customer = session.getTty().getConnectedApplication().getCustomer();
		this.template = template;
		this.session = session;
		ArrayList<String> queryList = new ArrayList<String>();
		
		paramClassifier = new ParamClassifier(template, session);
		paramClassifier.classifierAll(splitedText);
		clazzCursor = paramClassifier.getClassifiedClass().iterator();
		
		
		if(paramClassifier.isAllIgnored())
			queryList = null;
		else do
		{
			initBuilders();
			putFactClass();
			putAttributes();
			putAgregations();
			putFilter();
			putReservedColumns();
			putReservedFilters();
			ajustQuery();
			
			finalQuery.append((selectClausule.toString() 
					+ joinClausule.toString() 
					+ whereClasule.toString() + '\n' + filterClasule.toString()
					+ groupByClasule.toString() 
					+ orderByClasule.toString()
					));
			paramClassifier.querys.append(finalQuery.toString().replaceAll("\\n", "\n"));
			queryList.add(finalQuery.toString().replaceAll("//.+\\n", "\n").trim());
			
		}
		while(clazzCursor.hasNext());
			
		return queryList;
	}
	private void putReservedColumns() 
	{	
		for(String temp : paramClassifier.getClassifiedTemporals())
		{
			String tempCol = ReservedPatterns.getInstance().getPreColumn(temp);
			selectClausule.insert(0, tempCol);
			groupByClasule.insert(0, tempCol);
			orderByClasule.insert(0, tempCol);
		}	
		
	}
	private void putReservedFilters() {
		
		if(paramClassifier.getClassifiedTemporals().size() > 0)
		{
			joinClausule.append(" join o.valores x \n");
			whereClasule.append(" and x.dateTimeData is not null\n");
		}

		for(String filter : paramClassifier.getClassifiedFilters())
		{
			String v = ReservedPatterns.getInstance().getFilter(filter);
			whereClasule.append(v);
		}
			
		
	}
	private void putAgregations() 
	{
		for(String agg:paramClassifier.getClassifiedAgregations())
		{
			String v = ReservedPatterns.getInstance().getPostColumn(agg);
			selectClausule.append(v+',');
		}
	}
	private void putAttributes() 
	{
		for(LinearProduct product : paramClassifier.getLinearProducts())
		{
			if(product.getClassifierType().equals(ClassifierType.ATTRIBUTE))
			{
				String keyword = product.getStringValue();
				ResultFind resultFind = ReportFinder.findAttribute(factClass, keyword,null,null);
				product.setAttribute(resultFind.getAttribute());
				
				Attribute a = resultFind.getAttribute();
				if(a != null)
				{
					putJoin(a,resultFind);
					putSelectAndGroupBy(a);		
				}
			}
		}
	}
	private class F
	{
		Class g3()
		{
			Class factClass = ReportFinder.findFactClass(
					paramClassifier.getClassifiedAtrs(),
					paramClassifier.getClassifiedClass(),
					paramClassifier.getClassifiedVals(),
					template,customer,session.getTty().getConnectedApplication());
			if(clazzCursor.hasNext())
				clazzCursor.next();
			System.gc();
			return factClass; 
		}
		Class g2()
		{
			Class factClass = ReportFinder.findFactClass(
					paramClassifier.getClassifiedAtrs(),
					paramClassifier.getClassifiedClass(),
					template,customer,session.getTty().getConnectedApplication());
			
			if(clazzCursor.hasNext())
				clazzCursor.next();
			System.gc();
			return factClass;
		}
		Class g1()
		{
			Class factClass = ReportFinder.
					findFactClass(paramClassifier.getClassifiedAtrs(),
									template,customer,session.getTty().getConnectedApplication());
			System.gc();
			return factClass;
		}
	}
	private F f = new F();
	private ArrayList<String> stringColuns = new ArrayList<String>();
	private void putFactClass() 
	{
		Class factClass = null;
		
		float Cc = paramClassifier.getClassifiedClass().size() * .5f;
		float Ca = paramClassifier.getClassifiedAtrs().size() * .1f;
		float Cv = paramClassifier.getClassifiedVals().size() * .05f;

		
		if(Cv > 0 && Cv > Ca)
		{
			factClass = f.g3();
			if(factClass == null)
			{
				factClass = f.g2();
				if(factClass == null)
					factClass = f.g1();
			}
		}
		else if(Cc > 0 && Ca > 0)
		{
			factClass = f.g2();
			if(factClass == null)
			{
				factClass = f.g1();
			}
		}
		else if(Cc == 0 && Ca > 0)
		{
			factClass =  f.g1();
		}
		else
			factClass = ClassDAO.getInstance().findByName(clazzCursor.next(),template,session, null); 
		
		if(factClass == null)
			throw new Error("Fact Class not found!");

		this.factClass = factClass;

		whereClasule.append(" o.clazz.id = "+factClass.getId()+"//"+factClass.getName()+"\n");
		
	}
	private void putFilter() 
	{
		if(paramClassifier.getClassifiedVals().size() > 0)
		{
			filterClasule.append("and (\n");
			Iterator<String> cursorClassifiedVals = paramClassifier.getClassifiedVals().iterator();
		
			boolean isFirst = true;
			while(cursorClassifiedVals.hasNext())
			{
				if(isFirst)
				{
					filterClasule.append("\t(\n");
					isFirst = false;
				}
				else
					filterClasule.append("\tor (\n");
				
				String value = cursorClassifiedVals.next();
				Iterator<String> cursorCol = stringColuns.iterator();
				while(cursorCol.hasNext())
				{
					String col = cursorCol.next();
					filterClasule.append(String.format(" regexp(%s.stringData,'%s') =1 ",col,value));
					if(cursorCol.hasNext())
						filterClasule.append(" or \n");
				}
				filterClasule.append("\t)\n");
			}
			filterClasule.append(")\n");	
		}
		

	}
	private void putJoin(Attribute a,ResultFind resultFind) 
	{
		if(resultFind != null && resultFind.getParents() != null)
		{
			for(Attribute resultAttribute : resultFind.getParents())
			{
				putJoin(resultAttribute, null);
			}	
		}

		if(!pooling.containsVal(a))
		{
			if(resultFind != null && pooling.containsRef(resultFind.getLastParent()))
			{		
				String val = pooling.getVal(a); 
				joinClausule.append(
						" left join "+
							(pooling.getRef(resultFind.getLastParent())+"//"+resultFind.getLastParent().getName()+"\n\t")
						+".valores  "+val+" //"+a.getName()+" ~if\n"
						);
				
				Attribute labelAttribute = a.getTyper().getAttr("*label");
				if(labelAttribute != null)
				{
					joinClausule.append("\t left join "+val+".refferences "+pooling.getRef(a)+"\n"
					+ " left join "+ pooling.getRef(a)+".valores "
							+pooling.getVal(labelAttribute)+" //"+labelAttribute.getName()+"\n");
				}
				
			}
			else
			{
				joinClausule.append(
						" left join "+
								"o.valores "+pooling.getVal(a)+" \n"
								+ "\t left join "+pooling.getVal(a)+".refferences "+pooling.getRef(a)+"//"+a.getName()+" //~elseUp\n"
						);

				Attribute labelAttribute = a.getTyper().getAttr("*label");
				if(labelAttribute != null)
				{
					joinClausule.append("\t left join "+pooling.getRef(a)
							+".valores "+pooling.getVal(labelAttribute)+" //"+labelAttribute.getName()+" ~elseDown\n");
				}
			}
			putWhere(a);			
		}
		
	}
	private void putWhere(Attribute a) 
	{
		String val = pooling.getVal(a);
		whereClasule.append(
				 " and "+val+".attribute.id = "+a.getId() 
				 + "// "+a.getName()+"// normalWhere \n");
		if(!a.getTyper().isPrimitive())
		{
			Attribute labelAttribute = a.getTyper().getAttr("*label");
			val = pooling.getVal(labelAttribute);
			whereClasule.append(" and "+val+".attribute.id = "+labelAttribute.getId() 
					+"//"+labelAttribute.getName()+"// labelWhere \n");	
		}
		
	}
	private void putSelectAndGroupBy(Attribute a) 
	{		
		Attribute labelAttribute  = a.getTyper().getAttr("*label");
		if(labelAttribute != null)
			a = labelAttribute;
		
		String val =  pooling.getVal(a);

 		if(pooling.isChecked(a))
		{
 			paramClassifier.removeFromLinear(a);
			return;
		}
		else
			pooling.check(a);
		
		if(PATTERN_NUMBER.matcher(a.getTyper().getName().toLowerCase()).find())
		{
			selectClausule.append("sum("+val+".numberData),");
		}
		else
		{
			if(!a.getTyper().isPrimitive())
			{
				selectClausule.append(val+".stringData," 
						+"//"+a.getName()+" @Label \n");	
			}
			else
				selectClausule.append(val+".stringData,");
			stringColuns .add(val);
			groupByClasule.append((val)+",");
			orderByClasule.append(val+".stringData,");
		}
		
	}
	private void ajustQuery() 
	{
		selectClausule.insert(0, "select ");
		
		if(groupByClasule.length() > 0)
		{
			groupByClasule.insert(0, "group by ");
			orderByClasule.insert(0,"order by ");
			groupByClasule.setCharAt(groupByClasule.length()-1, '\n');
			orderByClasule.setCharAt(orderByClasule.length()-1, '\n');
		}
		
		selectClausule.setCharAt(selectClausule.length()-1, '\n');
		selectClausule.append(" from Object o \n");
		whereClasule.append(" and o.fileStatus.id in (2,3)");
		
	}
	
	private static final Pattern PATTERN_NUMBER = Pattern.compile("^(decimal|integer|currency)");

	
	public static ReportBuilder getInstance() 
	{
		return new ReportBuilder();
	}
	
}
