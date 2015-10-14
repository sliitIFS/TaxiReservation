package com.example.user.mymapfinal;

/**
 * Created by user on 7/15/2015.
 */
public class Person {
    private String Pickup_Location;
    private String Destination;
    private int Passenger_Id;
    private String email;

    public int getPassenger_Id() {
        return Passenger_Id;
    }
    public void setPassenger_Id(int id)
    {
        this.Passenger_Id = id;
    }

    public String getPickup_Location() {
        return Pickup_Location;
    }
    public void setPickup_Location(String taskName) {
        this.Pickup_Location = taskName;
    }

    public String getDestination() {
        return Destination;
    }
    public void setDestination(String des) {
        this.Destination = des;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {

        this.email = email;
    }
}




