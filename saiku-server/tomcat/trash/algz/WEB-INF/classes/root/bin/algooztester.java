package root.bin;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import root.etc.Binary;
import root.etc.MsgCode;
import root.etc.Pipeline;
import root.etc.RootInstruction;
import root.net.Tester;
import algz.endpoint.KernelEndPoint;
import algz.model.Notification;
import algz.model.Session;
import algz.model.TTY;

public class algooztester extends Binary implements RootInstruction
{
	ArrayList<String> returns = new ArrayList<String>();
	
	@Override
	protected void init()
	{
		this.test("login test", new Tester()
		{
			@Override
			public boolean test()
			{
				Session output = (Session) algooztester.this.executeShell("login root toor");
				if(output.getDeathDate() != null)
				{
					return false;	
				}
				return true;
			}
		});
		this.test("list root", new Tester()
		{
			@Override
			public boolean test()
			{
				//Object d =  AlgoozTester.this.executeShell("ls");
				algooztester.this.executeShell("cd /");
				Object ls = algooztester.this.executeShell("ls");
				if(ls instanceof TTY)
				{
					TTY tty = (TTY)ls;
					if(tty.getConnectedApplication() == null && tty.getConnectedClass() == null && tty.getConnectedModule() == null && tty.getConnectedObject() == null)
					{
						return true;
					}
				}
					
				return false;
			}
		});
		this.test("make app", new Tester()
		{
			@Override
			public boolean test()
			{
				Object mkAppReturn = algooztester.this.executeShell("mkapp testApp");
				if(!(mkAppReturn instanceof Notification))
				{
					Object tty = (Object) 
					algooztester.this.executeShell("cd testApp");
					if(tty instanceof TTY)
					{
						if(((TTY)tty).getConnectedApplication() != null)
						{
							algooztester.this.executeShell("cd ..");
							return true;
						}	
					}	
				}
				else
				{
					Notification notification = (Notification)mkAppReturn;
					if(notification.getMessage().getMsgCode().equals(MsgCode.NME_IN.name()))
					{
						return true;
					}
						//TODO
				}
				return false;
			}
		});
		this.test("validate duplicate app",new Tester()
		{
			@Override
			public boolean test()
			{ 
				Notification notification = (Notification) algooztester.this.executeShell("mkapp testApp");
				if(notification.getMessage().getMsgCode().equals(MsgCode.NME_IN.name()))
				{
					return true;
				}
				return false;
			}
		});
		this.test("wrong name in app '..'", new Tester()
		{
			@Override
			public boolean test()
			{
				Notification notification = (Notification) algooztester.this.executeShell("mkapp ..");
				if(notification.getMessage().getMsgCode().equals(MsgCode.NME_UW.name()))
				{
					return true;
				}
				return false;
			}
		});
		this.test("wrong name in app '/*'", new Tester()
		{
			@Override
			public boolean test()
			{
				Notification notification = (Notification) algooztester.this.executeShell("mkapp /tt");
				if(notification.getMessage().getMsgCode().equals(MsgCode.NME_UW.name()))
				{
					return true;
				}
				return false;
			}
		});
		this.test("make mod", new Tester()
		{
			@Override
			public boolean test()
			{
				algooztester.this.executeShell("mkmod testMod");
				Object obj = (Object) 
						algooztester.this.executeShell("cd testMod");
				if(obj instanceof TTY)
				{
					if(((TTY)obj).getConnectedModule() != null)
					{
						algooztester.this.executeShell("cd ..");
						return true;
					}
				}
				else if(obj instanceof Notification)
				{
					Notification notification = (Notification)obj;
					if(notification.getMessage().getMsgCode().equals(MsgCode.NME_IN.name()))
						return true;
				}
				return false;
			}
		});
		this.test("validate duplicate module",new Tester()
		{
			@Override
			public boolean test()
			{ 
				Notification notification = (Notification) algooztester.this.executeShell("mkmod testMod");
				if(notification.getMessage().getMsgCode().equals(MsgCode.NME_IN.name()))
				{
					return true;
				}
				return false;
			}
		});
		this.test("wrong name in module '..'", new Tester()
		{
			@Override
			public boolean test()
			{
				Notification notification = (Notification) algooztester.this.executeShell("mkmod ..");
				if(notification.getMessage().getMsgCode().equals(MsgCode.NME_UW.name()))
				{
					return true;
				}
				return false;
			}
		});
		this.test("wrong name in app '/*'", new Tester()
		{
			@Override
			public boolean test()
			{
				Notification notification = (Notification) algooztester.this.executeShell("mkmod /tt");
				if(notification.getMessage().getMsgCode().equals(MsgCode.NME_UW.name()))
				{
					return true;
				}
				return false;
			}
		});
		this.getPipeline().setOutput(returns);
		System.out.println();
	}
	@Override
	public boolean needLogin()
	{
		return true;
	}
	private void test(String description, Tester tester)
	{
		String info = description;
		
		if(tester.test())
		{
			info = " [ ok ] "+info;
		}
		else
		{
			info = " [erro] "+info;
		}
		returns.add(info);
	}
	Object executeShell(String command)
	{
		KernelEndPoint kernelEndPoint = new KernelEndPoint();
		try {
			kernelEndPoint.initShell(command, getRequest().getRemoteAddr(), getRequest(), getResponse(),new Pipeline());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return kernelEndPoint.getBus().getBin().getPipeline().getOutput();
	}
}
