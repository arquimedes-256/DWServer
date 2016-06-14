package algz.model;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import core.base.BaseEntity;

@Entity
public class Location extends BaseEntity 
{

	private static final long serialVersionUID = 288128373944374528L;

	private String lat;
	private String lng;
	
	private String formatted_address;
	private String street_number;
	private String route;
	private String neighborhood;
	private String locality;
	private String administrative_area_level_2;
	private String administrative_area_level_1;
	private String country;
	private String postal_code;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar time;
	

	public Calendar getTime() {
		return time;
	}

	public void setTime(Calendar time) {
		this.time = time;
	}

	public String getFormatted_address() {
		return formatted_address;
	}

	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}

	public String getStreet_number() {
		return street_number;
	}

	public void setStreet_number(String street_number) {
		this.street_number = street_number;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getAdministrative_area_level_2() {
		return administrative_area_level_2;
	}

	public void setAdministrative_area_level_2(String administrative_area_level_2) {
		this.administrative_area_level_2 = administrative_area_level_2;
	}

	public String getAdministrative_area_level_1() {
		return administrative_area_level_1;
	}

	public void setAdministrative_area_level_1(String administrative_area_level_1) {
		this.administrative_area_level_1 = administrative_area_level_1;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPostal_code() {
		return postal_code;
	}

	public void setPostal_code(String postal_code) {
		this.postal_code = postal_code;
	}

	public  double getLat() {
		return Double.valueOf(lat);
	}

	public void setLat(double lat) {
		this.lat = String.valueOf(lat);
	}
	public  double getLng() {
		return Double.valueOf(lng);
	}
	public void setLng(double lng) {
		this.lng = String.valueOf(lng);
	}
	public void setLatString(String lat)
	{
		this.lat = lat;
	}
	public void setLngString(String lng)
	{
		this.lng = lng;
	}
}
