package sy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HostParams;
import org.apache.http.client.params.ClientPNames;

public class HttpUtil {
	public static String Get(String url)
	{
		HttpClient httpClient  = new HttpClient();
//		HostConfiguration hostConfiguration = new HostConfiguration();
//		hostConfiguration.setProxy("proxy.statestr.com", 80);
//		HostParams hostparams = new HostParams();
//		hostparams.setParameter(ClientPNames.COOKIE_POLICY,CookiePolicy.BROWSER_COMPATIBILITY);
//		hostConfiguration.setParams(hostparams);
//		httpClient.setHostConfiguration(hostConfiguration);
		String response = null;

		HttpMethod head = new GetMethod(url);
		//head.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "GBK");  
		 int statusCode = 0;
	        // execute the method and handle any error responses.
			try {
				statusCode = httpClient.executeMethod(head);
				InputStream inputStm = head.getResponseBodyAsStream();	
				BufferedReader br = new BufferedReader(new InputStreamReader(inputStm,"utf-8"));  
				String tempbf;  
				StringBuffer html = new StringBuffer(100);  
				while ((tempbf = br.readLine()) != null) {  
				    html.append(tempbf +"\n");  
				}  
				response = html.toString();
			} catch (HttpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				head.releaseConnection();
			}
	        // Retrieve all the headers.
	        
		return response;
	}
}

