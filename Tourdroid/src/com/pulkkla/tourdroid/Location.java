package com.pulkkla.tourdroid;

import org.osmdroid.util.GeoPoint;

public class Location {
	public final float latitude;
	public final float longitude;
	
	public final GeoPoint point;
	
	public Location(float lat, float lon) {
		this.latitude = lat;
		this.longitude = lon;
		
		this.point = new GeoPoint(latitude, longitude);
	}
	
	@Override
	public String toString() {
		return Float.toString(latitude) + " " + Float.toString(longitude);
	}
}
