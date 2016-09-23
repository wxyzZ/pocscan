/**
 * 
 */
package scan;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.net.util.SubnetUtils;

import attack.ExpLoad;
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
		new MainApp().start("test", "127.0.0.0/16", 80);
		System.out.println();
	}

	public void start(String pluginName, String ip, int port) {
		String startip;
		String endip;
		if (ip.contains("-")) {
			startip = ip.split("-")[0];
			endip = ip.split("-")[1];
		} else if (ip.contains("/")) {
			SubnetUtils utils = new SubnetUtils(ip);
			startip = utils.getInfo().getLowAddress();
			endip = utils.getInfo().getHighAddress();
		} else {
			startip = ip;
			endip = ip;
		}
		long start = Ip2Tools.ipToLong(startip);
		long end = Ip2Tools.ipToLong(endip);
		System.out.println(new Date());
//		for (int i = 0; i <= end - start; i++) {
			new ExpLoad().plugInLoad(pluginName, "www.baidu.com", port);
//		}
		System.out.println(Runtime.getRuntime().availableProcessors());
		System.out.println(new Date());
	}
}
