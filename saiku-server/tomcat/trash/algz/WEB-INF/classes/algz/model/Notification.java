package algz.model;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import core.base.BaseEntity;

@Entity
public class Notification extends BaseEntity {

	private static final long serialVersionUID = 973496747559309621L;
	@OneToOne
	private Message message;
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar date;
	
	@OneToOne
	private Grouper grouper;
	@OneToOne
	private User user;
	private String body;
	
	
	public Grouper getGrouper() {
		return grouper;
	}
	public void setGrouper(Grouper grouper) {
		this.grouper = grouper;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public Calendar getDate() {
		return date;
	}
	public void setDate(Calendar date) {
		this.date = date;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getBody()
	{
		return body;
	}

}
