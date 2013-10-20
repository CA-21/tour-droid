package com.pulkkla.tourdroid.contentproviders;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import android.os.AsyncTask;
import android.util.Log;

public abstract class ContentProvider {
	private static final String TAG = "ContentProvider";

	protected RequestHandler currentHandler;
	
	public abstract void getSingleSpot(int id, RestCallback callback);

	protected class RequestHandler extends AsyncTask<String, Void, String> {

		private final String url;
		private final RestCallback callback;

		public RequestHandler(String url, RestCallback c) {
			this.url = url;
			callback = c;
		}

		@Override
		protected String doInBackground(String... params) {
			RestTemplate rest = new RestTemplate();
			rest.getMessageConverters().add(new StringHttpMessageConverter());
			try {
				return rest.getForObject(url, String.class);
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
		protected void onPostExecute(String response) {
			callback.postExecute(response);
		}
	}
}
