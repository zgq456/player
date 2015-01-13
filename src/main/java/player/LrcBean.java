package player;

public class LrcBean {
	private String start;
	private String end;
	private String text;
	private String lastUpt;

	public LrcBean(String start, String end, String text, String lastUpt) {
		super();
		this.start = start;
		this.end = end;
		this.text = text;
		this.lastUpt = lastUpt;
	}

	public String getLastUpt() {
		return lastUpt;
	}

	public void setLastUpt(String lastUpt) {
		this.lastUpt = lastUpt;
	}


	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
