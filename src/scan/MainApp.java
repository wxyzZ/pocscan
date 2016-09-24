/**
 * 
 */
package scan;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.net.util.SubnetUtils;

import attack.ExpLoad;
import groovy.lang.GroovyObject;
import groovy.util.GroovyScriptEngine;
import util.Ip2Tools;

/**
 * @author wxy
 *
 */
public class MainApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new MainApp().start("test", "127.0.0.1/24", 80);
	}

	/**
	 * 扫描一个b段用时大约25分钟，使用内存150m左右，线程150
	 * @param pluginName
	 * @param ip
	 * @param port
	 */
	public void start(String pluginName, String ip, int port) {
		String startip;
		String endip;
		if (ip.contains("-")) {
			startip = ip.split("-")[0];
			endip = ip.split("-")[1];
		} else if (ip.contains("/")) {
			SubnetUtils utils = new SubnetUtils(ip);
			utils.setInclusiveHostCount(true);
			startip = utils.getInfo().getLowAddress();
			endip = utils.getInfo().getHighAddress();
		} else {
			startip = ip;
			endip = ip;
		}
//		BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(100);
//		ExecutorService threadPool = new ThreadPoolExecutor(100, 120, 3, TimeUnit.SECONDS, queue,
//				new ThreadPoolExecutor.CallerRunsPolicy());
		ExecutorService threadPool = Executors.newCachedThreadPool(); 
		
		
		
		long start = Ip2Tools.ipToLong(startip);
		long end = Ip2Tools.ipToLong(endip);
		GroovyObject groovyObject = getGroovyObject(pluginName);
		for (long i = 0L; i <= end - start; i++) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Runnable exp = new ExpLoad(groovyObject, Ip2Tools.longToIP(start + i), 80);
			threadPool.execute(exp);
//			if (queue.size() == 100) {
//				try {
//					queue.put(exp);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
		}
		threadPool.shutdown();
	}

	private GroovyObject getGroovyObject(String pluginName) {
		String path = String.valueOf(System.getProperty("user.dir")) + File.separator + "plugIn" + File.separator
				+ "Exploit" + File.separator;
		GroovyScriptEngine engine = null;
		try {
			engine = new GroovyScriptEngine(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			return (GroovyObject) engine.loadScriptByName(pluginName + ".groovy").newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
