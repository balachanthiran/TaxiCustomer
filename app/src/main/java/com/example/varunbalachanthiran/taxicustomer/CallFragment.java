package com.example.varunbalachanthiran.taxicustomer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        taxiType = mView.findViewById(R.id.call_taxitype);
        System.out.println("nigger: " + Order.getPickUpPlaceAddress());

        if(Order.getPickUpPlaceAddress() != null && Order.getDestination() != null && Order.getPickUpTime() != null) {
            pickUpAddress.setText(Order.getPickUpPlaceAddress());
            destinationAddress.setText(Order.getDestination());
            estimatedCost.setText(Order.getEstimatedCost());
            estimatedDistance.setText(Order.getDistance());
            pickupTime.setText(Order.getPickUpTime());
            taxiType.setText(Vehicle.getCarType());
            System.out.println("CHECK: " + Order.isBicycles());

            if(Order.isPets() == false) {
                pets.setVisibility(View.INVISIBLE);
                pets.setHeight(0);
            } else {
                pets.setText("Kæledyr");
            }
            if(Order.isBicycles() == false) {
                bicycles.setVisibility(View.INVISIBLE);
                bicycles.setHeight(0);
            } else {
                bicycles.setText("2 cykler");
            }
            if(Order.isExtraLuggage() == false) {
                extraLuggage.setVisibility(View.INVISIBLE);
                extraLuggage.setHeight(0);
            } else {
                extraLuggage.setText("Ekstra baggage");
            }
            if(Order.isPets() == false && Order.isBicycles() == false && Order.isExtraLuggage() == false) {
                pets.setText("Ingen tilvalg blev valgt");
                bicycles.setHeight(0);
                extraLuggage.setHeight(0);
                tilvalg.setText("Tilvalg:" + "\n" + "Ingen tilvalg blev valgt");
            } else {
                tilvalg.setText("Tilvalg");
            }
        }

        return mView;
    }
}
