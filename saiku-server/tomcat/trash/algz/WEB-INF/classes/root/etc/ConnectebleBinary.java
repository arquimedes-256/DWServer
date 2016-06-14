package root.etc;

/**
 * @warnning , que fique documentado que qualquer problema de recursividade construtiva pode estar relacionado � ConnetableBinary().enableRunOnInit();
 */
public abstract class ConnectebleBinary extends NavigableBinary{

	private String path;	

	@Override
	protected void init() 
	{
		if(path == null)
			path = getFirstArgumentWithQuotesOrNot();
		if(path == null)
			path = "";
	}
	public abstract void connectInModule();
	public abstract void connectInApplication();
	public abstract void connectInClass();
	public abstract void connectInObjecT();
	public abstract void backToRoot();
	public abstract void backToApplication();
	public abstract void backToModule();
	public abstract void backToClass();
	public abstract void fullBackToRoot();
	protected void setDir()
	{
		if(getPath().equals(ROOT_DIR))
		{
			fullBackToRoot();
		}
		else
		{
			//TODO Refazer isso
			if(isClass() && !getPath().equals(BACK_DIR))
			{
				connectInObjecT();
			}
			else if(isObject() && getPath().equals(BACK_DIR))
			{
				backToClass();
			}
			else if(isClass() && getPath().equals(BACK_DIR))
			{
				backToModule();
			}
			else if(isModule() && !getPath().equals(BACK_DIR))
			{
				connectInClass();
			}
			else if(isModule() && getPath().equals(BACK_DIR))
			{
				backToApplication();
			}
			else if(isApplication() && !getPath().equals(BACK_DIR))
			{
				connectInModule();
			}
			else if(isApplication() && getPath().equals(BACK_DIR))
			{
				backToRoot();
			}
			else if(isRoot() && !getPath().equals(BACK_DIR))
			{
				connectInApplication();
			}
			else if(isRoot() && getPath().equals(BACK_DIR))
			{ 
				pathNotExist();
			}
			else
				return;
		}
		onSetDir();
	}
	protected String getPath() {
		return path;
	}
	protected void setPath(String path)
	{
		this.path = path;
	}
	protected abstract void onSetDir();
}
