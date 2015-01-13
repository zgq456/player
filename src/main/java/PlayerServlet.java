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
@WebServlet(urlPatterns = "/play.do", asyncSupported = true)
@MultipartConfig
public class PlayerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String SPLIT_TOKEN = "|||";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PlayerServlet() {
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
		System.out.println("###doPost### date:" + new Date() + " remoteAddr:"
				+ remoteAddr);
		String action = request.getParameter("action");
		String name = request.getParameter("name");
		String startStr = request.getParameter("start");
		String endStr = request.getParameter("end");
		String textStr = request.getParameter("text");
		String lastUptStr = request.getParameter("lastUpt");
		if (StringUtils.isEmpty(textStr)) {
			textStr = "  ";
		}

		textStr = StringUtils.replace(textStr, "\r", " ");
		textStr = StringUtils.replace(textStr, "\n", " ");

		name = getLrcNameFromSrc(name);

		System.out.println("action:" + action + " startStr:" + startStr
				+ " endStr:" + endStr + " textStr:" + textStr);
		System.out.println("name:" + name);

		File lrcDir = new File("lrcDir");
		if (!lrcDir.exists()) {
			lrcDir.mkdirs();
		}
		File lrcFile = new File(lrcDir, name);
		File externalAudioList = new File(lrcDir, "externalAudio.txt");
		System.out.println("##externalAudioList##:"
				+ externalAudioList.getAbsolutePath());
		// external files
		if (!externalAudioList.exists()) {
			externalAudioList.createNewFile();
		}

		if ("add".equals(action)) {
			String msg = "0";
			int start = getSecondsInt(startStr);
			int end = getSecondsInt(endStr);

			List<String> lineList = new ArrayList<String>();
			if (lrcFile.exists()) {
				lineList = FileUtils.readLines(lrcFile, "UTF-8");
			}

			System.out.println("###lrcDir:" + lrcDir.getAbsolutePath());

			StringBuilder sb = new StringBuilder();
			String lastestUptStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date());
			String lineInfo = start + SPLIT_TOKEN + end + SPLIT_TOKEN + textStr
					+ SPLIT_TOKEN + lastestUptStr + SPLIT_TOKEN + remoteAddr;
			boolean matched = false;
			for (int i = 0; i < lineList.size(); i++) {
				String oneLine = lineList.get(i);
				if (StringUtils.trim(oneLine).isEmpty()) {
					continue;
				}
				String[] fieldsArr = StringUtils.split(oneLine, SPLIT_TOKEN);
				// update data
				if (fieldsArr[0].equals(start + "")) {
					if (!oneLine.contains(SPLIT_TOKEN + lastUptStr)) {
						msg = "-1";
					}
					oneLine = lineInfo;
					matched = true;
				}

				// insert data
				if (!matched && Integer.parseInt(fieldsArr[0]) > start) {
					matched = true;
					sb.append(lineInfo + "\r\n");
				}
				sb.append(oneLine + "\r\n");
			}

			if (lineList.size() == 0 || !matched) {
				sb.append(lineInfo + "\r\n");
			}

			if ("0".equals(msg)) {
				FileUtils.writeStringToFile(lrcFile, sb.toString(), "UTF-8");
				FileUtils.writeStringToFile(new File(lrcDir, "historyLog_"
						+ name), lineInfo + "\r\n", "UTF-8", true);
			}

