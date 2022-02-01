package com.geekspace.beyikyol.Activity.Map;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.geekspace.beyikyol.Adapter.ListAdapter;
import com.geekspace.beyikyol.CustomView.Alert;
import com.geekspace.beyikyol.Receiver.MyLocationService;
import com.geekspace.beyikyol.R;
import com.geekspace.beyikyol.Database.SearchDB;
import com.geekspace.beyikyol.Model.SearchItem;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.GeoObjectCollection;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.directions.DirectionsFactory;
import com.yandex.mapkit.directions.driving.DrivingOptions;
import com.yandex.mapkit.directions.driving.DrivingRoute;
import com.yandex.mapkit.directions.driving.DrivingRouter;
import com.yandex.mapkit.directions.driving.DrivingSession;
import com.yandex.mapkit.directions.driving.VehicleOptions;
import com.yandex.mapkit.geometry.BoundingBox;
import com.yandex.mapkit.geometry.BoundingBoxHelper;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.GeoObjectTapEvent;
import com.yandex.mapkit.layers.GeoObjectTapListener;
import com.yandex.mapkit.map.CameraListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CameraUpdateReason;
import com.yandex.mapkit.map.GeoObjectSelectionMetadata;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.VisibleRegionUtils;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.search.Response;
import com.yandex.mapkit.search.SearchFactory;
import com.yandex.mapkit.search.SearchManager;
import com.yandex.mapkit.search.SearchManagerType;
import com.yandex.mapkit.search.SearchOptions;
import com.yandex.mapkit.search.Session;
import com.yandex.mapkit.traffic.TrafficLayer;
import com.yandex.mapkit.traffic.TrafficLevel;
import com.yandex.mapkit.traffic.TrafficListener;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import pl.droidsonroids.gif.GifImageView;

//import pl.droidsonroids.gif.GifImageView;

public class MapActivity extends AppCompatActivity implements TrafficListener, GeoObjectTapListener, DrivingSession.DrivingRouteListener, InputListener, Session.SearchListener, CameraListener {
    private final String MAPKIT_API_KEY = "32c7faf6-4337-44bd-b0cc-eaf7a157d5ff";
    private final Point TARGET_LOCATION = new Point(37.95, 58.3833);
    private UserLocationLayer userLocationLayer;
    private MapView mapView;
    private int which = 0;
    private EditText searchEdit;
    private boolean isFirstTime = true;
    private boolean isMarkerHas = false;
    PlacemarkMapObject myMarker;
    private MapKit mapKit;
    private Handler animationHandler;
    Button search_btn, history_btn;
    static MapActivity instance;
    ImageView no_search;
    int s = 0;
    int type = 3;
    int end_type = 0;
    LocationRequest locationRequest;
    PlacemarkMapObject s_marker, e_marker, search_mark,share_mark;
    FusedLocationProviderClient fusedLocationProviderClient;

    CardView drawFunctionalRoute;

    String[] countries = new String[100000];
    String smsMessage = "";


    private SearchManager searchManager;
    private Session searchSession;

    ListView listView;

    ArrayList<SearchItem> listofsearch = new ArrayList<>();

    private Session.SearchListener searchListener;

    private Point ROUTE_START_LOCATION = new Point(37.95, 58.3833);
    private Point ROUTE_END_LOCATION = new Point(37.95, 58.3933);
    private final Point SCREEN_CENTER = new Point(
            (ROUTE_START_LOCATION.getLatitude() + ROUTE_END_LOCATION.getLatitude()) / 2,
            (ROUTE_START_LOCATION.getLongitude() + ROUTE_END_LOCATION.getLongitude()) / 2);

    int tap_counter = 0;
    Intent intentThatCalled;
    public double latitude;
    public double longitude;
    public LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;

    CardView drawRoute;

    public GifImageView loading;

    String voice2text;

    ImageButton share_my_location;

    Point share_map_location=null;


    private Double my_lat = 37.95;
    private Double my_long = 58.3833;
    private Float my_speed = (float) 0;
    private Float my_bearing = (float) 90;


    ImageButton mode, camera, close;
    private int mapType = 1;


    //    private SearchManager searchManager;
    private boolean isCameraMoveWithMyLocation = false;

    private TextView levelText;
    private TrafficLevel trafficLevel = null;
    Context context = this;
    private ImageButton levelIcon;
    private ImageButton zoomUp;
    private ImageButton zoomDown;
    private ImageButton routing;
    private ImageButton maptype;
    private MapObjectCollection mapObjects;

    private DrivingRouter drivingRouter;
    private DrivingSession drivingSession;


    Dialog dialog;

    EditText smsEditText;


    boolean isMylocation=true;






    public static MapActivity getInstance() {
        return instance;
    }


//    private MasstransitRouter mtRouter;

//    @Override
//    public void onMasstransitRoutes(@NonNull List<Route> routes) {
//        if (routes.size() > 0) {
//            for (Section section : routes.get(0).getSections()) {
//                drawSection(
//                        section.getMetadata().getData(),
//                        SubpolylineHelper.subpolyline(
//                                routes.get(0).getGeometry(), section.getGeometry()));
//            }
//        }
//    }


//    @Override
//    public void onMasstransitRoutesError(@NonNull Error error) {
//        String errorMessage = error.toString();
//        if (error instanceof RemoteError) {
//            errorMessage = error.toString();
//        } else if (error instanceof NetworkError) {
//            errorMessage = error.toString();
//        }
//
//        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
//    }

    @Override
    public boolean onObjectTap(@NonNull GeoObjectTapEvent geoObjectTapEvent) {
        final GeoObjectSelectionMetadata selectionMetadata = geoObjectTapEvent
                .getGeoObject()
                .getMetadataContainer()
                .getItem(GeoObjectSelectionMetadata.class);

        if (selectionMetadata != null) {
            mapView.getMap().selectGeoObject(selectionMetadata.getId(), selectionMetadata.getLayerId());


        }

        return selectionMetadata != null;
    }

//    @Override
//    public void onMapTap(@NonNull Map map, @NonNull Point point) {
//       // Toast.makeText(context,which+"",Toast.LENGTH_SHORT).show();
//        if(which!=0) {
//            if(which==1){
//
//                mapView.getMap().deselectGeoObject();
//                mapObjects.clear();
//                ROUTE_END_LOCATION = point;
//
//                       mapView.getMap().getMapObjects().addPlacemark(point,
//                               ImageProvider.fromResource(MapActivity.this, R.drawable.ic_baseline_location_on_24),
//                               new IconStyle().setAnchor(new PointF((float) 0.5,(float) 0.5))
//                                       .setRotationType(RotationType.ROTATE)
//                                       .setZIndex(0f)
//                                       .setScale(1f)
//                                        .setFlat(false));
//                submitRequest();
//                which=0;
//            }
//
//        }
//
//
//    }

