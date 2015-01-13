
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
@WebFilter(filterName="log",urlPatterns={"/*"})
public class MyFilter implements Filter {
	public static boolean isLocal = false;
	public static String audioDirStr = "F:/research/2014/fcm/player/src/main/webapp/audio";
	public void init(FilterConfig fConfig) throws ServletException {
		String os = System.getProperty("os.name");
		String osUser = System.getProperty("user.name");
		isLocal = !StringUtils.contains(os, "Linux");
		System.out.println("os:" + os + " osUser:" + osUser + " isLocal:" + isLocal);
	}
	
	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		System.out.println();
		System.out.println(new Date() + " remoteAddr: "
				+ request.getRemoteAddr());
		System.out.println(new Date() + " contextPath: "
				+ ((HttpServletRequest) request).getContextPath());
		

		Map map = request.getParameterMap();
		Set keSet = map.entrySet();
		for (Iterator itr = keSet.iterator(); itr.hasNext();) {
			Map.Entry me = (Map.Entry) itr.next();
			Object ok = me.getKey();
			Object ov = me.getValue();
			String[] value = new String[1];
			if (ov instanceof String[])
				value = (String[]) ov;
			else {
				value[0] = ov.toString();
			}

			for (int k = 0; k < value.length; ++k) {
				System.out.println(new Date() + " " + ok + "=" + value[k]);
			}

		}

		chain.doFilter(request, response);
	}



}