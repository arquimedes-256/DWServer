package core.manager;

import java.util.regex.Pattern;

import org.hibernate.cfg.ImprovedNamingStrategy;
public class CMRNameStrategy extends ImprovedNamingStrategy
{
	private static final long serialVersionUID = 1L;
	private String tableName;
	
	@Override
	public String logicalColumnName(String columnName, String propertyName) {
		return this.collumnTransform(propertyName, tableName);
	}
	@Override
	public String propertyToColumnName(String columnName) {
		
		columnName = collumnTransform(columnName,tableName);
		return columnName;
	}
	private String collumnTransform(String columnName,String tableName)
	{
		String TS = "_";
		if(tableName.equals("*"))
		{
			TS = "";
			tableName = "";
		}
		//NME
		if(columnName.equals("nome"))
		{
			columnName = "nme";
			columnName =  columnName + TS + tableName;
		}
		//COD
		else if(columnName.equals("id"))
			{
				columnName = "cod";
				columnName =  columnName + TS + tableName;
			}
		//DATA
		else if(Pattern.compile("^data").matcher(columnName).find())
			{
				columnName = "dta_"+((columnName).replace("data", ""));
			}
		//TIPO
		else if(Pattern.compile("^tipo").matcher(columnName).find())
		{
			columnName = "TIPO_"+(columnName.toUpperCase()).replaceFirst("TIPO", "");
		}
		else
		{
			columnName = columnName + TS +tableName; 
		}
		return columnName.toUpperCase();
	}
	@Override
	public String classToTableName(String className) {
		this.tableName = className;
		return super.classToTableName(className).toUpperCase();
	}
	@Override
	public String foreignKeyColumnName(String propertyName,
			String propertyEntityName, String propertyTableName,
			String referencedColumnName) {
		return "COD_"+super.foreignKeyColumnName(propertyName, propertyEntityName, propertyTableName, referencedColumnName).toUpperCase();
	}
	@Override
	public String logicalCollectionColumnName(String columnName,
			String propertyName, String referencedColumn) {
		return super.logicalCollectionColumnName(columnName, propertyName,
				referencedColumn);
	}
	@Override
	public String joinKeyColumnName(String joinedColumn, String joinedTable) {
		tableName = joinedTable;
		return super.joinKeyColumnName(joinedColumn, joinedTable).toUpperCase();
	}
	@Override
	public String collectionTableName(String ownerEntity,
			String ownerEntityTable, String associatedEntity,
			String associatedEntityTable, String propertyName) {
		
		return (ownerEntityTable+"_"+propertyName).toUpperCase();
	}
	
	
	
}
