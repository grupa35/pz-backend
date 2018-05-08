package pl.shopgen.controllers;

import org.springframework.web.bind.annotation.*;
import pl.shopgen.models.Role;
import pl.shopgen.models.RoleRepository;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Role addRole(@RequestBody Role role) {
        return roleRepository.insert(role);
    }

    @RequestMapping(value = "/{roleId}", method = RequestMethod.DELETE)
    public Role deleteRole(@PathVariable("roleId") String RoleId) {
        Role role = roleRepository.findById(RoleId).orElse(null);
        if (role != null) {
            roleRepository.deleteById(RoleId);
        }
        return role;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public List<Role> deleteRoles() {
        List<Role> roles = roleRepository.findAll();
        roleRepository.deleteAll();
        return roles;
    }

    @RequestMapping(value = "/{roleId}", method = RequestMethod.GET)
    @ResponseBody
    public Role getRole(@PathVariable("roleId") String RoleId) {
        return roleRepository.findById(RoleId).orElse(null);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Role updateRole(@RequestBody Role role) {
        return roleRepository.save(role);
    }
}
