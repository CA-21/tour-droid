package com.pulkkla.tourdroid;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.*;

import com.pulkkla.tourdroid.contentproviders.POIProvider;
import com.pulkkla.tourdroid.contentproviders.HelsinkiServiceMap;
import com.pulkkla.tourdroid.contentproviders.RestCallback;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends Activity implements RestCallback, LocationListener {
	
	private static final String TAG = "MainActivity";
	
	private MapView myMap;
	private MapController myMapCtrl;
	
	private MyItemizedIconOverlay myMapOL;
	private ArrayList<OverlayItem> overlayItems;
	
	private LocationManager locMgr;
	private Location loc;
	
	private int helsinkiLat = (int)(60.17 * 1E6);
    private int helsinkiLon = (int)(24.94 * 1E6);
    
    private GeoPoint currentCenter;
    
	// The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
 
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 10; // 10 seconds
    
    private boolean initialLocationSet = false;
    
    private POIProvider[] providers = new POIProvider[1];
 
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
        currentCenter = new GeoPoint(helsinkiLat, helsinkiLon);
        myMapCtrl.setZoom(14);
        myMapCtrl.setCenter(currentCenter);
        
        overlayItems = new ArrayList<OverlayItem>();
    	DefaultResourceProxyImpl defaultResourceProxyImpl = new DefaultResourceProxyImpl(this);
    	myMapOL = new MyItemizedIconOverlay(overlayItems, null, defaultResourceProxyImpl);
    	myMap.getOverlays().add(myMapOL);
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
    		providers[0].searchSpots(3, "restaurant", currentCenter, 1500, this);
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
			
			overlayItems.clear();
			// Draw spots to the map
			for (Spot s : response) {
				overlayItems.add(new OverlayItem(s.getName(), s.getName(), s.getLocation().point));
			}
			myMap.invalidate();
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
			currentCenter = new GeoPoint(location);
	        myMapCtrl.animateTo(currentCenter);
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
    
	public class MyItemizedIconOverlay extends ItemizedIconOverlay<OverlayItem> {
		
		public MyItemizedIconOverlay(List<OverlayItem> pList,
				org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener<OverlayItem> pOnItemGestureListener,
				ResourceProxy pResourceProxy) {
			super(pList, pOnItemGestureListener, pResourceProxy);
		}
		
		@Override
		public void draw(Canvas canvas, MapView mapview, boolean arg2) {
			super.draw(canvas, mapview, arg2);
			
			if (!overlayItems.isEmpty()) {
				for (OverlayItem i : overlayItems) {
					GeoPoint in = i.getPoint();
					
					Point out = new Point();
					mapview.getProjection().toPixels(in, out);
					
					Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.restaurant);
					canvas.drawBitmap(bm, out.x - bm.getWidth()/2, out.y - bm.getHeight()/2, null);
				}
			}
		}
		
	}
	
}
