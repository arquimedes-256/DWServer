package algz.factory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import root.bin.backpack;
import root.dao.ObjectDAO;
import root.etc.ClassDAO;
import algz.model.Object;
import algz.model.Session;
import algz.model.ui.Box;

public class BoxesFactory {

	private static BoxesFactory instance;

	public static BoxesFactory getInstance() 
	{
		return instance == null ? instance = new BoxesFactory() : instance;
	}

	@SuppressWarnings("unchecked")
	public List<Box> buildBindedBoxes(List<Box> boxes,HibernateTemplate template,Session session) 
	{
		List<Box> bindedBoxes = new ArrayList<Box>();
		
		for (Box box : boxes) 
		{
			if(box.getClazzId() > 0 ^ box.getBackpackId() > 0)
			{
				List<Object> classifiedObjects = null;
				ObjectDAO dao = ObjectDAO.getInstance();
				
				if(box.getClazzId() > 0)
					classifiedObjects = dao.findObjects(template,session, box.getClazzId());
				if(box.getBackpackId() > 0)
				{
					backpack b = new backpack();
					java.lang.Object output = null;
					algz.model.Class c = ClassDAO.getInstance().findById(box.getBackpackId(), template, session);
					b.run(new String[]{"backpack","-g","@"+c.getId()}, session, null, null);
					
					output = b.getPipeline().getOutput();
					classifiedObjects = (List<Object>) output;
				}
				
				for (Object object : classifiedObjects) 
				{
					Box objectedBox = null;
					
					objectedBox = (Box) box.getClone();
					objectedBox.setObject(object);
					bindedBoxes.add(objectedBox);
				}
			}
			else
			{
				bindedBoxes.add(box);
			}
		}
		return bindedBoxes;
	}

}
