package com.pulkkla.tourdroid;

public class Location {
	public final float latitude;
	public final float longitude;
	
	public Location(float lat, float lon) {
		this.latitude = lat;
		this.longitude = lon;
	}
	
	@Override
	public String toString() {
		return Float.toString(latitude) + " " + Float.toString(longitude);
	}
}
