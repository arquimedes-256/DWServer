package model;

public class Perfil 
{
	public final static Perfil GERENTE_GERAL			= new Perfil(true);
	public final static Perfil GERENTE_RJ1_SEP 			= new Perfil(new String[]{"RJ1","SEP","RJ1 + SEP"},true);
	public final static Perfil GERENTE_STR 				= new Perfil(new String[]{"STR"},true);
	public final static Perfil GERENTE_TMD_SDR_VIT_MAC 	= new Perfil(new String[]{"MAC","TMD","SDR","VIT","SDR + TMD"},true);
	public final static Perfil GERENTE_TMD_SDR_MAC 		= new Perfil(new String[]{"TMD","SDR","MAC","SDR + TMD"},true);
	public final static Perfil GERENTE_PNG_RGD 			= new Perfil(new String[]{"PNG","RGD"},true);
	public final static Perfil GERENTE_PNG_RGD_SDR_MAC 	= new Perfil(new String[]{"PNG","RGD","SDR","MAC"},true);
	public final static Perfil GERENTE_RGD 				= new Perfil(new String[]{"RGD"},true);
	
	public Perfil(String[] filiais,boolean isGerente) {
		super();
		this.filiais = filiais;
		this.isGerente = isGerente;
	}
	public Perfil(String[] filiais, String[] gruposPermitidos,
			String[] contasPerimitidas) {
		super();
		this.filiais = filiais;
		this.gruposPermitidos = gruposPermitidos;
		this.contasPerimitidas = contasPerimitidas;
	}
	

	public Perfil(boolean isGerente) {
		super();
		this.isGerente = isGerente;
	}

	private String[] filiais;
	private String[] gruposPermitidos;
	private String[] contasPerimitidas;
	private boolean isGerente;
	
	public boolean getIsMaster()
	{
		return isGerente && (gruposPermitidos == null && contasPerimitidas == null && filiais == null);
	}
	
	public String[] getFiliais() {
		return filiais;
	}
	public String[] getGruposPermitidos() {
		return gruposPermitidos;
	}
	public String[] getContasPerimitidas() {
		return contasPerimitidas;
	}
	public boolean isGerente() {
		return isGerente;
	}

}