			response.setContentType("application/json");
			Gson gson = new Gson();
			response.getWriter().print(
					gson.toJson(new String[] { msg, lastestUptStr }));
			response.getWriter().close();
		} else if ("getLrcList".equals(action)) {
			List<String> lineList = new ArrayList<String>();
			List<LrcBean> lrcBeanList = new ArrayList<LrcBean>();
			if (lrcFile.exists() && lrcFile.isFile()) {
				lineList = FileUtils.readLines(lrcFile, "UTF-8");
			}

			for (int i = 0; i < lineList.size(); i++) {
				String oneLine = lineList.get(i);
				if (StringUtils.trim(oneLine).isEmpty()) {
					continue;
				}
				String[] fieldsArr = StringUtils.split(oneLine, SPLIT_TOKEN);
				LrcBean lrcBean = new LrcBean(fieldsArr[0], fieldsArr[1],
						fieldsArr.length <= 2 ? ""
								: (StringUtils.isBlank(fieldsArr[2]) ? ""
										: fieldsArr[2]), fieldsArr[3]);
				lrcBeanList.add(lrcBean);
			}

			response.setContentType("application/json");
			Gson gson = new Gson();
			response.getWriter().print(gson.toJson(lrcBeanList));
			response.getWriter().close();
		} else if ("downloadLrc".equals(action)) {
			if (!lrcFile.exists()) {
				FileUtils.writeStringToFile(lrcFile, "", "UTF-8");
			}

			response.setHeader("Content-disposition", "attachment; filename="
					+ lrcFile.getName());

			BufferedInputStream buffInput = new BufferedInputStream(
					new FileInputStream(lrcFile.getAbsolutePath()));
			BufferedOutputStream buffout = new BufferedOutputStream(
					response.getOutputStream());
			int length = -1;
			byte[] buff = new byte[1024];
			while ((length = buffInput.read(buff)) != -1) {
				buffout.write(buff, 0, length);
			}
			buffout.flush();
			buffInput.close();
			buffout.close();
		} else if ("getAudioList".equals(action)) {
			List<AudioBean> audioBeanList = new ArrayList<AudioBean>();
			String rootPath = getServletConfig().getServletContext()
					.getRealPath("/");
			// internal files
			File audioDir = new File(MyFilter.audioDirStr);
			for (int i = 0; i < audioDir.list().length; i++) {
				audioBeanList
						.add(new AudioBean(audioDir.list()[i], null, false));
			}

			List<String> audioLines = FileUtils.readLines(externalAudioList,
					"UTF-8");
			for (int i = 0; i < audioLines.size(); i++) {
				String oneAudioInfo = audioLines.get(i);
				if (StringUtils.isEmpty(oneAudioInfo)) {
					continue;
				}
				String[] oneAudioInfoArr = StringUtils.split(oneAudioInfo,
						SPLIT_TOKEN);
				audioBeanList.add(new AudioBean(oneAudioInfoArr[0],
						oneAudioInfoArr[1], true));
			}

			response.setContentType("application/json");
			Gson gson = new Gson();
			response.getWriter().print(gson.toJson(audioBeanList));
			response.getWriter().close();
		} else if ("fetchRawMsg".equals(action)) {
			String fileContent = "N/A";
			if (lrcFile.exists()) {
				fileContent = FileUtils.readFileToString(lrcFile, "UTF-8");
			}

			response.getWriter().print(fileContent);
			response.getWriter().close();

		} else if ("addExtFile".equals(action)) {
			String filename = request.getParameter("filename");
			String fileurl = request.getParameter("fileurl");
			FileUtils.writeStringToFile(externalAudioList, "\r\n" + filename
					+ SPLIT_TOKEN + fileurl, "UTF-8", true);

			response.getWriter().print(1);
			response.getWriter().close();

		} else if ("getWordDicList".equals(action)) {
			int currPage = Integer.parseInt(request.getParameter("page"));
			int rows = Integer.parseInt(request.getParameter("rows"));

			String _search = request.getParameter("_search");
			String searchField = request.getParameter("searchField");
			String searchOper = request.getParameter("searchOper");
			String searchString = request.getParameter("searchString");
			boolean ignoreEasy = "true".equals(request
					.getParameter("ignoreEasy"));

			String sidx = request.getParameter("sidx");
			String sord = request.getParameter("sord");
			Object[] objArr = DBUtil.getWordDicList((currPage - 1) * rows,
					rows, sidx, sord, _search, searchField, searchOper,
					searchString, ignoreEasy);
			List<WordBean> wordBeanList = (List<WordBean>) objArr[0];
			int totalCount = (Integer) objArr[1];

			response.setContentType("application/json");
			// Gson gson = new Gson();
			// response.getWriter().print(gson.toJson(wordBeanList));

			int totalNumberOfRecords = totalCount;
			int totalNumberOfPages = (totalCount % rows == 0 ? (totalCount / rows)
					: (totalCount / rows + 1));

			JqGridData<WordBean> gridData = new JqGridData<WordBean>(
					totalNumberOfPages, currPage, totalNumberOfRecords,
					wordBeanList);
			System.out.println("Grid Data: " + gridData.getJsonString());
			response.getWriter().print(gridData.getJsonString());

			response.getWriter().close();
		} else if ("getSimilarList".equals(action)) {
			int id = Integer.parseInt(request.getParameter("id"));
			int currPage = 1;
			List<WordBean> infoBeanList = (List<WordBean>) (DBUtil
					.getWordDicList(0, Integer.MAX_VALUE, "", "", "false",
							"rank", "gt", "0", false)[0]);
			List<CompareBean> compareBeanList = new ArrayList<CompareBean>();

			WordBean sourceWordBean = DBUtil.getWordBean(id);
			// String s1 = "integrations";
			String s1 = sourceWordBean.getContent();
			s1 = " " + s1;
			s1 = s1.toLowerCase();
			for (int j = 0; j < infoBeanList.size(); j++) {
				WordBean wordBean = infoBeanList.get(j);
				String s2 = " " + wordBean.getContent();
				s2 = s2.toLowerCase();
				if (s1.equals(s2) || s2.trim().length() <= 3) {
					continue;
				}
				if (wordBean.getRank() < 0 && wordBean.getRank() != -10) {
					continue;
				}

				char[] x = s1.toCharArray();
				char[] y = s2.toCharArray();

				int[][] b = LCSProblem.getLength(x, y);
				StringBuffer common = new StringBuffer();
				LCSProblem.Display(b, x, x.length - 1, y.length - 1, common);

				if (common.length() >= Math.min(s1.length(), s2.length()) - 1 - 2) {
					compareBeanList.add(new CompareBean(s1, s2, common
							.toString(), wordBean.getId()));
					// System.out.println("s1:" + s1 + " s2:" + s2 + " common:"
					// + common);
				}
			}

			Collections.sort(compareBeanList);

			System.out.println("s1:" + s1);
			System.out.println("compareBeanList:" + compareBeanList);

			List<WordBean> wordBeanList = new ArrayList<WordBean>();
			for (int i = 0; i < compareBeanList.size(); i++) {
				WordBean oneWordBean = DBUtil.getWordBean(compareBeanList
						.get(i).getS2id());
				wordBeanList.add(oneWordBean);
			}

			int totalCount = wordBeanList.size();

			response.setContentType("application/json");
			// Gson gson = new Gson();
			// response.getWriter().print(gson.toJson(wordBeanList));

			int totalNumberOfRecords = totalCount;
			int totalNumberOfPages = 1;

			JqGridData<WordBean> gridData = new JqGridData<WordBean>(
					totalNumberOfPages, currPage, totalNumberOfRecords,
					wordBeanList);
			System.out.println("Grid Data For Similar: "
					+ gridData.getJsonString());
			response.getWriter().print(gridData.getJsonString());

			response.getWriter().close();
		} else if ("getSenList".equals(action)) {
			String word = request.getParameter("word");

			List<SentenBean> sentenBean = DBUtil.getSenList(word);
			int totalCount = sentenBean.size();
			int rows = totalCount;
			int currPage = 1;

			response.setContentType("application/json");
			// Gson gson = new Gson();
			// response.getWriter().print(gson.toJson(wordBeanList));

			int totalNumberOfRecords = totalCount;
			int totalNumberOfPages = 1;

			JqGridData<SentenBean> gridData = new JqGridData<SentenBean>(
					totalNumberOfPages, currPage, totalNumberOfRecords,
					sentenBean);
			System.out.println("Sentences Grid Data: "
					+ gridData.getJsonString());
			response.getWriter().print(gridData.getJsonString());

			response.getWriter().close();
		} else if ("addOrUpdateWord".equals(action)) {
			String id = request.getParameter("id");
			String comment = request.getParameter("comment");
			String content = request.getParameter("content");
			String hit = request.getParameter("hit");
			String oper = request.getParameter("oper");// edit
			String rank = request.getParameter("rank");

			if ("edit".equals(oper)) {
				WordBean wordBean = DBUtil.getWordBean(Integer.parseInt(id));
				wordBean.setComment(comment);
				wordBean.setContent(content);
				wordBean.setHit(Integer.parseInt(hit));
				wordBean.setRank(Integer.parseInt(rank));
				DBUtil.updateInfo(wordBean);
			} else if ("add".equals(oper)) {
				WordBean wordBean = new WordBean();
				wordBean.setComment(comment);
				wordBean.setContent(content);
				wordBean.setHit(Integer.parseInt(hit));
				wordBean.setRank(Integer.parseInt(rank));
				DBUtil.saveInfo(wordBean);
			} else if ("del".equals(oper)) {
				DBUtil.delInfo(id);
			}
			response.getWriter().print("");
			response.getWriter().close();
		} else if ("deleteWord".equals(action)) {

		} 

		/*
		 * else if ("saveRawMsg".equals(action)) { String fileContent =
		 * request.getParameter("content"); FileUtils.writeStringToFile(lrcFile,
		 * fileContent);
		 * 
		 * response.getWriter().print("save successfully");
		 * response.getWriter().close(); }
		 */
	}

}
