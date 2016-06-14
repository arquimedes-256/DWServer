package core.services;

import java.util.List;

/**
 * Interface para Data Acess Object
 * @author Lucas Oliveira
 * @see DefaultService
 * @param <T>
 */
public interface ICoreDao<T> 
{
	/**
	 * Retorna uma lista de itens.
	 * @return
	 */
	public List<T> find(String className);
	public List<T> find(String className,int from,int to);
	/**
	 * Retorna um único resultado
	 * @param id
	 * @return
	 */
	public T find(String className,int id);
	/**
	 * 
	 * @param object
	 */
	public void save(String className,T object);
	/**
	 * Insere uma lista de itens.
	 * @param object
	 */
	public void save(String className,List<T> object);
	/**
	 * Atualiza um único item
	 * @param object
	 */
	public void update(String className,T object);
	/**
	 * Atualiza uma lista de itens.
	 * @param object
	 */
	public void update(String className,List<T> object);
	/**
	 * Remove um único item
	 * @param object
	 */
	public void remove(String className,T object);
	/**
	 * Insere uma lista de itens.
	 * @param object
	 */
	public void remove(String className,List<T> object);
	/**
	 * Faz merge em um único item
	 * @param object
	 */
	public void merge(String className,T object);
	/**
	 * Faz merge em uma lista de itens.
	 * @param object
	 * @throws ClassNotFoundException 
	 */
	public void merge(String className,List<T> object) throws ClassNotFoundException;
}
