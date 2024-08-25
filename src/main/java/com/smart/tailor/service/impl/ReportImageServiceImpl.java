package com.smart.tailor.service.impl;

import com.smart.tailor.entities.Report;
import com.smart.tailor.entities.ReportImage;
import com.smart.tailor.mapper.ReportImageMapper;
import com.smart.tailor.repository.ReportImageRepository;
import com.smart.tailor.service.ReportImageService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.ReportImageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportImageServiceImpl implements ReportImageService {
    private final Logger logger = LoggerFactory.getLogger(ReportImageServiceImpl.class);
    private final ReportImageRepository reportImageRepository;
    private final ReportImageMapper reportImageMapper;

    @Transactional
    @Override
    public List<ReportImage> createReportImage(Report report, List<ReportImageRequest> reportImageRequestList) {
        List<ReportImage> reportImageList = new ArrayList<>();
        for (ReportImageRequest reportImageRequest : reportImageRequestList) {
            byte[] base64ReportImageUrl = null;
            if (Optional.ofNullable(reportImageRequest.getReportImageUrl()).isPresent()) {
                base64ReportImageUrl = Utilities.encodeStringToBase64(reportImageRequest.getReportImageUrl());
            }

            var saveReportImage = reportImageRepository.save(
                    ReportImage
                            .builder()
                            .reportImageName(reportImageRequest.getReportImageName())
                            .reportImageUrl(base64ReportImageUrl)
                            .report(report)
                            .build()
            );

            reportImageList.add(saveReportImage);
        }
        return reportImageList;
    }
}
