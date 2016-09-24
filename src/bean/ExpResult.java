/**
 * 
 */
package bean;

import java.util.Map;

/**
 * @author wxy
 *
 */
public class ExpResult {
	private Map<String,String> headers;
	private byte[] entity;
	private int statusCode=0;

	public ExpResult(){}
	
	public Map<String,String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String,String> headers) {
		this.headers = headers;
	}

	public byte[] getEntity() {
		return entity;
	}

	public void setEntity(byte[] entity) {
		this.entity = entity;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public ExpResult(Map<String,String> headers, byte[] entity, int statusCode) {
		super();
		this.headers = headers;
		this.entity = entity;
		this.statusCode = statusCode;
	}

}
