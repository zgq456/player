import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

class SentenItemMapper implements RowMapper {
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		String word = null;
		word = rs.getString("word").toLowerCase();
		String content = rs.getString("content");
		content =  content.replace(word, "<font color='red'>" + word + "</font>");
		SentenBean sentenBean = new SentenBean(rs.getInt("id"),
				content, rs.getInt("rank"), word);
		return sentenBean;
	}
}
