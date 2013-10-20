package com.pulkkla.tourdroid;

import com.pulkkla.tourdroid.contentproviders.ContentProvider;
import com.pulkkla.tourdroid.contentproviders.HelsinkiServiceMap;
import com.pulkkla.tourdroid.contentproviders.RestCallback;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity implements RestCallback {
	
	private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ContentProvider hsm = new HelsinkiServiceMap();
        hsm.getSingleSpot(19835, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


	@Override
	public void preExecute() {
		Toast.makeText(this, "Retrieving a spot", Toast.LENGTH_SHORT).show();
	}


	@Override
	public void postExecute(String response) {
		if (response == null || response.isEmpty()) {
			Toast.makeText(this, "Error retrieving data", Toast.LENGTH_SHORT).show();
		} else {
			Log.v(TAG, response);
		}
	}


	@Override
	public String inExecute() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void cancelExecute() {
		// TODO Auto-generated method stub
		
	}
    
}
