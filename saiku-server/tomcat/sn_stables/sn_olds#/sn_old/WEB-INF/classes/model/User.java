package model;

public class User 
{
	private String login;
	private String nome;
	private Perfil perfil;
	private boolean status;
	
	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public User(String login, String nome, Perfil perfil, boolean status) {
		super();
		this.login = login;
		this.nome = nome;
		this.perfil = perfil;
		this.status = status;
	}

	public boolean isStatus() 
	{
		if(getPerfil() == null)
			return false;
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}


	
	
}
