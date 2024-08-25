package com.smart.tailor.utils.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListSizeRequest {
    @NotEmpty(message = "sizeRequestList can not be empty")
    List<SizeRequest> sizeRequestList;
}
