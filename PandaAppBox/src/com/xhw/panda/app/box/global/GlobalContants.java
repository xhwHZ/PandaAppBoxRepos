package com.xhw.panda.app.box.global;

public class GlobalContants
{
	public static final String SERVER_URL = "http://192.168.1.100/PandaAppBox";

	public static final String HOME_SERVLET = SERVER_URL
			+ "/servlet/HomeServlet?index=";
	
	public static final String IMAGE_SERVLET = SERVER_URL
			+ "/servlet/ImageServlet?name=";
	
	public static final String SUBJECT_SERVLET = SERVER_URL
			+ "/servlet/SubjectServlet?index=";
	
	public static final String APP_SERVLET = SERVER_URL
			+ "/servlet/AppServlet?index=";
	
	public static final String GAME_SERVLET = SERVER_URL
			+ "/servlet/GameServlet?index=";
	
	public static final String USER_SERVLET = SERVER_URL
			+ "/servlet/UserServlet";
	
	public static final String DETAIL_SERVLET = SERVER_URL
			+ "/servlet/DetailServlet?packageName=";
	
	public static final String CATEGORY_SERVLET = SERVER_URL
			+ "/servlet/CategoryServlet";
	
	public static final String RANK_SERVLET = SERVER_URL
			+ "/servlet/HotServlet";
	
	public static final String DOWN_SERVLET = SERVER_URL
			+ "/servlet/DownloadServlet?name=";
	
}
