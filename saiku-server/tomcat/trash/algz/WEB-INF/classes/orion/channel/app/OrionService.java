package orion.channel.app;

import java.util.List;

import core.services.DefaultDAO;

public class OrionService extends DefaultDAO<Object>
{
	public OrionService() {
		
	}
	@Override
	public List<Object> find(String className, int from, int to) 
	{
		return super.find(className, from, to);
	}
	/* (non-Javadoc)
	 * @see core.services.DefaultDAO#find(java.lang.String)
	 */
	@Override
	public List<Object> find(String className) {
		return super.find(className);
	}

	/* (non-Javadoc)
	 * @see core.services.DefaultDAO#find(java.lang.String, int)
	 */
	@Override
	public Object find(String className, int id) {
		return super.find(className, id);
	}

	/* (non-Javadoc)
	 * @see core.services.DefaultDAO#save(java.lang.String, java.lang.Object)
	 */
	@Override
	public void save(String className, Object object) {
		super.save(className, object);
	}
	/* (non-Javadoc)
	 * @see core.services.DefaultDAO#update(java.lang.String, java.lang.Object)
	 */
	@Override
	public void update(String className, Object object) {
		super.update(className, object);
	}

	/* (non-Javadoc)
	 * @see core.services.DefaultDAO#update(java.lang.String, java.util.List)
	 */
	@Override
	public void update(String className, List<Object> object) {
		super.update(className, object);
	}

	/* (non-Javadoc)
	 * @see core.services.DefaultDAO#remove(java.lang.String, java.lang.Object)
	 */
	@Override
	public void remove(String className, Object object) {
		super.remove(className, object);
	}

	/* (non-Javadoc)
	 * @see core.services.DefaultDAO#remove(java.lang.String, java.util.List)
	 */
	@Override
	public void remove(String className, List<Object> object) {
		super.remove(className, object);
	}

	/* (non-Javadoc)
	 * @see core.services.DefaultDAO#merge(java.lang.String, java.lang.Object)
	 */
	@Override
	public void merge(String className, Object object) {
		super.merge(className, object);
	}

	/* (non-Javadoc)
	 * @see core.services.DefaultDAO#merge(java.lang.String, java.util.List)
	 */
	@Override
	public void merge(String className, List<Object> object) {
		super.merge(className, object);
	}

	/* (non-Javadoc)
	 * @see core.services.DefaultDAO#getEntityClass()
	 */
	@Override
	public Class<?> getEntityClass() {
		return super.getEntityClass();
	}

	/* (non-Javadoc)
	 * @see core.services.DefaultDAO#setEntityClass(java.lang.Class)
	 */
	@Override
	public void setEntityClass(Class<?> c) {
		super.setEntityClass(c);
	}



}
