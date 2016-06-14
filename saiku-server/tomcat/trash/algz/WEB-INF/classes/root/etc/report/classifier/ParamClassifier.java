package root.etc.report.classifier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.orm.hibernate3.HibernateTemplate;

import root.dao.ObjectDAO;
import root.etc.ClassDAO;
import algz.model.Attribute;
import algz.model.Session;
import algz.model.Value;

public class ParamClassifier 
{
	private HibernateTemplate template;
	private Session session;
	
	public StringBuilder querys = new StringBuilder();
	
	private ArrayList<String> classifiedAtrs 		= new ArrayList<String>();
	private ArrayList<String> classifiedAgregations = new ArrayList<String>();
	private ArrayList<String> classfiedAttrLabel	= new ArrayList<String>();
	private ArrayList<String> classifiedVals 		= new ArrayList<String>();
	private ArrayList<String> classifiedClass 		= new ArrayList<String>();
	private ArrayList<String> classifiedPatterns 	= new ArrayList<String>();
	private ArrayList<String> classifiedIgnores 	= new ArrayList<String>();
	private ArrayList<String> classifiedFilters		= new ArrayList<String>();
	private ArrayList<String> classifiedTemporals	= new ArrayList<String>();
	
	private ArrayList<LinearProduct> linearProducts = new ArrayList<LinearProduct>();
	
	public ParamClassifier(HibernateTemplate template,Session session) 
	{
		this.template = template;
		this.session = session;
	}
	public boolean isAllIgnored()
	{
		for(Iterator<LinearProduct> productsCursor =  linearProducts.iterator();productsCursor.hasNext();)
			if(!productsCursor.next().getClassifierType().equals(ClassifierType.IGNORE))
				return false;
		return true;
	}
	public void classifierAll(String[] params)
	{
		for(String p : params)
		{
			if(!p.trim().equals(""))
			{
				long attrisCount = (long) 
						(ClassDAO.getInstance()
								.getAttributesCount(template,session,p));
				
				Pattern pattern = null;
				
				if(attrisCount == 0 && (pattern = ReservedPatterns.getInstance().getPatternOf(p)) != null)
				{

					
					if(ReservedPatterns.getInstance().isAgregation(pattern))
					{
						classifiedAgregations.add(p);
						linearProducts.add(new LinearProduct(ClassifierType.AGREGATION, p,pattern));
					}
					else if(ReservedPatterns.getInstance().isFilter(pattern))
					{
						classifiedFilters.add(p);
						if(ReservedPatterns.getInstance().isTemporal(pattern))
						{
							classifiedTemporals.add(p);
							linearProducts.add(0,new LinearProduct(ClassifierType.TEMPORAL_FILTER, p,""));
						}
						else
							linearProducts.add(new LinearProduct(ClassifierType.FILTER, p,""));
					}
					else
					{
						classifiedPatterns.add(p);
						linearProducts.add(new LinearProduct(ClassifierType.PATTERN, p,pattern));
					}
				}
				else
				{
					String s = p;
					p = new RegexBuilder(p).toString();
					long valuesCount = (long) (ObjectDAO.getInstance().getValuesCount(template,session,p));
					long labelsCount = (long) (ObjectDAO.getInstance().getLabelsCount(template,session,p));		
					
					if(valuesCount > (attrisCount + labelsCount))
					{
						classifiedVals.add(p);
						linearProducts.add(new LinearProduct(ClassifierType.VALUE, p,s));
						if(labelsCount > 0)
						{
							classfiedAttrLabel.add(p);
						}
					}
					else if(valuesCount < (attrisCount + labelsCount))
					{
						classifiedAtrs.add(p);
						linearProducts.add(new LinearProduct(
								ClassifierType.ATTRIBUTE, p,s));
					}
					else
					{
						if(ClassDAO.getInstance().exists(p,template,session))
						{
							classifiedClass.add(p);
							linearProducts.add(new LinearProduct(ClassifierType.CLASS, p,s));
						} else {
							classifiedIgnores.add(p);
							linearProducts.add(new LinearProduct(ClassifierType.IGNORE, p,s));
						}
					}
				}
			}
			if(classifiedVals.size() > 0 && classfiedAttrLabel.size() == 0)
				fixUndefineds();	
		}	
	}
	public ArrayList<String> getClassifiedTemporals() {
		return classifiedTemporals;
	}
	public ArrayList<String> getClassifiedFilters() {
		return classifiedFilters;
	}
	public ArrayList<String> getClassifiedAgregations() {
		return classifiedAgregations;
	}
	private void fixUndefineds() 
	{
		List<?> l = ObjectDAO.getInstance().findAttributeByValue(classifiedVals,template,session);
		
		for (Iterator<?> iterator = l.iterator(); iterator.hasNext();) 
		{
			Value val = (Value) iterator.next();
			String p = val.getAttribute().getName().toLowerCase();
			if(!classifiedAtrs.contains(p))
			{
				classifiedAtrs.add(0,p);
				classfiedAttrLabel.add(0,p);
				linearProducts.add(new LinearProduct(ClassifierType.ATTRIBUTE,null, p));
			}
		}
		
	}
	public ArrayList<String> getClassifiedAtrs() {
		return classifiedAtrs;
	}
	public ArrayList<String> getClassifiedVals() {
		return classifiedVals;
	}
	public ArrayList<String> getClassifiedClass() {
		return classifiedClass;
	}
	public ArrayList<String> getClassifiedIgnores() {
		return classifiedIgnores;
	}
	public ArrayList<LinearProduct> getLinearProducts() {
		return linearProducts;
	}
	public LinearProduct getLinearProductByKeyword(String keyword) 
	{
		for(Iterator<LinearProduct> cursorProduct = linearProducts.iterator();cursorProduct.hasNext();)
		{
			LinearProduct product = cursorProduct.next();
			if(keyword.equals(product.getStringValue()))
			{
				return product;
			}
		}
		return null;
	}
	public void removeFromLinear(Attribute a) 
	{
		for(Iterator<LinearProduct> iterator = linearProducts.iterator();iterator.hasNext();)
		{
			LinearProduct product = iterator.next();
			
			if(product.getAttribute() != null && product.getAttribute().equals(a))
			{
				product.setClassifierType(ClassifierType.LABEL);
				return;
			}
			
		}
	}
}
