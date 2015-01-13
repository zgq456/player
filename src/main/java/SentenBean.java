import com.alibaba.fastjson.JSONObject;

public class SentenBean {
	private int id;
	private String content;
	private int rank;
	private String word;

	public SentenBean() {
	}

	public SentenBean(int id, String content, int rank, String word) {
		super();
		this.id = id;
		this.content = content;
		this.rank = rank;
		this.word = word;
	}
	
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		if(content != null && content.length() >= 255) {
			content = content.substring(0, 250) + "...";
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

	@Override
	public String toString() {
		return JSONObject.toJSON(this).toString();
	}

}