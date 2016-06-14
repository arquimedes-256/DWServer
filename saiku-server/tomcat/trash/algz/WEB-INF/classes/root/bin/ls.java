package root.bin;

import java.util.ArrayList;
import java.util.List;

import root.dao.ApplicationDAO;
import root.dao.ObjectDAO;
import root.etc.AlgzFile;
import root.etc.ClassDAO;
import root.etc.ConnectebleBinary;
import root.etc.EventResponse;
import root.etc.MsgCode;
import algz.factory.JSONFactory;
import algz.model.Application;
import algz.model.FileStatus;
import algz.model.TTY;

import com.fasterxml.jackson.core.type.TypeReference;

public class ls extends ConnectebleBinary {

	private static class EventRequest
	{
		public int mounth,year,classID,objectID;
	};
	@Override
	protected void init() {
		
		super.init();
		if(testParam(1, "-l"))
		{
			if(isClass())
			{
				this.getPipeline().setOutput(getObjectList());	
			}
			else if(isRoot())
			{
				this.getPipeline().setOutput(getApplicationList());
			}
			else 
				this.getPipeline().setOutput(getTTY());
		}
		else if(testParam(1,"-c"))
		{
			this.getPipeline().setOutput(ClassDAO.getInstance()
					.getAvaliables(
							getHibernateTemplate(),
							getTTY().getConnectedApplication()
			));
		}
		else if(testParam(1,"-b"))
		{
			this.getPipeline().setOutput(getCurrentSession());
		}
		else if(testParam(1,"tty"))
		{
			this.getPipeline().setOutput(this.getTTY());
		}
		else if(testParam(1,"-s"))
		{
			SimpleTTY simpleTTY = new SimpleTTY();
			getSimpleListFiles(simpleTTY.root,getTTY());
			getSimpleListFiles(simpleTTY.apps,getTTY().getConnectedApplication());
			getSimpleListFiles(simpleTTY.mods,getTTY().getConnectedModule());
			getSimpleListFiles(simpleTTY.clas,getTTY().getConnectedClass());
			
			this.getPipeline().setOutput(simpleTTY);
		}
		else if(testParam(1, "events"))
		{
			if(testParam(2, "\\{.+\\}"))
			{
				EventRequest er = (EventRequest) JSONFactory.getInstance().fromJSON(getArgs()[2], new TypeReference<EventRequest>() {});
				this.getPipeline().setOutput(new EventResponse(getAllDateObjects(er)));
			}
			else
				getNotificator().emmitNotification(this, MsgCode.ARG_ER, null);
		}
		else
		{
			setDir();	
			
			if(getPath().equals(ROOT_DIR))
			{
				fullBackToRoot();
			}
		}
	}
	public static class SimpleTTY
	{
		public ArrayList<String> root = new ArrayList<String>();
		public ArrayList<String> apps = new ArrayList<String>();
		public ArrayList<String> mods = new ArrayList<String>();
		public ArrayList<String> clas = new ArrayList<String>();
	}
	private void getSimpleListFiles(List<String> sb, AlgzFile file) 
	{
		if(file instanceof TTY)
			((TTY)file).setApplicationsList(this.getApplicationList());
		if(file instanceof algz.model.Class)
			((algz.model.Class)file).setChildrens(ObjectDAO.getInstance()
					.findObjects(getHibernateTemplate(), getCurrentSession(), ((algz.model.Class) file).getId()));
		if(file != null && file.getChildrens()!=null)
		{
			for(AlgzFile child : file.getChildrens())
			{
				if(child.getFileStatus() != null 
						&& !(child.getFileStatus().equals(FileStatus.IN_TRASH)))
				{
					sb.add(child.getName());
				}
			}
		}
	}
	private List<?> getAllDateObjects(EventRequest er) {
		List<?> list = ObjectDAO.getInstance()
				.getEvents(
						getHibernateTemplate(), 
						getPath(), 
						getTTY().getConnectedClass()
				,er.mounth,er.year,er.classID,er.objectID);
		if(list == null)
			getNotificator().emmitNotification(this, MsgCode.ARG_ER, null);
		return list;
	}
	@SuppressWarnings("unchecked")
	private List<Application> getApplicationList()
	{
		return (List<Application>)ApplicationDAO.getInstance().getAll(getHibernateTemplate(), getCurrentSession().getUser().getCustomer());
	}
	private Object getObjectList()
	{
		List<?> list = ObjectDAO.getInstance()
					.getAll(
							getHibernateTemplate(), 
							getPath(), 
							getTTY().getConnectedClass()
					);
		return list;
	}
	@Override
	public void connectInModule() {
		this.getPipeline().setOutput(getTTY().getConnectedApplication());
	}
	@Override
	public void connectInApplication() 
	{
		this.getPipeline().setOutput(getTTY());
	}
	@Override
	public void connectInClass() {

		this.getPipeline().setOutput(getTTY().getConnectedModule());
	}
	@Override
	public void connectInObjecT() {

		this.getPipeline().setOutput(getTTY().getConnectedClass());
	}
	@Override
	public void backToRoot() {

		this.getPipeline().setOutput(getCurrentSession());
	}
	@Override
	public void backToApplication() {
		this.getPipeline().setOutput(getTTY().getConnectedApplication());
	}
	@Override
	public void backToModule() {
		this.getPipeline().setOutput(getTTY().getConnectedModule());
	}
	@Override
	public void backToClass() {
		this.getPipeline().setOutput(getTTY().getConnectedClass());
	}
	@Override
	public void fullBackToRoot() {
		this.getPipeline().setOutput(getTTY());
	}
	@Override
	public void onSetDir() {
		//TODO 
	}
}
