package root.bin;

import root.dao.ApplicationDAO;
import root.etc.Binary;
import root.etc.FolderMaker;
import root.etc.MsgCode;
import algz.model.Application;
import algz.model.Customer;
import algz.model.FileStatus;
import algz.model.Session;
import algz.model.User;
import core.base.BaseEntity;

public class mkapp extends Binary implements FolderMaker{

	@Override
	protected void init() {
		
		Session s = super.getCurrentSession();
		String appName = getArgs()[1];
		
		if(!isValidFolderName(appName))
		{
			getNotificator().emmitNotification(this, MsgCode.NME_UW, null);
			return;
		}
		else
		{
			User user =	 s.getUser();
			Customer customer = user.getCustomer();
			
			if(!existsThisFolder(appName,customer))
			{
				Application application = new Application();
				application.setFileStatus(FileStatus.UNLOCKED);
				application.setName(appName);
				application.setCustomer(
						getCurrentSession()
							.getUser()
							.getCustomer());
				
				
				getHibernateTemplate().saveOrUpdate(Application.class.getName(), application);
				getHibernateTemplate().saveOrUpdate(Customer.class.getName(),customer);
				this.getPipeline().setOutput(s);
			}
			else
			{
				getNotificator().emmitNotification(this, MsgCode.NME_IN, null);	
				return;
			}	
		}
	}
	@Override
	public boolean existsThisFolder(String folderName,BaseEntity baseEntityAux)
	{
		return ApplicationDAO.getInstance()
					.getAll(getHibernateTemplate(), 
							folderName, baseEntityAux).size() > 0;
		 
	}

	@Override
	public boolean needLogin() {
		return true;
	}
}
