package root.bin;

import java.util.regex.Pattern;

import root.dao.ApplicationDAO;
import root.dao.ObjectDAO;
import root.etc.ClassDAO;
import root.etc.ConnectebleBinary;
import root.etc.Pipeline;
import root.etc.RegexPool;
import algz.model.Application;
import algz.model.Class;
import algz.model.Module;
import algz.model.Object;
import algz.model.TTY;

/**
 * 
 * @warnning , que fique documentado que qualquer problema de recursividade construtiva pode estar relacionado � ConnetableBinary().enableRunOnInit();
 */
public class cd extends ConnectebleBinary
{
	private String identifier = "T";
	@Override
	protected void init()
	{
		if (isFullPathChange())
		{
			setPath(getFirstArgumentWithQuotesOrNot());
			connectInEachPath();
		}
		else
		{
			super.init();
			setDir();
		}
	}

	private void connectInEachPath()
	{
		String[] paths = getPath().split(RegexPool.PATH_SEP_SPLIT.toString());
		java.lang.Object lastOutput = null;
		for (String p : paths)
		{
			if (p.equals(""))
			{
				p = "/";
			}
			cd cd = new cd();
			cd.setIdentifier("Loop to > "+p);
			cd.setPipeline(new Pipeline());
			cd.setPath(p);
			cd.enableRunOnInit(false);
			cd.run(new String[] { p }, getSession(), getRequest(),
					getResponse());
			cd.init();
			if(cd.getPipeline().getOutput() instanceof TTY)
			lastOutput = cd.getPipeline().getOutput();
		}
			getPipeline().setOutput(lastOutput); 
	}

	private boolean isFullPathChange()
	{
		return getArgs().length > 1
				&& Pattern.compile(RegexPool.GT_1PATH.toString())
						.matcher(getArgs()[1]).find();
	}

	@Override
	public void connectInModule()
	{
		Application application = getTTY().getConnectedApplication();

		Module module = getModule(application, getPath());
		if (module != null)
			getTTY().setConnectedModule(module);
		else
			pathNotExist();
	}

	@Override
	public void backToApplication()
	{
		getTTY().setConnectedModule(null);
	}

	@Override
	public void connectInApplication()
	{
		Application application = null;
		if (!getPath().equals(ROOT_DIR))
		{
			try
			{
				application = ApplicationDAO.getInstance().getApplication(
						getHibernateTemplate(), getPath(), getCurrentSession());

			}
			catch (IndexOutOfBoundsException e)
			{
				pathNotExist();
			}
		}
		else
		{
			fullBackToRoot();
		}
		if (application != null)
		{
			getTTY().setConnectedApplication(application);
		}

	}

	@Override
	public void backToRoot()
	{
		getTTY().setConnectedApplication(null);

	}

	@Override
	public void fullBackToRoot()
	{
		getTTY().setConnectedApplication(null);
		getTTY().setConnectedClass(null);
		getTTY().setConnectedModule(null);
		getTTY().setConnectedObject(null);
	}

	@Override
	public void connectInClass()
	{
		try
		{
			Class clazz = (Class) ClassDAO.getInstance()
					.getAll(getHibernateTemplate(), getPath(), getTTY()).get(0);
			getTTY().setConnectedClass(clazz);
		}
		catch (Exception e)
		{
			pathNotExist();
		}
	}

	@Override
	public void backToModule()
	{
		getTTY().setConnectedClass(null);
	}

	@Override
	protected void onSetDir()
	{
		getHibernateTemplate().update(TTY.class.getName(), getTTY());
	}

	private Module getModule(Application application, String path)
	{

		for (Module module : application.getModules())
		{
			if (module.getName().equals(path))
			{
				return module;
			}
		}
		return null;
	}

	@Override
	public boolean needLogin()
	{
		return true;
	}

	@Override
	public void connectInObjecT()
	{
		Object o = ObjectDAO.getInstance().getUnique(getHibernateTemplate(),getTTY().getConnectedClass(), getPath());
		getTTY().setConnectedObject(o);
	}

	@Override
	public void backToClass()
	{
		getTTY().setConnectedObject(null);
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
}
