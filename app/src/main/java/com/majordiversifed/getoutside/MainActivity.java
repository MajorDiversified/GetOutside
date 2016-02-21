package com.majordiversifed.getoutside;

import android.app.ProgressDialog;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;


import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapOptions;
import com.esri.android.map.MapView;
import com.esri.android.map.RasterLayer;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.ags.ArcGISImageServiceLayer;
import com.esri.android.map.ags.ArcGISLayerInfo;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.io.UserCredentials;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Graphic;
import com.esri.core.map.Legend;
import com.esri.core.raster.RasterSource;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.tasks.query.QueryTask;
import com.esri.core.tasks.query.QueryParameters;

import java.io.InputStream;
import java.security.KeyStore;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    MapView mMapView;
    ArcGISFeatureLayer mFeatureLayer;
    GraphicsLayer mGraphicsLayer;
    boolean mIsMapLoaded;
    String mFeatureServiceURL;

    // The basemap switching menu items.
    MenuItem mStreetsMenuItem = null;
    MenuItem mTopoMenuItem = null;
    MenuItem mGrayMenuItem = null;
    MenuItem mOceansMenuItem = null;

    MenuItem mWeatherMenuItem = null;


    // Create MapOptions for each type of basemap.
    final MapOptions mTopoBasemap = new MapOptions(MapOptions.MapType.TOPO);
    final MapOptions mStreetsBasemap = new MapOptions(MapOptions.MapType.STREETS);
    final MapOptions mGrayBasemap = new MapOptions(MapOptions.MapType.GRAY);
    final MapOptions mOceansBasemap = new MapOptions(MapOptions.MapType.OCEANS);

    GraphicsLayer custom = new GraphicsLayer();
    HardCodeGraphic test = new HardCodeGraphic();

    public static final int RESULT_GALLERY = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /**
         *  on create method that grabs the toolbar and creation of the floating action button
        */

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        /**
         * Retrive the map and initial extent from XML layout also grabing weather url
         */

        mMapView = (MapView) findViewById(R.id.map);
        mFeatureServiceURL = this.getResources().getString(R.string.mapserviceURL);
        final ArcGISDynamicMapServiceLayer aGDMS = new ArcGISDynamicMapServiceLayer(mFeatureServiceURL);



        // Construct a feature service layer
        ArcGISFeatureLayer featureLayer = new ArcGISFeatureLayer(this.getResources().getString(
                R.string.featureServiceURL), ArcGISFeatureLayer.MODE.SNAPSHOT);

        // Add feature layer to MapView
        mMapView.addLayer(featureLayer);


        /**
         * toggle button that sets when the weather is available or not
         */

        Switch toggle1 = (Switch) findViewById(R.id.switch1);
        toggle1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mMapView.addLayer(aGDMS);
                } else {
                    mMapView.removeLayer(aGDMS);
                }
            }
        });

        /**
         * toggle button 2 for setting when you see possible locations to travel to nearby
         *
         * Graphic layer creation and adding it to the mapview
         */


        int[] uIDS;
        uIDS = custom.addGraphics(test.getGraphics());

        mMapView.addLayer(custom);


        ToggleButton toggle2 = (ToggleButton) findViewById(R.id.switch2);
        toggle2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //mMapView.addLayer(custom);
                } else {
                   // mMapView.removeLayer(custom);
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationDisplayManager locationDisplayManager = mMapView.getLocationDisplayManager();
                locationDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);
                locationDisplayManager.start();
                mMapView.zoomToScale(locationDisplayManager.getPoint(), 100000);

            }
        });

        /**
         * DrawerLayout creation as well as syncing with the nav view
         */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /**
        * Enable map to wrap around date line and some status change duda
        */

        mMapView.enableWrapAround(true);
        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            public void onStatusChanged(Object source, STATUS status) {
                if ((source == mMapView) && (status == OnStatusChangedListener.STATUS.INITIALIZED)) {
                    mIsMapLoaded = true;
                }
            }
        });

        /**
         * current location manager
         */
        LocationDisplayManager locationDisplayManager = mMapView.getLocationDisplayManager();
        //LocationDisplayManager.AutoPanMode panMode = new LocationDisplayManager.AutoPanMode.LOCATION;
        locationDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);
       // mMapView.zoomToScale(locationDisplayManager.getPoint(), 13);
        locationDisplayManager.start();

    }

    public void testfeatureLayer(ArcGISFeatureLayer featureLayer){
        int[] collectionGraphics = featureLayer.getGraphicIDs(42.2828f,-83.714699f,500);
        for (int a: collectionGraphics) {
            System.out.println(a);
        }
    }


    public void newLoc(View view) {
        LocationDisplayManager locationDisplayManager = mMapView.getLocationDisplayManager();
        test.addGraphic(locationDisplayManager.getPoint());
        custom.addGraphic(test.getGraphics()[17]);
    }


    protected void onPause() {
        super.onPause();
        mMapView.pause();
    }

    protected void onResume() {
        super.onResume();
        LocationDisplayManager locationDisplayManager = mMapView.getLocationDisplayManager();
        locationDisplayManager.start();
        mMapView.unpause();
    }

    @Override
    public void onStop() {
        super.onStop();
        // Turn off tracking, but leave the service around for further instructions

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        // Get the basemap switching menu items.
        mStreetsMenuItem = menu.getItem(0);
        mTopoMenuItem = menu.getItem(1);
        mGrayMenuItem = menu.getItem(2);
        mOceansMenuItem = menu.getItem(3);
        mWeatherMenuItem = menu.getItem(4);



        // Get the other switching menu items.
        // Also set the topo basemap menu item to be checked, as this is the default.
        mTopoMenuItem.setChecked(true);


        return true;
    }

    private class QueryFeatureLayer extends AsyncTask<String, Void, FeatureResult> {

        @Override
        protected FeatureResult doInBackground(String... params) {

            String whereClause = "COUNTRY='" + params[0] + "'";

            // Define a new query and set parameters
            QueryParameters mParams = new QueryParameters();
            mParams.setWhere(whereClause);
            mParams.setReturnGeometry(true);

            // Define the new instance of QueryTask
            QueryTask queryTask = new QueryTask(mFeatureServiceURL);
            FeatureResult results;

            try {
                // run the querytask
                results = queryTask.execute(mParams);
                return results;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(FeatureResult results) {

            // Remove the result from previously run query task
            mGraphicsLayer.removeAll();

            // Define a new marker symbol for the result graphics
            SimpleMarkerSymbol sms = new SimpleMarkerSymbol(Color.BLUE, 10, SimpleMarkerSymbol.STYLE.CIRCLE);

            // Envelope to focus on the map extent on the results
            Envelope extent = new Envelope();

            // iterate through results
            if (results != null) {
                for (Object element : results) {
                    // if object is feature cast to feature
                    if (element instanceof Feature) {
                        Feature feature = (Feature) element;
                        // convert feature to graphic
                        Graphic graphic = new Graphic(feature.getGeometry(), sms, feature.getAttributes());
                        // merge extent with point
                        extent.merge((Point) graphic.getGeometry());
                        // add it to the layer
                        mGraphicsLayer.addGraphic(graphic);
                    }
                }
            }

            // Set the map extent to the envelope containing the result graphics
            mMapView.setExtent(extent, 100);
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        boolean weather = false;
            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            // Handle menu item selection.
            switch (item.getItemId()) {
                case R.id.World_Street_Map:
                    mMapView.setMapOptions(mStreetsBasemap);
                    mStreetsMenuItem.setChecked(true);
                    return true;
                case R.id.World_Topo:
                    mMapView.setMapOptions(mTopoBasemap);
                    mTopoMenuItem.setChecked(true);
                    return true;
                case R.id.Gray:
                    mMapView.setMapOptions(mGrayBasemap);
                    mGrayMenuItem.setChecked(true);
                    return true;
                case R.id.Ocean_Basemap:
                    mMapView.setMapOptions(mOceansBasemap);
                    mOceansMenuItem.setChecked(true);
                    return true;
                    default:
                        return super.onOptionsItemSelected(item);
                }


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intentC = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivity(intentC);
        } else if (id == R.id.nav_gallery) {
            Intent galleryIntent = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent , RESULT_GALLERY );
        } else if (id == R.id.nav_slideshow) {
            LocationDisplayManager locationDisplayManager = mMapView.getLocationDisplayManager();
            locationDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);
            locationDisplayManager.start();
            mMapView.zoomToScale(locationDisplayManager.getPoint(), 24000);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