    private void routing() {

        mapView.getMap().addInputListener(new InputListener() {
            @Override
            public void onMapTap(@NonNull Map map, @NonNull final Point point) {


            }

            @Override
            public void onMapLongTap(@NonNull Map map, @NonNull Point point) {

            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();

    }


//    @Override
//    public void onMapLongTap(@NonNull Map map, @NonNull Point point) {
////        Toast.makeText(context,point.getLatitude()+"",Toast.LENGTH_SHORT).show();
////        mapView.getMap().getMapObjects().addPlacemark(point,
////                ImageProvider.fromResource(this, R.drawable.ic_baseline_location_on_24));
//    }

    @Override
    public void onDrivingRoutes(@NonNull List<DrivingRoute> list) {
        // Toast.makeText(context,list.size()+"",Toast.LENGTH_SHORT).show();

        int nji = 0;
        double min = 0;

        //  for (DrivingRoute route : list) {
        //  Toast.makeText(context,+"",Toast.LENGTH_SHORT).show();
        if (list.size() > 0) {
            min = list.get(0).getMetadata().getWeight().getDistance().getValue();
            for (int i = 1; i < list.size(); i++) {
                if (min > list.get(i).getMetadata().getWeight().getDistance().getValue()) {
                    min = list.get(i).getMetadata().getWeight().getDistance().getValue();
                    nji = i;
                }
            }
            mapObjects.addPolyline(list.get(nji).getGeometry());
            TextView distance = findViewById(R.id.distance);
            TextView time = findViewById(R.id.time);
            TextView traffic_lights = findViewById(R.id.traffic_lights);
            LinearLayout linear = findViewById(R.id.linear);

            linear.setVisibility(View.VISIBLE);
            android.view.animation.Animation slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
            android.view.animation.Animation slideBottom = AnimationUtils.loadAnimation(context, R.anim.slide_bottom);
            linear.startAnimation(slideUp);
            distance.setText(list.get(nji).getMetadata().getWeight().getDistance().getText());
            time.setText(list.get(nji).getMetadata().getWeight().getTimeWithTraffic().getText());
            traffic_lights.setText(list.get(nji).getTrafficLights().size() + "");

            close.setVisibility(View.VISIBLE);
            close.startAnimation(slideBottom);


            drawFunctionalRoute.setVisibility(View.VISIBLE);

            drawFunctionalRoute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MapBox.class);
                    intent.putExtra("start_lat", ROUTE_START_LOCATION.getLatitude() + "");
                    intent.putExtra("start_lang", ROUTE_START_LOCATION.getLongitude() + "");

                    intent.putExtra("end_lat", ROUTE_END_LOCATION.getLatitude() + "");
                    intent.putExtra("end_lang", ROUTE_END_LOCATION.getLongitude() + "");
                    startActivity(intent);
                }
            });


            BoundingBox box = BoundingBoxHelper.getBounds(list.get(nji).getGeometry());
            CameraPosition boundingBoxPosition = mapView.getMap()
                    .cameraPosition(box);

            mapView.getMap().move(new CameraPosition(ROUTE_START_LOCATION, 20, -105, 200),
                    new Animation(Animation.Type.SMOOTH, 1),
                    null);


            //  Toast.makeText(instance, ""+list.get(0).getWayPoints().size(), Toast.LENGTH_SHORT).show();
//           // list.get(0).getWayPoints().size()
//           for(int i=0;i<list.get(0).getRestrictedTurns().size();i++){
//               mapObjects.addPlacemark(
//
//
//                       list.get(0).getGeometry().getPoints().get(list.get(0).getRestrictedTurns().get(i).getPosition().getSegmentIndex()),
//                       ImageProvider.fromResource(context, R.drawable.red_mark));
//           }
        } else {
            Alert alert = new Alert(context, "no", getResources().getString(R.string.error_title2), getResources().getString(R.string.route_not_found));
            alert.ShowDialog();
            mapObjects.clear();

            if(search_mark==null || !search_mark.isValid() || !search_mark.isVisible()) {
                search_mark = mapObjects.addPlacemark(
                        new Point(0, 0),
                        ImageProvider.fromResource(context, R.drawable.blue_marker));
            }

            search_mark.setVisible(false);


            e_marker = mapObjects.addPlacemark(
                    new Point(0, 0),
                    ImageProvider.fromResource(context, R.drawable.red_mark));
            e_marker.setVisible(false);

            s_marker = mapObjects.addPlacemark(
                    new Point(0, 0),
                    ImageProvider.fromResource(context, R.drawable.green_mark));
            s_marker.setVisible(false);

            isMarkerHas = false;

            drawFunctionalRoute.setVisibility(View.GONE);
            drawFunctionalRoute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }


        // }


