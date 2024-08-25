package com.smart.tailor.mapper;

import com.smart.tailor.entities.ExpertTailoring;
import com.smart.tailor.utils.response.ExpertTailoringResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpertTailoringMapper {
    @Mapping(source = "expertTailoring.expertTailoringID", target = "expertTailoringID")
    @Mapping(source = "expertTailoring.status", target = "status")
    @Mapping(source = "expertTailoring.createDate", target = "createDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(source = "expertTailoring.lastModifiedDate", target = "lastModifiedDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    ExpertTailoringResponse mapperToExpertTailoringResponse(ExpertTailoring expertTailoring);
}
