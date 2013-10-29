package com.pulkkla.tourdroid.contentproviders;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.pulkkla.tourdroid.Spot;

import android.os.AsyncTask;
import android.util.Log;

public abstract class POIProvider {
	private static final String TAG = "DataProvider";
	
	protected static final int FETCH_TYPE_SINGLE = 0x00;
	protected static final int FETCH_TYPE_MULTI = 0x01;

	protected SpotFetch currentHandler;
	
	public abstract void getSingleSpot(int id, RestCallback c);
	public abstract void searchSpots(int count, String keyword, RestCallback c);
	
	protected abstract JsonDeserializer<Spot[]> getSpotDeserializer();
	protected abstract Type getSpotType();
	
	protected class SpotFetch extends AsyncTask<Integer, Void, List<Spot>> {

		private final String url;
		private final RestCallback callback;
		private Random randomGenerator;
		
		private List<Spot> spotList;
		
		public SpotFetch(String url, RestCallback c) {
			this.url = url;
			callback = c;
		}
		

		@Override
		protected List<Spot> doInBackground(Integer... params) {
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(Spot[].class, getSpotDeserializer());
			Gson gson = gsonBuilder.create();
			
			if (params[0] == FETCH_TYPE_MULTI) {
				Log.d(TAG, "Params multi");
			}
			
			RestTemplate rest = new RestTemplate();
			rest.getMessageConverters().add(new GsonHttpMessageConverter(gson));
			try {
				Spot[] data = rest.getForObject(url, Spot[].class);
				spotList = Arrays.asList(data);
				if (spotList != null && params[1] != null)
					limitResults(params[1]);
				return spotList;
			} catch (RestClientException e) {
				Log.e(TAG, e.getMessage());
				return null;
			}
		}
		
		private void limitResults(int limit) {
			randomGenerator = new Random();
			int count = spotList.size();
			if (count <= limit) return;
			int[] selected = new int[limit];
			List<Spot> filtered = new ArrayList<Spot>();
			
			for (int i=0; i<limit; i++) {
				selected[i] = randomGenerator.nextInt(count);
				// TODO: finish the duplicate checking below to prevent duplicate random index creation
				//for (int j : selected) { if (selected[i] == j)
				filtered.add(spotList.get(selected[i]));
			}
			spotList = filtered;
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