        // Toast.makeText(context,distance,Toast.LENGTH_SHORT).show();
    }


    private void turnGPSOn() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    @Override
    public void onDrivingRoutesError(@NonNull Error error) {
        String errorMessage = "Error";
        if (isMarkerHas) {
            s_marker.setVisible(false);
            e_marker.setVisible(false);

        }

        isMarkerHas = false;

        LinearLayout linear = findViewById(R.id.linear);
        android.view.animation.Animation slide_bottom = AnimationUtils.loadAnimation(context, R.anim.slide_bottom);
        android.view.animation.Animation slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        linear.startAnimation(slide_bottom);
        close.startAnimation(slideUp);
        linear.setVisibility(View.GONE);
        close.setVisibility(View.GONE);


        if (error instanceof RemoteError) {

            errorMessage = "remote error";
        } else if (error instanceof NetworkError) {
            errorMessage = "Network error";
        }

        //   Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void updateLocation() {
        buildLocationRequest();

        Context context;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());

    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, MyLocationService.class);
        intent.setAction(MyLocationService.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(50);
        locationRequest.setSmallestDisplacement(1f);
    }


    public void updatemylocation(final Double latitude, final Double longitude, final Float speed, final Float bearing) {
        MapActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {


                Point point = new Point(latitude, longitude);
                if (isCameraMoveWithMyLocation) {
                    mapView.getMap().move(new CameraPosition(point, mapView.getMap().getCameraPosition().getZoom(), mapView.getMap().getCameraPosition().getAzimuth(), mapView.getMap().getCameraPosition().getTilt()),
                            new Animation(Animation.Type.LINEAR, 1), null);
                }

                if (isFirstTime) {
                    if(getIntent().getData()==null) {
                        mapView.getMap().move(new CameraPosition(point, 14, -105, 200),
                                new Animation(Animation.Type.SMOOTH, 3),
                                null);
                    }

                    /// Rect rect=new Rect(new PointF(100,100),new PointF(100,100));


                    myMarker = mapView.getMap().getMapObjects().addPlacemark(point, ImageProvider.fromResource(MapActivity.this, R.drawable.user_arrow));
                    myMarker.setDraggable(true);
                    // myMarker.setVisible(false);


                    isFirstTime = false;
                } else {
                    myMarker.setGeometry(point);
                    myMarker.setDirection(bearing);
                    myMarker.useAnimation();
                    //  myMarker.setVisible(false);

                }


                //Toast.makeText(context,"Location: "+latitude+","+longitude,Toast.LENGTH_SHORT).show();


//                Toast.makeText(Sargyt_Barada.this,value,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapTap(@NonNull Map map, @NonNull final Point point) {

        if(type==3){
            if(share_mark==null || !share_mark.isValid() || !share_mark.isVisible()){
                share_mark=mapObjects.addPlacemark(
                        ROUTE_START_LOCATION,
                        ImageProvider.fromResource(context, R.drawable.sky_blue_marker));
                share_mark.setGeometry(point);
                share_map_location = point;

                share_mark.addTapListener(new MapObjectTapListener() {
                    @Override
                    public boolean onMapObjectTap(@NonNull @NotNull MapObject mapObject, @NonNull @NotNull Point point) {
                        isMylocation=false;
                        showDialog();
                        return true;
                    }
                });
            } else {
                share_mark.setGeometry(point);
                share_map_location = point;
            }
        }
        //  Toast.makeText(context,type+" type",Toast.LENGTH_SHORT).show();
        if (type == 1) {
            if (s == 0) {


                //mapView.getMap().deselectGeoObject();


                // locationEnabled();
                buildLocationRequest();
                if ((ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)) {

                    ActivityCompat.requestPermissions(MapActivity.this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }

                final Task location = fusedLocationProviderClient.getLastLocation();

                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            android.location.Location currentLocation = (android.location.Location) task.getResult();
                            if (currentLocation != null) {
                                Point my_point = new Point(currentLocation.getLatitude(), currentLocation.getLongitude());
                                mapObjects.clear();
                                ROUTE_START_LOCATION = my_point;
                                ROUTE_END_LOCATION = point;


                                if (!isMarkerHas) {
                                    s_marker = mapObjects.addPlacemark(
                                            ROUTE_START_LOCATION,
                                            ImageProvider.fromResource(context, R.drawable.green_mark));

                                    e_marker = mapObjects.addPlacemark(
                                            ROUTE_END_LOCATION,
                                            ImageProvider.fromResource(context, R.drawable.red_mark));


                                    isMarkerHas = true;
                                } else if (isMarkerHas && search_mark != null && e_marker != null) {
                                    e_marker.setVisible(true);
                                    s_marker.setVisible(true);
                                    s_marker.setGeometry(ROUTE_START_LOCATION);
                                    e_marker.setGeometry(ROUTE_END_LOCATION);

                                }

                                e_marker.addTapListener(new MapObjectTapListener() {
                                    @Override
                                    public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
                                        Toast.makeText(context, getResources().getString(R.string.end_position), Toast.LENGTH_SHORT).show();
                                        return false;
                                    }
                                });

                                s_marker.addTapListener(new MapObjectTapListener() {
                                    @Override
                                    public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
                                        Toast.makeText(context, getResources().getString(R.string.start_position), Toast.LENGTH_SHORT).show();
                                        return false;
                                    }
                                });
                                type=3;

//                                LayoutInflater inflater=LayoutInflater.from(context);
//                                View view=inflater.inflate(R.layout.select_dialog,null);
//                                e_marker.setView(new ViewProvider(view,false));
                                submitRequest();


                            } else {
                                locationEnabled();
                            }


                            // Toast.makeText(context,"Location: "+currentLocation.getLatitude(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                s++;


            }

        } else if (type == 2) {
            if (s == 0) {
                // Toast.makeText(context,tap_counter+"",Toast.LENGTH_SHORT).show();
                // Toast.makeText(context,type+" type",Toast.LENGTH_SHORT).show();

                if (tap_counter == 0) {

                    mapObjects.clear();
                    ROUTE_START_LOCATION = point;
                    tap_counter = 1;
                    end_type = 1;
                    if (isMarkerHas && search_mark != null) {
                        s_marker.setGeometry(point);
                        s_marker.setVisible(true);
                    } else {
                        s_marker = mapObjects.addPlacemark(
                                ROUTE_START_LOCATION,
                                ImageProvider.fromResource(context, R.drawable.green_mark));
                    }


                    s_marker.addTapListener(new MapObjectTapListener() {
                        @Override
                        public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
                            Toast.makeText(context, getResources().getString(R.string.start_position), Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    });


                } else if (end_type == 1) {
                    // Toast.makeText(context,tap_counter+"",Toast.LENGTH_SHORT).show();
                    ROUTE_END_LOCATION = point;
                    if (isMarkerHas && e_marker != null) {
                        e_marker.setGeometry(point);
                        e_marker.setVisible(true);
                    } else {
                        e_marker = mapObjects.addPlacemark(
                                ROUTE_END_LOCATION,
                                ImageProvider.fromResource(context, R.drawable.red_mark));
                    }

                    submitRequest();

                    s++;
                    //tap_counter++;
                    isMarkerHas = true;
                    e_marker.addTapListener(new MapObjectTapListener() {
                        @Override
                        public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
                            Toast.makeText(context, getResources().getString(R.string.end_position), Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    });
                    type=3;

                }


            }
        }


    }

    @Override
    public void onMapLongTap(@NonNull Map map, @NonNull Point point) {

    }

    private void submitQuery(String query) {
        //Toast.makeText(context, query, Toast.LENGTH_SHORT).show();
        searchSession = searchManager.submit(
                query,
                VisibleRegionUtils.toPolygon(mapView.getMap().getVisibleRegion()),
                new SearchOptions(),
                this);
    }


    @Override
    public void onCameraPositionChanged(
            Map map,
            CameraPosition cameraPosition,
            CameraUpdateReason cameraUpdateReason,
            boolean finished) {
        if (finished) {
            submitQuery(searchEdit.getText().toString());
        }
    }

    @Override
    public void onSearchResponse(@NonNull Response response) {

//        MapObjectCollection mapObjects = mapView.getMap().getMapObjects();
//        mapObjects.clear();
        // Toast.makeText(context, ""+response.toString(), Toast.LENGTH_SHORT).show();
//        for (GeoObjectCollection.Item searchResult : response.getCollection().getChildren()) {
//            Point resultLocation = searchResult.getObj().getGeometry().get(0).getPoint();
//            if (resultLocation != null) {
//                mapObjects.addPlacemark(
//                        resultLocation,
//                        ImageProvider.fromResource(this, R.drawable.green_marker));
//            }
//        }
    }

    @Override
    public void onSearchError(@NonNull Error error) {
        String errorMessage = "error";
        if (error instanceof RemoteError) {
            errorMessage = "RemoteError";
        } else if (error instanceof NetworkError) {
            errorMessage = "NetworkError";
        }
        //   Log.d("Yandex Search",error.toString());
        //  Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onObjectAdded(@NonNull UserLocationView userLocationView) {
////        userLocationLayer.setAnchor(
////                new PointF((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.5)),
////                new PointF((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.83)));
////
////        userLocationView.getArrow().setIcon(ImageProvider.fromResource(
////                this, R.drawable.user_arrow));
////
////        CompositeIcon pinIcon = userLocationView.getPin().useCompositeIcon();
////
////        pinIcon.setIcon(
////                "icon",
////                ImageProvider.fromResource(this, R.drawable.user_arrow),
////                new IconStyle().setAnchor(new PointF(0f, 0f))
////                        .setRotationType(RotationType.ROTATE)
////                        .setZIndex(0f)
////                        .setScale(1f)
////        );
//
////        pinIcon.setIcon(
////                "pin",
////                ImageProvider.fromResource(this, R.drawable.search_result),
////                new IconStyle().setAnchor(new PointF(0.5f, 0.5f))
////                        .setRotationType(RotationType.ROTATE)
////                        .setZIndex(1f)
////                        .setScale(0.5f)
////        );
//
//        userLocationView.getAccuracyCircle().setFillColor(getResources().getColor(R.color.blue_transparent));
//
//    }
//
//    @Override
//    public void onObjectRemoved(@NonNull UserLocationView userLocationView) {
//
//    }
//
//    @Override
//    public void onObjectUpdated(@NonNull UserLocationView userLocationView, @NonNull ObjectEvent objectEvent) {
//
//    }


//    @Override
//    public void onObjectAdded(@NonNull UserLocationView userLocationView) {
//        userLocationLayer.setAnchor(new PointF((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.5)),new PointF((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.83)));
//
//
//
//
//        userLocationView.getArrow().setIcon(ImageProvider.fromResource(
//                this, R.drawable.user_arrow));
//        userLocationLayer.setAutoZoomEnabled(false);
//
//        userLocationLayer.setTapListener(new UserLocationTapListener() {
//            @Override
//            public void onUserLocationObjectTap(@NonNull Point point) {
//                Toast.makeText(context,"User Location",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        CompositeIcon pinIcon = userLocationView.getPin().useCompositeIcon();
//
//        pinIcon.setIcon(
//                "icon",
//                ImageProvider.fromResource(this, R.drawable.user_arrow),
//                new IconStyle().setAnchor(new PointF(0f, 0f))
//                        .setRotationType(RotationType.ROTATE)
//                        .setZIndex(0f)
//                        .setScale(1f)
//        );
//
////        pinIcon.setIcon(
////                "pin",
////                ImageProvider.fromResource(this, R.drawable.user_arrow),
////                new IconStyle().setAnchor(new PointF(0.5f, 0.5f))
////                        .setRotationType(RotationType.ROTATE)
////                        .setZIndex(1f)
////                        .setScale(0.5f)
////        );
//
//        // ROUTE_START_LOCATION = userLocationLayer.cameraPosition().getTarget();
//
//        userLocationView.getAccuracyCircle().setFillColor(getResources().getColor(R.color.arrow));
//    }
//
//    @Override
//    public void onObjectRemoved(@NonNull UserLocationView userLocationView) {
//        userLocationLayer.resetAnchor();
//    }
//
//    @Override
//    public void onObjectUpdated(@NonNull UserLocationView userLocationView, @NonNull ObjectEvent objectEvent) {
//        //ROUTE_START_LOCATION = userLocationLayer.cameraPosition().getTarget();
//    }


    private enum TrafficFreshness {Loading, OK, Expired}

    ;
    private TrafficFreshness trafficFreshness;
    private TrafficLayer traffic;
    ImageButton myLocation1;


    private void getListOfSearch(String queryString) {


        searchSession = searchManager.submit(
                queryString,
                VisibleRegionUtils.toPolygon(mapView.getMap().getVisibleRegion()),
                new SearchOptions(),
                searchListener);
        searchListener = new Session.SearchListener() {
            @Override
            public void onSearchResponse(@NonNull Response response) {
                listofsearch.clear();
                listView.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
//        MapObjectCollection mapObjects = mapView.getMap().getMapObjects();
//        mapObjects.clear();
                // Toast.makeText(context, ""+response.toString(), Toast.LENGTH_SHORT).show();
                for (GeoObjectCollection.Item searchResult : response.getCollection().getChildren()) {
                    Point resultLocation = searchResult.getObj().getGeometry().get(0).getPoint();
                    //  Toast.makeText(context, searchResult.getObj().getName(), Toast.LENGTH_SHORT).show();
                    if (resultLocation != null) {
                        listofsearch.add(new SearchItem(searchResult.getObj().getName(), resultLocation));

                    }
                }
                ListAdapter adapter = new ListAdapter(context, listofsearch);
                listView.setAdapter(adapter);
                loading.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSearchError(@NonNull Error error) {
                listofsearch.clear();
                listView.setVisibility(View.GONE);
                loading.setVisibility(View.GONE);
                no_search.setVisibility(View.VISIBLE);
                //   Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
//                   Alert alert=new Alert(context,"no",context.getString(R.string.error_title2),error.toString());
//                   alert.ShowDialog();
            }
        };
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // MapKitFactory.setApiKey(MAPKIT_API_KEY);
        /**
         * Initialize the library to load required native libraries.
         * It is recommended to initialize the MapKit library in the Activity.onCreate method
         * Initializing in the Application.onCreate method may lead to extra calls and increased battery use.
         */

        loadLocal();
        MapKitFactory.initialize(this);

        DirectionsFactory.initialize(this);
        SearchFactory.initialize(this);


        animationHandler = new Handler();



//        TransportFactory.initialize(this);
        View decorView = getWindow().getDecorView();
// Calling setSystemUiVisibility() with a value of 0 clears
// all flags.
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_map);



        super.onCreate(savedInstanceState);

        mapView = findViewById(R.id.mapview);


        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED);


        // turnGPSOn();


        instance = this;
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        updateLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(MapActivity.this, "Siz bu soragnamany tassyklamaly", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();



        myLocation1 = findViewById(R.id.myLocation);
        share_my_location = findViewById(R.id.share_my_location);

        zoomUp = findViewById(R.id.zoomUp);
        zoomDown = findViewById(R.id.zoomDown);
        routing = findViewById(R.id.routing);
        camera = findViewById(R.id.camera);
        mode = findViewById(R.id.mode);
        maptype = findViewById(R.id.maptype);
        close = findViewById(R.id.close);
        drawRoute = findViewById(R.id.drawRoute);

        drawFunctionalRoute = findViewById(R.id.drawFunctionalRoute);

        final BottomSheetLayout bottomSheet = (BottomSheetLayout) findViewById(R.id.bottomsheet);


//        bottomSheet.showWithSheetView(new IntentPickerSheetView(this, shareIntent, "Share with...", new IntentPickerSheetView.OnIntentPickedListener() {
//            @Override
//            public void onIntentPicked(IntentPickerSheetView.ActivityInfo activityInfo) {
//                bottomSheet.dismissSheet();
//                startActivity(activityInfo.getConcreteIntent(shareIntent));
//            }
//        }));


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

//        listofsearch.add("Hello");
//        listofsearch.add("world");
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,listofsearch);
//        searchEdit.setAdapter(adapter);

        mapView.getMap().setRotateGesturesEnabled(true);
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String str = sdf.format(new Date());
        int hour = Integer.parseInt(str);
        //Toast.makeText(context,hour+"",Toast.LENGTH_SHORT).show();
        if (hour >= 18 || hour <= 7) {
            mapView.getMap().setNightModeEnabled(true);
            mode.setImageResource(R.drawable.ic_baseline_wb_sunny_24);
        }

        mapView.getMap().addInputListener(this);
//        mapView.getMap().setMapType(MapType.HYBRID);
//        BoundingBox s= new BoundingBox(ROUTE_START_LOCATION, ROUTE_END_LOCATION);
//        mapView.getMap().cameraPosition(s);



        mapKit = MapKitFactory.getInstance();
//        userLocationLayer = mapKit.createUserLocationLayer(mapView.getMapWindow());
//        userLocationLayer.setVisible(true);
//        userLocationLayer.setHeadingEnabled(false);
//        userLocationLayer.setObjectListener(MapActivity.this);

        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter();
        mapObjects = mapView.getMap().getMapObjects().addCollection();

        //submitRequest();

        //routing();



        Intent browserIntent=getIntent();
        if(browserIntent.getExtras()!=null){
            // Toast.makeText(context, "Has intent", Toast.LENGTH_SHORT).show();
            Bundle bundle = browserIntent.getExtras();
            Set<String> bundleKeySet = bundle.keySet(); // string key set
            for(String key : bundleKeySet){ // traverse and print pairs
                // Toast.makeText(context, ""+key+" : " + bundle.get(key), Toast.LENGTH_SHORT).show();

                //  Log.e(key," : " + bundle.get(key)+"::"+data.toString())
            }
            Uri data = browserIntent.getData();
            String fullUrl=data.toString();

            //"maps.yandex.ru/?pt="+currentLocation.getLongitude()+","+currentLocation.getLatitude()+"&z=20";
            String lat="",lon="";
            boolean isLongitude=false;
            for(int i=0;i<fullUrl.length();i++){
                if(i>=26){
                    String indexChar= String.valueOf(fullUrl.charAt(i));
                    if(!isLongitude) {

                        if(indexChar.equals(",")){
                            isLongitude=true;
                        } else{
                            lat += fullUrl.charAt(i);
                        }


                    } else if(isLongitude){
                        if(indexChar.equals("&")){
                            break;
                            // isLongitude=false;
                        } else{
                            lon += fullUrl.charAt(i);
                        }
                    }
                }
            }
            Log.e("Latitude",lon);
            Log.e("Longitude",lat);

            try{
                Double first=Double.parseDouble(lon);
                Double second=Double.parseDouble(lat);


                mapView.getMap().move(new CameraPosition(new Point(first, second), 20, -105, 200),
                        new Animation(Animation.Type.SMOOTH, 0),
                        null);
                ROUTE_END_LOCATION=new Point(first,second);
                search_mark = mapObjects.addPlacemark(
                        ROUTE_END_LOCATION,
                        ImageProvider.fromResource(context, R.drawable.blue_marker));

                search_mark.setVisible(true);
                search_mark.setGeometry(new Point(first,second));


                drawRoute.setVisibility(View.VISIBLE);

            } catch (Exception ex){
                Toast.makeText(context, getString(R.string.error_title2), Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
            }
            //Toast.makeText(context, ""+lat+","+lon, Toast.LENGTH_SHORT).show();
        } else{
            // Toast.makeText(context, "Has not intent", Toast.LENGTH_SHORT).show();
            mapView.getMap().move(new CameraPosition(new Point(37.95, 58.3833), 14, -105, 200),
                    new Animation(Animation.Type.SMOOTH, 6),
                    null);
        }

        drawRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                buildLocationRequest();
                if ((ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)) {

                    ActivityCompat.requestPermissions(MapActivity.this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }

                final Task location = fusedLocationProviderClient.getLastLocation();

                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            android.location.Location currentLocation = (android.location.Location) task.getResult();
                            if (currentLocation != null) {
                                ROUTE_START_LOCATION = new Point(currentLocation.getLatitude(), currentLocation.getLongitude());
                                drawRoute.setVisibility(View.GONE);
                                mapObjects.clear();

                                search_mark = mapObjects.addPlacemark(
                                        ROUTE_END_LOCATION,
                                        ImageProvider.fromResource(context, R.drawable.blue_marker));

                                search_mark.setVisible(true);

                                e_marker = mapObjects.addPlacemark(
                                        new Point(0, 0),
                                        ImageProvider.fromResource(context, R.drawable.red_mark));
                                e_marker.setVisible(false);

                                s_marker = mapObjects.addPlacemark(
                                        ROUTE_START_LOCATION,
                                        ImageProvider.fromResource(context, R.drawable.green_mark));
                                s_marker.setVisible(true);
                                submitRequest();


                                isMarkerHas = false;

                                if (isFirstTime) {

                                } else {
                                    myMarker.setGeometry(ROUTE_START_LOCATION);
                                    myMarker.setDirection(currentLocation.getBearing());
                                }
                            } else {
                                locationEnabled();
                            }


                            // Toast.makeText(context,"Location: "+currentLocation.getLatitude(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        routing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawRoute.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(getResources().getString(R.string.attention));
                builder.setMessage(getResources().getString(R.string.select));
                builder.setPositiveButton(getResources().getString(R.string.from_my), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        closeRouteAlert();

                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setMessage(getResources().getString(R.string.attention));
                        alert.setMessage(getResources().getString(R.string.points));
                        alert.setCancelable(false);
                        alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alert.show();
                        s = 0;
                        type = 1;
                        //  routing();
                        if (isMarkerHas) {
                            s_marker.setVisible(false);
                            e_marker.setVisible(false);
                        }
                    }
                });

                builder.setNegativeButton(getResources().getString(R.string.two_lo), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        closeRouteAlert();
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setMessage(getResources().getString(R.string.attention));
                        alert.setMessage(getResources().getString(R.string.points));
                        alert.setCancelable(false);
                        alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alert.show();
                        s = 0;
                        tap_counter = 0;
                        type = 2;
                        end_type = 0;
                        // routing();
                        if (isMarkerHas) {
                            s_marker.setVisible(false);
                            e_marker.setVisible(false);
                        }
                    }
                });

                //   builder.setIcon(R.drawable.question);
                builder.show();
            }
        });

