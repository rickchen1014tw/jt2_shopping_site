package com.jt.config;

import javax.annotation.PreDestroy;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//現在HttpClient是設成多例的，用完需要等jvm的gc來回收，時間會等比較長，高併發的情況下，等待的請求可能會很多
//這裡使用一個異步的方式來關閉用完的連接
@Component	//交给spring容器管理
public class HttpClientClose extends Thread{
	@Autowired
	private PoolingHttpClientConnectionManager manage;
	private volatile boolean shutdown;	//开关 volatitle表示多线程可变数据,一个线程修改,其他线程立即修改
	//volatile 不允許執行緒快取 變數值的存取一定是在共享記憶體中 取有執行緒變動了變數值，其它執行緒一定可以看到變更
	                                    //shutdown共享變量
	public HttpClientClose() {
		///System.out.println("执行构造方法,实例化对象");
		//线程开启启动
		this.start();	//這個類會交給Spring管理，Spring實例化這個對象，就會執行構造方法，就會執行run方法
	}
	
	
	@Override
	public void run() {
		try {
			//如果服务没有关闭,执行线程
			while(!shutdown) {
				synchronized (this) {
					wait(5000);			//等待5秒 每5秒把多餘的連接給關掉
					//System.out.println("线程开始执行,关闭超时链接");
					//关闭超时的链接
					PoolStats stats = manage.getTotalStats();
					int av = stats.getAvailable();	//获取可用的线程数量
					int pend = stats.getPending();	//获取阻塞的线程数量
					int lea = stats.getLeased();    //获取当前正在使用的链接数量
					int max = stats.getMax();
					//System.out.println("max/"+max+":	av/"+av+":  pend/"+pend+":   lea/"+lea);
					manage.closeExpiredConnections();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		super.run();
	}

	//关闭清理无效连接的线程
	@PreDestroy	//容器关闭时执行该方法.
	public void shutdown() {
		shutdown = true;
		synchronized (this) {
			//System.out.println("关闭全部链接!!");
			notifyAll(); //全部从等待中唤醒.执行关闭操作;
		}
	}
}
