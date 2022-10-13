package com.macewan305;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.macewan305.PropertyAssessments.*;
import static org.junit.jupiter.api.Assertions.*;

class PropertyAssessmentsTest {

    private PropertyAssessment[] testProperties;

    @BeforeEach
    void setup() throws Exception {

        Path file = Paths.get("Property_Assessment_Data_2022.csv");
        testProperties = formatData(file);

    }

    @Test
    void neighbourHoodFilterTest() {
        PropertyAssessment[] filtered = neighbourHoodFilter(testProperties, "Granville");
        assertEquals(1208, numOfLines(filtered));
        filtered = neighbourHoodFilter(testProperties, "Does Not Exist");
        assertNull(filtered);
    }

    @Test
    void assessClassFilterTest() {
        PropertyAssessment[] filtered = assessClassFilter(testProperties, "residential");
        assertEquals(389388, numOfLines(filtered));
        filtered = assessClassFilter(testProperties, "Does Not Exist");
        assertNull(filtered);
    }

    @Test
    void numOfLinesTest() {
        assertEquals(416044, numOfLines(testProperties));
    }

    @Test
    void lowHighAssessTest() {
        int[] output = lowHighAssess(testProperties);
        assertEquals(0, output[0]);
        assertEquals(989492500, output[1]);
    }

    @Test
    void rangeTest() {
        int[] output = lowHighAssess(testProperties);
        assertEquals(989492500, range(output));
    }

    @Test
    void meanTest() {
        assertEquals(453045, mean(testProperties));
    }

    @Test
    void findAccountTest() {
        PropertyAssessment output = findAccount(testProperties, 1103530);
        assertEquals(1103530, output.accountNum());
        output = findAccount(testProperties, 1);
        assertNull(output);
    }
}