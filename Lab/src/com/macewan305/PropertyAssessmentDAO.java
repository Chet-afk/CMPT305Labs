package com.macewan305;

public interface PropertyAssessmentDAO {

    PropertyAssessment getAccountNum(int accountNumber);
    PropertyAssessment[] getNeighbourhood(String nameOfNeighbourhood);

}
