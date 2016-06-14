package model;

import java.nio.ByteBuffer;

import factory.QueryFactory.LocalOptions;


public class Options {
	public String filial = "";
	public String mesInicial;
	public String mesFinal;
	public String ano;
	public String idGrupoMaster = "";
	public String idGrupo = "";
	public String metodo = null;
	public boolean isEvolucao = false;
	public boolean isConsolidado = false;
	public boolean isKeepContaContabil = true;
	public String idContaContabil;
	public LocalOptions localOptions;
	public boolean isOrcNovo = false;
	
	private String _hashcode = null;
	private byte[] _hashbytes;
	
	public byte[] get_hashbytes() {
		_compilehash();
		return _hashbytes;
	}
	
	private void _compilehash()
	{
		if(_hashcode == null)
		{
			String locIdGrupoMaster = idGrupoMaster;
			
			if(idGrupoMaster != null && idGrupoMaster.equals("9898989"))
				locIdGrupoMaster = "98";
			if(idGrupoMaster != null && idGrupoMaster.equals("EBT"))
				locIdGrupoMaster = "88";
			
			_hashbytes = new byte[]{
					/* 0 */Byte.parseByte(mesInicial),
					/* 1 */Byte.parseByte(mesFinal),
					/* 2 */Byte.parseByte(ano.substring(2, 4)),
					/* 3 */Byte.parseByte(idGrupoMaster == null ? "0":locIdGrupoMaster),
					/* 4 */Byte.parseByte(idGrupo== null ? "0":idGrupo),
					/* 5 */Byte.parseByte(isEvolucao ? "1" :"0"),
					/* 6 */Byte.parseByte(isConsolidado ? "1" :"0"),
					/* 7 */Byte.parseByte(isKeepContaContabil ? "1" :"0"),
					/* 8 */Byte.parseByte(metodo != null ? String.valueOf(buildMethod(metodo)):"0"),
					/* 9 */Byte.parseByte(filial != null ? String.valueOf(buildFilial(filial)) : "0"),
					/* 10 */Byte.parseByte(isOrcNovo ? "1" :"0")
					};
			 long bits = Double.doubleToLongBits(ByteBuffer.wrap(_hashbytes).getDouble());
			 
			_hashcode = String.valueOf((int)(bits ^ (bits >>> 32))) + "-"+
					((idContaContabil != null) ? idContaContabil : "");
				
		}
	}
	public String get_hashcode() 
	{
		_compilehash();
		return _hashcode;
	}
	public static int buildMethod(String method)
	{
		int id = 0;
		
		if(method.equals("disponibilidade_contabil"))
			id = 4;
		else if(method.equals("orcadorealizado"))
			id = 1;
		else if(method.equals("orcadorealizado_detalhado"))
			id =2;
		else if(method.equals("orcadorealizado_detalhado_intern"))
			id =2;
		else if(method.equals("centro_de_custo_mtz"))
			id = 5;
		else if(method.equals("evolucao_grafico"))
			id = 6;
		return id;
		
	}
	public static int buildFilial(String sigla)
	{
		int id = 0;
		
		if(sigla.equals("Total Empresa"))
			id = 15;
		else if(sigla.equals("SDR"))
			id = 6;
		else if(sigla.equals("TMD"))
			id = 16;
		else if(sigla.equals("STR") )
			id = 3;
		else if(sigla.equals("PNG"))
			id = 5;
		else if(sigla.equals("SEP"))
			id = 9;
		else if(sigla.equals("RJ1"))
			id = 2;
		else if(sigla.equals("VIT"))
			id = 1;
		else if(sigla.equals("RGD"))
			id = 8;
		else if(sigla.equals("MAC"))
			id = 7;
		else if(sigla.equals("APM"))
			id = 10;
		else if(sigla.equals("GUA"))
			id = 11;
		else if(sigla.equals("MTZ"))
			id = 12;
		else if(sigla.equals("CONSOLIDE"))
			id = 13;
		else if(sigla.equals("LISTAR"))
			id = 14;
		else if(sigla.equals("RJ1 %2B SEP"))
			id = 17;
		else if(sigla.equals("SDR %2B TMD"))
			id = 18;
		else if(sigla.equals("ITA"))
			id = 4;
		else
			throw new Error("Filial nao existente "+sigla);
		return id;
	}
}
