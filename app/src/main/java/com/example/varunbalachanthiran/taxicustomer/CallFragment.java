package com.example.varunbalachanthiran.taxicustomer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.varunbalachanthiran.taxicustomer.Enitity.Order;
import com.example.varunbalachanthiran.taxicustomer.Enitity.Vehicle;

public class CallFragment extends Fragment {

    private View mView;
    private TextView pickUpAddress;
    private TextView destinationAddress;
    private TextView estimatedCost;
    private TextView estimatedDistance;
    private TextView pickupTime;
    private TextView tilvalg;
    private TextView taxiType;
    private TextView pets;
    private TextView bicycles;
    private TextView extraLuggage;
    private Button trackerBtn;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_call, container, false);
        pickUpAddress = mView.findViewById(R.id.call_afhentning);
        destinationAddress = mView.findViewById(R.id.call_destination);
        estimatedCost = mView.findViewById(R.id.call_estimatedCost);
        estimatedDistance = mView.findViewById(R.id.call_estimatedDistance);
        pickupTime = mView.findViewById(R.id.call_date);
        tilvalg = mView.findViewById(R.id.call_ekstratilvalg);
        pets = mView.findViewById(R.id.call_pets);
        bicycles = mView.findViewById(R.id.call_bicycles);
        extraLuggage = mView.findViewById(R.id.call_extraluggage);
        trackerBtn = mView.findViewById(R.id.track);
        trackerBtn.setVisibility(View.INVISIBLE);

        taxiType = mView.findViewById(R.id.call_taxitype);

        trackerBtn.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!getActivity().isFinishing() && Order.getPickUpPlaceAddress() != null && Order.getDestination() != null && Order.getPickUpTime() != null) { // make checks to see if activity is still available
                    trackerBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Tracking of taxi is now available", Toast.LENGTH_SHORT).show();
                }
            }
        }, 1000 * 5);

        if (Order.getPickUpPlaceAddress() != null && Order.getDestination() != null && Order.getPickUpTime() != null) {
            pickUpAddress.setText(Order.getPickUpPlaceAddress());
            destinationAddress.setText(Order.getDestination());
            estimatedCost.setText(Order.getEstimatedCost());
            estimatedDistance.setText(Order.getDistance());
            pickupTime.setText(Order.getPickUpTime());
            taxiType.setText(Vehicle.getCarType());

            System.out.println("CHECK: " + Order.isBicycles());

            if (Order.isPets() == false) {
                pets.setVisibility(View.INVISIBLE);
                pets.setHeight(0);
            } else {
                pets.setText("Kæledyr");
            }
            if (Order.isBicycles() == false) {
                bicycles.setVisibility(View.INVISIBLE);
                bicycles.setHeight(0);
            } else {
                bicycles.setText("2 cykler");
            }
            if (Order.isExtraLuggage() == false) {
                extraLuggage.setVisibility(View.INVISIBLE);
                extraLuggage.setHeight(0);
            } else {
                extraLuggage.setText("Ekstra baggage");
            }
            if (Order.isPets() == false && Order.isBicycles() == false && Order.isExtraLuggage() == false) {
                pets.setText("Ingen tilvalg blev valgt");
                bicycles.setHeight(0);
                extraLuggage.setHeight(0);
                tilvalg.setText("Tilvalg:" + "\n" + "Ingen tilvalg blev valgt");
            } else {
                tilvalg.setText("Tilvalg");
            }
        }

        trackerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new TrackerFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return mView;
    }


}
