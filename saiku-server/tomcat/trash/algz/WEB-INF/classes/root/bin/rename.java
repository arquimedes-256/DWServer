package root.bin;

import algz.model.TTY;
import algz.utils.StringUtils;
import root.etc.MsgCode;
import root.etc.NavigableBinary;

public class rename extends NavigableBinary {

	private String valueToRename;
	
	@Override
	protected void init() {
		
		valueToRename = getFirstArgumentWithQuotesOrNot();
		
		if(isRoot())
		{
			objectCannotBeRenamed();
		}
		else if(isApplication())
		{
			renameApplication();
		}
		else if(isModule())
		{
			renameModule();
		}
		else if(isClass())
		{
			if(this.testParam(1, "-a"))
				if(this.testParam(3, "\\w+"))
					renameAttribute();
				else
					getNotificator().emmitNotification(this, MsgCode.ARG_ER, null);
			else
				renameClass();
		}
		else if(isObject())
		{
			objectCannotBeRenamed();
		}
		getHibernateTemplate().saveOrUpdate(TTY.class.getName(),getTTY());
	}

	private void renameAttribute() 
	{	
		String from  	= StringUtils.fix(getArgs()[2]);
		String to 		= StringUtils.fix(getArgs()[3]);
		
		getTTY().getConnectedClass().getAttr(from).setName(to);	
	}

	private void renameClass() {
		getTTY().getConnectedClass().setName(valueToRename);
	}
	private void renameModule() {
		getTTY().getConnectedModule().setName(valueToRename);
	}
	private void renameApplication() {
		getTTY().getConnectedApplication().setName(valueToRename);
	}

	private void objectCannotBeRenamed() {
		getNotificator().emmitNotification(this, MsgCode.RN_OBJ, null);
	}

	@Override
	public boolean needLogin() {
		return true;
	}

}
