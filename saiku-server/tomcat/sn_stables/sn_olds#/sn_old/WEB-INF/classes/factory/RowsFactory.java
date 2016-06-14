package factory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import model.Row;

public class RowsFactory {

	private static final String ID_CONTA_TOTAL = "99999999999";
	private static final String ID_CC_TOTAL_DGA_PLUS_FINANC= ID_CONTA_TOTAL+"#";
	private ArrayList<Row> rows;
	private class OrganizedMeses
	{
		
		public TreeMap<String, ArrayList<Row>> meses = new TreeMap<>();
		public OrganizedMeses()
		{
			this.meses.put("01", new ArrayList<Row>());
			this.meses.put("02", new ArrayList<Row>());
			this.meses.put("03", new ArrayList<Row>());
			this.meses.put("04", new ArrayList<Row>());
			this.meses.put("05", new ArrayList<Row>());
			this.meses.put("06", new ArrayList<Row>());
			this.meses.put("07", new ArrayList<Row>());
			this.meses.put("08", new ArrayList<Row>());
			this.meses.put("09", new ArrayList<Row>());
			this.meses.put("10", new ArrayList<Row>());
			this.meses.put("11", new ArrayList<Row>());
			this.meses.put("12", new ArrayList<Row>());
		}
	};
	private OrganizedMeses organizedMeses = new OrganizedMeses();
	public OrganizedMeses buildEvolucao(ArrayList<Row> rows) 
	{
		this.rows = rows;
		organizeMeses();
		return organizedMeses;
	}
	private void organizeMeses() 
	{
		for(Row r :rows)
		{
			String strIdMes = String.valueOf(r.getMes());
			if(strIdMes.length() == 1)
				strIdMes = "0"+strIdMes;
			organizedMeses.meses.get(strIdMes).add(r);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//FILIAIS
	private OrganizedFiliais conj_organizedFiliais = new OrganizedFiliais();
	private OrganizedFiliais conj_organizedFiliaisDespFinanc = new OrganizedFiliais();
	private OrganizedFiliais conj_dga_financ = new OrganizedFiliais();
	
	private class OrganizedFiliais
	{
		public LinkedHashMap<String, LinkedHashMap<String,Row>> filiais 
			= new LinkedHashMap<String, LinkedHashMap<String,Row>>();
		
		public OrganizedFiliais()
		{
			/** FILIAL<CONTA,Row> **/
			this.filiais.put("RGD", new LinkedHashMap<String,Row>());
			this.filiais.put("PNG", new LinkedHashMap<String,Row>());
			this.filiais.put("STR", new LinkedHashMap<String,Row>());
			this.filiais.put("SEP", new LinkedHashMap<String,Row>());
			this.filiais.put("RJ1", new LinkedHashMap<String,Row>());
			this.filiais.put("VIT", new LinkedHashMap<String,Row>());
			this.filiais.put("SDR", new LinkedHashMap<String,Row>());
			this.filiais.put("TMD", new LinkedHashMap<String,Row>());
			this.filiais.put("MAC", new LinkedHashMap<String,Row>());
			this.filiais.put("MTZ", new LinkedHashMap<String,Row>());
			this.filiais.put("Total Empresa",   new LinkedHashMap<String,Row>());
		}
	};
	
	/** Ultima Coluna Total Empresa **/
	private LinkedHashMap<String, Row> lastcol_conta_row = new LinkedHashMap<>();
	/** Ultima Coluna Total Empresa **/
	private LinkedHashMap<String, Row> lastcol_conta_row_despfinanc = new LinkedHashMap<>();
	
	
	/** Ultima Linha Vertical para total da Filial **/
	private LinkedHashMap<String, Row> lastline_filial_row = new LinkedHashMap<>();
	/** Ultima Linha Vertical para total da Filial **/
	private LinkedHashMap<String, Row> lastline_filial_row_despfinanc = new LinkedHashMap<>();
	
	public OrganizedFiliais[] buildFiliais(ArrayList<Row> rows)
	{
		this.rows = rows;

		Row exemple = rows.size() > 0 ? rows.get(0) : null ; 
		boolean isDGA = exemple != null && exemple.getIdContaContabil().startsWith("(Dga.MTZ+Dga.Fls) ");
		organizeFiliais(isDGA);
		return isDGA ? new OrganizedFiliais[]{conj_organizedFiliais,conj_organizedFiliaisDespFinanc,conj_dga_financ} : new OrganizedFiliais[]{conj_organizedFiliais};
	}
	private void organizeFiliais(boolean isDGAReport) 
	{
		for(Row r :rows)
		{
			if(isDGAReport && isOutrasDespFinanc(r))
			{
				conj_organizedFiliaisDespFinanc.filiais.get(r.getFilial()).put(r.getIdContaContabil(),r);
				adicionaParaTotal(r,lastline_filial_row_despfinanc,lastcol_conta_row_despfinanc);
			}
			else
			{
				conj_organizedFiliais.filiais.get(r.getFilial()).put(r.getIdContaContabil(),r);
				adicionaParaTotal(r,lastline_filial_row,lastcol_conta_row);
			}
		}
		
		filialRowToMappedFiliais(conj_organizedFiliais,lastline_filial_row);
		if(isDGAReport)
			filialRowToMappedFiliais(conj_organizedFiliaisDespFinanc,lastline_filial_row_despfinanc);
		
		contaRowToTotalEmpresa(conj_organizedFiliais,lastcol_conta_row);
		if(isDGAReport)
			contaRowToTotalEmpresa(conj_organizedFiliaisDespFinanc,lastcol_conta_row_despfinanc);
		
		putAndSumTotalEmpresa(conj_organizedFiliais);
		if(isDGAReport)
			putAndSumTotalEmpresa(conj_organizedFiliaisDespFinanc);

		if(isDGAReport)
		{
			putLastTotal();
		}
	}
	private void putLastTotal() 
	{
		for(String filial : conj_organizedFiliais.filiais.keySet())
		{
			Row newTotal = new Row();
			
			Row r1 =  conj_organizedFiliais.filiais.get(filial).get(ID_CONTA_TOTAL);
			Row r2 =  conj_organizedFiliaisDespFinanc.filiais.get(filial).get(ID_CONTA_TOTAL);
			
			if(r1 != null && r2 != null)
			{
				newTotal.setVlrOrcado(r1.getVlrOrcado() + r2.getVlrOrcado());
				newTotal.setVlrRealizado(r1.getVlrRealizado() + r2.getVlrRealizado());
				
				newTotal.setIdContaContabil(ID_CC_TOTAL_DGA_PLUS_FINANC);
				newTotal.setNomeContaContabil("Total :DGA + Outras Desp. Financeiras");
				
				conj_dga_financ.filiais.get(filial).put(ID_CC_TOTAL_DGA_PLUS_FINANC, newTotal);
				//lastline_dga.put(newTotal, value);
			}
		}
	}
	private void putAndSumTotalEmpresa(OrganizedFiliais conj) 
	{
		Row linhaFinal = new Row();
		linhaFinal.setVlrOrcado(0);
		linhaFinal.setVlrRealizado(0);
		linhaFinal.setIdContaContabil(ID_CONTA_TOTAL);
		
		for(Row r : conj.filiais.get("Total Empresa").values())
		{
			linhaFinal.setVlrOrcado(r.getVlrOrcado() + linhaFinal.getVlrOrcado());
			linhaFinal.setVlrRealizado(r.getVlrRealizado() + linhaFinal.getVlrRealizado());
			if(linhaFinal.getNomeContaContabil() == null)
			{
				if(isOutrasDespFinanc(r))
					linhaFinal.setNomeContaContabil("Total :Outras Desp. Financeiras");
				else
				{
					if(r.getIdContaContabil() != null && 
							r.getIdContaContabil().startsWith("(Emb+Man+Adm)"))
					{
						linhaFinal.setNomeContaContabil("Total: (Emb+Man+Adm)");
					}
					else
						linhaFinal.setNomeContaContabil("Total :"+r.getNomeGrupo());
				}
			}
		}
		conj.filiais.get("Total Empresa").put(ID_CONTA_TOTAL,linhaFinal);
	}
	
	private void filialRowToMappedFiliais(OrganizedFiliais conj, LinkedHashMap<String, Row> last_line) 
	{
		for(Row r : last_line.values())
		{
			conj.filiais.get(r.getFilial()).put(ID_CONTA_TOTAL,r);
		}
		
	}
	private boolean isOutrasDespFinanc(Row r)
	{
		String[] contasFinanceiras = new String[]{
				"Provisão para Contingências Fiscais" ,
				"Despesas Financeiras Outros",
				"Juros Pagos ou Incorridos",
				"Encargos ICMS",
				"Bens de Natureza Permanente",
				"Comissão e Despesas Bancárias",
				"Imposto sobre Operações Financeiras",
				"Despesas Não Dedutíveis"
				};
		
		for(String c : contasFinanceiras)
		{
			if(r.getIdContaContabil().equals("(Dga.MTZ+Dga.Fls) "+c))
				return true;
		}
			
		return false;
	}
	private void contaRowToTotalEmpresa(OrganizedFiliais conj, LinkedHashMap<String, Row> lastcol) 
	{
		for(Row r : lastcol.values())
		{
			conj.filiais.get("Total Empresa").put(r.getIdContaContabil(),r);
		}
	}
	private void adicionaParaTotal(Row r, LinkedHashMap<String, Row> last_line, LinkedHashMap<String, Row> lastcol)
	{
		
		if(!last_line.containsKey(r.getFilial()))
		{
			Row clone = r.getClone();
			last_line.put(r.getFilial(), clone);
			clone.setIdContaContabil(ID_CONTA_TOTAL);
			
			clone.setNomeContaContabil("Total "+r.getNomeGrupo()+":");
		}
		else
		{
			Row acum = last_line.get(r.getFilial());
			acum.setVlrOrcado(acum.getVlrOrcado() + r.getVlrOrcado());
			acum.setVlrRealizado(acum.getVlrRealizado() + r.getVlrRealizado());
		}
		
		
		if(!lastcol.containsKey(r.getIdContaContabil()))
		{
			Row clone = r.getClone();
			lastcol.put(r.getIdContaContabil(), clone);
			clone.setFilial("Total Empresa");
		}
		else
		{
			Row acum = lastcol.get(r.getIdContaContabil());
			acum.setVlrOrcado(acum.getVlrOrcado() + r.getVlrOrcado());
			acum.setVlrRealizado(acum.getVlrRealizado() + r.getVlrRealizado());
		}
	}
	/**
	 * 
	 */
	public Object buildTotal(ArrayList<Row> rows) 
	{
		if(rows.size() >0)
		{
			Row sum = new Row();
			sum.setIdContaContabil(ID_CONTA_TOTAL);
			sum.setIdGrupo("99");
			sum.setFilial(rows.get(0).getFilial());
			
			for(Row r :rows)
			{
				sum.setVlrOrcado(r.getVlrOrcado()+sum.getVlrOrcado());
				sum.setVlrRealizado(r.getVlrRealizado()+sum.getVlrRealizado());
				if(sum.getNomeGrupo() == null)
				{
					sum.setNomeGrupo("Total: ");
					sum.setNomeContaContabil("Total: ");
					
					if(sum.getNomeGrupo().equals("Outras DGA Adm. Matriz"))
					{
						sum.setNomeGrupo("Total: Outras DGA (MTZ+Filiais)");
						sum.setNomeContaContabil("Total: Outras DGA (MTZ+Filiais)");
					}
					
					
				}
			}
			rows.add(sum);
		}
		return rows;
	}

}
