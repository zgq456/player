import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class DBUtil {
	static Connection conn = null;

	private static JdbcTemplate getJDBCTemplate() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			String hostname = null;
			String port = null;
			String user = null;
			String password = null;
			String dbName = null;

			if (MyFilter.isLocal) {
				hostname = "localhost";
				port = "3306";
				user = "root";
				password = "root";
				dbName = "english";

			} else {
				hostname = System.getenv("MOPAAS_MYSQL10846_HOST");
				port = System.getenv("MOPAAS_MYSQL10846_PORT");
				user = System.getenv("MOPAAS_MYSQL10846_USER");
				password = System.getenv("MOPAAS_MYSQL10846_PASSWORD");
				dbName = System.getenv("MOPAAS_MYSQL10846_NAME");

			}

			String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/"
					+ dbName + "?user=" + user + "&password=" + password
					+ "&characterEncoding=UTF8";
			if (conn == null) {
				conn = DriverManager.getConnection(jdbcUrl);
			}
			// System.out.println("jdbcUrl:" + jdbcUrl);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// System.out.println("conn:" + conn);
		JdbcTemplate jdbcTempalte = new JdbcTemplate(
				new SingleConnectionDataSource(conn, false));
		return jdbcTempalte;
	}

	// public static String queryInfo(String queryStr) {
	// StringBuilder sb = new StringBuilder();
	// JdbcTemplate jdbcTempalte = getJDBCTemplate();
	// System.out.println("jdbcTempalte:" + jdbcTempalte + " queryStr:"
	// + queryStr);
	// String queryStr2 = "%" + queryStr + "%";
	// List infoBeanList = jdbcTempalte
	// .query("select * from info where (title like ? or content like ? or keywords like ? ) and status =  "
	// + MyConstant.APPROVED,
	// new Object[] { queryStr2, queryStr2, queryStr2 },
	// new ItemMapper());
	// System.out.println(infoBeanList);
	// if (!(infoBeanList.isEmpty())) {
	// sb.append("搜索结果为:\n");
	// }
	// int infoBeanListSize = infoBeanList.size() >= 5 ? 5 :
	// infoBeanList.size();
	// for (int i = 0; i < infoBeanListSize; ++i) {
	// WordBean oneInfoBean = (WordBean) infoBeanList.get(i);
	// sb.append((i + 1) + ".标题:" + oneInfoBean.getTitle());
	// if (StringUtils.isNotEmpty(oneInfoBean.getContent())) {
	// sb.append("\n内容:" + oneInfoBean.getContent());
	// }
	// if (StringUtils.isNotEmpty(oneInfoBean.getContactor())) {
	// sb.append("\n联系人:" + oneInfoBean.getContactor());
	// }
	// if (StringUtils.isNotEmpty(oneInfoBean.getTel())) {
	// sb.append("\n联系电话:" + oneInfoBean.getTel());
	// }
	//
	// sb.append("\n\n");
	// }
	// if (sb.length() == 0) {
	// sb.append("抱歉，没有找到您想要的信息");
	// }
	// return sb.toString();
	// }
	//
	// public static String getTopStr() {
	// StringBuilder sb = new StringBuilder();
	// JdbcTemplate jdbcTempalte = getJDBCTemplate();
	// System.out.println("jdbcTempalte:" + jdbcTempalte);
	// List infoBeanList = jdbcTempalte
	// .query("select * from (select * from info where status = " +
	// MyConstant.APPROVED + " order by weight desc) t limit 0, 10",
	// new ItemMapper());
	// System.out.println(infoBeanList);
	// for (int i = 0; i < infoBeanList.size(); ++i) {
	// WordBean oneInfoBean = (WordBean) infoBeanList.get(i);
	// sb.append((i + 1) + ".标题:" + oneInfoBean.getTitle());
	// if (StringUtils.isNotEmpty(oneInfoBean.getContent())) {
	// sb.append("\n内容:" + oneInfoBean.getContent());
	// }
	// if (StringUtils.isNotEmpty(oneInfoBean.getContactor())) {
	// sb.append("\n联系人:" + oneInfoBean.getContactor());
	// }
	// if (StringUtils.isNotEmpty(oneInfoBean.getTel())) {
	// sb.append("\n联系电话:" + oneInfoBean.getTel());
	// }
	//
	// sb.append("\n\n");
	// }
	// return sb.toString();
	// }
	//
	// public static List<WordBean> getInfoList(String category) {
	// JdbcTemplate jdbcTempalte = getJDBCTemplate();
	// System.out.println("jdbcTempalte:" + jdbcTempalte);
	// List infoBeanList = jdbcTempalte
	// .query("select * from (select * from info where status = " +
	// MyConstant.APPROVED + " and comment2 = '" + category +
	// "' order by lastUpdateDate desc) t ",
	// new ItemMapper());
	//
	// if("CQ".equals(category)) {
	// Collections.sort(infoBeanList);
	// }
	//
	// System.out.println(infoBeanList);
	// return infoBeanList;
	// }
	//
	// public static List<WordBean> getInfoList(String type, String status) {
	// JdbcTemplate jdbcTempalte = getJDBCTemplate();
	// System.out.println("jdbcTempalte:" + jdbcTempalte);
	// String sql = "select * from info where 1 = 1 ";
	// if(!StringUtils.isEmpty(type)) {
	// sql += " and comment2 = '" + type + "'";
	// } else {
	// sql += " and comment2 in ('CQ', 'resource') ";
	// }
	//
	// if(!StringUtils.isEmpty(status)) {
	// sql += " and status = " + status;
	// }
	//
	// sql += " order by lastUpdateDate desc";
	// List infoBeanList = jdbcTempalte
	// .query(sql ,
	// new ItemMapper());
	//
	// System.out.println(infoBeanList);
	// return infoBeanList;
	// }
	//
	// public static List<WordBean> getClosedCQList() {
	// String category = "CQ";
	// JdbcTemplate jdbcTempalte = getJDBCTemplate();
	// System.out.println("jdbcTempalte:" + jdbcTempalte);
	// List infoBeanList = jdbcTempalte
	// .query("select * from (select * from info where status = " +
	// MyConstant.CLOSED + " and comment2 = '" + category +
	// "' order by weight desc) t ",
	// new ItemMapper());
	// System.out.println(infoBeanList);
	// return infoBeanList;
	// }
	//
	public static Object[] getWordDicList(int page, int rows, String sidx,
			String sord, String _search, String searchField, String searchOper,
			String searchString, boolean ignoreEasy) {
		if (StringUtils.isEmpty(sidx)) {
			sidx = "id";
		}
		if (StringUtils.isEmpty(sord)) {
			sord = "asc";
		}
		JdbcTemplate jdbcTempalte = getJDBCTemplate();
		String whereCaluse = " where 1 = 1 ";
		if (ignoreEasy) {
			whereCaluse += " and rank != 1 ";
		}
		if ("true".equals(_search)) {
			whereCaluse += " and "
					+ transOper(searchField, searchOper, searchString);
		}
		String sql = "select * from words " + whereCaluse + " order by " + sidx
				+ " " + sord + " limit " + page + ", " + rows;
		String countSql = "select count(*) from words" + whereCaluse;
		System.out.println("###sql:" + sql);
		List<WordBean> infoBeanList = jdbcTempalte.query(sql, new ItemMapper());
		int totalCount = jdbcTempalte.queryForObject(countSql, Integer.class);
		return new Object[] { infoBeanList, totalCount };
	}

	private static String transOper(String searchField, String searchOper,
			String searchString) {
		String result = "";
		if ("eq".equals(searchOper)) {
			result = searchField + " = '" + searchString + "'";
		} else if ("ne".equals(searchOper)) {
			result = searchField + " != '" + searchString + "'";
		} else if ("cn".equals(searchOper)) {
			result = searchField + " like '%" + searchString + "%'";
		} else if ("nc".equals(searchOper)) {
			result = searchField + " not like '%" + searchString + "%'";
		} else if ("bw".equals(searchOper)) {
			result = searchField + " like '" + searchString + "%'";
		} else if ("ew".equals(searchOper)) {
			result = searchField + " like '%" + searchString + "'";
		} else if ("bn".equals(searchOper)) {
			result = searchField + " not like '" + searchString + "%'";
		} else if ("en".equals(searchOper)) {
			result = searchField + " not like '%" + searchString + "'";
		} else if ("gt".equals(searchOper)) {
			result = searchField + " > " + searchString;
		}
		return result;
	}

	public static Map<String, WordBean> getWordDicMap() {
		// int page, int rows, String sidx,
		// String sord, String _search, String searchField, String searchOper,
		// String searchString
		List<WordBean> wordDicList = (List<WordBean>) (DBUtil.getWordDicList(0,
				Integer.MAX_VALUE, "", "", "false", "", "", "", false)[0]);

		Map<String, WordBean> wordDicMap = new HashMap<String, WordBean>();
		for (int i = 0; i < wordDicList.size(); i++) {
			WordBean oneWordBean = wordDicList.get(i);
			System.out.println("oneWordBean:" + oneWordBean);
			if (StringUtils.isNotEmpty(oneWordBean.getContent())) {
				wordDicMap.put(oneWordBean.getContent().toLowerCase(),
						oneWordBean);
			}
		}
		return wordDicMap;
	}

	public static WordBean getWordBean(int id) {
		JdbcTemplate jdbcTempalte = getJDBCTemplate();
		String sql = "select * from words where id = " + id;
		WordBean wordBean = jdbcTempalte.queryForObject(sql, new ItemMapper());
		return wordBean;
	}

	public static void delInfo(String ids) {
		JdbcTemplate jdbcTempalte = getJDBCTemplate();
		jdbcTempalte.execute("delete from words where id in (" + ids + ")");
	}

	public static boolean saveInfo(SentenBean sentenBean) {
		JdbcTemplate jdbcTempalte = getJDBCTemplate();
		int count = ((Integer) jdbcTempalte.queryForObject(
				"select count(*) from sentences", Integer.class)).intValue();
		int maxId = 0;
		if (count == 0) {
			maxId = 0;
		} else {
			maxId = ((Integer) jdbcTempalte.queryForObject(
					"select max(id) maxId from sentences", Integer.class))
					.intValue();
		}
		int updateCount = jdbcTempalte.update(
				"insert into sentences (id, content, rank) values(? , ? , ?)",
				new Object[] { Integer.valueOf(maxId + 1),
						sentenBean.getContent(), sentenBean.getRank() });

		System.out.println("save sentence Info updateCount:" + updateCount);

		return (updateCount != 0);

	}

	public static boolean saveInfo(WordBean wordBean) {
		JdbcTemplate jdbcTempalte = getJDBCTemplate();
		System.out.println("jdbcTempalte:" + jdbcTempalte);
		int count = ((Integer) jdbcTempalte.queryForObject(
				"select count(*) from words", Integer.class)).intValue();
		int maxId = 0;
		if (count == 0) {
			maxId = 0;
		} else {
			maxId = ((Integer) jdbcTempalte.queryForObject(
					"select max(id) maxId from words", Integer.class))
					.intValue();
		}
		int updateCount = jdbcTempalte
				.update("insert into words (id, content, comment, rank,  hit, createDate, lastUpt) values(? , ? , ? , ?, ?, ?, ?)",
						new Object[] {
								Integer.valueOf(maxId + 1),
								wordBean.getContent(),
								wordBean.getComment(),
								wordBean.getRank(),
								wordBean.getHit(),
								new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
										.format(new Date()),
								new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
										.format(new Date()) });

		System.out.println("save word Info updateCount:" + updateCount);

		return (updateCount != 0);
	}

	public static boolean updateInfo(WordBean wordBean) {
		JdbcTemplate jdbcTempalte = getJDBCTemplate();
		System.out.println("jdbcTempalte:" + jdbcTempalte);
		int updateCount = jdbcTempalte
				.update("update words set content = ?, comment = ?, hit = ?, rank = ?, lastUpt = ? where id = ?",
						new Object[] {
								wordBean.getContent(),
								wordBean.getComment(),
								wordBean.getHit(),
								wordBean.getRank(),
								new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
										.format(new Date()), wordBean.getId() });

		System.out.println("updateInfo updateCount:" + updateCount);
		return (updateCount != 0);
	}

	public static void main(String[] args) {
		// String str = "abssdf中";
		// System.out.println(str.matches("^[a-zA-Z]*"));
		String str = "1111? aaaaaa  aaaaa,bbbb bbb     bbbbbb.cccccccccc!ddddd ddd.";
		String[] arr = str.split("\\.|\\?|!");
		for (int i = 0; i < arr.length; i++) {
			System.out.println(i + ":" + arr[i]);
		}

	}

	public static void main0(String[] args) throws Exception {
		MyFilter.isLocal = true;
		WordBean wordBean = new WordBean();
		List<String> wordList = FileUtils.readLines(new File("words.txt"));
		int size = wordList.size();
		// int size = 3;
		boolean isWordLine = true;
		for (int i = 0; i < size; i++) {
			String oneLine = StringUtils.trim(wordList.get(i));
			System.out.println("###oneLine:" + oneLine + " isWordLine:"
					+ isWordLine);
			if (StringUtils.isEmpty(oneLine)) {
				continue;
			}
			if (oneLine.length() == 1) {
				int firstChar = oneLine.charAt(0);
				// System.out.println(firstChar);
				if (firstChar >= 65 && firstChar <= 90) {
					continue;
				}
			}
			if (isWordLine) {
				if (!oneLine.matches("^[a-zA-Z]*")) {
					continue;
				}
			}

			if (isWordLine) {
				wordBean = new WordBean();
				wordBean.setContent(oneLine);
			} else {
				wordBean.setComment(oneLine);
				DBUtil.saveInfo(wordBean);
			}

			isWordLine = !isWordLine;

		}
	}

	public static List<SentenBean> getSenList(String word) {
		JdbcTemplate jdbcTempalte = getJDBCTemplate();
		String sql = "select '" + word + "' word, t.* from sentences t where content like '"
				+ word.toLowerCase() + " %' or content like '% "
				+ word.toLowerCase() + "' or content like '% "
				+ word.toLowerCase() + " %' order by rank desc limit 0, 20";
		System.out.println("###sql:" + sql);
		List<SentenBean> infoBeanList = jdbcTempalte.query(sql,
				new SentenItemMapper());
		if (infoBeanList.isEmpty()) {
			sql = "select '" + word + "' word, t.* from sentences t where content like '%"
					+ word.toLowerCase() + "%' order by rank desc limit 0, 20";
			infoBeanList = jdbcTempalte.query(sql, new SentenItemMapper());
		}
		return infoBeanList;
	}

}
