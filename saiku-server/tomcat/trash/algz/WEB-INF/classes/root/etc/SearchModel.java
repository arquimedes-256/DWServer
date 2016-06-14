package root.etc;

public class SearchModel
{
	private String[] q = {};
	private int c;
	private int t = 0;
	public int getT() {
		return t;
	}
	public void setT(int t) {
		this.t = t;
	}
	public String[] getQ()
	{
		return q;
	}
	public void setQ(String[] q)
	{
		this.q = q;
	}
	public int getC()
	{
		return c;
	}
	public void setC(int c)
	{
		this.c = c;
	}
}
