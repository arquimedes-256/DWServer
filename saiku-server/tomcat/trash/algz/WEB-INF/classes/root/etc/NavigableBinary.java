package root.etc;


public abstract class NavigableBinary extends Binary {

	
	@Override
	public boolean needLogin() {
 		return true;
	}
	protected void pathNotExist() 
	{	
		getNotificator().emmitNotification(this, MsgCode.CD_NE, null);
	}
	protected boolean isRoot()
	{
		return (getTTY().getConnectedApplication() 	== null && 
				getTTY().getConnectedClass() 		== null &&
				getTTY().getConnectedModule() 		== null &&
				getTTY().getConnectedObject() 		== null);
	}
	protected boolean isApplication()
	{
		return (getTTY().getConnectedApplication() != null &&
				getTTY().getConnectedModule() == null &&
				getTTY().getConnectedClass() == null &&
				getTTY().getConnectedObject() == null);
	}
	protected boolean isModule()
	{
		return (getTTY().getConnectedApplication() != null &&
				getTTY().getConnectedModule() != null &&
				getTTY().getConnectedClass() == null &&
				getTTY().getConnectedObject() == null);
	}
	protected boolean isClass()
	{
		return (getTTY().getConnectedApplication() != null &&
				getTTY().getConnectedModule() != null &&
				getTTY().getConnectedClass() != null &&
				getTTY().getConnectedObject() == null);
	}
	protected boolean isObject()
	{
		return (getTTY().getConnectedApplication() != null &&
				getTTY().getConnectedModule() != null &&
				getTTY().getConnectedClass() != null &&
				getTTY().getConnectedObject() != null);
	}

	
}
