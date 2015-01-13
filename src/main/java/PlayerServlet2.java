import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import player.AudioBean;
import player.LrcBean;

import com.google.gson.Gson;

/**
 * Servlet implementation class PlayerServlet
 */
@WebServlet(urlPatterns = "/play2.do", asyncSupported = true)
@MultipartConfig
public class PlayerServlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PlayerServlet2() {
		super();
	}

	public String getLrcNameFromSrc(String srcName) {
		if (StringUtils.isEmpty(srcName)) {
			return "";
		}

		if (!srcName.contains(".")) {
			srcName += ".mp3";
		}

		return srcName.substring(srcName.lastIndexOf("/") + 1,
				srcName.lastIndexOf("."))
				+ ".txt";
	}

	public int getSecondsInt(String timeStr) {
		if (timeStr.length() == 5) {
			timeStr = "00:" + timeStr;
		}
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = null;
		try {
			date = dateFormat.parse(timeStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long seconds = date.getTime() / 1000L;
		return (int) seconds;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doGet......");
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected synchronized void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		String remoteAddr = request.getRemoteAddr();
		System.out.println("###doPost2### date:" + new Date() + " remoteAddr:"
				+ remoteAddr);

		Part filePart = request.getPart("myfile"); // Retrieves <input type="file"
													// name="file">
		InputStream is = filePart.getInputStream();
		String fileName = getFileName(filePart);
		System.out.println("fileName:" + fileName);
		
		Files.copy(is,
				new File(MyFilter.audioDirStr + "/"
						+ fileName).toPath());
		response.sendRedirect("test.jsp");
	}
	
	private static String getFileName(Part part) {
	    for (String cd : part.getHeader("content-disposition").split(";")) {
	        if (cd.trim().startsWith("filename")) {
	            String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
	            return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
	        }
	    }
	    return null;
	}

}