//        MasstransitOptions options = new MasstransitOptions(
//                new ArrayList<String>(),
//                new ArrayList<String>(),
//                new TimeOptions());
//        List<RequestPoint> points = new ArrayList<RequestPoint>();
//        points.add(new RequestPoint(ROUTE_START_LOCATION, RequestPointType.WAYPOINT, null));
//        points.add(new RequestPoint(ROUTE_END_LOCATION, RequestPointType.WAYPOINT, null));
//        mtRouter = TransportFactory.getInstance().createMasstransitRouter();
//        mtRouter.requestRoutes(points, options, this);


        mapView.getMap().addTapListener(this);

        search_mark = mapObjects.addPlacemark(
                new Point(0, 0),
                ImageProvider.fromResource(context, R.drawable.blue_marker));

        search_mark.setVisible(false);
        //mapView.getMap().addInputListener(this);


        myLocation1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //locationEnabled();
                buildLocationRequest();
                if ((ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)) {

                    ActivityCompat.requestPermissions(MapActivity.this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }

                final Task location = fusedLocationProviderClient.getLastLocation();

                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            android.location.Location currentLocation = (android.location.Location) task.getResult();
                            if (currentLocation != null) {
                                Point point = new Point(currentLocation.getLatitude(), currentLocation.getLongitude());
                                mapView.getMap().move(new CameraPosition(point, mapView.getMap().getCameraPosition().getZoom(), mapView.getMap().getCameraPosition().getAzimuth(), mapView.getMap().getCameraPosition().getTilt()),
                                        new Animation(Animation.Type.SMOOTH, 3),
                                        null);


                                if (isFirstTime) {

                                } else {
                                    myMarker.setGeometry(point);
                                    myMarker.setDirection(currentLocation.getBearing());
                                }
                            } else {
                                locationEnabled();
                            }


                            // Toast.makeText(context,"Location: "+currentLocation.getLatitude(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });


//               mapKit.createLocationManager().requestSingleUpdate(new LocationListener() {
//                   @Override
//                   public void onLocationUpdated(@NonNull Location location) {
//                       mapView.getMap().move(new CameraPosition(new Point(location.getPosition().getLatitude(), location.getPosition().getLongitude()), mapView.getMap().getCameraPosition().getZoom(), mapView.getMap().getCameraPosition().getAzimuth(), mapView.getMap().getCameraPosition().getTilt()),
//                               new Animation(Animation.Type.SMOOTH, 3),
//                               null);
//
////                       mapView.getMap().getMapObjects().addPlacemark(location.getPosition(),
////                               ImageProvider.fromResource(MapActivity.this, R.drawable.user_arrow),
////                               new IconStyle().setAnchor(new PointF((float) 0.5,(float) 0.5))
////                                       .setRotationType(RotationType.ROTATE)
////                                       .setZIndex(0f)
////                                       .setScale(1f)
////                                        .setFlat(false)
////                       );
//                       ROUTE_START_LOCATION=location.getPosition();
//
//
////                       mtRouter = MapKitFactory.getInstance().createMasstransitRouter();
////                       mtRouter.requestRoutes(ROUTE_START_LOCATION, ROUTE_END_LOCATION,
////                               new MasstransitOptions(new ArrayList<String>(), new ArrayList<String>(),
////                                       // Specify departure time or arrival time here
////                                       new TimeOptions()),
////                               this);
//
//                   }
//
//                   @Override
//                   public void onLocationStatusUpdated(@NonNull LocationStatus locationStatus) {
//
//                   }
//               });


            }
        });

        // Toast.makeText(context,mapView.getMap().getMapType().name(),Toast.LENGTH_SHORT).show();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeRoute();
            }
        });

        share_my_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMylocation=true;
                showDialog();
            }
        });


        final View bottomView = LayoutInflater.from(context).inflate(R.layout.my_sheet_layout, bottomSheet, false);
        TextView cancelText = bottomView.findViewById(R.id.cancel);
        searchEdit = bottomView.findViewById(R.id.searchEdit);
        listView = bottomView.findViewById(R.id.list);
        no_search = bottomView.findViewById(R.id.no_search);

        search_btn = bottomView.findViewById(R.id.search);
        history_btn = bottomView.findViewById(R.id.history);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_btn.setBackgroundResource(R.drawable.corner_white);
                search_btn.setTextColor(Color.WHITE);
                history_btn.setBackgroundResource(R.drawable.corner_gray);
                history_btn.setTextColor(Color.parseColor("#656565"));
                listofsearch.clear();
                ListAdapter adapter = new ListAdapter(context, listofsearch);
                listView.setAdapter(adapter);
                getListOfSearch(searchEdit.getText().toString());


            }
        });

        history_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_btn.setBackgroundResource(R.drawable.corner_gray);
                search_btn.setTextColor(Color.parseColor("#656565"));
                history_btn.setBackgroundResource(R.drawable.corner_white);
                history_btn.setTextColor(Color.WHITE);
                SearchDB searchDB = new SearchDB(context);


                Cursor cursor = searchDB.getAll();
                if (cursor.getCount() != 0) {
                    listofsearch.clear();
                    while (cursor.moveToNext()) {
                        Double lat = Double.parseDouble(cursor.getString(2));
                        Double lon = Double.parseDouble(cursor.getString(3));
                        listofsearch.add(new SearchItem(cursor.getString(1), new Point(lat, lon)));
                    }

                }


                ListAdapter adapter = new ListAdapter(context, listofsearch);
                listView.setAdapter(adapter);
                loading.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);

                if (listofsearch.size() == 0) {
                    listView.setVisibility(View.GONE);
                    loading.setVisibility(View.GONE);
                    no_search.setVisibility(View.VISIBLE);
                } else {
                    listView.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                    no_search.setVisibility(View.GONE);
                }
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchEdit.setText(listofsearch.get(position).getName());
                SearchDB searchDB = new SearchDB(context);

                Cursor cursor = searchDB.getSelect(listofsearch.get(position).getName());
                if (cursor.getCount() == 0) {
                    searchDB.insertData(listofsearch.get(position).getName(), listofsearch.get(position).getPoint().getLatitude() + "", listofsearch.get(position).getPoint().getLongitude() + "");
                }


                mapView.getMap().move(new CameraPosition(listofsearch.get(position).getPoint(), 17, mapView.getMap().getCameraPosition().getAzimuth(), mapView.getMap().getCameraPosition().getTilt()),
                        new Animation(Animation.Type.SMOOTH, 0),
                        null);
                bottomSheet.dismissSheet();
                search_mark.setGeometry(listofsearch.get(position).getPoint());
                search_mark.setVisible(true);

                ROUTE_END_LOCATION = listofsearch.get(position).getPoint();
                drawRoute.setVisibility(View.VISIBLE);

                search_btn.setBackgroundResource(R.drawable.corner_white);
                search_btn.setTextColor(Color.WHITE);
                history_btn.setBackgroundResource(R.drawable.corner_gray);
                history_btn.setTextColor(Color.parseColor("#656565"));
                // Toast.makeText(context, listofsearch.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });

        loading = bottomView.findViewById(R.id.loading);

        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.dismissSheet();
            }
        });


        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getListOfSearch(searchEdit.getText().toString());
                    // submitQuery(searchEdit.getText().toString());
                }
                return false;
            }
        });


        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getListOfSearch(s.toString());

                if (!s.toString().isEmpty()) {
                    no_search.setVisibility(View.GONE);
                }
