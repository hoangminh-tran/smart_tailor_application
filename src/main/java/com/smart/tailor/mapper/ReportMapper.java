package com.smart.tailor.mapper;

import com.smart.tailor.entities.Report;
import com.smart.tailor.utils.response.ReportResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderMapper.class, ReportImageMapper.class, UserMapper.class})
public interface ReportMapper {
    @Mapping(source = "report.user", target = "userResponse")
    @Mapping(source = "report.order", target = "orderResponse")
    @Mapping(source = "report.reportImageList", target = "reportImageList")
    @Mapping(source = "report.createDate", target = "createDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(source = "report.lastModifiedDate", target = "lastModifiedDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    ReportResponse mapperToReportResponse(Report report);
}
