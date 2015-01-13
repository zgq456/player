import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import sy.HttpUtil;

public class ParserUtil {
	@Before
	public void init() {
		// System.setProperty("webdriver.firefox.bin",
		// "C:/soft/Mozilla Firefox/firefox.exe");

		MyFilter.isLocal = true;
	}

	long time1 = 0;
	WebDriver driver = null;
	boolean isDone = false;

	@Test
	public void translateTODO() throws Exception {
		do {
			try {
				time1 = System.currentTimeMillis();
				// driver = new FirefoxDriver();
				driver = new HtmlUnitDriver();
				// WebDriver driver = new HtmlUnitDriver();
				// ((HtmlUnitDriver)driver).setProxy("proxy.statestr.com", 80);
				List<WordBean> infoBeanList = (List<WordBean>) (DBUtil
						.getWordDicList(0, Integer.MAX_VALUE, "", "", "true",
								"comment", "eq", "TODO", false)[0]);
				if (infoBeanList.isEmpty()) {
					isDone = true;
				}
				for (int i = 0; i < infoBeanList.size(); i++) {
					time1 = System.currentTimeMillis();
					WordBean wordBean = infoBeanList.get(i);
					String word = wordBean.getContent();
					System.out.println("===word:" + word + " currDate:"
							+ new Date());
					driver.get("http://dict.cn/" + word);
					String pageSource = driver.getPageSource();
					// System.out.println(pageSource);
					String comment = "";
					if (pageSource.contains("您要查找的是不是")) {
						comment = "UNKNOWN";
						wordBean.setRank(-1);
					} else {
						WebElement div = null;
						try {
							div = driver.findElement(By
									.xpath("//div[@class='basic clearfix']"));
							comment = div.getText();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							comment = "TODO2";
						}
					}
					System.out.println("comment:" + comment);
					wordBean.setComment(comment);
					DBUtil.updateInfo(wordBean);

				}
				isDone = true;
			} catch (Exception e) {
				e.printStackTrace();
			}

		} while (!isDone);

		new Thread() {
			public void run() {
				while (!isDone) {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (System.currentTimeMillis() - time1 >= 90 * 1000) {
						System.out.println("###longer than 90s");
						time1 = System.currentTimeMillis();
						driver.quit();
						time1 = System.currentTimeMillis();
					}

				}

				if (driver != null) {
					driver.quit();
				}

			}
		}.start();

		// String word = "cat";
	}

	@Test
	public void parseSentencesTest() throws Exception {
		parseSen("h2database.txt");
	}

	private void parseSen(String filename) throws IOException {
		File file = new File("lib/" + filename);
		String content = FileUtils.readFileToString(file);
		content = content.replaceAll("-(\r)?\n", "");
		content = content.replaceAll("(\r)?\n", " ");
		// System.out.println("content:" + content);
		String[] arr = content.split("\\.|\\?|!");
		for (int i = 0; i < arr.length; i++) {
			String oneSen = StringUtils.trim(arr[i]);
			System.out.println("line" + i + ":" + oneSen);

			if (StringUtils.isBlank(oneSen)
					|| StringUtils.split(oneSen, "\\s+").length == 1) {
				continue;
			}
			SentenBean sentenBean = new SentenBean(0, oneSen.toLowerCase(), 1);
			DBUtil.saveInfo(sentenBean);
		}
	}

	@Test
	public void parseWordsTest() throws Exception {
		String fileName = "h2database.txt";
		parseWords(fileName);
	}

	private void parseWords(String fileName) throws IOException {
		File file = new File("lib/" + fileName);
		Map<String, WordBean> wordDicMap = DBUtil.getWordDicMap();
		String content = FileUtils.readFileToString(file);
		content = content.replaceAll("-(\r)?\n", ""); // hap-pen --> happen
		FileUtils.writeStringToFile(file, content);
		List<String> lineList = FileUtils.readLines(file);
		int size = lineList.size();
		for (int i = 0; i < size; i++) {
			System.out.println("line:" + (i + 1));
			String oneLine = lineList.get(i);
			String[] oneLineArr = StringUtils.split(oneLine, ' ');
			// System.out.println(Arrays.asList(oneLineArr));
			for (int j = 0; j < oneLineArr.length; j++) {
				String oneWord = StringUtils.trim(oneLineArr[j]);
				oneWord = oneWord.replace(",", "").replace(":", "")
						.replace(".", "").replace("(", "").replace(")", "");
				if (StringUtils.isEmpty(oneWord) || oneWord.length() == 1
						|| oneWord.startsWith("www")
						|| !oneWord.matches("^[a-zA-Z\\-]*")) {
					System.out.println("##ignore:" + oneWord);
					continue;
				}
				System.out.println("get:" + oneWord);
				String lowerCaseWord = oneWord.toLowerCase();
				if (wordDicMap.containsKey(lowerCaseWord)) {
					WordBean oneWordBean = wordDicMap.get(lowerCaseWord);
					oneWordBean.setHit(oneWordBean.getHit() + 1);
					DBUtil.updateInfo(oneWordBean);
				} else {
					WordBean oneWordBean = new WordBean(0, oneWord, "TODO", 5,
							1);
					wordDicMap.put(oneWord.toLowerCase(), oneWordBean);
					DBUtil.saveInfo(oneWordBean);
				}
			}
		}
	}

