package root.etc;

import core.base.BaseEntity;

public interface FolderMaker
{
	boolean existsThisFolder(String folderName, BaseEntity baseEntityAux);
}
