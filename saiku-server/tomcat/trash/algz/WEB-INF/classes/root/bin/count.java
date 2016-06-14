package root.bin;

import java.util.List;

import root.etc.AlgzFile;
import root.etc.Binary;
import root.etc.SimpleStream;

public class count extends Binary {

	@Override
	protected void init() {
		AlgzFile output = null ;
		int size = 0;
		
		if(this.getPipeline().getInput() == null)
		{
			output = this.getTTY().getCurrentFile();
		} 
		else if(this.getPipeline().getInput() instanceof AlgzFile) {

			output = (AlgzFile)this.getPipeline().getInput();
		}
		else if(this.getPipeline().getInput() instanceof List<?>)
		{
			size = ((List<?>)this.getPipeline().getInput()).size();
		}
		if(output != null && output.getChildrens() != null)
			size = output.getChildrens().size();
		this.getPipeline().setOutput(new SimpleStream(size));

	}

	@Override
	public boolean needLogin() {
		return true;
	}

}
