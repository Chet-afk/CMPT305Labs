package com.macewan305;

import java.text.NumberFormat;
import java.util.Objects;

interface Comparable{
    boolean compareAssessValue(Object otherProperty);
}

public class PropertyAssessment implements Comparable{
    private final int accountNum;
    private final String suite;
    private final String houseNum;
    private final String streetName;
    private final String garage;
    private final String neighID;
    private final String neighName;
    private final String ward;
    private final int assessment;
    private final String lat;
    private final String lon;
    private final String point;
    private final String assess1P;
    private final String assess2P;
    private final String assess3P;
    private final String assess1Name;
    private final String assess2Name;
    private final String assess3Name;


    // Section for Constructors
    // No default constructor since this data should not be initialized with nothing. As such, there are no mutator functions.
    public PropertyAssessment(int accountNum, String suite, String houseNum, String streetName,
                              String garage, String neighID, String neighName, String ward,
                              int assessment, String lat, String lon, String point, String assess1P,
                              String assess2P, String assess3P, String assess1Name, String assess2Name, String assess3Name){
        this.accountNum = accountNum;
        this.suite = suite;
        this.houseNum = houseNum;
        this.streetName = streetName;
        this.garage = garage;
        this.neighID = neighID;
        this.neighName = neighName;
        this.ward = ward;
        this.assessment = assessment;
        this.lat = lat;
        this.lon = lon;
        this.point = point;
        this.assess1P = assess1P;
        this.assess2P = assess2P;
        this.assess3P = assess3P;
        this.assess1Name = assess1Name;
        this.assess2Name = assess2Name;
        this.assess3Name = assess3Name;

    }


    // Getters

    public int accountNum(){
        return this.accountNum;
    }
    public String getSuite(){
        return this.suite;
    }
    public String getHouseNum(){
        return this.houseNum;
    }
    public String getStreet(){
        return this.streetName;
    }
    public String garage(){
        return this.garage;
    }
    public String neighbourhoodID(){
        return this.neighID;
    }
    public String neighbourhoodName(){
        return this.neighName;
    }
    public String getWard(){
        return this.ward;
    }
    public int assessmentVal(){
        return this.assessment;
    }
    public String latitude(){
        return this.lat;
    }
    public String longitude(){
        return this.lon;
    }
    public String point(){
        return this.point;
    }
    public String assess1P(){
        return this.assess1P;
    }
    public String assess2P(){
        return this.assess2P;
    }
    public String assess3P(){
        return this.assess3P;
    }
    public String assess1Name(){
        return this.assess1Name;
    }
    public String assess2Name(){
        return this.assess2Name;
    }
    public String assess3Name(){
        return this.assess3Name;
    }

    // More advanced Getters.
    public String Address(){
        return String.join(" ",this.suite, this.houseNum, this.streetName);
    }
    public String Location(){
        return String.join(" ","(" + this.lon +",",this.lat + ")");
    }
    public String AllClasses() {
        if (this.assess2P.compareTo("") == 0) {
            return String.join(" ", "[" + this.assess1Name, this.assess1P + "%]");
        } else if (this.assess3P.compareTo("") == 0) {
            return String.join(" ", "[" + this.assess1Name, this.assess1P + "%,", this.assess2Name, this.assess2P + "%]");
        } else{
            return String.join(" ", "[" +this.assess1Name, this.assess1P + "%,", this.assess2Name, this.assess2P + "%,",
                    this.assess3Name, this.assess3P+"%]" );
        }
    }
    public String Area(){
        return String.join(" ", this.neighName, "(" + this.ward + ")");
    }

    public boolean compareAssessValue(Object otherProp){
        if (otherProp == null || otherProp.getClass() != this.getClass()){
            System.out.println("Not a valid Property object");
            return false;
        }
        else{
            if(((PropertyAssessment) otherProp).assessment >= this.assessment){ // Return True if other property is greater than or equal to this property
                return true;
            }
            else{
                return false;
            }
        }
    }


    // Overrides
    public String toString(){
        return ("Account Number: " + accountNum +
                "\nSuite: " + suite +
                "\nHouse Number: " + houseNum +
                "\nStreet Name: " + streetName +
                "\nGarage: " + garage +
                "\nNeighbourhood ID: " + neighID +
                "\nNeighbourhood Name: " + neighName +
                "\nWard: " + ward +
                "\nAssessment Value: $" + NumberFormat.getIntegerInstance().format(assessment) +
                "\nLatitude: " + lat +
                "\nLongitude: " + lon +
                "\nPoint: " + point +
                "\nAssessment 1 Name and Percent: " + assess1Name + assess1P + " %" +
                "\nAssessment 2 Name and Percent: " + assess2Name + assess2P + " %" +
                "\nAssessment 3 Name and Percent: " + assess3Name + assess3P + " %");
    }

    public boolean equals(Object x){
        if (this == x){
            return true;
        }
        if(x == null || this.getClass() != x.getClass()){
            return false;
        }

        PropertyAssessment y = (PropertyAssessment) x;
        return this.hashCode() == y.hashCode();
    }

    public int hashCode(){
        return Objects.hash(accountNum, suite, houseNum, streetName,garage, neighID,neighName,ward,
                assessment,lat,lon,point,assess1P,assess1Name,assess2P,assess2Name,assess3P,assess3Name);
    }
}
