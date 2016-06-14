package root.bin;

import java.util.ArrayList;

import root.dao.ModuleDAO;
import root.etc.Binary;
import root.etc.FolderMaker;
import root.etc.MsgCode;
import algz.model.Application;
import algz.model.FileStatus;
import algz.model.Module;
import core.base.BaseEntity;

public class mkmod extends Binary implements FolderMaker{

	@Override
	protected void init() {
		
		String modName = getFirstArgumentWithQuotesOrNot();
		if(!isValidFolderName(modName))
			{
				getNotificator().emmitNotification(this, MsgCode.NME_UW, null);
				return;
			}
		if(modName != null)
		{
			Application app = getCurrentSession().getTty().getConnectedApplication();
					//existsThisFolder
			if(app != null)
			{	
				if(!existsThisFolder(modName, app))
				{
					Module module = new Module();
					module.setName(modName);
					module.setFileStatus(FileStatus.UNLOCKED);
					
					Application application = getCurrentSession().getTty().getConnectedApplication();
					
					if(application.getModules() == null || application.getModules().size() == 0)
					{
						application.setModules(new ArrayList<Module>());
					}
					application.getModules().add(module);
					
					getHibernateTemplate().saveOrUpdate(Module.class.getName(),module);
					getHibernateTemplate().saveOrUpdate(Application.class.getName(),application);
					
					this.getPipeline().setOutput(application);
				}
				else
				{
					getNotificator().emmitNotification(this, MsgCode.NME_IN, null);
				}
			}
			else
			{
				getNotificator().emmitNotification(this, MsgCode.APP_NF, null);;
			}
		}
		
	}
	@Override
	public boolean needLogin() {
		return true;
	}

	@Override
	public boolean existsThisFolder(String folderName, BaseEntity application)
	{
		return ModuleDAO.getInstance().getAll(getHibernateTemplate(),folderName,application).size() > 0;
	}

}
