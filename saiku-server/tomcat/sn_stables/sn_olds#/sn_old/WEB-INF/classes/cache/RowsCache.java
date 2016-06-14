package cache;

import java.util.Calendar;
import java.util.HashMap;

import model.Options;

import org.apache.catalina.util.HexUtils;


public class RowsCache 
{
	private static RowsCache instance = new RowsCache();
	private static Calendar bornDate = Calendar.getInstance();
	public RowsCache()
	{
		if(instance != null)
			throw new Error("use get instance");
	}
	private static HashMap<String, String> cache = new HashMap<String, String>();
	private static HashMap<String, Options> cacheOpt = new HashMap<String, Options>();
	
	
	public String get(Options opt) 
	{
		String c = cache.get(HexUtils.convert(opt.get_hashbytes()));
		if(c != null)
		{
			Options options = cacheOpt.get(HexUtils.convert(opt.get_hashbytes()));
			System.out.println("[Valores de cache regatados] :"+
					HexUtils.convert(options.get_hashbytes())+"/"+
							options.metodo+" equivalente à param:"+opt.metodo+":"+HexUtils.convert(opt.get_hashbytes())+" ~~ ["+options.filial+"] "
							+options.ano+"/"+options.mesInicial+" à "+options.mesFinal+" \n" +
									"\t\t ->isEvolucao:"+options.isEvolucao+"\n"+
									"\t\t ->isConsolidado:"+options.isConsolidado+"\n"+
									"\t\t ->keepCC:"+options.isKeepContaContabil+"\n"+
									"\t\t ->Grupo:"+options.idGrupo+"\n"+
									"\t\t ->Grupo Master:"+options.idGrupoMaster+"\n");
		}
		return c;
	}
	public void put(Options options,String json)
	{
		if(!(cache.containsKey(options.get_hashcode())))
		{
			cache.put(HexUtils.convert(options.get_hashbytes()), json);
			cacheOpt.put(HexUtils.convert(options.get_hashbytes()), options);
			System.out.println("[Salvo em cache] :"+
			HexUtils.convert(options.get_hashbytes())+
			"/"+
			options.metodo+" ~~ ["+options.filial+"] "
			+options.ano+"/"+options.mesInicial+" à "+options.mesFinal+" \n" +
					"\t\t ->isEvolucao:"+options.isEvolucao+"\n"+
					"\t\t ->isConsolidado:"+options.isConsolidado+"\n"+
					"\t\t ->keepCC:"+options.isKeepContaContabil+"\n"+
					"\t\t ->Grupo:"+options.idGrupo+"\n"+
					"\t\t ->Grupo Master:"+options.idGrupoMaster+"\n");
		}
	}
	
	public static RowsCache getInstance()
	{
		Calendar now = Calendar.getInstance();
		if((bornDate.get(Calendar.DATE) < now.get(Calendar.DATE)) ||
			(bornDate.get(Calendar.MONTH) < now.get(Calendar.MONTH) ||
			(bornDate.get(Calendar.YEAR) < now.get(Calendar.YEAR)
			)))
		{
			System.out.println("[Cache limpo] "+now);
			cache.clear();
		}
		return instance;
	}
}
