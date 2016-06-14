package algz.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import root.etc.ReasonCode;
import core.base.BaseEntity;

@Entity
public class Reason extends BaseEntity
{
	private static final long serialVersionUID = 8018121823019128443L;
	private String text;
	private String reasonCode;
	@OneToOne
	private Locale locale;

	public Reason()
	{
		
	}
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Reason(String text,ReasonCode reasonCode,Locale locale)
	{
		this.text = text;
		this.locale = locale;
		this.setReasonCode(reasonCode.name());
	}

	public String getText() {
		return text;
	}
	public void setText(String text) 
	{
		this.text = text;
	}
	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
}
