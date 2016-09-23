/**
 * 
 */
package attack;

import java.io.File;
import java.io.IOException;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

/**
 * @author wxy
 *
 */
public class ExpLoad implements Runnable {
	private String pluginName;
	private String host;
	private int port;
	
	
	public ExpLoad(String pluginName, String host, int port) {
		super();
		this.pluginName = pluginName;
		this.host = host;
		this.port = port;
	}

	public void run() {
		plugInLoad(pluginName, host, port);
	}

	private void plugInLoad(String pluginName, String host, int port) {
		String path = String.valueOf(System.getProperty("user.dir")) + File.separator + "plugIn" + File.separator
				+ "Exploit" + File.separator;
		GroovyScriptEngine engine = null;
		try {
			engine = new GroovyScriptEngine(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Binding binding = new Binding();
//		System.out.println("Options:\thost:" + host + "\tport:" + port);
		binding.setVariable("host", host);
		binding.setVariable("port", port);
		try {
			engine.run(pluginName + ".groovy", binding);
		} catch (ResourceException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}
}
