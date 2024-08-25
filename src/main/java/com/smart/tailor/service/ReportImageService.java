package com.smart.tailor.service;

import com.smart.tailor.entities.Report;
import com.smart.tailor.entities.ReportImage;
import com.smart.tailor.utils.request.ReportImageRequest;

import java.util.List;

public interface ReportImageService {
    List<ReportImage> createReportImage(Report report, List<ReportImageRequest> reportImageRequestList);
}
