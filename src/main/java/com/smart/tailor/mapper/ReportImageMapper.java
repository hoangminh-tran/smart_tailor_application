package com.smart.tailor.mapper;

import com.smart.tailor.entities.ReportImage;
import com.smart.tailor.utils.response.ReportImageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Base64;

@Mapper(componentModel = "spring")
public interface ReportImageMapper {
    @Mapping(target = "reportImage.reportImageUrl", expression = "java(decodeByteArrayToString(reportImage.getReportImageUrl()))")
    @Mapping(source = "reportImage.createDate", target = "createDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(source = "reportImage.lastModifiedDate", target = "lastModifiedDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    ReportImageResponse mapperToReportImageResponse(ReportImage reportImage);

    default String decodeByteArrayToString(byte[] values) {
        if (values != null) {
            return new String(Base64.getDecoder().decode(values));
        }
        return null;
    }
}
