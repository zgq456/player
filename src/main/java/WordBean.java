
import com.alibaba.fastjson.JSONObject;
public class WordBean {
	private int id;
	private String content;
	private String comment;
	private int rank;
	private int hit;
	private String createDate;
	private String lastUpt;

	public WordBean() {
	}

	public WordBean(int id, String content, String comment, int rank, int hit, String createDate, String lastUpt) {
		super();
		this.id = id;
		this.content = content;
		this.comment = comment;
		this.rank = rank;
		this.hit = hit;
		this.createDate = createDate;
		this.lastUpt = lastUpt;
	}
	
	public WordBean(int id, String content, String comment, int rank, int hit) {
		super();
		this.id = id;
		this.content = content;
		this.comment = comment;
		this.rank = rank;
		this.hit = hit;
	}

	public String getComment() {
		if(comment != null && comment.length() >= 150) {
			comment = comment.substring(0, 145) + "...";
		}
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		if(content != null && content.length() >= 50) {
			content = content.substring(0, 45);
		}
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}
	
	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getLastUpt() {
		return lastUpt;
	}

	public void setLastUpt(String lastUpt) {
		this.lastUpt = lastUpt;
	}

	@Override
	public String toString() {
		return JSONObject.toJSON(this).toString();
	}

}