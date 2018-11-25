package com.example.varunbalachanthiran.taxicustomer.Enitity;

import com.google.android.gms.maps.model.LatLng;

public class Order {

    static String distance;
    static String pickUpTime;
    static String pickUpPlaceAddress;
    static String destination;
    static String estimatedCost;
    static boolean pets;
    static boolean bicycles;
    static boolean extraLuggage;
    static LatLng pickUpPlaceCoordinates;

    public static LatLng getPickUpPlaceCoordinates() {
        return pickUpPlaceCoordinates;
    }

    public static void setPickUpPlaceCoordinates(LatLng pickUpPlaceCoordinates) {
        Order.pickUpPlaceCoordinates = pickUpPlaceCoordinates;
    }

    public Order() {
    }

    public static String getPickUpTime() {
        return pickUpTime;
    }

    public static void setPickUpTime(String pickUpTime) {
        Order.pickUpTime = pickUpTime;
    }

    public static String getDistance() {
        return distance;
    }

    public static void setDistance(String distance) {
        Order.distance = distance;
    }

    public static String getPickUpPlaceAddress() {
        return pickUpPlaceAddress;
    }

    public static void setPickUpPlaceAddress(String pickUpPlaceAddress) {
        Order.pickUpPlaceAddress = pickUpPlaceAddress;
    }

    public static String getDestination() {
        return destination;
    }

    public static void setDestination(String destination) {
        Order.destination = destination;
    }

    public static String getEstimatedCost() {
        return estimatedCost;
    }

    public static void setEstimatedCost(String estimatedCost) {
        Order.estimatedCost = estimatedCost;
    }

    public static boolean isPets() {
        return pets;
    }

    public static void setPets(boolean pets) {
        Order.pets = pets;
    }

    public static boolean isBicycles() {
        return bicycles;
    }

    public static void setBicycles(boolean bicycles) {
        Order.bicycles = bicycles;
    }

    public static boolean isExtraLuggage() {
        return extraLuggage;
    }

    public static void setExtraLuggage(boolean extraLuggage) {
        Order.extraLuggage = extraLuggage;
    }
}