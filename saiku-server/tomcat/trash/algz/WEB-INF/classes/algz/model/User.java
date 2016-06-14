package algz.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import core.base.BaseEntity;

@Entity
public class User extends BaseEntity {
	
	private static final long serialVersionUID = 8853109672988274810L;
	
	private String login;
	@JsonIgnore
	private String passw;
	
	@OneToOne
	private Customer customer;
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassw() {
		return passw;
	}
	public void setPassw(String passw) {
		this.passw = passw;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}