//                } else{
//                    no_search.setVisibility(View.VISIBLE);
//                    listView.setVisibility(View.GONE);
//                    loading.setVisibility(View.GONE);
//                }

                if (listofsearch.size() == 0) {
                    loading.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                } else {
                    loading.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        maptype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.showWithSheetView(bottomView);
            }
        });


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isCameraMoveWithMyLocation) {
                    isCameraMoveWithMyLocation = false;
                    camera.setImageResource(R.drawable.ic_baseline_videocam_off_24);

                } else {
                    isCameraMoveWithMyLocation = true;
                    camera.setImageResource(R.drawable.ic_baseline_videocam_24);

                    // locationEnabled();
                    buildLocationRequest();
                    if ((ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED)) {

                        ActivityCompat.requestPermissions(MapActivity.this, new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    }

                    final Task location = fusedLocationProviderClient.getLastLocation();

                    location.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                android.location.Location currentLocation = (android.location.Location) task.getResult();
                                if (currentLocation != null) {
                                    Point point = new Point(currentLocation.getLatitude(), currentLocation.getLongitude());
                                    mapView.getMap().move(new CameraPosition(point, mapView.getMap().getCameraPosition().getZoom(), mapView.getMap().getCameraPosition().getAzimuth(), mapView.getMap().getCameraPosition().getTilt()),
                                            new Animation(Animation.Type.SMOOTH, 3),
                                            null);


                                    if (isFirstTime) {

                                    } else {
                                        myMarker.setGeometry(point);
                                        myMarker.setDirection(currentLocation.getBearing());
                                    }
                                } else {
                                    locationEnabled();
                                }


                                // Toast.makeText(context,"Location: "+currentLocation.getLatitude(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });


        mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapView.getMap().isNightModeEnabled()) {
                    mode.setImageResource(R.drawable.ic_baseline_nights_stay_24);
                    mapView.getMap().setNightModeEnabled(false);
                } else {
                    mode.setImageResource(R.drawable.ic_baseline_wb_sunny_24);
                    mapView.getMap().setNightModeEnabled(true);
                }
            }
        });


        levelText = (TextView) findViewById(R.id.traffic_light_text);
        levelIcon = (ImageButton) findViewById(R.id.traffic_light);
        traffic = MapKitFactory.getInstance().createTrafficLayer(mapView.getMapWindow());
        traffic.setTrafficVisible(true);
        traffic.addTrafficListener(this);
        updateLevel();

        zoomUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.getMap().move(new CameraPosition(mapView.getMap().getCameraPosition().getTarget(),
                                mapView.getMap().getCameraPosition().getZoom() + 1, mapView.getMap().getCameraPosition().getAzimuth(), mapView.getMap().getCameraPosition().getTilt()),
                        new Animation(Animation.Type.SMOOTH, 1),
                        null);
            }
        });

        zoomDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.getMap().move(new CameraPosition(mapView.getMap().getCameraPosition().getTarget(),
                                mapView.getMap().getCameraPosition().getZoom() - 1, mapView.getMap().getCameraPosition().getAzimuth(), mapView.getMap().getCameraPosition().getTilt()),
                        new Animation(Animation.Type.SMOOTH, 1),
                        null);
            }
        });

        submitQuery(searchEdit.getText().toString());


