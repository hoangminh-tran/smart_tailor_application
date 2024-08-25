package com.smart.tailor.service.impl;

import com.smart.tailor.entities.Roles;
import com.smart.tailor.mapper.RoleMapper;
import com.smart.tailor.repository.RoleRepository;
import com.smart.tailor.service.RoleService;
import com.smart.tailor.utils.response.RoleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public Optional<Roles> findRoleByRoleName(String role_name) {
        return roleRepository.findByRoleName(role_name);
    }

    @Override
    public List<RoleResponse> findAllRole() {
        return roleRepository.findAll().stream().map(roleMapper::mapperToRoleResponse).collect(Collectors.toList());
    }

    @Override
    public void createRole(Roles roles) {
        roleRepository.save(roles);
    }
}
