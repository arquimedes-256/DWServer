package factory;

import model.Perfil;

public class PerfilFactory {
	/**
	 * Rafael Dias -> rafael.dias (SN Santos)
	 * Ricardo Patta -> ricardo.patta (SN Rio Grande / Paranagua)
	 * Ederson -> ederson (SN Vitoria / Maceio / Salvador)
	 * Marcus Paulo -> marcus.paulo (SN Rio de Janeiro)
	 * Jose Ramos -> jose.ramos (SN Matriz) 
	 * ------------------------------------------------------------------------------
	 * Coordenadores
	 * 
	 * Raphael Laguna -> raphael.laguna (SN Rio de Janeiro)
	 * Daniel Unno -> daniel.unno (SN Salvador)
	 * 
	 * ---------------------------------------------------------------------------------
	 * Diretores
	 * 
	 * Nilson Almeida -> nilson.almeida​
	 * Sergio Tavares -> sergio
	 * Adolfo Acioli -> adolfoacioli
	 * Paulo Oliveira ->  paulo.oliveira
	 * @param user
	 * @return
	 */
	public static Perfil buildPerfil(String user) 
	{
		if(user.equals("marcus.paulo")
				|| user.equals("nilson.almeida")
				|| user.equals("jose.ramos")
				|| user.equals("nilson")
				|| user.equals("sergio")
				|| user.equals("adolfoacioli")
				|| user.equals("paulo.oliveira")
				|| user.equals("carla.rodrigues")
				|| user.equals("kaiser")
				|| user.equals("ricardo.costa")
				|| user.equals("asouto")
				|| user.equals("lucas.silva"))
			return Perfil.GERENTE_GERAL;
		if(user.equals("rafael.dias"))
			return Perfil.GERENTE_STR;
		if(user.equals("ricardo.patta"))
			return Perfil.GERENTE_PNG_RGD;
		if(user.equals("ederson"))
			return Perfil.GERENTE_TMD_SDR_VIT_MAC;
		if(user.equals("raphael.laguna"))
			return Perfil.GERENTE_RJ1_SEP;
		if(user.equals("daniel.unno"))
			return Perfil.GERENTE_TMD_SDR_MAC;
		return null;
	}

}
