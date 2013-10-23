package com.pulkkla.tourdroid.contentproviders;

import java.lang.reflect.Type;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.pulkkla.tourdroid.Location;
import com.pulkkla.tourdroid.Spot;

public class HelsinkiServiceMapSpotDeserializer implements JsonDeserializer<Spot> {
	private static final String TAG = "HelsinkiServiceMapSpotDeserializer";

	@Override
	public Spot deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		
		Spot spot = new HelsinkiServiceMapSpot();
		spot.setId(obj.get("id").getAsInt());
		spot.setName(obj.get("name_en").getAsString());
		spot.setDataSource("HelsinkiServiceMap");
		spot.setLocation(new Location(obj.get("latitude").getAsFloat(), obj.get("longitude").getAsFloat()));
		Log.d(TAG, spot.toString());
		return spot;
	}

}
