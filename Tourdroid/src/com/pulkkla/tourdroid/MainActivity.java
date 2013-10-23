package com.pulkkla.tourdroid;

import java.util.List;

import com.pulkkla.tourdroid.contentproviders.DataProvider;
import com.pulkkla.tourdroid.contentproviders.HelsinkiServiceMap;
import com.pulkkla.tourdroid.contentproviders.RestCallback;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity implements RestCallback {
	
	private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void buttonClick(View view) {
    	
    	DataProvider hsm = new HelsinkiServiceMap();
        hsm.getSingleSpot(19835, this);
    }
    
    
	@Override
	public void preExecute() {
		Toast.makeText(this, "Retrieving a spot", Toast.LENGTH_SHORT).show();
	}


	@Override
	public void postExecute(List<Spot> response) {
		if (response == null) {
			Toast.makeText(this, "Error retrieving data", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, response.get(0).toString(), Toast.LENGTH_LONG).show();
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
