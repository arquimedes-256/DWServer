package root.etc;

public class SimpleStream {
	public Object data;
	
	public SimpleStream() throws Exception {
		throw new Exception("Use SimpleStream(arg...");
	}
	public SimpleStream(Object data)
	{
		this.data = data;
	}
}
