package root.etc.report;

import algz.model.Class;
import algz.model.Value;
import algz.utils.StringUtils;

public class FactProduct
{
	private Class factClass;
	private Long position;
	private Value value;
	
	public FactProduct(Class clazz,Value value,Long position) {
		this.factClass = clazz;
		this.position = position;
		this.value = value;
	}
	
	public Class getFactClass() {
		return factClass;
	}
	public float getPosition() {
		return position;
	}
	public Value getValue()
	{
		return value;
	}
	@Override
	public String toString() {
		return this.factClass.getName()+"#"+position+"@"+StringUtils.getRealValue(value);
	}
	
	
}