//        mapView.getMap().setMapType(MapType.VECTOR_MAP);
//
//        // Apply customization
//        try {
//            mapView.getMap().setMapStyle(style());
//        }
//        catch (IOException e) {
//            Log.e("Custom", "Failed to read customization style", e);
//        }

        // And to show what can be done with it, we move the camera to the center of Saint Petersburg.
        // map.move(new CameraPosition(TARGET_LOCATION, 15.0f, 0.0f, 0.0f));


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 15 && resultCode == RESULT_OK) {
            // Get the URI and query the content provider for the phone number
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor cursor = context.getContentResolver().query(contactUri, projection,
                    null, null, null);

            // If the cursor returned is valid, get the phone number
            if (cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberIndex);
                smsEditText.setText(number);
                // Do something with the phone number
                // Toast.makeText(context, number+"", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
        }

        if(requestCode==16){

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void closeRoute() {
        android.view.animation.Animation slide_bottom = AnimationUtils.loadAnimation(context, R.anim.slide_bottom);
        android.view.animation.Animation slideUp = AnimationUtils.loadAnimation(context, R.anim.close_btn);
        LinearLayout linearLayout = findViewById(R.id.linear);
        close.startAnimation(slideUp);
        linearLayout.startAnimation(slide_bottom);
        close.setVisibility(View.GONE);
        drawFunctionalRoute.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
        if (isMarkerHas) {
            s_marker.setVisible(false);
            e_marker.setVisible(false);
        }

        mapObjects.clear();

        search_mark = mapObjects.addPlacemark(
                new Point(0, 0),
                ImageProvider.fromResource(context, R.drawable.blue_marker));

        search_mark.setVisible(false);


        e_marker = mapObjects.addPlacemark(
                new Point(0, 0),
                ImageProvider.fromResource(context, R.drawable.red_mark));
        e_marker.setVisible(false);

        s_marker = mapObjects.addPlacemark(
                new Point(0, 0),
                ImageProvider.fromResource(context, R.drawable.green_mark));
        s_marker.setVisible(false);

        isMarkerHas = false;
    }

    private void closeRouteAlert() {

        LinearLayout linearLayout = findViewById(R.id.linear);
        close.setVisibility(View.GONE);
        drawFunctionalRoute.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
        if (isMarkerHas) {
            s_marker.setVisible(false);
            e_marker.setVisible(false);
        }

        mapObjects.clear();

        search_mark = mapObjects.addPlacemark(
                new Point(0, 0),
                ImageProvider.fromResource(context, R.drawable.blue_marker));

        search_mark.setVisible(false);


        e_marker = mapObjects.addPlacemark(
                new Point(0, 0),
                ImageProvider.fromResource(context, R.drawable.red_mark));
        e_marker.setVisible(false);

        s_marker = mapObjects.addPlacemark(
                new Point(0, 0),
                ImageProvider.fromResource(context, R.drawable.green_mark));
        s_marker.setVisible(false);

        isMarkerHas = false;
    }

    @Override
    protected void onStop() {
        // Activity onStop call must be passed to both MapView and MapKit instance.
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        // Activity onStart call must be passed to both MapView and MapKit instance.
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

//    @Override
//    public void onObjectAdded(UserLocationView userLocationView) {
//
//
//        userLocationLayer.resetAnchor();
//    }
//
//    @Override
//    public void onObjectRemoved(UserLocationView view) {
//    }
//
//    @Override
//    public void onObjectUpdated(UserLocationView view, ObjectEvent event) {
//        userLocationLayer.resetAnchor();
//    }

    private void updateLevel() {
        int iconId;
        String level = "";
        if (!traffic.isTrafficVisible()) {
            iconId = R.drawable.icon_traffic_light_dark;
        } else if (trafficFreshness == TrafficFreshness.Loading) {
            iconId = R.drawable.icon_traffic_light_violet;
        } else if (trafficFreshness == TrafficFreshness.Expired) {
            iconId = R.drawable.icon_traffic_light_blue;
        } else if (trafficLevel == null) {  // state is fresh but region has no data
            iconId = R.drawable.icon_traffic_light_green;
        } else {
            switch (trafficLevel.getColor()) {
                case RED:
                    iconId = R.drawable.icon_traffic_light_red;
                    break;
                case GREEN:
                    iconId = R.drawable.icon_traffic_light_green;
                    break;
                case YELLOW:
                    iconId = R.drawable.icon_traffic_light_yellow;
                    break;
                default:
                    iconId = R.drawable.icon_traffic_light_grey;
                    break;
            }
            level = Integer.toString(trafficLevel.getLevel());
        }
        levelIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(), iconId));
        levelText.setText(level);
    }

    public void onLightClick(View view) {
        traffic.setTrafficVisible(!traffic.isTrafficVisible());
        updateLevel();
    }

    public void onClickBack(View view) {
        finish();
    }

    @Override
    public void onTrafficChanged(TrafficLevel trafficLevel) {
        this.trafficLevel = trafficLevel;
        this.trafficFreshness = TrafficFreshness.OK;
        updateLevel();
    }

    @Override
    public void onTrafficLoading() {
        this.trafficLevel = null;
        this.trafficFreshness = TrafficFreshness.Loading;
        updateLevel();
    }

    @Override
    public void onTrafficExpired() {
        this.trafficLevel = null;
        this.trafficFreshness = TrafficFreshness.Expired;
        updateLevel();
    }

    private void locationEnabled() {
        LocationManager lm = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(MapActivity.this)
                    .setMessage(getResources().getString(R.string.gps_enable))
                    .setPositiveButton(getResources().getString(R.string.sazlama), new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                    .setNegativeButton(getResources().getString(R.string.cancel), null)
                    .show();
        }
    }


