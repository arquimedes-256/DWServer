package root.bin;

import algz.model.Attribute;
import root.etc.Binary;
import root.etc.MsgCode;
import root.etc.SimpleStream;

public class pwd extends Binary {

	@Override
	protected void init() {
		StringBuilder pwdString = new StringBuilder(ROOT_DIR);
		
		if(testParam(1, "-a"))
		{
			if(getTTY().getConnectedClass() != null)
			{
				algz.model.Class clazz = getTTY().getConnectedClass();
				
				pwdString.append(clazz.getName()+" [");
				
				for (Attribute attribute : clazz.getAttributes()) 
				{
					String t = attribute.getTyper() != null ? attribute.getTyper().getName() : "N/A";
					pwdString.append(attribute.getName()+":"+t+",");
				}
				pwdString.deleteCharAt(pwdString.length()-1);
				pwdString.append("]");
			}
			else
				{getNotificator().emmitNotification(this, MsgCode.CLS_NF, null);return;};
		}
		else
		{
			if(getTTY().getConnectedApplication() != null)
			{
				pwdString.append(getTTY().getConnectedApplication().getName()+ROOT_DIR);
				if(getTTY().getConnectedModule() != null)
				{
					pwdString.append(getTTY().getConnectedModule().getName()+ROOT_DIR);				
					if(getTTY().getConnectedClass() != null)
					{
						pwdString.append(getTTY().getConnectedClass().getName()+ROOT_DIR);
						if(getTTY().getConnectedObject()!=null)
						{
							pwdString.append(getTTY().getConnectedObject().getId());
						}
					}
				}
			}
		}
		getPipeline().setOutput(new SimpleStream(pwdString.toString()));
	}

	@Override
	public boolean needLogin() {
		return true;
	}
}
