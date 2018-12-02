package com.example.varunbalachanthiran.taxicustomer;

import android.animation.ValueAnimator;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.varunbalachanthiran.taxicustomer.Enitity.Order;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TrackerFragment extends Fragment implements OnMapReadyCallback, RoutingListener {

    private GoogleMap mMap;
    private MapView mMapView;
    private Polyline blackPolyline;
    private View mView;
    private Handler handler;
    private int index;
    private int next;
    private float v;
    private double lat, lng;
    private Marker carMarker;
    private LatLng startPosition, endPosition, pickUpPlaceCoordinates;
    private List<LatLng> polylineList;
    private PolylineOptions blackPolylineOptions;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_tracker, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = mView.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
        polylineList = new ArrayList<>();
        pickUpPlaceCoordinates = Order.getPickUpPlaceCoordinates();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mMap = googleMap;
        startPosition = new LatLng(55.370617, 10.4270541);
        endPosition = new LatLng(55.401779, 10.3845503);
        mMap.addMarker(new MarkerOptions()
                .position(pickUpPlaceCoordinates)
                .title("Afhentning").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.addMarker(new MarkerOptions()
                .position(Order.getDestinationCoordinates())
                .title("Destination"));
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom((startPosition), 18.2f));

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
        if (Order.getPickUpPlaceCoordinates() != null) {
            try {
                Routing routing = new Routing.Builder()
                        .travelMode(Routing.TravelMode.DRIVING)
                        .withListener(this)
                        .waypoints(startPosition, pickUpPlaceCoordinates)
                        .key("AIzaSyAA7Z1CpXCo7KAFRzyWRuXR8EayP4KclQo")
                        .build();
                routing.execute();
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Unable to Route", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "No order is made", Toast.LENGTH_SHORT).show();
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
    public void onRoutingSuccess(final ArrayList<com.directions.route.Route> arrayList, final int i) {
        try {
            polylineList = arrayList.get(i).getPoints();

            blackPolylineOptions = new PolylineOptions();
            blackPolylineOptions.color(Color.BLACK);
            blackPolylineOptions.width(5);
            blackPolylineOptions.addAll(polylineList);
            blackPolyline = mMap.addPolyline(blackPolylineOptions);

            //Add car marker
            carMarker = mMap.addMarker(new MarkerOptions().position(startPosition).flat(true).title("Taxi").icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
            //car moving
            handler = new Handler();
            index = 0;
            next = 1;

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (index < polylineList.size() - 1) {
                        index++;
                        next = index + 1;
                    }
                    if (index < polylineList.size() - 1) {
                        startPosition = polylineList.get(index);
                        pickUpPlaceCoordinates = polylineList.get(next);
                    }

                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
                    valueAnimator.setDuration(1000);
                    valueAnimator.setInterpolator(new LinearInterpolator());
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            v = animation.getAnimatedFraction();
                            lng = v * pickUpPlaceCoordinates.longitude + (1 - v) * startPosition.longitude;
                            lat = v * pickUpPlaceCoordinates.latitude + (1 - v) * startPosition.latitude;
                            LatLng newPos = new LatLng(lat, lng);
                            carMarker.setPosition(newPos);
                            carMarker.setAnchor(0.5f, 0.5f);
                            //carMarker.setRotation(getBearing(startPosition, newPos));
                            float rotation = getBearing(startPosition, newPos);
                            if (carMarker.getRotation() > rotation) {
                                carMarker.setRotation(carMarker.getRotation() - animation.getAnimatedFraction());
                            } else {
                                carMarker.setRotation(carMarker.getRotation() + animation.getAnimatedFraction());
                            }
                            //mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(newPos).zoom(15.5f).build()));

                        }
                    });
                    valueAnimator.start();
                    handler.postDelayed(this, 1000);


                }
            }, 1000);


        } catch (Exception e) {
            Toast.makeText(getActivity(), "EXCEPTION: Cannot parse routing response", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingCancelled() {

    }

    //Courtesy of Google Inc.
    private float getBearing(LatLng orign, LatLng dest) {
        double lat = Math.abs(orign.latitude - dest.latitude);
        double lng = Math.abs(orign.longitude - dest.longitude);
        if (orign.latitude < dest.latitude && orign.longitude < dest.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (orign.latitude >= dest.latitude && orign.longitude < dest.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (orign.latitude >= dest.latitude && orign.longitude >= dest.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (orign.latitude < dest.latitude && orign.longitude >= dest.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }


}
