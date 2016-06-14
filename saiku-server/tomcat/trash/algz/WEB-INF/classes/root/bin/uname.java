package root.bin;

import root.etc.Binary;
import root.etc.SimpleStream;

public class uname extends Binary{

	@Override
	protected void init() 
	{
		getPipeline().setOutput(new SimpleStream(this.getCurrentSession().getUser()));
	}

	@Override
	public boolean needLogin() {
		return true;
	}

}
