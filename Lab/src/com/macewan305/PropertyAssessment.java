package com.macewan305;

import java.util.Objects;

public class PropertyAssessment {
    private int accountNum;
    private int suite;
    private int houseNum;
    private String streetName;
    private boolean garage;
    private int neighID;
    private String neighName;
    private String ward;
    private int assessment;
    private String lat;
    private String lon;
    private String point;
    private int assess1P;
    private int assess2P;
    private int assess3P;
    private String assess1Name;
    private String assess2Name;
    private String assess3Name;

    public PropertyAssessment(){        // Default object creation
        this(0,0,0,"",false,0,"","",0,"","","",0,0,0,"","","");
    }

    public PropertyAssessment(int accountNum, int suite, int houseNum, String streetName,
                              boolean garage, int neighID, String neighName, String ward,
                              int assessment, String lat, String lon, String point, int assess1P,
                              int assess2P, int assess3P, String assess1Name, String assess2Name, String assess3Name){
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

    public String toString(){
        return ("Account Number: " + accountNum +
                "\nSuite: " + suite +
                "\nHouse Number: " + houseNum +
                "\nStreet Name: " + streetName +
                "\nGarage: " + garage +
                "\nNeighbourhood ID: " + neighID +
                "\nNeighbourhood Name: " + neighName +
                "\nWard: " + ward +
                "\nAssessment Value: " + assessment +
                "\nLatitude: " + lat +
                "\nLongitude: " + lon +
                "\nPoint: " + point +
                "\nAssessment Name and Percent: " + assess1Name + assess1P + "%" +
                "\nAssessment Name and Percent: " + assess2Name + assess2P + "%" +
                "\nAssessment Name and Percent: " + assess3Name + assess3P + "%");
    }

    public boolean equals(Object x){
        if (this == x){
            return true;
        }
        if(x == null || this.getClass() != x.getClass()){
            return false;
        }

        PropertyAssessment y = (PropertyAssessment) x;
        return this.accountNum == y.accountNum;
    }

    public int hashCode(){
        return Objects.hash(accountNum, suite, houseNum, streetName,garage, neighID,neighName,ward,
                assessment,lat,lon,point,assess1P,assess1Name,assess2P,assess2Name,assess3P,assess3Name);
    }
}
