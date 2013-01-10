package GAPDataBase;

public class ImageInfo {
	String file;
	String url;
	String color;
	Integer id;
	
	
	
	/**
	 * @param file
	 * @param url
	 * @param color
	 * @param id
	 */
	public ImageInfo(String file, String url, String color, Integer id) {
		super();
		this.file = file;
		this.url = url;
		this.color = color;
		this.id = id;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return file + "," + url + "," + color + "," + id;
	}
	
	
}
