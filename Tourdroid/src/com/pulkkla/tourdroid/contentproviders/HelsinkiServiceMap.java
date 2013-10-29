package com.pulkkla.tourdroid.contentproviders;

import java.lang.reflect.Type;

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

}
