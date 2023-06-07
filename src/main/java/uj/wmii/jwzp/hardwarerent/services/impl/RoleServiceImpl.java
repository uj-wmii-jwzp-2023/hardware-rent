package uj.wmii.jwzp.hardwarerent.services.impl;

import org.springframework.stereotype.Service;
import uj.wmii.jwzp.hardwarerent.services.interfaces.RoleService;
import uj.wmii.jwzp.hardwarerent.repositories.RoleRepository;
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
}
