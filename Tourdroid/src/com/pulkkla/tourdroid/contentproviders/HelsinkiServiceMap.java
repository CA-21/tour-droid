package com.pulkkla.tourdroid.contentproviders;

public class HelsinkiServiceMap extends com.pulkkla.tourdroid.contentproviders.ContentProvider {
	
	private static String url_base = "http://www.hel.fi/palvelukarttaws/rest/v2/";
	private static String url_spot = "unit/";
	private static String url_list = "service/25954";
	private static String xmlformat = "?format=xml";
	
	
	public HelsinkiServiceMap() {
		
	}
	
	public void getSingleSpot(int id, RestCallback callback) {
		final String url = url_base + url_spot + Integer.toString(id);
		currentHandler = new RequestHandler(url, callback);
		currentHandler.execute();
	}

}
