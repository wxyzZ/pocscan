/**
 * 
 */
package bean;

import org.apache.http.Header;

/**
 * @author wxy
 *
 */
public class ExpResult2 {
	private Header[] headers;
	private byte[] entity;
	private int statusCode;

	public Header[] getHeaders() {
		return headers;
	}

	public void setHeaders(Header[] headers) {
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

	public ExpResult2(Header[] headers, byte[] entity, int statusCode) {
		super();
		this.headers = headers;
		this.entity = entity;
		this.statusCode = statusCode;
	}

}
