package com.xhw.panda.app.box.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;

/**
 * �����̳߳�
 * 
 * @author itcast
 * 
 */
public class ThreadManager
{
	private ThreadManager()
	{

	}

	private static ThreadManager instance = new ThreadManager();

	public static ThreadManager getInstance()
	{
		return instance;
	}

	private static ThreadPoolProxy mLongPool=null;
	private static Object mLongLock=new Object();
	
	private static ThreadPoolProxy mShortPool=null;
	private static Object mShortLock=new Object();
	
	private static ThreadPoolProxy mDownLoadPool=null;
	private static Object mDownLoadLock=new Object();
	
	/**
	 * һ���������� �ó����̳߳�  ������ʱ�Ƚϳ� �����������������ڱ���߳��� 
	 * @return
	 */
	public static ThreadPoolProxy createLongPool(){
		synchronized (mLongLock) {
			if(mLongPool==null){
				mLongPool=new ThreadPoolProxy(5, 5, 5L);
			}
			return mLongPool;
		}
	}
	/**����IO*/
	public static ThreadPoolProxy createShortPool(){
		synchronized (mShortLock) {
			if(mShortPool==null){
				mShortPool=new ThreadPoolProxy(2, 2, 5L);
			}
			return mShortPool;
		}
	}
	public static ThreadPoolProxy createDownLoadPool(){
		synchronized (mDownLoadLock) {
			if(mDownLoadPool==null){
				mDownLoadPool=new ThreadPoolProxy(2, 2, 5L);
			}
			return mDownLoadPool;
		}
	}
	
	/**
	 * ��װ���̳߳�
	 * 
	 * @author itcast
	 * 
	 */
	public static class ThreadPoolProxy {
		private ThreadPoolExecutor mPool;// ϵͳ�ṩ���̳߳�
		private int mCorePoolSize;  // �߳����� 
		private int maximumPoolSize; //���������
		private long mKeepAliveTime;  //���ֵ�ʱ��
		
		

		public ThreadPoolProxy(int mCorePoolSize,
				int maximumPoolSize, long mKeepAliveTime) {
			super();
			this.mCorePoolSize = mCorePoolSize;
			this.maximumPoolSize = maximumPoolSize;
			this.mKeepAliveTime = mKeepAliveTime;
		}



		public synchronized void execute(Runnable runnable) {
			if (runnable == null) {
				return;
			}
			if (mPool == null) {
				/*
				 * 1 �̳߳ص���������
				 * 2 ������з����� ���ⴴ�����߳� 
				 * 3 û������Ļ��ܻ���
				 * 4 ���ʱ��ĵ�λ 
				 * 5 �̳߳����˶���   ����ָ������
				 * 6 �����̳߳صĹ��� 
				 * 7 �����쳣��Handler (�̶�д�� )
				 */
				mPool = new ThreadPoolExecutor(mCorePoolSize, maximumPoolSize,
						mKeepAliveTime, TimeUnit.SECONDS,
						new LinkedBlockingQueue<Runnable>(10),
						Executors.defaultThreadFactory(), new AbortPolicy());
			}
			mPool.execute(runnable);
		}
		/**
		 * �Ƴ�����
		 * @param runnable
		 */
		public synchronized void cancel(Runnable runnable){
			if(runnable==null){
				return;
			}
			if(mPool!=null&& (!mPool.isShutdown()||mPool.isTerminated())){
				mPool.getQueue().remove(runnable);
			}
			
		}
	}
}
