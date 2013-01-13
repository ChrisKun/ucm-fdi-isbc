package GAPDataBase;

import java.util.ArrayList;

public class Product {

	String name;
	String url;
	String category;
	String division;
	String composition;
	String washing;
	String importation;
	String overview;
	String sizeAndFiting;
	ArrayList<ImageInfo> imageInfo;
	ArrayList<String> reviews;
	ArrayList<Rating> ratings;
	Integer id;
	String price;

	public Product()
	{
		imageInfo = new ArrayList<ImageInfo>();
		reviews = new ArrayList<String>();
		ratings = new ArrayList<Rating>();
	}



	@Override
	public String toString() {
		return  id + "," + name + "," + url + "," + category + "," + division + ","
				+ composition + "," + washing + "," + importation + ","
				+ overview + "," + sizeAndFiting + ",\n\t" + imageInfo + ",\n\t"
				+ reviews + ",\n\t" + ratings + ",\n\t" + price;
	}



	public String getPrice() {
		return price;
	}



	public void setPrice(String price) {
		this.price = price;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getComposition() {
		return composition;
	}

	public void setComposition(String composition) {
		this.composition = composition;
	}

	public String getWashing() {
		return washing;
	}

	public void setWashing(String washing) {
		this.washing = washing;
	}

	public String getImportation() {
		return importation;
	}

	public void setImportation(String importation) {
		this.importation = importation;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getSizeAndFiting() {
		return sizeAndFiting;
	}

	public void setSizeAndFiting(String sizeAndFiting) {
		this.sizeAndFiting = sizeAndFiting;
	}

	public ArrayList<ImageInfo> getImageInfo() {
		return imageInfo;
	}

	public void setImageInfo(ArrayList<ImageInfo> imageInfo) {
		this.imageInfo = imageInfo;
	}

	public ArrayList<String> getReviews() {
		return reviews;
	}

	public void setReviews(ArrayList<String> reviews) {
		this.reviews = reviews;
	}

	public ArrayList<Rating> getRatings() {
		return ratings;
	}

	public void setRatings(ArrayList<Rating> ratings) {
		this.ratings = ratings;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
}
