package com.pulkkla.tourdroid.contentproviders;

import java.lang.reflect.Type;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.pulkkla.tourdroid.Location;
import com.pulkkla.tourdroid.Spot;

public class HelsinkiServiceMapSpotDeserializer implements JsonDeserializer<Spot[]> {
	private static final String TAG = "HelsinkiServiceMapSpotDeserializer";
	
	Spot[] spotlist = null;

	@Override
	public Spot[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) throws JsonParseException {
		if (json.isJsonArray()) {
			JsonArray arr = json.getAsJsonArray();
			spotlist = new Spot[arr.size()];
			for (int i=0; i<arr.size(); i++) {
				if (arr.get(i).getAsJsonObject() != null) {
					JsonObject spotObj = arr.get(i).getAsJsonObject();
					spotlist[i] = jsonToSpot(spotObj);
				} else break;
			}
		} else if (json.isJsonObject()) {
			JsonObject obj = json.getAsJsonObject();
			spotlist = new HelsinkiServiceMapSpot[1];
			spotlist[0] = jsonToSpot(obj);
		}
		
		return spotlist;
	}
	
	private Spot jsonToSpot(JsonObject jsonSpot) {
		Spot spot = new HelsinkiServiceMapSpot();
		
		spot.setId(jsonSpot.get("id").getAsInt());
		spot.setName(jsonSpot.get("name_en").getAsString());
		spot.setDataSource("HelsinkiServiceMap");
		spot = addLocation(spot, jsonSpot);
		
		return spot;
	}
	
	private Spot addLocation(Spot spot, JsonObject jsonSpot) {
		if (jsonSpot.has("latitude") && jsonSpot.has("longitude")) {
			spot.setLocation(new Location(jsonSpot.get("latitude").getAsFloat(), jsonSpot.get("longitude").getAsFloat()));
		} else {
			Log.w(TAG, "Spot " + spot.getId() + " does not contain lat/lon coordinates! No other location system is implemented.");
		}
		return spot;
	}

}
