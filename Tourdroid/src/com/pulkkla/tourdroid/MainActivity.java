package com.pulkkla.tourdroid;

import java.util.List;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import com.pulkkla.tourdroid.contentproviders.POIProvider;
import com.pulkkla.tourdroid.contentproviders.HelsinkiServiceMap;
import com.pulkkla.tourdroid.contentproviders.RestCallback;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends Activity implements RestCallback, LocationListener {
	
	private static final String TAG = "MainActivity";
	
	private MapView myMap;
	private MapController myMapCtrl;
	
	private LocationManager locMgr;
	private Location loc;
	
	private int helsinkiLat = (int)(60.17 * 1E6);
    private int helsinkiLon = (int)(24.94 * 1E6);
    
	// The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
 
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 10; // 10 seconds
    
    private boolean initialLocationSet = false;
    
    private POIProvider[] providers = new POIProvider[1];;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	// Initialize POI service providers
    	providers[0] = new HelsinkiServiceMap();
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Get current location
        loc = getLocation();
        Toast.makeText(this, "Resolving current location...", Toast.LENGTH_SHORT).show();
        
        // Initialize map view
        myMap = (MapView)findViewById(R.id.mapview);
        myMap.setBuiltInZoomControls(true);
        myMapCtrl = myMap.getController();
        myMapCtrl.setZoom(14);
        myMapCtrl.setCenter(new GeoPoint(helsinkiLat, helsinkiLon));
    }
    
    private Location getLocation() {
    	locMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
    	if (locMgr != null) {
    		loc = locMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    	}
    	return loc;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void topButtonClick(View view) {
    	switch(view.getId()) {
    	case R.id.button1:
    		Toast.makeText(this, "Fetching restaurants", Toast.LENGTH_SHORT).show();
    		providers[0].searchSpots(3, "restaurant", this);
    		break;
    	}
    }
    
    
	@Override
	public void preExecute() {
		Toast.makeText(this, "Retrieving spots", Toast.LENGTH_SHORT).show();
	}


	@Override
	public void postExecute(List<Spot> response) {
		if (response == null) {
			Toast.makeText(this, "Error retrieving data", Toast.LENGTH_SHORT).show();
		} else {
			Log.i(TAG, "Got " + response.size() + " spots in result (limit 3).");
			Toast.makeText(this, "Got " + response.size() + " POIs", Toast.LENGTH_LONG).show();
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

	@Override
	public void onLocationChanged(Location location) {
		if (!initialLocationSet) {
			Toast.makeText(this, "Got location! " + location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_LONG).show();
	        myMapCtrl.animateTo(new GeoPoint(location));
	        locMgr.removeUpdates(this);
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
    
}
