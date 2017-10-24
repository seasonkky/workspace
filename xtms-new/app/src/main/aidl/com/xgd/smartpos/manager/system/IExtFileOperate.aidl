package com.xgd.smartpos.manager.system;


interface IExtFileOperate {

	boolean copyFile(String srcFileName, String destFileName, boolean overlay);
	boolean deleteFile(String filePath);
	boolean deleteDirectory(String filePath);
	boolean DeleteFolder(String filePath);
}
