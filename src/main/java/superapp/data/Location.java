package superapp.data;

public class Location {

	private double lat;
	private double lng;

	public Location() {
		super();
	}

	public Location(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public Location setLat(double lat) {
		this.lat = lat;
		return this;
	}

	public Double getLng() {
		return lng;
	}

	public Location setLng(double lng) {
		this.lng = lng;
		return this;
	}

}
