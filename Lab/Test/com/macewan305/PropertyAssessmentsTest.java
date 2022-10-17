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
        assertEquals(1208, filtered.length);
        filtered = neighbourHoodFilter(testProperties, "Does Not Exist");
        assertNull(filtered);
    }

    @Test
    void assessClassFilterTest() {
        PropertyAssessment[] filtered = assessClassFilter(testProperties, "residential");
        assertEquals(389388, filtered.length);
        filtered = assessClassFilter(testProperties, "Does Not Exist");
        assertNull(filtered);
    }


    @Test
    void findAccountTest() {
        PropertyAssessment output = findAccount(testProperties, 1103530);
        assertEquals(1103530, output.accountNum());
        output = findAccount(testProperties, 1);
        assertNull(output);
    }
}