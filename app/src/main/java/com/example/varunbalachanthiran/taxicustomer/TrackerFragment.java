package com.example.varunbalachanthiran.taxicustomer;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TrackerFragment extends Fragment implements OnMapReadyCallback, RoutingListener {

    GoogleMap mMap;
    MapView mMapView;
    Polyline line;
    private View mView;
    LatLng origin;
    LatLng dest;
    String KEY1 = "AIzaSyBod8LTgKLRnycwZI-qDNoR_cYo_T1kJPQ";
    String KEY2 = "AIzaSyAPAX9ZKaC9pwklCwISzeBqmp6umINM8Fo";
    String KEY3 = "AIzaSyDbo8JM_Nluc631AHvSJG77W8NWamHfnrM";
    String KEY4 = "AIzaSyCmje-0G5PmooYaPrFaIlcYYtpUWGE15fo";
    String KEY5 = "AIzaSyDouE0Hb7F2XVb_JZBt8gmg2ZrAsz71itA";

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_tracker, container, false);
        return mView;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = mView.findViewById(R.id.map);
        if(mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mMap = googleMap;
        origin = new LatLng(55.370617,10.4270541);
        dest = new LatLng(55.401779,10.3845503);
        mMap.addMarker(new MarkerOptions()
                .position(origin)
                .title("origin"));
        mMap.addMarker(new MarkerOptions()
                .position(dest)
                .title("dest"));
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom((origin), 18.2f));

        makeRoute();
    }


    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    private void makeRoute() {
        try {
            Routing routing = new Routing.Builder()
                    .travelMode(Routing.TravelMode.DRIVING)
                    .withListener(this)
                    .waypoints(origin, dest)
                    .key(KEY3)
                    .build();
            routing.execute();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Unable to Route", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        System.out.println("fail " + e);

    }

    @Override
    public void onRoutingStart() {
    }

    @Override
    public void onRoutingSuccess(ArrayList<com.directions.route.Route> arrayList, int i) {
        try {
            ArrayList points;
            points = (ArrayList) arrayList.get(0).getPoints();
            PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);

            Iterator<LatLng> iterator = points.iterator();
            while (iterator.hasNext()) {
                LatLng data = iterator.next();
                options.add(data);
            }
            if (line != null) {
                line.remove();
            }
            line = mMap.addPolyline(options);



        } catch (Exception e) {
            Toast.makeText(getActivity(), "EXCEPTION: Cannot parse routing response", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingCancelled() {

    }

}
