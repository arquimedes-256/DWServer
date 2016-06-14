package algz.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Index;

import core.base.BaseEntity;

@Entity
public class Message extends BaseEntity
{

	private static final long serialVersionUID = -6208334459977671031L;
	@OneToOne
	private Locale locale;
	@Index(name="MESSAGE_MSGCODE")
	private String msgCode;

	public String getMsgCode()
	{
		return msgCode;
	}

	public void setMsgCode(String msgCode)
	{
		this.msgCode = msgCode;
	}

	public Locale getLocale()
	{
		return locale;
	}

	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	private String value;
	@OneToOne
	private MessageType messageType;

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
		
	}

	public MessageType getMessageType() {
		return messageType;
	}
}
