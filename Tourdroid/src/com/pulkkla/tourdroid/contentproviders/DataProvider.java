package com.pulkkla.tourdroid.contentproviders;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.pulkkla.tourdroid.Spot;

import android.os.AsyncTask;
import android.util.Log;

public abstract class DataProvider {
	private static final String TAG = "DataProvider";

	protected SpotFetch currentHandler;
	
	public abstract void getSingleSpot(int id, RestCallback c);
	protected abstract JsonDeserializer<Spot> getSpotDeserializer();
	protected abstract Type getSpotType();
	
	protected class SpotFetch extends AsyncTask<String, Void, List<Spot>> {

		private final String url;
		private final RestCallback callback;

		public SpotFetch(String url, RestCallback c) {
			this.url = url;
			callback = c;
		}

		@Override
		protected List<Spot> doInBackground(String... params) {
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(Spot.class, getSpotDeserializer());
			Gson gson = gsonBuilder.create();
			
			RestTemplate rest = new RestTemplate();
			rest.getMessageConverters().add(new GsonHttpMessageConverter(gson));
			try {
				Spot data = rest.getForObject(url, Spot.class);
				ArrayList<Spot> spotList = new ArrayList<Spot>();
				spotList.add(data);
				return spotList;
			} catch (RestClientException e) {
				Log.e(TAG, e.getMessage());
				return null;
			}
		}

		@Override
		protected void onPreExecute() {
			callback.preExecute();
		}

		@Override
		protected void onPostExecute(List<Spot> response) {
			callback.postExecute(response);
		}
	}
}
