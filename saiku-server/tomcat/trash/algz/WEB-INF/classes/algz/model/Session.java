package algz.model;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


import core.base.BaseEntity;

@Entity
public class Session extends BaseEntity 
{	

	private static final long serialVersionUID = 1056460772784080818L;

	@Index(name="IDX_SESSION_IP")
	private String ip;
	@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<TTY> ttys;
	
	@OneToOne
	private Locale locale;
	
	public Locale getLocale() {
		return locale;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	@OneToOne
	private User user;
	
	@OneToOne
	private TTY tty;
	

	@Index(name="IDX_SESSION_BORNDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar bornDate;
	
	@Index(name="IDX_SESSION_DEATHDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar deathDate;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public List<TTY> getTtys() {
		return ttys;
	}
	public void setTtys(List<TTY> ttys) {
		this.ttys = ttys;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Calendar getBornDate() {
		return bornDate;
	}
	public void setBornDate(Calendar bornDate) {
		this.bornDate = bornDate;
	}
	public Calendar getDeathDate() {
		return deathDate;
	}
	public void setDeathDate(Calendar deathDate) {
		this.deathDate = deathDate;
	}
	public TTY getTty() {
		return tty;
	}
	public void setTty(TTY tty) {
		this.tty = tty;
	}
}
