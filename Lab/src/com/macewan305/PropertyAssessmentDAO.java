package com.macewan305;

import java.io.UnsupportedEncodingException;
import java.util.*;

public interface PropertyAssessmentDAO {

    PropertyAssessment getAccountNum(int accountNumber);
    List<PropertyAssessment> getNeighbourhood(String nameOfNeighbourhood);
    List<PropertyAssessment>  getAssessClass(String nameOfAssessClass) throws UnsupportedEncodingException;
    List<PropertyAssessment>  getWard(String nameOfWard);
    List<PropertyAssessment>  getAll();
    List<PropertyAssessment> getData(int limit);
    List<PropertyAssessment> getData(int limit, int offset);
}
