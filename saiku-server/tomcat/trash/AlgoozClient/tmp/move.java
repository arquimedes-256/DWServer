package root.bin;

import root.etc.ConnectebleBinary;
import root.etc.MsgCode;
import algz.model.FileStatus;

public class move extends ConnectebleBinary
{
	private FileStatus currentFileStatus;
	
	@Override
	protected void init()
	{	
		if(testParam(2, "/@unlock"))
		{
			currentFileStatus = FileStatus.UNLOCKED;
		}
		else if(testParam(2, "/@lock"))
		{
			currentFileStatus = FileStatus.LOCKED;
		}
		else if(testParam(2,"/@trash"))
		{
			currentFileStatus = FileStatus.IN_TRASH;
		}
		super.init();
		setDir();
	}
	@Override
	public void connectInModule()
	{
		getTTY().getConnectedClass().setFileStatus(currentFileStatus);
	}

	@Override
	public void connectInApplication()
	{
		getNotificator().emmitNotification(this, MsgCode.MOV_ERR, null);
	}

	@Override
	public void connectInClass()
	{
		getTTY().getConnectedModule().setFileStatus(currentFileStatus);
	}

	@Override
	public void connectInObjecT()
	{
		getTTY().getConnectedClass().setFileStatus(currentFileStatus);
		
	}

	@Override
	public void backToRoot()
	{
		getNotificator().emmitNotification(this, MsgCode.MOV_ERR, null);
	}

	@Override
	public void backToApplication()
	{
		getNotificator().emmitNotification(this, MsgCode.MOV_ERR, null);
	}

	@Override
	public void backToModule()
	{
		getNotificator().emmitNotification(this, MsgCode.MOV_ERR, null);
	}

	@Override
	public void backToClass()
	{
		getNotificator().emmitNotification(this, MsgCode.MOV_ERR, null);
	}

	@Override
	public void fullBackToRoot()
	{
		getNotificator().emmitNotification(this, MsgCode.MOV_ERR, null);
	}

	@Override
	protected void onSetDir()
	{
		getHibernateTemplate().save(getTTY());
	}

}
