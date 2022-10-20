package com.macewan305;

import java.util.*;

public interface PropertyAssessmentDAO {

    PropertyAssessment getAccountNum(int accountNumber);
    List<PropertyAssessment> getNeighbourhood(String nameOfNeighbourhood);
    List<PropertyAssessment>  getAssessClass(String nameOfAssessClass);
    List<PropertyAssessment>  getWard(String nameOfWard);
    List<PropertyAssessment>  getAll();

    List<PropertyAssessment> getData(int limit);

}
