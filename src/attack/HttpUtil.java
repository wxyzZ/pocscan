/**
 * 
 */
package attack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import bean.Result;

/**
 * @author wxy
 *
 */
/**
 * httputil工具类
 * 
 * @author rex
 */
public class HttpUtil {

    private static CloseableHttpClient client;

    private static BasicCookieStore cookieStore;

    private static HttpGet get;

    private static HttpPost post;

    private static HttpResponse response;

    private static Result result;

    private static HttpEntity entity;

    /**
     * //TODO get请求
     * 
     * @param url 请求地址
     * @param headers 请求头
     * @param params 请求参数
     * @param encoding 请求编码
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static Result get(String url, Map<String, String> headers, Map<String, String> params) throws ClientProtocolException, IOException {

        cookieStore = new BasicCookieStore();
        client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

        url = (null == params ? url : url + "?" + parseParam(params));

        get = new HttpGet(url);

        get.setHeaders(parseHeader(headers));

        response = client.execute(get);
        entity = response.getEntity();

        result = new Result();

        result.setHttpClient(client);

        result.setCookies(cookieStore.getCookies());

        result.setStatusCode(response.getStatusLine().getStatusCode());

        result.setHeaders(response.getAllHeaders());

        result.setHttpEntity(entity);

        result.setBody(EntityUtils.toString(entity));
        return result;
    }

    /**
     * //TODO post请求
     * 
     * @param url 请求地址
     * @param headers 请求头
     * @param params 请求参数
     * @param encoding 请求编码
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static Result post(String url, Map<String, String> headers, Map<String, String> params, String encoding) throws ClientProtocolException, IOException {

        cookieStore = new BasicCookieStore();
        client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

        post = new HttpPost(url);

        List<NameValuePair> list = new ArrayList<NameValuePair>();
        for (String temp : params.keySet()) {
            list.add(new BasicNameValuePair(temp, params.get(temp)));
        }
        post.setEntity(new UrlEncodedFormEntity(list, encoding));

        post.setHeaders(parseHeader(headers));

        response = client.execute(post);
        entity = response.getEntity();

        result = new Result();

        result.setHttpClient(client);

        result.setCookies(cookieStore.getCookies());

        result.setStatusCode(response.getStatusLine().getStatusCode());

        result.setHeaders(response.getAllHeaders());

        result.setHttpEntity(entity);

        result.setBody(EntityUtils.toString(entity));
        return result;
    }

    /**
     * //TODO 转换header
     * 
     * @param headers
     * @return
     */
    private static Header[] parseHeader(Map<String, String> headers) {
        if (null == headers || headers.isEmpty()) {
            return getDefaultHeaders();
        }
        Header[] allHeader = new BasicHeader[headers.size()];
        int i = 0;
        for (String str : headers.keySet()) {
            allHeader[i] = new BasicHeader(str, headers.get(str));
            i++;
        }
        return allHeader;
    }

    /**
     * //TODO 默认header
     * 
     * @return
     */
    private static Header[] getDefaultHeaders() {
        Header[] allHeader = new BasicHeader[2];
        allHeader[0] = new BasicHeader("Content-Type", "application/x-www-form-urlencoded");
        allHeader[1] = new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
        return allHeader;
    }

    /**
     * //TODO 转换参数列表
     * 
     * @param params
     * @return
     */
    private static String parseParam(Map<String, String> params) {
        if (null == params || params.isEmpty()) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (String key : params.keySet()) {
            sb.append(key + "=" + params.get(key) + "&");
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 释放httpclient对象
     */
    public static void closeClient(CloseableHttpClient client) {
        if (null != client) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}