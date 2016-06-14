package root.bin;

import java.util.TreeSet;

import root.etc.Binary;
import root.etc.ClassDAO;
import root.etc.FolderMaker;
import algz.factory.JSONFactory;
import algz.model.Attribute;
import algz.model.Class;
import algz.model.Module;
import algz.model.TTY;

import com.fasterxml.jackson.core.type.TypeReference;

import core.base.BaseEntity;

public class mkclass extends Binary implements FolderMaker {

	@Override
	protected void init() 
	{
		String jsonString = getArgs()[1];
		Class clazz =  (Class) JSONFactory.getInstance().fromJSON(jsonString, 
				new TypeReference<Class>() {});
		
		Module module;
		
		if( (module = getTTY().getConnectedModule()) != null )
			if(clazz.getId() == 0 || getTTY().getConnectedClass().getId() == clazz.getId())
			{
				getTTY().setConnectedClass(clazz);
				
				if(clazz.getId() == 0)
				{
					module.getClasses().add(clazz);
				}
				getHibernateTemplate().saveOrUpdate(TTY.class.getName(),getTTY());
				
				getPipeline().setOutput(clazz);
			}
	}
	@SuppressWarnings({ "unchecked", "unused" })
	@Deprecated
	private TreeSet<Attribute> buildAttributes(Class clazz,String attributeSolid)
	{
		mkattr mkAttr = new mkattr();
		mkAttr.enableRunOnInit(false);
		mkAttr.run(null, getCurrentSession(), getRequest(), getResponse());
		mkAttr.buildAttributes(clazz, attributeSolid);
		return ((TreeSet<Attribute>) mkAttr.getPipeline().getOutput());
	}
	@Override
	public boolean needLogin() {
		return true;
	}
	@Override
	public boolean existsThisFolder(String folderName, BaseEntity module)
	{
		return ClassDAO.getInstance().findByName(folderName, getHibernateTemplate(), getCurrentSession(), null) != null;
	}
	public Class getExistent(String folderName, BaseEntity module)
	{
		return ClassDAO.getInstance().findByName(folderName, 
				getHibernateTemplate(), 
				getCurrentSession(), 
				(Module) module);
	}

}
