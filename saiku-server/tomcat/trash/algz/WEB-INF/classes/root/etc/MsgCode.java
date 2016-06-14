package root.etc;

public enum MsgCode {
	
	/** TTY not found */
	TTY_NE,
	/** Path not exists */
	CD_NE, 
	/** Module not found */
	MDL_NF,
	/** Application not found */
	APP_NF,
	/** Class not found */
	CLS_NF,
	/** Cannot rename object */
	RN_OBJ, 
	/** Link not found */
	LNK_NF,
	/** Object not found */
	OBJ_NE,
	/** Class not exist */
	CLS_NE,
	/** Type not exist */
	TPE_NE,
	/** Module not exist  */
	MDL_NE,
	/** Application not exist*/
	APP_NE,
	/** Attribute not exist */
	ATR_NE,
	/** Name in use */
	NME_IN,
	/** Argument Error */ 
	ARG_ER,
	/** Name Incorrect */
	NME_UW,
	/** Cannot move this **/
	MOV_ERR,

	/** Sucess on Create Application **/
	SUC_APP,
	/** Sucess on Create Module **/
	SUC_MOD,
	/** Sucess on Create Class**/
	SUC_CLS,
	/** Sucess on Create Object **/
	SUC_OBJ;
	
	@Override
	public String toString()
	{
		return this.name();
	}
}
