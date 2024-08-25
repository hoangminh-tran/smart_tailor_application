package com.smart.tailor.mapper;

import com.smart.tailor.entities.DesignDetail;
import com.smart.tailor.utils.response.DesignDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.transaction.annotation.Transactional;

@Mapper(componentModel = "spring", uses = {OrderMapper.class, BrandMapper.class, SizeMapper.class})
public interface DesignDetailMapper {
    @Transactional(readOnly = true)
    @Mapping(source = "designDetail.designDetailID", target = "designDetailId")
    @Mapping(source = "designDetail.quantity", target = "quantity")
    @Mapping(source = "designDetail.size", target = "size")
    @Mapping(source = "designDetail.detailStatus", target = "detailStatus")
    DesignDetailResponse mapperToDesignDetailResponse(DesignDetail designDetail);
}