	@Test
	public void parseUrl() throws Exception {
		String[][] urlArr = {
				// {"h2database", "http://www.h2database.com/html/main.html"},
				// {"cssheader",
				// "http://www.imaputz.com/cssStuff/bigFourVersion.html"} ,
				// {"cslibrary", "http://cslibrary.stanford.edu/112/"} ,
//				{ "", "http://www.filamentgroup.com/dwpe/#codeexamples" },
//				{ "", "https://github.com/wayou/selected/" },
//				{ "", "http://www.filamentgroup.com/dwpe/#codeexamples" },
//				{ "", "http://zetcode.com/tutorials/javagamestutorial/tetris/#" },
//				{ "", "http://www.kosbie.net/cmu/fall-08/15-100/handouts/notes-tetris/2_1_DesignOverview.html" },
//				{ "", "http://html5.svnlabs.com/karaoke-html5-mp3-player-with-lyrics/" },
//				{ "", "http://www.scratchinginfo.net/top-10-best-html5-audio-players/" },
//				{ "", "http://amazingaudioplayer.com/examples/" },
//				{ "", "http://www.instantshift.com/2012/06/08/15-free-html5-audio-players-for-your-website-and-blogs/" },
//				{ "", "http://www.codebasehero.com/2011/07/html5-music-player-updated/" },
//				{ "", "http://servicemix.apache.org/docs/5.0.x/user/what-is-smx4.html" },
//				{ "", "http://michael.laffargue.fr/blog/2013/11/15/play-framework-2-0-error-connection-timed-out/" },
//				{ "", "http://stackoverflow.com/questions/8834806/m2eclipse-error" },
//				{ "", "http://stackoverflow.com/questions/6665245/deploy-webapp-from-eclipse-to-tomcat-root-context" },
//				{ "", "http://www.mkyong.com/spring3/spring-3-mvc-hello-world-example/" },
//				{ "", "http://www.mkyong.com/spring3/spring-3-javaconfig-import-example/" },
//				{ "", "http://www.mkyong.com/spring3/spring-3-javaconfig-import-example/" },
//				{ "", "http://stackoverflow.com/questions/15947523/apache-camel-enrich-message-with-file-content-on-request" },
//				{ "", "http://www.lmi.net/support/using-a-publicprivate-key-pair-with-sshsftp" },
//				{ "", "http://stackoverflow.com/questions/12731649/perforce-error-cannot-submit-from-non-stream-client" },
				{ "", "https://docs.oracle.com/javase/tutorial/java/annotations/" },
				{ "", "http://localhost:8080/player/raw/raw.txt" },
				{ "", "" },
				{ "", "" },
				{ "", "" },
				{ "", "" },
				};
		for (int i = 0; i < urlArr.length; i++) {
			String filename = urlArr[i][0];
			String url = urlArr[i][1];
			if(StringUtils.isEmpty(url)) {
				continue;
			}

			if (StringUtils.isEmpty(filename)) {
				filename = url.replace(":", "").replace(".", "_")
						.replace("/", "_").replace("\\", "").replace("*", "_")
						.replace("?", "_").replace("<", "_").replace(">", "_")
						.replace("|", "_");
			}

			filename += ".txt";
			
			System.out.println("###process:" + filename);

			String returnBody = HttpUtil.Get(url);
			returnBody = Jsoup.parse(returnBody).text();
			System.out.println("returnBody:" + returnBody);

			FileUtils
					.writeStringToFile(new File("lib/" + filename), url + "\r\n\r\n" +  returnBody);

			parseWords(filename);

			parseSen(filename);
		}

	}

}
