package player;

public class AudioBean {
	private String name;
	private String url;
	private boolean external = true;

	public AudioBean() {
	}

	public AudioBean(String name, String url, boolean external) {
		super();
		this.name = name;
		this.url = url;
		this.external = external;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isExternal() {
		return external;
	}

	public void setExternal(boolean external) {
		this.external = external;
	}

}
