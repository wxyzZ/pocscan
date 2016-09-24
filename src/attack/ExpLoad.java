/**
 * 
 */
package attack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import groovy.lang.Binding;
import groovy.lang.GroovyObject;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

/**
 * @author wxy
 *
 */
public class ExpLoad implements Runnable {
	private String host;
	private int port;
	private GroovyObject groovyObject;
	
	public ExpLoad(GroovyObject groovyObject,String host, int port) {
		super();
		this.groovyObject = groovyObject;
		this.host = host;
		this.port = port;
	}

	public void run() {
		plugInLoad(host, port);
	}

	private void plugInLoad(String host, int port) {
		Map<String, Object> param = new HashMap<>();
		param.put("host", host);
		param.put("port", port);
		groovyObject.invokeMethod("exp", param);
	}
	
	/**
	 * 通过run方法调用plugin方法比较耗内存，不建议使用
	 * @param pluginName
	 * @param host
	 * @param port
	 */
	@Deprecated
	private void plugInLoad(String pluginName,String host, int port) {
		String path = String.valueOf(System.getProperty("user.dir")) + File.separator + "plugIn" + File.separator
				+ "Exploit" + File.separator;
		Binding binding = new Binding();
		 System.out.println("Options:\thost:" + host + "\tport:" + port);
		binding.setVariable("host", host);
		binding.setVariable("port", port);
		try {
			new GroovyScriptEngine(path).run(pluginName + ".groovy", binding);
		} catch (ResourceException | ScriptException | IOException e) {
			e.printStackTrace();
		}
	}

}
