package com.geekspace.beyikyol.Activity.Map;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.geekspace.beyikyol.R;
import com.yandex.mapkit.map.CameraListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CameraUpdateReason;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.VisibleRegionUtils;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.search.Response;
import com.yandex.mapkit.search.SearchManager;
import com.yandex.mapkit.search.SearchOptions;
import com.yandex.mapkit.search.Session;
import com.yandex.runtime.Error;

public class MapSearch extends AppCompatActivity implements Session.SearchListener, CameraListener {
    EditText searchEdit;
    TextView cancel;
    ListView list;
    Context context=this;

    private final String MAPKIT_API_KEY = "your_api_key";

    private MapView mapView;
    private SearchManager searchManager;
    private Session searchSession;

    private void submitQuery(String query) {
        searchSession = searchManager.submit(
                query,
                VisibleRegionUtils.toPolygon(mapView.getMap().getVisibleRegion()),
                new SearchOptions(),
                this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);
      //  searchEdit=findViewById(R.id.search_edit);
        cancel=findViewById(R.id.cancel);
        list=findViewById(R.id.list);



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // Animatoo.animateSlideDown(context);
    }

    @Override
    public void onCameraPositionChanged(@NonNull Map map, @NonNull CameraPosition cameraPosition, @NonNull CameraUpdateReason cameraUpdateReason, boolean b) {

    }

    @Override
    public void onSearchResponse(@NonNull Response response) {

    }

    @Override
    public void onSearchError(@NonNull Error error) {

    }
}