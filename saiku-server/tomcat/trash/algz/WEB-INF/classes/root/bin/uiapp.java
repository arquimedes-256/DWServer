package root.bin;

import root.etc.Binary;

public class uiapp extends Binary
{
/*
	@SuppressWarnings("unchecked")
	@Override
	protected void init() 
	{
		String method = getArgs()[1];
		
		if(method.equals("--new"))
		{
			if(testParam(2, "\\w+"))
			{
				String appName = getArgs()[2];
				if(!UIAppDAO.getInstance().exists(appName, getHibernateTemplate(), getCurrentSession().getUser().getCustomer()))
				{
					UIApp uiApp = new UIApp();
					uiApp.setName(appName);
					uiApp.setCustomer(getCurrentSession().getUser().getCustomer());
					getHibernateTemplate().save(uiApp);	
				}
			}
			else
				getNotificator().emmitNotification(this, MsgCode.ARG_ER, null);
		}
		else if(method.equals("--use"))
		{
			if(testParam(2, "\\w+"))
			{
				String appName = getArgs()[2];
				if(UIAppDAO.getInstance().exists(appName, getHibernateTemplate(), getCurrentSession().getUser().getCustomer()))
				{
					UIApp uiApp = UIAppDAO.getInstance().get(appName, getHibernateTemplate(), getCurrentSession());
					getTTY().setConnectedUIApp(uiApp);
					getHibernateTemplate().saveOrUpdate(getTTY());
				}
			}
			else
				getNotificator().emmitNotification(this, MsgCode.ARG_ER, null);
		}
		else if(method.equals("--save"))
		{
			if(getTTY().getConnectedUIApp() != null && testParam(2, "\\{.+\\}"))
			{
				UIApp currentUIApp = getTTY().getConnectedUIApp();
				String json = getArgs()[2];
				UIApp jsonUIApp = (UIApp)JSONFactory.getInstance().fromJSON(json, new TypeReference<UIApp>() {});
				
				jsonUIApp.setId(currentUIApp.getId());
				jsonUIApp.setCustomer(getCurrentSession().getUser().getCustomer());
				
				if(jsonUIApp.getConfig() == null)
					jsonUIApp.setConfig(new UIConfig());
				
				getTTY().setConnectedUIApp(jsonUIApp);
				
				
				if(jsonUIApp.getName() == null)
				{
					jsonUIApp.setName(currentUIApp.getName());
				}
				else if(jsonUIApp.getBoxes() == null)
				{
					jsonUIApp.setBoxes(currentUIApp.getBoxes());
				}
				
				getHibernateTemplate().saveOrUpdate(getTTY());
			}
		}
		else if(method.equals("--list-apps"))
		{
			getPipeline().setOutput(UIAppDAO.getInstance().getUIApps(getHibernateTemplate(),getCurrentSession()));
		}
		else if(method.equals("--list"))
		{
			if(getTTY().getConnectedUIApp() != null)
			{
				List<?> bindedBoxes = BoxesFactory.getInstance().buildBindedBoxes(getTTY().getConnectedUIApp().getBoxes(), getHibernateTemplate(), getCurrentSession()); 
				if(getTTY().getConnectedUIApp().getConfig() == null)
					getTTY().getConnectedUIApp().setConfig(new UIConfig());
				getTTY().getConnectedUIApp().setBindedBoxes((List<Box>) bindedBoxes);
				getPipeline().setOutput(getTTY().getConnectedUIApp());
			}
		}
		else if(method.equals("--list-components"))
		{
			List<Component> components = UIAppDAO.getInstance().getComponents(getHibernateTemplate());
			getPipeline().setOutput(components);
		}
	}
*/
	@Override
	public boolean needLogin() 
	{
		return true;
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}
}
