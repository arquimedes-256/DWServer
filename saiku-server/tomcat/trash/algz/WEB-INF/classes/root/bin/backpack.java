package root.bin;

import java.util.List;
import java.util.regex.Pattern;

import root.dao.BackpackDAO;
import root.dao.ObjectDAO;
import root.etc.Binary;
import root.etc.ClassDAO;
import algz.model.Attribute;
import algz.model.Class;
import algz.model.Value;
import algz.utils.StringUtils;

public class backpack extends Binary {

	private List<Class> classes = null;
	private Class clazz;
	
	@Override
	protected void init() 
	{
		String m = getArgs()[1];
		String a = getArgs()[2];
		classes  = BackpackDAO.getInstance().getBackpacks(getHibernateTemplate(),getCurrentSession().getUser());
		
		if(m.equals("--list")||m.equals("-l"))
		{
			getPipeline().setOutput(classes);	
		}
		else if(a != null && !a.isEmpty())
		{
			Attribute userAttribute = null;
			clazz = get(a);
			
			if(clazz != null) 
				userAttribute = getUserAttribute(clazz);

			if(clazz != null && userAttribute != null)
			{
				if(m.equals("--get") || m.equals("-g"))
				{	
					List<Object> objects = ClassDAO.getInstance().getFromUser(getCurrentSession().getUser(),clazz,userAttribute,getHibernateTemplate());
					getPipeline().setOutput(objects);
				}
				/**
				 * @exemple backpack --set Pedido [id_pedido] [name_attr_to_set] [value_to_set:[stringData|name_of_refference]]
				 */
				else if(m.equals("--set")||m.equals("-s"))
				{
					String objectId = getArgs()[3];
					String attrToSet = getArgs()[4];
					String valueToPut = getArgs()[5];
					
					algz.model.Object o = ObjectDAO.getInstance().findObject(getHibernateTemplate(), 
							getCurrentSession(), 
							clazz.getId(),
							Long.valueOf(objectId));
					
					Value v = o.getVal(attrToSet);
					
					if(v != null)
					{
						if(!StringUtils.isNotNumber(valueToPut) && v.getAttribute().getTyper().isSimplePrimitive())
						{
								/*algz.model.Object object = ObjectDAO.getInstance().findObject(getHibernateTemplate(), 
									getCurrentSession(), 
									clazz.getId(), 
									Long.valueOf(valueToPut));
									*/
								
						}
					}
				}
				/**
				 * @exemple backpack --push Pedido -item 33 -qty 1
				 */
				else if(m.equals("--push") || m.equals("-ph"))
				{
					String shellAttr_1 = getArgs()[3];
					String shellAttr_2 = getArgs()[5];
					
					String valAttr_1 = getArgs()[4];
					String valAttr_2 = getArgs()[6];
					
					if(shellAttr_1.equals("-item") && shellAttr_2.equals("-qty"))
					{
						algz.model.Object item = ObjectDAO.getInstance().findObject(getHibernateTemplate(), 
								getCurrentSession(), 
								clazz.getId(), 
								Long.valueOf(valAttr_1));
						setQTY(item,Double.valueOf(valAttr_2));
					}
				}
				else if(m.equals("--pop")||m.equals("-pp"))
				{
					
				}
			}
			else throw new Error("clazz or attribute not found!");
		}
		
	}
	
	private void setQTY(algz.model.Object item, Double d) {
		
		Attribute qtyAttribute = getQTYAttribute(clazz);
		item.getVal(qtyAttribute.getName()).setNumberData(d);
		getHibernateTemplate().update(item);
	}

	private Attribute getQTYAttribute(Class clazz) 
	{
		Attribute r = null;
		Attribute intA = null;
		
		for(Attribute a : clazz.getAttributes())
		{
			if(a.getTyper().getName().equals("Integer") || a.getTyper().getName().equals("Decimal"))
			{
				intA = a;
				if(isMeasure(a))
				{
					r = a;
				}
			}
		}
		if(r == null)
			r = intA;
		return r;
	}
	
	private static Pattern MEASURE_PATTERN = Pattern.compile("value|valor|vlr|qtd|count");
	
	private boolean isMeasure(Attribute a) 
	{
		return MEASURE_PATTERN.matcher(a.getName()).find();
	}

	private Attribute getUserAttribute(Class clazz) 
	{
		for(Attribute a : clazz.getAttributes())
		{
			if(a.getTyper().getName().equalsIgnoreCase("user"))
			{
				return a;
			}
		}
		return null;
	}

	private Class get(String a) 
	{
		long clazzId = 0;
		if(a.startsWith("@"))
		{
			clazzId = Long.valueOf(a.replace('@', '0'));
		}
		for(Class c: classes)
		{
			if((clazzId > 0 && c.getId() == clazzId) ||c.getName().equalsIgnoreCase(a))
			{
				return c;
			}
		}
		return null;
	}

	@Override
	public boolean needLogin() {
		return true;
	}

}
