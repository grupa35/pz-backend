package pl.shopgen.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.builders.ApiErrorMessageBuilder;
import pl.shopgen.codes.ApiStatusCode;
import pl.shopgen.models.Role;
import pl.shopgen.models.RoleDto;
import pl.shopgen.repositories.RoleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public RoleDto addRole(@RequestBody Map<String, String> roleNameMap) {
        RoleDto roleDto;
        String roleName = roleNameMap.getOrDefault("name", null);
        if(roleName == null) {
            roleDto = new RoleDto();
            roleDto.setStatus(ApiStatusCode.BAD_ARGUMENT);
            roleDto.setErrorMessage(ApiErrorMessageBuilder.getInstance()
                    .badParameter("name", "not exists").build());
            return roleDto;
        } else if(roleRepository.findByName(roleName).isPresent()) {
            roleDto = new RoleDto(roleRepository.findByName(roleName).orElse(null));
            roleDto.setStatus(ApiStatusCode.OBJECT_EXISTS);
            roleDto.setErrorMessage(ApiErrorMessageBuilder.getInstance()
                    .badParameter("name", "exists")
                    .build());
        } else {
            roleDto = new RoleDto(roleRepository.insert(new Role(roleName)));
            roleDto.setStatus(ApiStatusCode.SUCCESS);
        }

        return roleDto;
    }

    @RequestMapping(value = "/{roleId}", method = RequestMethod.DELETE)
    public RoleDto deleteRole(@PathVariable("roleId") String RoleId) {
        RoleDto roleDto;

        Role role = roleRepository.findById(RoleId).orElse(null);
        if(role == null) {
            roleDto = new RoleDto();
            roleDto.setStatus(ApiStatusCode.NOT_FOUND);
        } else {
            roleRepository.deleteById(RoleId);
            roleDto = new RoleDto(role);
            roleDto.setStatus(ApiStatusCode.SUCCESS);
        }
        return roleDto;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public List<RoleDto> deleteRoles() {
        List<Role> roles = roleRepository.findAll();
        roleRepository.deleteAll();
        List<RoleDto> roleDtos = new ArrayList<>();
        roles.forEach(role -> {
            RoleDto roleDto = new RoleDto(role);
            roleDto.setStatus(ApiStatusCode.SUCCESS);
            roleDtos.add(roleDto);
        });
        return roleDtos;
    }

    @RequestMapping(value = "/{roleId}", method = RequestMethod.GET)
    @ResponseBody
    public RoleDto getRole(@PathVariable("roleId") String roleId) {
        Role role = roleRepository.findById(roleId).orElse(null);
        RoleDto roleDto = new RoleDto(role);

        if(role == null) {
            roleDto.setStatus(ApiStatusCode.NOT_FOUND);
            roleDto.setErrorMessage(ApiErrorMessageBuilder.getInstance()
                    .notFound("Not found role with id: " + roleId)
                    .build());
        } else {
            roleDto.setStatus(ApiStatusCode.SUCCESS);
        }

        return roleDto;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public RoleDto updateRole(@RequestBody Role role) {
        RoleDto roleDto;

        if(role == null) {
            roleDto = new RoleDto();
            roleDto.setStatus(ApiStatusCode.BAD_ARGUMENT);
        } else if(role.getName() == null || role.getName().equals("")) {
            roleDto = new RoleDto();
            roleDto.setStatus(ApiStatusCode.BAD_ARGUMENT);
            roleDto.setErrorMessage(ApiErrorMessageBuilder.getInstance()
                    .badParameter("name", "Cannot create update role by setting empty name")
                    .build());
        } else {
            roleDto = new RoleDto(roleRepository.save(role));
            roleDto.setStatus(ApiStatusCode.SUCCESS);
        }


        return roleDto;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<RoleDto> getRoles() {
        List<RoleDto> roleDtos = new ArrayList<>();
        roleRepository.findAll().forEach(role -> {
            RoleDto roleDto = new RoleDto(role);
            roleDto.setStatus(ApiStatusCode.SUCCESS);
            roleDtos.add(roleDto);
        });
        return roleDtos;
    }
}
