package core.base;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import orion.channel.app.OrionService;
import root.etc.Binary;
import root.etc.MsgCode;
import root.etc.ReasonCode;
import algz.model.Class;
import algz.model.Customer;
import algz.model.FileStatus;
import algz.model.Grouper;
import algz.model.Link;
import algz.model.Locale;
import algz.model.Message;
import algz.model.MessageType;
import algz.model.Module;
import algz.model.Program;
import algz.model.Reason;
import algz.model.User;
import algz.model.ui.Component;

public class BasicAPI 
{	
	public OrionService service;
	
	private static boolean isReady = false;
	
	public OrionService getService() {
		return service;
	}
	public void setService(OrionService service) {
		this.service = service;
		
		SingletonsPool.setService(service);


		if(classListIsEmpty())
		{
			createFileStatus();
			setReady(true);
			createBasicTypes();	
			createAdmin(createCustommer());
			createLinks();
			createComponents();
			createLocales();
			createMessagesTypes();
			createMessages();
			createReasons();
		}
		createBasicTypesAttributes();
	}
	private void createBasicTypesAttributes() 
	{
		
	}
	private void createComponents() 
	{
		service.save(Component.class.toString(), Component.LABEL);
		service.save(Component.class.toString(), Component.BUTTON);
		service.save(Component.class.toString(), Component.IMAGE);
	}
	private void createReasons() 
	{
		Reason r1 = new Reason("Start Date greater then End Date", ReasonCode.CALENDAR_COMPARE,Locale.EN);
		service.getHibernateTemplate().save(Reason.class.getName(), r1);
		Reason r2 =  new Reason("Data inicial maior que data final", ReasonCode.CALENDAR_COMPARE,Locale.PT);
		service.getHibernateTemplate().save(Reason.class.getName(),r2);
	}
	public static boolean isReady() {
		return isReady;
	}
	public static void setReady(boolean isReady) {
		BasicAPI.isReady = isReady;
	}
	private void createFileStatus()
	{
		service.save(FileStatus.class.getName(), FileStatus.IN_TRASH);
		service.save(FileStatus.class.getName(), FileStatus.LOCKED);
		service.save(FileStatus.class.getName(), FileStatus.UNLOCKED);
	}
	private void createLocales() 
	{
		service.save(Locale.class.getName(),Locale.EN);
		service.save(Locale.class.getName(),Locale.PT);
	}
	private void createMessagesTypes()
	{
		service.save(MessageType.class.getName(), MessageType.ERROR);
		service.save(MessageType.class.getName(), MessageType.WARN);
		service.save(MessageType.class.getName(), MessageType.INFO);
	}
	private void createMessages()
	{
		service.getHibernateTemplate().bulkUpdate("delete from Message");
		buildMessage("TTY not exists", "en",MsgCode.TTY_NE, MessageType.ERROR);
		buildMessage("TTY n��o existe", "pt",MsgCode.TTY_NE, MessageType.ERROR);
		
		buildMessage("Path not exists","en",MsgCode.CD_NE,MessageType.ERROR);
		buildMessage("Caminho nao existente","pt",MsgCode.CD_NE,MessageType.ERROR);
		
		buildMessage("You need stay in a Module to use this instruction","en",MsgCode.MDL_NF,MessageType.ERROR);
		buildMessage("Voc�� precisa estar em um M��dulo para usar esta instru����o","pt",MsgCode.MDL_NF,MessageType.ERROR);
		
		buildMessage("You need stay in a Application to use this instruction","en",MsgCode.APP_NF,MessageType.ERROR);
		buildMessage("Voc�� precisa estar em uma Aplica����o para usar esta instru����o","pt",MsgCode.APP_NF,MessageType.ERROR);
 
		buildMessage("You need stay in a Class to use this instruction","en",MsgCode.CLS_NF,MessageType.ERROR);
		buildMessage("Voc�� precisa estar em uma Classe para usar esta instru����o","pt",MsgCode.CLS_NF,MessageType.ERROR);
		
		buildMessage("Command not found", "en", MsgCode.LNK_NF, MessageType.ERROR);
		buildMessage("Comando n��o existente", "pt", MsgCode.LNK_NF, MessageType.ERROR);
		
		buildMessage("You cannot does rename this object", "en", MsgCode.RN_OBJ, MessageType.ERROR);
		buildMessage("Voc�� n��o pode renomear este objeto", "pt", MsgCode.RN_OBJ, MessageType.ERROR);
		
		buildMessage("Class not exists", "en", MsgCode.CLS_NE, MessageType.ERROR);
		buildMessage("Classe n��o existe", "pt", MsgCode.CLS_NE, MessageType.ERROR);
		
		buildMessage("Module not exists", "en", MsgCode.MDL_NE, MessageType.ERROR);
		buildMessage("M��dulo n��o existe", "pt", MsgCode.MDL_NE, MessageType.ERROR);
		
		buildMessage("Application not exists", "en", MsgCode.APP_NE, MessageType.ERROR);
		buildMessage("Aplica����o n��o existe", "pt", MsgCode.APP_NE, MessageType.ERROR);
		
		buildMessage("Object not exists", "en", MsgCode.OBJ_NE, MessageType.ERROR);
		buildMessage("Objeto n��o existe", "pt", MsgCode.OBJ_NE, MessageType.ERROR);
		
		buildMessage("Attribute not exists", "en", MsgCode.ATR_NE, MessageType.ERROR);
		buildMessage("Attributo n��o existe", "pt", MsgCode.ATR_NE, MessageType.ERROR);
	
		buildMessage("Name in use!", "en", MsgCode.NME_IN, MessageType.ERROR);
		buildMessage("Nome em uso!", "pt", MsgCode.NME_IN, MessageType.ERROR);
		
		buildMessage("Invalid name!", "en", MsgCode.NME_UW, MessageType.ERROR);
		buildMessage("Nome inv��lido!", "pt", MsgCode.NME_UW, MessageType.ERROR);
		
		buildMessage("Cannot move this!", "en", MsgCode.MOV_ERR, MessageType.ERROR);
		buildMessage("N��o se pode mover isto!", "pt", MsgCode.MOV_ERR, MessageType.ERROR);
		
		buildMessage("Erro em argumentos", "pt", MsgCode.ARG_ER, MessageType.ERROR);
		buildMessage("Argument error", "en", MsgCode.ARG_ER, MessageType.ERROR);
		
		buildMessage("Success on create a new Object", "en", MsgCode.SUC_OBJ, MessageType.INFO);
		buildMessage("Novo objeto criado com sucesso!", "pt", MsgCode.SUC_OBJ, MessageType.INFO);
		
		buildMessage("Success on create a new Class!", "en", MsgCode.SUC_CLS, MessageType.INFO);
		buildMessage("Nova classe criada com sucesso!", "pt", MsgCode.SUC_CLS, MessageType.INFO);
		
		buildMessage("Success on create a new Module!", "en", MsgCode.SUC_MOD, MessageType.INFO);
		buildMessage("Novo m��dulo criado com sucesso!", "en", MsgCode.SUC_MOD, MessageType.INFO);

		buildMessage("Success on create a new Application!", "en", MsgCode.SUC_APP, MessageType.INFO);
		buildMessage("Nova Aplica����o criada com sucesso!", "en", MsgCode.SUC_APP, MessageType.INFO);
	}
	private void buildMessage(String messageText,String sign,MsgCode msgCode,MessageType messageType)
	{
		Locale l = (Locale)service.getHibernateTemplate()
			.find("Select l from Locale as l where l.sign = ?",sign).get(0);
		
		Message message = new Message();
		message.setValue(messageText);
		message.setLocale(l);
		message.setMsgCode(msgCode.toString());
		message.setMessageType(messageType);
		service.save(Message.class.getName(),message);
	}
	private void createAdmin(Customer customer)
	{	
		User user = new User();
		user.setLogin("root");
		user.setPassw("toor");
		user.setCustomer(customer);
		service.save(User.class.getName(), user);
		
		Grouper grouper = new Grouper();
		grouper.setName("administrator");
		grouper.setUsers(new ArrayList<User>());
		grouper.getUsers().add(user);
		service.save(Grouper.class.getName(), grouper);
		
	}
	private Customer createCustommer()
	{
		Customer customer = new Customer();
		customer.setName("@root");
		service.save(Customer.class.getName(), customer);
		return customer;
	}
	private void createLinks()
	{
		String[] loginList = {"login"};
		createProgram(root.bin.login.class, loginList);
	
		String[] lsList = {"ls","list"};
		createProgram(root.bin.ls.class, lsList);
		
		String[] mkAppList = {"mkapp","makeapp"};
		createProgram(root.bin.mkapp.class,mkAppList);
		
		String[] mkClassList = {"mkclass","makeclass"};
		createProgram(root.bin.mkclass.class,mkClassList);
		
		String[] mkModList = {"mkmod","makemodule"};
		createProgram(root.bin.mkmod.class, mkModList);
		
		String[] mkObjList = {"mkobj","makeobject"};
		createProgram(root.bin.mkobj.class,mkObjList);
		
		String[] cdList = {"cd"};
		createProgram(root.bin.cd.class,cdList);
		
		String[] pwdList = {"pwd"};
		createProgram(root.bin.pwd.class,pwdList);
		
		String[] renameList = {"rename"};
		createProgram(root.bin.rename.class, renameList);
		
		String[] algoozTest = {"algooztest"};
		createProgram(root.bin.algooztester.class,algoozTest);

		String[] searchList = {"search","sr"};
		createProgram(root.bin.search.class,searchList);
		
		String[] rm = {"rm"};
		createProgram(root.bin.rm.class,rm);
		
		String[] alter = {"alter","alt"};
		createProgram(root.bin.alter.class, alter);
	}
	private void createProgram(java.lang.Class<? extends Binary> c,String[] linksNames)
	{
		Program program = new Program();
		program.setName(c.getName());
		program.setLinks(new ArrayList<Link>());
		
		for (int i = 0; i < linksNames.length; i++) 
		{
			Link link = new Link();
			link.setName(linksNames[i]);
			program.getLinks().add(link);
			service.save(Link.class.getName(), link);
		}
		
		service.save(Program.class.getName(), program);
	}
	private void createBasicTypes() 
	{
		service.getHibernateTemplate().bulkUpdate("delete from Module m where m.name = 'Algooz API' ");
		service.getHibernateTemplate().bulkUpdate("delete from Class c where c.isPrimitive is true");
		
		Module module = new Module();
		module.setName("Algooz API");
		module.setFileStatus(FileStatus.UNLOCKED);
		module.setClasses(new ArrayList<Class>());
		
		String[] clazzNameList =
			{
				"Description",
				"Location",
				"Email",
				"Currency",
				"Date",
				"File",
				"Boolean",
				"Decimal",
				"Integer",
				"String",
				"Function"
			};
		for (int i = 0; i < clazzNameList.length; i++) 
		{
			Class clazz = new Class();
			clazz.setName(clazzNameList[i]);
			clazz.setPrimitive(true);
			clazz.setFileStatus(FileStatus.UNLOCKED);
			service.save(Class.class.getName(), clazz);
			module.getClasses().add(clazz);
		}	
		this.service.getHibernateTemplate().save(Module.class.getName(), module);
	}
	private boolean classListIsEmpty()
	{
		@SuppressWarnings("unchecked")
		List<Object> list = service.getHibernateTemplate()
			.findByCriteria(DetachedCriteria.forClass(Class.class)
					.add(Restrictions.eq("isPrimitive", true)), 1, 1);
		return list.size() == 0;
	}
}