//    private void drawSection(SectionMetadata.SectionData data,
//                             Polyline geometry) {
//        // Draw a section polyline on a map
//        // Set its color depending on the information which the section contains
//        PolylineMapObject polylineMapObject = mapObjects.addPolyline(geometry);
//        // Masstransit route section defines exactly one on the following
//        // 1. Wait until public transport unit arrives
//        // 2. Walk
//        // 3. Transfer to a nearby stop (typically transfer to a connected
//        //    underground station)
//        // 4. Ride on a public transport
//        // Check the corresponding object for null to get to know which
//        // kind of section it is
//        if (data.getTransports() != null) {
//            // A ride on a public transport section contains information about
//            // all known public transport lines which can be used to travel from
//            // the start of the section to the end of the section without transfers
//            // along a similar geometry
//            for (Transport transport : data.getTransports()) {
//                // Some public transport lines may have a color associated with them
//                // Typically this is the case of underground lines
//                if (transport.getLine().getStyle() != null) {
//                    polylineMapObject.setStrokeColor(
//                            // The color is in RRGGBB 24-bit format
//                            // Convert it to AARRGGBB 32-bit format, set alpha to 255 (opaque)
//                            transport.getLine().getStyle().getColor() | 0xFF000000
//                    );
//                    return;
//                }
//            }
//            // Let us draw bus lines in green and tramway lines in red
//            // Draw any other public transport lines in blue
//            HashSet<String> knownVehicleTypes = new HashSet<>();
//            knownVehicleTypes.add("bus");
//            knownVehicleTypes.add("tramway");
//            for (Transport transport : data.getTransports()) {
//                String sectionVehicleType = getVehicleType(transport, knownVehicleTypes);
//                if (sectionVehicleType.equals("bus")) {
//                    polylineMapObject.setStrokeColor(0xFF00FF00);  // Green
//                    return;
//                } else if (sectionVehicleType.equals("tramway")) {
//                    polylineMapObject.setStrokeColor(0xFFFF0000);  // Red
//                    return;
//                } else{
//                    polylineMapObject.setStrokeColor(0xFFFF0000);  // Red
//                    return;
//                }
//
//            }
//            polylineMapObject.setStrokeColor(0xFF0000FF);  // Blue
//            Toast.makeText(context,"works",Toast.LENGTH_LONG).show();
//        } else {
//            // This is not a public transport ride section
//            // In this example let us draw it in black
//            polylineMapObject.setStrokeColor(Color.RED);  // Black
//        }
//    }
//
//    private String getVehicleType(Transport transport, HashSet<String> knownVehicleTypes) {
//        // A public transport line may have a few 'vehicle types' associated with it
//        // These vehicle types are sorted from more specific (say, 'histroic_tram')
//        // to more common (say, 'tramway').
//        // Your application does not know the list of all vehicle types that occur in the data
//        // (because this list is expanding over time), therefore to get the vehicle type of
//        // a public line you should iterate from the more specific ones to more common ones
//        // until you get a vehicle type which you can process
//        // Some examples of vehicle types:
//        // "bus", "minibus", "trolleybus", "tramway", "underground", "railway"
//        for (String type : transport.getLine().getVehicleTypes()) {
//            Toast.makeText(context,type,Toast.LENGTH_LONG).show();
//            if (knownVehicleTypes.contains(type)) {
//
//                return type;
//            }
//        }
//        return null;
//    }


    private void submitRequest() {
        DrivingOptions drivingOptions = new DrivingOptions();
        VehicleOptions vehicleOptions = new VehicleOptions();


        ArrayList<RequestPoint> requestPoints = new ArrayList<>();
        requestPoints.clear();
        requestPoints.add(new RequestPoint(
                ROUTE_START_LOCATION,
                RequestPointType.WAYPOINT,
                null));
        requestPoints.add(new RequestPoint(
                ROUTE_END_LOCATION,
                RequestPointType.WAYPOINT,
                null));
        drivingSession = drivingRouter.requestRoutes(requestPoints, drivingOptions, vehicleOptions, this);

        search_mark = mapObjects.addPlacemark(
                new Point(0, 0),
                ImageProvider.fromResource(context, R.drawable.blue_marker));

        search_mark.setVisible(false);
    }


    private String readRawResource(String name) throws IOException {
        final StringBuilder builder = new StringBuilder();
        final int resourceIdentifier =
                getResources().getIdentifier(name, "raw", getPackageName());
        InputStream is = getResources().openRawResource(resourceIdentifier);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException ex) {
            Log.e("Custom", "Cannot read raw resource " + name);
            throw ex;
        } finally {
            reader.close();
        }

        return builder.toString();
    }

    private String style() throws IOException {
        return readRawResource("customization_example");
    }


    public void showDialog() {
        Button back, cancel;
        TextView tv1, tv2, t3, t4,my_location_tv,select_map_tv;
        ImageView status,mylocation_dialog_icon,select_map_icon;
        LinearLayout my_location_dialog,select_map_dialog;
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.sendmessagedialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.DialogAnimation;
        Typeface Ping_medium = Typeface.createFromAsset(context.getAssets(), "fonts/Ping LCG Medium.otf");
        back = dialog.findViewById(R.id.back);
        smsEditText = dialog.findViewById(R.id.tel);
        tv1 = dialog.findViewById(R.id.t1);
        tv2 = dialog.findViewById(R.id.t2);
        t3 = dialog.findViewById(R.id.t3);
        t4 = dialog.findViewById(R.id.t4);
        status = dialog.findViewById(R.id.status);
        cancel = dialog.findViewById(R.id.cancel);

        my_location_tv = dialog.findViewById(R.id.my_location_tv);
        select_map_tv = dialog.findViewById(R.id.select_map_tv);
        mylocation_dialog_icon = dialog.findViewById(R.id.mylocation_dialog_icon);
        select_map_icon = dialog.findViewById(R.id.select_map_icon);
        my_location_dialog = dialog.findViewById(R.id.my_location_dialog);
        select_map_dialog = dialog.findViewById(R.id.select_map_dialog);


        my_location_tv.setTypeface(Ping_medium);
        select_map_tv.setTypeface(Ping_medium);

        if(isMylocation){
            my_location_dialog.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.first));
            //mylocation_dialog_icon.setImageTintList(ContextCompat.getColorStateList(context,R.color.second));
            my_location_tv.setTextColor(getResources().getColor(R.color.second));

            select_map_dialog.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.second));
           // select_map_icon.setImageTintList(ContextCompat.getColorStateList(context,R.color.third));
            select_map_tv.setTextColor(getResources().getColor(R.color.third));
        } else{
            my_location_dialog.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.second));
            //mylocation_dialog_icon.setImageTintList(ContextCompat.getColorStateList(context,R.color.third));
            my_location_tv.setTextColor(getResources().getColor(R.color.third));

            select_map_dialog.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.first));
           // select_map_icon.setImageTintList(ContextCompat.getColorStateList(context,R.color.second));
            select_map_tv.setTextColor(getResources().getColor(R.color.second));
        }

        my_location_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_location_dialog.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.first));
               // mylocation_dialog_icon.setImageTintList(ContextCompat.getColorStateList(context,R.color.second));
                my_location_tv.setTextColor(getResources().getColor(R.color.second));

                select_map_dialog.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.second));
               // select_map_icon.setImageTintList(ContextCompat.getColorStateList(context,R.color.third));
                select_map_tv.setTextColor(getResources().getColor(R.color.third));
                isMylocation=true;
            }
        });


        select_map_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_location_dialog.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.second));
            //    mylocation_dialog_icon.setImageTintList(ContextCompat.getColorStateList(context,R.color.third));
                my_location_tv.setTextColor(getResources().getColor(R.color.third));

                select_map_dialog.setBackgroundTintList(ContextCompat.getColorStateList(context,R.color.first));
             //   select_map_icon.setImageTintList(ContextCompat.getColorStateList(context,R.color.second));
                select_map_tv.setTextColor(getResources().getColor(R.color.second));
                isMylocation=false;
            }
        });



        tv1.setTypeface(Ping_medium);
        tv2.setTypeface(Ping_medium);
        t3.setTypeface(Ping_medium);
        t4.setTypeface(Ping_medium);
        smsEditText.setTypeface(Ping_medium);
        cancel.setTypeface(Ping_medium);
        back.setTypeface(Ping_medium);

        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(i, 15);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    if(isMylocation) {
                        getMyLocation();
                    } else{
                        if(share_map_location!=null){
                            smsMessage="maps.yandex.ru/?pt="+share_map_location.getLongitude()+","+share_map_location.getLatitude()+"&z=20";
                            //smsMessage="http://maps.google.com/maps?z=12&t=m&q=loc:"+currentLocation.getLatitude()+"+"+currentLocation.getLongitude()+"";
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT,
                                    smsMessage);
                            sendIntent.setType("text/plain");
                            startActivityForResult(sendIntent,16);
                            dialog.dismiss();
                        } else{
                            Toast.makeText(context, getResources().getString(R.string.yer_sayla), Toast.LENGTH_LONG).show();
                        }
                    }

                // dialog.dismiss();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.setCancelable(true);
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }

    private void getMyLocation() {
        buildLocationRequest();
        if ((ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)) {

            ActivityCompat.requestPermissions(MapActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        final Task location = fusedLocationProviderClient.getLastLocation();

        location.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    android.location.Location currentLocation = (android.location.Location) task.getResult();
                    if (currentLocation != null) {
                       // smsMessage = "google.com/maps/search/?api=1&query="+currentLocation.getLatitude()+","+currentLocation.getLongitude()+"";//"maps.google.com/maps?saddr=" + currentLocation.getLatitude() + "," + currentLocation.getLongitude();
                        smsMessage="maps.yandex.ru/?pt="+currentLocation.getLongitude()+","+currentLocation.getLatitude()+"&z=20";
                        //smsMessage="http://maps.google.com/maps?z=12&t=m&q=loc:"+currentLocation.getLatitude()+"+"+currentLocation.getLongitude()+"";
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT,
                                smsMessage);
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                        dialog.dismiss();
                    } else {
                        locationEnabled();
                    }


                    // Toast.makeText(context,"Location: "+currentLocation.getLatitude(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        //saved data to shared preferences
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    // load language saved in shared preferences
    public void loadLocal() {
        SharedPreferences share = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String languages = share.getString("My_Lang", "");
        setLocale(languages);
    }

}



