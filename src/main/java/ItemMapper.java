import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

class ItemMapper implements RowMapper {
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		WordBean wordBean = new WordBean(rs.getInt("id"),
				rs.getString("content"), rs.getString("comment"),
				rs.getInt("rank"), rs.getInt("hit"),
				rs.getString("createDate"), rs.getString("lastUpt"));
		return wordBean;
	}
}