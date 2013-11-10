package com.pulkkla.tourdroid.contentproviders;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.Locale;

import org.osmdroid.util.GeoPoint;

import android.util.Log;

import com.google.gson.JsonDeserializer;
import com.pulkkla.tourdroid.Spot;


public class HelsinkiServiceMap extends com.pulkkla.tourdroid.contentproviders.POIProvider {
	private static final String TAG = "HelsinkiServiceMap";
	
	private static String url_base = "http://www.hel.fi/palvelukarttaws/rest/v2/";
	private static String url_spot = "unit/";
	private static String url_list = "service/25954";
	private static String url_search = "search=";
	private static String xmlformat = "format=xml";
	
	
	public HelsinkiServiceMap() {
		
	}
	
	public void getSingleSpot(int id, RestCallback c) {
		final String url = url_base + url_spot + Integer.toString(id);
		currentHandler = new SpotFetch(url, c);
		currentHandler.execute(FETCH_TYPE_SINGLE);
	}

	@Override
	protected JsonDeserializer<Spot[]> getSpotDeserializer() {
		return new HelsinkiServiceMapSpotDeserializer();
	}
	
	protected Type getSpotType() {
		return HelsinkiServiceMapSpot.class;
	}

	@Override
	public void searchSpots(int count, String keyword, RestCallback c) {
		final String url = url_base + url_spot + "?" + url_list + "&" + url_search + keyword;
		currentHandler = new SpotFetch(url, c);
		currentHandler.execute(FETCH_TYPE_MULTI, count);
	}

	@Override
	public void searchSpots(int count, String keyword, GeoPoint loc, int range,
			RestCallback c) {
		NumberFormat f = NumberFormat.getInstance(new Locale("en", "EN"));
		f.setMaximumFractionDigits(6);
		String lat = f.format(loc.getLatitudeE6()/1E6);
		String lon = f.format(loc.getLongitudeE6()/1E6);
		
		final String url = url_base + url_spot + "?" + url_list + "&" + url_search + keyword +
				"&lat=" + lat + "&lon=" + lon + "&distance=" + range;
		Log.i(TAG, url);
		currentHandler = new SpotFetch(url, c);
		currentHandler.execute(FETCH_TYPE_MULTI, count);
	}

}
