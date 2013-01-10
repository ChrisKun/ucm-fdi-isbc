package GAPDataBase;

public class Rating {
	Integer id;
    String overall;
    String length;
    String chest;
    String waist;
    String sleeves;
    String hr;
    String rise;
    String general;
    String ocasions;
    String cupsize;
    String support;
    String coverage;
    String highlights;
    String shrinkage;
    
    public Rating(){
    	super();
    }
    
	/**
	 * @param id
	 * @param overall
	 * @param length
	 * @param chest
	 * @param waist
	 * @param sleeves
	 * @param hr
	 * @param rise
	 * @param general
	 * @param ocasions
	 * @param cupsize
	 * @param support
	 * @param coverage
	 * @param highlights
	 * @param shrinkage
	 */
	public Rating(Integer id, String overall, String length, String chest,
			String waist, String sleeves, String hr, String rise,
			String general, String ocasions, String cupsize, String support,
			String coverage, String highlights, String shrinkage) {
		super();
		this.id = id;
		this.overall = overall;
		this.length = length;
		this.chest = chest;
		this.waist = waist;
		this.sleeves = sleeves;
		this.hr = hr;
		this.rise = rise;
		this.general = general;
		this.ocasions = ocasions;
		this.cupsize = cupsize;
		this.support = support;
		this.coverage = coverage;
		this.highlights = highlights;
		this.shrinkage = shrinkage;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOverall() {
		return overall;
	}
	public void setOverall(String overall) {
		this.overall = overall;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getChest() {
		return chest;
	}
	public void setChest(String chest) {
		this.chest = chest;
	}
	public String getWaist() {
		return waist;
	}
	public void setWaist(String waist) {
		this.waist = waist;
	}
	public String getSleeves() {
		return sleeves;
	}
	public void setSleeves(String sleeves) {
		this.sleeves = sleeves;
	}
	public String getHr() {
		return hr;
	}
	public void setHr(String hr) {
		this.hr = hr;
	}
	public String getRise() {
		return rise;
	}
	public void setRise(String rise) {
		this.rise = rise;
	}
	public String getGeneral() {
		return general;
	}
	public void setGeneral(String general) {
		this.general = general;
	}
	public String getOcasions() {
		return ocasions;
	}
	public void setOcasions(String ocasions) {
		this.ocasions = ocasions;
	}
	public String getCupsize() {
		return cupsize;
	}
	public void setCupsize(String cupsize) {
		this.cupsize = cupsize;
	}
	public String getSupport() {
		return support;
	}
	public void setSupport(String support) {
		this.support = support;
	}
	public String getCoverage() {
		return coverage;
	}
	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}
	public String getHighlights() {
		return highlights;
	}
	public void setHighlights(String highlights) {
		this.highlights = highlights;
	}
	public String getShrinkage() {
		return shrinkage;
	}
	public void setShrinkage(String shrinkage) {
		this.shrinkage = shrinkage;
	}

	@Override
	public String toString() {
		return id + "," + overall + "," + length + "," + chest + "," + waist
				+ "," + sleeves + "," + hr + "," + rise + "," + general + ","
				+ ocasions + "," + cupsize + "," + support + "," + coverage
				+ "," + highlights + "," + shrinkage;
	}

    
    
}
