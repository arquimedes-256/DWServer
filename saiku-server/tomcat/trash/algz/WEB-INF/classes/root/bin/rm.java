package root.bin;

import root.dao.ObjectDAO;
import root.etc.Binary;
import root.etc.ClassDAO;
import root.etc.MsgCode;

public class rm extends Binary {

	@Override
	protected void init() {
		String fileList = getFirstArgumentWithQuotesOrNot();
		if (fileList != null) {

			if(getTTY().getConnectedClass() != null) 
			{

				ObjectDAO.getInstance().sendObjectToTrash(
						getHibernateTemplate(), fileList,
						getTTY().getConnectedClass());
			} 
			else if(getTTY().getConnectedModule() != null) 
			{
				
				ClassDAO.getInstance().sendObjectToTrash(
						getHibernateTemplate(), fileList,
						getTTY().getConnectedModule());
			}

		} else
			getNotificator().emmitNotification(this, MsgCode.ARG_ER, null);
	}

	@Override
	public boolean needLogin() {
		return true;
	}
}
