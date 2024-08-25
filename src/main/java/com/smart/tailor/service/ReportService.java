package com.smart.tailor.service;

import com.smart.tailor.utils.request.ReportRequest;
import com.smart.tailor.utils.response.ReportResponse;

import java.util.List;


public interface ReportService {
    void createReport(String jwtToken, ReportRequest reportRequest) throws Exception;

    List<ReportResponse> getAllReport();

    List<ReportResponse> getAllReportByOrderID(String orderID);

    List<ReportResponse> getAllReportByUserID(String jwtToken, String userID) throws Exception;

    List<ReportResponse> getAllReportByBrandID(String jwtToken, String brandID) throws Exception;

    List<ReportResponse> getAllReportByParentOrderID(String jwtToken, String parentOrderID);
}
