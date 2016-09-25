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
public class ScanTask {

	private ExecutorService threadPool;
	private GroovyObject groovyObject;
	private int port;
	private String scan_ips;

	/**
	 * 创建扫描任务
	 * @param pluginName 插件名称
	 * @param scan_ips	扫描范围,支持的格式有:<br>127.0.0.1-127.0.1.5,127.0.0.1/32,127.0.0.1,www.baidu.com<br>并且可以使用","分割各种格式
	 * @param port	扫描端口。默认情况是80端口使用http协议，443端口使用https协议。端口必须指定，如果不是80/443则需要指定上面的扫描ip是否那种协议，
	 * 比如：10.0.0.1的端口是8080，则在指定扫描ip时格式为：http://10.0.0.1:8080
	 */
	public ScanTask(String pluginName, String scan_ips, int port) {
		this.threadPool = Executors.newCachedThreadPool();
		this.groovyObject = getGroovyObject(pluginName);
		this.port = port;
		this.scan_ips = scan_ips;
	}

	/**
	 *启动扫描任务
	 */
	public void start() {
		String ips[] = scan_ips.split(",");
		for (String ip : ips) {
			if (ip.contains("-") || ip.contains("/")) {
				// 127.0.0.1-127.0.0.5
				startByIpRange(ip);
			} else {
				// 127.0.0.1 or www.baidu.com
				startBySingleIp(ip);
			}
		}
		threadPool.shutdown();
	}

	private void startByIpRange(String ip) {
		String startip;
		String endip;
		if (ip.contains("-")) {
			startip = ip.split("-")[0];
			endip = ip.split("-")[1];
		} else {
			SubnetUtils utils = new SubnetUtils(ip);
			utils.setInclusiveHostCount(true);
			startip = utils.getInfo().getLowAddress();
			endip = utils.getInfo().getHighAddress();
		}
		long start = Ip2Tools.ipToLong(startip);
		long end = Ip2Tools.ipToLong(endip);
		for (long i = 0L; i <= end - start; i++) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Runnable exp = new ExpLoad(groovyObject, Ip2Tools.longToIP(start + i), 80);
			threadPool.execute(exp);
		}
	}

	/**
	 * 扫描一个b段用时大约25分钟，使用内存150m左右，线程150
	 *
	 * @param pluginName
	 * @param ip
	 * @param port
	 */
	private void startBySingleIp(String ip) {

		Runnable exp = new ExpLoad(groovyObject, ip, this.port);
		threadPool.execute(exp);
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
