import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * Servlet implementation class PlayerServlet
 */
@WebServlet(urlPatterns = "/audio/*", asyncSupported = true)
@MultipartConfig
public class AudioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AudioServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("AudioServlet doGet......");
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected synchronized void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("audio/mp3");
		// System.out.println("###REAL_FILE_DIR:" + REAL_FILE_DIR);
		// System.out.println("###URI:" + request.getRequestURI());
		// System.out.println("###URL:" + request.getRequestURL());
		// System.out.println("###contextPath:" + request.getContextPath());
		String fileUrlPath = request.getRequestURI().replaceFirst(
				request.getContextPath(), "");
		fileUrlPath = fileUrlPath.replace("audio", "");
		String readFilePath = MyFilter.audioDirStr + File.separator
				+ fileUrlPath;
		File f = new File(readFilePath);
		System.out.println("###audio file:" + f.getAbsolutePath());
		OutputStream out = response.getOutputStream();
//		BufferedImage bi = ImageIO.read(f);
//		ImageIO.write(bi, "jpg", out);
		
		IOUtils.write(FileUtils.readFileToByteArray(f), out);
		
		out.close();
	}

}
