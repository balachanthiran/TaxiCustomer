

package com.example.varunbalachanthiran.taxicustomer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.varunbalachanthiran.taxicustomer.Enitity.Order;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.support.constraint.Constraints.TAG;

public class OrderFragment extends Fragment {

    String dateTime;
    private View mView;
    private Calendar mCalendar;
    private EditText mDate;
    private EditText pickUpAddress;
    private EditText destinationAddress;
    private TextView estimatedDistance;
    private TextView estimatedCost;
    private Switch pets;
    private Switch bicycles;
    private Switch extraLuggage;
    private Button orderButton;
    private int mDay,mMonth,mYear, mHour, mMinute;
    private FusedLocationProviderClient mFusedLocationClient;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP = 1;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE_DESTINATION = 2;
    boolean petsSwitchStatus;
    boolean bicycleSwitchStatus;
    boolean extraLuggageSwitchStatus;
    LatLng pickUpCoordinates;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_order, container, false);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        pickUpAddress = mView.findViewById(R.id.afhentning);
        destinationAddress = mView.findViewById(R.id.destination);
        pets = mView.findViewById(R.id.pets);
        bicycles = mView.findViewById(R.id.bicycles);
        extraLuggage = mView.findViewById(R.id.extraLuggage);
        estimatedDistance = mView.findViewById(R.id.estimatedDistance);
        estimatedCost = mView.findViewById(R.id.estimatedCost);
        orderButton = mView.findViewById(R.id.orderBtn);
        mCalendar = Calendar.getInstance();
        mDate= mView.findViewById(R.id.editText_date);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        //getLocation();
        pickUpPlace();
        destinationPlace();
        estimatedCostAndDistance();
        optionalService();
        orderTaxi();

        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialog();
            }
        });
        return mView;
    }

    public void dateDialog(){
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                dateTime = dayOfMonth + "/" + monthOfYear + "/" + year;
                timeDialog();
            }};
        DatePickerDialog dpDialog=new DatePickerDialog(getActivity(), listener, mYear, mMonth, mDay);
        dpDialog.show();
    }

    private void timeDialog(){
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay,int minute)
            {
                mHour = hourOfDay;
                mMinute = minute;
                mDate.setText(String.format(dateTime + "  -  " + "%02d:%02d", mHour, mMinute));
                Order.setPickUpTime(mDate.getText().toString());
            }
        }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    private void pickUpPlace(){
        pickUpAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });
    }

    private void destinationPlace(){
        destinationAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_DESTINATION);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_PICKUP) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.i(TAG, "Place: " + place.getLatLng());
                pickUpAddress.setText(place.getAddress());
                pickUpCoordinates = place.getLatLng();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        } else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_DESTINATION) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.i(TAG, "Place: " + place.getName());
                destinationAddress.setText(place.getAddress());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void estimatedCostAndDistance(){
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(pickUpAddress.getText().toString().length() != 0 && destinationAddress.getText().toString().length() != 0) {
                    estimatedDistance.setText("Distance: 13 km ");
                    estimatedCost.setText("Estimeret kost: 104 kr");
                }
            }
        };
        pickUpAddress.addTextChangedListener(textWatcher);
        destinationAddress.addTextChangedListener(textWatcher);
    }

    private void optionalService(){
        pets.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                petsSwitchStatus = isChecked;
                System.out.println("nigger: " + petsSwitchStatus);
            }
        });

        bicycles.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bicycleSwitchStatus = isChecked;
            }
        });

        extraLuggage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                extraLuggageSwitchStatus = isChecked;
            }
        });
    }

    private void orderTaxi(){
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pickUpAddress.getText().toString().length() == 0 || destinationAddress.getText().toString().length() == 0 || mDate.getText().toString().length() == 0 ) {
                    Toast.makeText(getActivity(), "Kan ikke bekræfte ordre. Udfyld venligst alle nødvendige informationer", Toast.LENGTH_LONG).show();
                } else{
                    Order.setPickUpPlaceAddress(pickUpAddress.getText().toString());
                    Order.setDestination(destinationAddress.getText().toString());
                    Order.setPickUpTime(mDate.getText().toString());
                    Order.setPets(petsSwitchStatus);
                    Order.setBicycles(bicycleSwitchStatus);
                    Order.setExtraLuggage(extraLuggageSwitchStatus);
                    Order.setDistance(estimatedDistance.getText().toString());
                    Order.setEstimatedCost(estimatedCost.getText().toString());
                    Order.setPickUpPlaceCoordinates(pickUpCoordinates);
                    Toast.makeText(getActivity(), "Order bekræftet", Toast.LENGTH_LONG).show();

                    pickUpAddress.setText("");
                    destinationAddress.setText("");
                    mDate.setText("");
                    estimatedDistance.setText("");
                    estimatedCost.setText("");
                    pets.setChecked(false);
                    bicycles.setChecked(false);
                    extraLuggage.setChecked(false);
                }

            }
        });
    }

    /* @SuppressLint("MissingPermission")
    private void getLocation(){
        System.out.println("nigger1");
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        if (location != null) {
                            try {
                                Geocoder geo = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                                List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
                                if (addresses.isEmpty()) {
                                } else {
                                    if (addresses.size() > 0) {
                                        String address = (addresses.get(0).getAddressLine(0));
                                        pickUpAddress.setText(address);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });



    }*/
}