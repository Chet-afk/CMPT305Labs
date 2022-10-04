package com.macewan305;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class PropertyAssessmentTest {
    private PropertyAssessment property1;
    private PropertyAssessment property2;
    private PropertyAssessment property3;

    @BeforeEach
    void setup(){
        property1 = new PropertyAssessment(101000 ,"3421","69230",
                "Test St.","Y","3515","Testing Neighbourhood","Honda Civic Ward",519603,"59.29503","102.352","POINT (102.352352, 59.29503319)",
                "100", "", "","RESIDENTIAL", "" ,"");

        property2 = new PropertyAssessment(101500 ,"","420",
                "Boulevard St.","N","1058","Probability Neighbourhood","Jaguar SVR F Type Ward",1572390,"109.5932049","132.3362","POINT (132.3362435, 109.5932049582)",
                "95", "5", "","RESIDENTIAL", "OTHER" ,"");

        property3 = new PropertyAssessment(101000 ,"3421","69230",
                "Test St.","Y","3515","Testing Neighbourhood","Honda Civic Ward",519603,"59.29503","102.352","POINT (102.352352, 59.29503319)",
                "100", "", "","RESIDENTIAL", "" ,"");
    }

    @Test
    void accountNum() {
        assertEquals(101000, property1.accountNum());
    }

    @Test
    void getSuite() {
        assertEquals("3421", property1.getSuite());
    }

    @Test
    void getHouseNum() {
        assertEquals("69230", property1.getHouseNum());
    }

    @Test
    void getStreet() {
        assertEquals("Test St.", property1.getStreet());
    }

    @Test
    void garage() {
        assertEquals("Y", property1.garage());
    }

    @Test
    void neighbourhoodID() {
        assertEquals("3515", property1.neighbourhoodID());
    }

    @Test
    void neighbourhoodName() {
        assertEquals("Testing Neighbourhood", property1.neighbourhoodName());
    }

    @Test
    void getWard() {
        assertEquals("Honda Civic Ward", property1.getWard());
    }

    @Test
    void assessmentVal() {
        assertEquals(519603, property1.assessmentVal());
    }

    @Test
    void latitude() {
        assertEquals("59.29503", property1.latitude());
    }

    @Test
    void longitude() {
        assertEquals("102.352", property1.longitude());
    }

    @Test
    void point() {
        assertEquals("POINT (102.352352, 59.29503319)",property1.point());
    }

    @Test
    void assess1P() {
        assertEquals("100", property1.assess1P());
    }

    @Test
    void assess2P() {
        assertEquals("", property1.assess2P());
    }

    @Test
    void assess3P() {
        assertEquals("", property1.assess3P());
    }

    @Test
    void assess1Name() {
        assertEquals("RESIDENTIAL", property1.assess1Name());
    }

    @Test
    void assess2Name() {
        assertEquals("", property1.assess2Name());
    }

    @Test
    void assess3Name() {
        assertEquals("", property1.assess3Name());
    }

    @Test
    void address() {
        assertEquals("3421 69230 Test St.", property1.Address());
    }

    @Test
    void location() {
        assertEquals("(102.352, 59.29503)", property1.Location());
    }

    @Test
    void allClasses() {
    }

    @Test
    void area() {
    }

    @Test
    void compareAssessValue() {
    }

    @Test
    void testToString() {
    }

    @Test
    void testEquals() {
    }

    @Test
    void testHashCode() {
    }
}