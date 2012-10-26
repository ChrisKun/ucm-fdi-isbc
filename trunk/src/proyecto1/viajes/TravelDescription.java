package proyecto1.viajes;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

public class TravelDescription implements CaseComponent {

	String caseId;
	String HolidayTipe;
	Integer NumberOfPersons;
	String Region;
	String Transportation;
	Integer Duration;
	String Season;
	String Acommodation;
	
	public Attribute getIdAttribute() {
		return new Attribute("caseId", TravelDescription.class);
	}

	public String toString(){
		return caseId + " " + HolidayTipe + NumberOfPersons.toString() + " " +
				Region + " " + Transportation + " " + Duration.toString() + " " +
				Season + " " + Acommodation;
	}
	
	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getHolidayTipe() {
		return HolidayTipe;
	}

	public void setHolidayTipe(String holidayTipe) {
		HolidayTipe = holidayTipe;
	}

	public Integer getNumberOfPersons() {
		return NumberOfPersons;
	}

	public void setNumberOfPersons(Integer numberOfPersons) {
		NumberOfPersons = numberOfPersons;
	}

	public String getRegion() {
		return Region;
	}

	public void setRegion(String region) {
		Region = region;
	}

	public String getTransportation() {
		return Transportation;
	}

	public void setTransportation(String transportation) {
		Transportation = transportation;
	}

	public Integer getDuration() {
		return Duration;
	}

	public void setDuration(Integer duration) {
		Duration = duration;
	}

	public String getSeason() {
		return Season;
	}

	public void setSeason(String season) {
		Season = season;
	}

	public String getAcommodation() {
		return Acommodation;
	}

	public void setAcommodation(String acommodation) {
		Acommodation = acommodation;
	}
}
