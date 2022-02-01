package com.geekspace.beyikyol.Activity.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.geekspace.beyikyol.R;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapBox extends AppCompatActivity implements OnMapReadyCallback {
    MapView mapView;
    private DirectionsRoute currentRoute;
    // private BuildingPlugin buildingPlugin;
    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;
    private MapboxMap mapboxMap;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1Ijoic2hhZ2VsZGkiLCJhIjoiY2tsdzZ2cnlqMDB2ZDMybzh5bmpqNmltaiJ9.X4WJV-cLSch7-K3eFsObWA");
        setContentView(R.layout.activity_map_box);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);
        progressDialog = new ProgressDialog(this);


    }


    private void getRoute(Point origin, Point destination) {
        assert Mapbox.getAccessToken() != null;
        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
// You can get the generic HTTP info about the response
                        try {
                            if (response.body() == null) {
                                return;
                            } else if (response.body().routes().size() < 1) {
                                return;
                            }
                            currentRoute = response.body().routes().get(0);

                            if (currentRoute != null) {
                                NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                        .directionsRoute(currentRoute)
                                        .shouldSimulateRoute(false)
                                        .build();


// Call this method with Context from within an Activity
                                NavigationLauncher.startNavigation(MapBox.this, options);

                            } else {
                                Toast.makeText(MapBox.this, getString(R.string.error_title2), Toast.LENGTH_SHORT).show();
                            }
                            finish();

// Draw the route on the map
                            if (navigationMapRoute != null) {
                                navigationMapRoute.removeRoute();
                            } else {
                                navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                            }
                            navigationMapRoute.addRoute(currentRoute);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onMapReady(@NonNull com.mapbox.mapboxsdk.maps.MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41"));

        Intent intent = getIntent();
        String start_lat = intent.getStringExtra("start_lat");
        String start_lang = intent.getStringExtra("start_lang");
        String end_lat = intent.getStringExtra("end_lat");
        String end_lang = intent.getStringExtra("end_lang");

        if (start_lang.isEmpty() || start_lat.isEmpty() || end_lat.isEmpty() || end_lang.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_title2), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        double lat1 = Double.parseDouble(start_lat);
        double lat2 = Double.parseDouble(end_lat);
        double lang1 = Double.parseDouble(start_lang);
        double lang2 = Double.parseDouble(end_lang);


        Point start = Point.fromLngLat(lang1, lat1);
        Point end = Point.fromLngLat(lang2, lat2);
        getRoute(start, end);
    }
}