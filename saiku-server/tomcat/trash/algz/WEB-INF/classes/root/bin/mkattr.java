package root.bin;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import root.etc.Binary;
import root.etc.ClassDAO;
import root.etc.FolderMaker;
import root.etc.MsgCode;
import algz.model.Attribute;
import algz.model.Class;
import algz.model.FileStatus;
import algz.model.Session;
import core.base.BaseEntity;

public class mkattr extends Binary implements FolderMaker
{
	
	public static final String SINGLE_DELIMITER = ":";
	private static final String LIST_DELIMITER = ",";
	private static final String FUNCTION_SET = "Function=>";
	
	@Override
	public boolean existsThisFolder(String folderName, BaseEntity clazz)
	{
		return getHibernateTemplate().find("select a from Class c "
				+ "join c.attributes a "
				+ "where a.name = ? "
				+ "and c.id = ? and a.fileStatus.id in(?,?)",
				new Object[]{folderName,clazz.getId(),
						FileStatus.LOCKED.getId(),FileStatus.UNLOCKED.getId()}).size() > 0;
	}
	@Override
	protected void init()
	{
		if(buildAttributes(getTTY().getConnectedClass(), getFirstArgumentWithQuotesOrNot()))
			saveClass();
	}
	public void saveClass()
	{
		getHibernateTemplate().saveOrUpdate(Class.class.getName(), getTTY().getConnectedClass());
	}
	public boolean removeAttributes(String attrName) 
	{
		boolean flag = false;
		
		//SortedSet<Attribute> attributes = new TreeSet<Attribute>();
		
		for(Attribute attr:getTTY().getConnectedClass().getAttributes())
		{
			if(!attrName.equals(attr.getName()))
			{
				//attributes.add(attr);
				flag = true;
			}
			else
			{
				attr.setFileStatus(FileStatus.IN_TRASH);
			}
		}
		//getTTY().getConnectedClass().setAttributes(attributes);
		return flag;
	}
	public boolean buildAttributes(algz.model.Class clazz,String attributeSolid)
	{
		if(clazz.getAttributes() == null)
			clazz.setAttributes(new TreeSet<Attribute>());
		String[] attrList = attributeSolid.split(LIST_DELIMITER);
		SortedSet<Attribute> attributes = new TreeSet<Attribute>();
		int i = 0;
		for(Attribute attribute:clazz.getAttributes())
		{
			attributes.add(attribute);
			attribute.setOrdering(i++);
		}
		for(String attr : attrList)
		{
			String[] a = attr.split(SINGLE_DELIMITER); 
			if(a.length < 2)
			{
				getNotificator().emmitNotification(this, MsgCode.ARG_ER, null);
				return false;
			}
			String attrName = a[0];
			String attrType = a[1];
			Attribute a2 = clazz.getAttr(attrName);
			if(a2 !=null && a2.getFileStatus() != FileStatus.IN_TRASH)
				continue;
			Attribute attribute = new Attribute();
			attribute.setName(attrName);
			attribute.setFileStatus(FileStatus.UNLOCKED);
			attribute.setOrdering(i++);
			if(attrType.startsWith(FUNCTION_SET))
			{
				attribute.setFunctionBody(attrType.replace(FUNCTION_SET, ""));
				attrType = "Function";
			}
			Class typer = getClassByName(attrType);
			attribute.setTyper(typer);
			
			attributes.add(attribute);
		}
		if(getTTY()!= null && getTTY().getConnectedClass() != null)
		{
			getTTY().getConnectedClass().setAttributes(attributes);
			getHibernateTemplate().saveOrUpdate(getTTY().getConnectedClass());
		}
		getPipeline().setOutput(attributes);
		return true;
	}
	private Class getClassByName(String name)
	{
		ClassDAO dao = ClassDAO.getInstance();
		Class clazz = dao.findByName(name, getHibernateTemplate(), getCurrentSession(), null);
		
		if(clazz != null)
			return clazz;
		else
		{
			getNotificator().emmitNotification(this, MsgCode.CLS_NE, null); 
			return null;
		}
	}
	@Override
	public void run(String[] args, Session session, HttpServletRequest request,
			HttpServletResponse response)
	{
		
		super.run(args, session, request, response);
	}

	@Override
	public boolean needLogin()
	{
		return true;
	}

	public boolean orderAttributtes(Class clazz, String attrSolid) 
	{
		if(getTTY()!= null && getTTY().getConnectedClass() != null)
		{
			String[] attrList = attrSolid.split(LIST_DELIMITER);
			SortedSet<Attribute> attributes = new TreeSet<Attribute>()
			{
				private static final long serialVersionUID = 58081116553909080L;

				@Override
				public boolean contains(Object o) {
					
					for (Iterator<Attribute> iterator = this.iterator(); iterator
							.hasNext();) 
					{
						Attribute attribute = (Attribute) iterator.next();
						if(attribute.equals(o))
						{
							return true;
						}
					}
					return false;
				}
			};

			int i = 0;
			for(String attrName : attrList)
			{
				Attribute attribute = getAttributeByName(attrName);
				if(attribute != null)
				{
					attribute.setOrdering(++i);
					attributes.add(attribute);
				}
			}
			for(Attribute attr: getTTY().getConnectedClass().getAttributes())
			{
				if(!attributes.contains(attr))
				{
					attr.setOrdering(++i);
					attributes.add(attr);
				}
			}
			getTTY().getConnectedClass().setAttributes(attributes);
			getHibernateTemplate().saveOrUpdate(getTTY().getConnectedClass());
			getPipeline().setOutput(attributes);
		}
		return false;
	}
	private Attribute getAttributeByName(String attrName) 
	{
		for(Attribute a : getTTY().getConnectedClass().getAttributes())
		{
			if(a.getName().equals(attrName))
			{
				return a;
			}
		}
		return null;
	}
}