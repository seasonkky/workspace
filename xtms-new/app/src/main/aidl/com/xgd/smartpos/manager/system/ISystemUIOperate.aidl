package com.xgd.smartpos.manager.system;


interface ISystemUIOperate {

	boolean enableHome(boolean enable);
	boolean enableRecv(boolean enable);
	boolean enableControlBar(boolean enable);
	boolean enableMessageBar(boolean enable);
	boolean enablePowerKey(boolean enable);
}
