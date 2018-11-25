package com.example.varunbalachanthiran.taxicustomer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CallFragment extends Fragment {

    private View mView;
    private TextView pickUpAddress;
    private TextView destinationAddress;
    private TextView estimatedCost;
    private TextView estimatedDistance;
    private TextView pickupTime;
    private TextView pets;
    private TextView bicycle;
    private TextView extraLuggage;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.content_main, container, false);
        pickUpAddress = mView.findViewById(R.id.pickupAddress);
        destinationAddress = mView.findViewById(R.id.destinationAddress);
        estimatedCost = mView.findViewById(R.id.estimatedCost);
        estimatedDistance = mView.findViewById(R.id.distance);
        pickupTime = mView.findViewById(R.id.pickupTime);
        pets = mView.findViewById(R.id.pets);
        bicycle = mView.findViewById(R.id.bicycles);
        extraLuggage = mView.findViewById(R.id.extraLuggage);

        return mView;
    }
}
