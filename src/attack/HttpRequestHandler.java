/**
 * 
 */
package attack;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import bean.ExpResult;
import bean.ExpResult2;

/**
 * @author wxy
 *
 */
public class HttpRequestHandler {

	private String ua;
	private CloseableHttpClient httpClient;
	private CloseableHttpResponse response;

	public static HttpRequestHandler getInstance() {
		return new HttpRequestHandler("poc_scan");
	}

	public static HttpRequestHandler getInstance(String ua) {
		return new HttpRequestHandler(ua);
	}

	private HttpRequestHandler(String ua) {
		this.ua = ua;
	}

	public ExpResult doRequest(String url, Method method, Map<String, String> headers, String... data) {
		Connection conn = Jsoup.connect(url).validateTLSCertificates(false).ignoreContentType(true);
		conn.method(method);
		conn.userAgent(ua);
		if (headers != null) {
			Set<String> keys = headers.keySet();
			for (String key : keys) {
				conn.header(key, headers.get(key));
			}
		}
		Response b = null;
		try {
			if (data != null)
				b = conn.data(data).execute();
			else
				b = conn.execute();
		} catch (IOException e) {
			return new ExpResult();
		}
		return new ExpResult(b.headers(), b.bodyAsBytes(), b.statusCode());
	}

	private ExpResult2 doRequest2(String url, Method method, AbstractHttpEntity entity) {
		SSLContext sslcontext = null;
		try {
			sslcontext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			e.printStackTrace();
		}
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, NoopHostnameVerifier.INSTANCE);
		this.httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).setUserAgent(this.ua).build();
		try {
			switch (method) {
			case GET:
				response = httpClient.execute(createHttpGet(url));
				break;
			case POST:
				response = httpClient.execute(createHttpPost(url, entity));
				break;
			// case DELETE:
			// response=httpClient.execute(createHttpGet());
			// break;
			// case HEAD:
			// response=httpClient.execute(createHttpGet());
			// break;
			// case OPTIONS:
			// response=httpClient.execute(createHttpGet());
			// break;
			// case PUT:
			// response=httpClient.execute(createHttpGet());
			// break;
			// case TRACE:
			// response=httpClient.execute(createHttpGet());
			// break;
			default:
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		try {
			return new ExpResult2(response.getAllHeaders(), EntityUtils.toByteArray(response.getEntity()),
					response.getStatusLine().getStatusCode());
		} catch (IOException e) {
			return null;
		} finally {
			try {
				this.response.close();
			} catch (IOException e) {
			}
		}
	}

	private HttpGet createHttpGet(String url) {
		return new HttpGet(url);
	}

	private HttpPost createHttpPost(String url, AbstractHttpEntity data) {
		HttpPost post = new HttpPost(url);
		post.setEntity(data);
		return post;
	}

	private void closeClient() {
		try {
			this.httpClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
