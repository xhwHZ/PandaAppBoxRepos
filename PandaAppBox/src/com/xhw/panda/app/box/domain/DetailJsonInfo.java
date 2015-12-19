package com.xhw.panda.app.box.domain;

import java.util.List;

public class DetailJsonInfo extends AppInfo
{
	public String author;
	public String date;
	public String downloadNum;
	public String version;
	
	public List<SafeInfo> safe;
	
	public class SafeInfo
	{
		public String safeDes;
		public int safeDesColor;
		public String safeDesUrl;
		public String safeUrl;
	}
	
	public List<String> screen;
}
