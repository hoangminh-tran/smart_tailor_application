package com.smart.tailor.utils.response;

import lombok.*;
import lombok.experimental.FieldDefaults;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
    private String roleID;
    private String roleName;
}
