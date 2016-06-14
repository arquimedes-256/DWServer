package root.etc;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.orm.hibernate3.HibernateTemplate;

import algz.factory.MessageFacoty;
import algz.factory.SessionFactory;
import algz.model.Locale;
import algz.model.Session;
import algz.model.TTY;
import core.base.ServiceBus;

public abstract class Binary 
{
	
	protected abstract void init();
	public abstract boolean needLogin();
	
	private String args[];
	private Session session;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Pipeline pipeline = new Pipeline();
	private TTY tty;
	private String fullShell;
	private boolean runOnInit = true;
	

	protected static final String BACK_DIR = "..";
	protected static final String ROOT_DIR = "/";
	
	public static final class Notificator
	{
		private Locale l = null;
		private Binary binary;
		private MsgCode msgCodes;
		
		private void init(Binary binary, MsgCode msgCodes)
		{
			this.binary = binary;
			this.msgCodes = msgCodes;
			if(binary.getCurrentSession() == null)
				l = Locale.EN;
			else
				l = binary.getCurrentSession().getLocale();
		}
		private void finalize(Notificable notificable)
		{
			binary.getPipeline()
			.setOutput(MessageFacoty.getInstance()
					.buildNotication(msgCodes, l, notificable));
		}
		public void emmitNotification(Binary binary, MsgCode msgCodes, Notificable notificable)
		{
			init(binary, msgCodes);
			finalize(notificable);
		}
	}
	private Notificator notificator = new Notificator();
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	public void enableRunOnInit(boolean flag)
	{
		runOnInit = flag;
	}
	public void run(
				String[] args,
				Session session,
				HttpServletRequest request, 
				HttpServletResponse response)
	{
		this.args = args;
		this.session = session;
		this.response = response;
		this.request = request;
		
		if(getCurrentSession() != null)
		{
			tty = getCurrentSession().getTty();
		}
		if(runOnInit)
			init();
	}
	
	public String[] getArgs() {
		return args;
	}
	public void setArgs(String[] args) {
		this.args = args;
	}
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}

	public Pipeline getPipeline() {
		if(this.pipeline == null)
		{
			this.setPipeline(new Pipeline());
		}
		return this.pipeline;
	}
	public void setPipeline(Pipeline pipeline) {
		this.pipeline = pipeline;
	}
	public boolean testParam(int index,String paramName)
	{
		return getArgs().length > index  && Pattern.compile(paramName).matcher(getArgs()[index]).find();
	}
	public boolean existParam(String paramName)
	{
		return this.getRequest().getParameter("shell").indexOf(paramName) != -1;
	}
	public HibernateTemplate getHibernateTemplate()
	{
		return ServiceBus.getInstace().getHibernateTemplate();
	}

	public Session getCurrentSession() 
	{
		Session s = null;
		if(getRequest() != null)
		{
			String ip = getRequest().getRemoteAddr();
			s = SessionFactory.getInstance().getSessionByIp(ip);
			return s;
		}
		return null;
	}
	protected String getFirstArgumentWithQuotesOrNot()
	{
		String sl = getRequest().getParameter("shell");
		if(getFullShell() != null)
			sl = getFullShell();
		ArrayList<String> out = new ArrayList<String>();
		Matcher matcher = Pattern.compile(RegexPool.QUOTES_OR_NOT.toString()).matcher(sl);
		
		while(matcher.find())
		{
			out.add(matcher.group(1));
		}
		return out.size() > 0 ? out.get(0).trim() : null;
	}
	protected String getObjectStringInArgument()
	{
		Matcher m = Pattern.compile(RegexPool.BRACES_DATA.toString())
				.matcher(getFullShell());
		m.find();
		return m.group(1);
	}

	protected boolean isValidFolderName(String folderName)
	{
		folderName = folderName.trim();
		return !(folderName.contains(ROOT_DIR) || folderName.equals(BACK_DIR) || folderName.equals("") || folderName.contains(RegexPool.RESERVED_CHARS.name()));
	}
	public String getIp() {
		
		return getRequest().getRemoteAddr();
	}
	protected TTY getTTY(){return tty;}

	public void setFullShell(String fullShell)
	{
		this.fullShell = fullShell;
	}
	public String getFullShell()
	{
		return fullShell;
	}
	public Notificator getNotificator() {
		return notificator;
	}
	public void setNotificator(Notificator notificator) {
		this.notificator = notificator;
	}
}
