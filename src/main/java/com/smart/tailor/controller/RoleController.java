package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIConstant.RoleAPI.ROLE)
@RequiredArgsConstructor
@Slf4j
public class RoleController {
    private final RoleService roleService;
    private final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @GetMapping(APIConstant.RoleAPI.GET_ALL_ROLES)
    public ResponseEntity<ObjectNode> getAllRoles() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var listRole = roleService.findAllRole();
            if (!listRole.isEmpty()) {
                response.put("status", 200);
                response.put("message", MessageConstant.GET_ALL_ROLES_SUCCESSFULLY);
                response.set("data", objectMapper.valueToTree(listRole));
            } else {
                response.put("status", 200);
                response.put("message", MessageConstant.ROLE_LIST_IS_EMPTY);
            }
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", -1);
            response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN GET ALL ROLES. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}
