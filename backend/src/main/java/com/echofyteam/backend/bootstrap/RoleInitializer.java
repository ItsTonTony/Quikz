package com.echofyteam.backend.bootstrap;

import com.echofyteam.backend.feature.role.entity.Permission;
import com.echofyteam.backend.feature.role.entity.Role;
import com.echofyteam.backend.feature.role.repository.PermissionRepository;
import com.echofyteam.backend.feature.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RoleInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    private static final String[] DEFAULT_PERMISSIONS = {
            "READ_PRIVILEGE",
            "WRITE_PRIVILEGE"
    };

    private static final Map<String, String[]> DEFAULT_ROLES = Map.of(
            "ADMIN", new String[] {"READ_PRIVILEGE", "WRITE_PRIVILEGE"},
            "USER", new String[] {"READ_PRIVILEGE"}
    );

    @Override
    public void run(String... args) throws Exception {
        Map<String, Permission> permissionsMap = new HashMap<>();
        for (String code : DEFAULT_PERMISSIONS) {
            Permission permission = permissionRepository.findByCode(code)
                    .orElseGet(() -> permissionRepository.save(
                            Permission.builder().code(code).build()));
            permissionsMap.put(code, permission);
        }

        for (var entry : DEFAULT_ROLES.entrySet()) {
            String roleName = entry.getKey();
            String[] permissionCodes = entry.getValue();

            roleRepository.findByName(roleName)
                    .orElseGet(() -> {
                        Set<Permission> perms = Arrays.stream(permissionCodes)
                                .map(permissionsMap::get)
                                .collect(Collectors.toSet());
                        Role newRole = Role.builder()
                                .name(roleName)
                                .permissions(perms)
                                .build();
                        return roleRepository.save(newRole);
                    });
        }
    }
}
