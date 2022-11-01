package com.macewan305;

import java.io.UnsupportedEncodingException;
import java.util.*;

public interface PropertyAssessmentDAO {

    PropertyAssessment getAccountNum(int accountNumber) throws UnsupportedEncodingException;
    List<PropertyAssessment> getNeighbourhood(String nameOfNeighbourhood) throws UnsupportedEncodingException;
    List<PropertyAssessment>  getAssessClass(String nameOfAssessClass) throws UnsupportedEncodingException;
    List<PropertyAssessment>  getWard(String nameOfWard) throws UnsupportedEncodingException;
    List<PropertyAssessment>  getAll() throws UnsupportedEncodingException;
    List<PropertyAssessment> getData(int limit) throws UnsupportedEncodingException;
    List<PropertyAssessment> getData(int limit, int offset) throws UnsupportedEncodingException;
}
