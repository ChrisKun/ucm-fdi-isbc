package proyecto1.viajes;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

public class TravelSolution implements CaseComponent{

	String caseId;
	String hotel;
	Integer price;
	
	public Attribute getIdAttribute() {
		return new Attribute("caseId", TravelSolution.class);
	}
	
	public String toString(){
		return caseId + " " + hotel + " " + price.toString();
	}
	
	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getHotel() {
		return hotel;
	}

	public void setHotel(String hotel) {
		this.hotel = hotel;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}
}
