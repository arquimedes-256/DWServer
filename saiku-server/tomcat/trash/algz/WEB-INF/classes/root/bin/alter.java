package root.bin;

import algz.model.Class;
import algz.utils.StringUtils;
import root.etc.NavigableBinary;

public class alter extends NavigableBinary{

	private mkattr mkattr;
	@Override
	protected void init() 
	{
		if(isClass())
		{
			boolean flag = false;
			mkattr = new mkattr();
			mkattr.enableRunOnInit(false);
			mkattr.setPipeline(this.getPipeline());
			mkattr.run(getArgs(), getCurrentSession(), getRequest(), getResponse());
			
			String attrName = testParam(2, "-scape") ? getArgs()[3]: StringUtils.fix(getArgs()[2]) ;
			
			if(testParam(1, "remove"))
			{
				flag = removeAttr(attrName);
			}
			else if(testParam(1, "add"))
			{
				flag = addAttr(attrName);
			}
			else if(testParam(1,"order"))
			{
				flag = orderAttr(attrName);
			}
			if(flag)
				mkattr.saveClass();
		}
	}
	private boolean orderAttr(String attrName)
	{
		Class clazz = getTTY().getConnectedClass();
		return mkattr.orderAttributtes(clazz,attrName);
	}
	private boolean addAttr(String attributeSolid) 
	{
		String folderName = attributeSolid.split(root.bin.mkattr.SINGLE_DELIMITER)[0];
		Class clazz = getTTY().getConnectedClass();
		if(!mkattr.existsThisFolder(folderName, clazz))
		{
			return mkattr.buildAttributes(clazz, attributeSolid);
		}
		return false;
	}
	private boolean removeAttr(String attrName) 
	{
		return mkattr.removeAttributes(attrName); 
	}
	@Override 
	public boolean needLogin() {
		return true;
	}
}