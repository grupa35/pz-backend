package pl.shopgen.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.builders.ApiErrorMessageBuilder;
import pl.shopgen.codes.ApiStatusCode;
import pl.shopgen.models.ErrorDTO;
import pl.shopgen.models.Role;
import pl.shopgen.repositories.RoleRepository;

import java.util.Map;

@RestController
@RequestMapping("/roles")
public class RoleController extends AbstractController {

    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String addRole(@RequestBody Map<String, String> roleNameMap) {
        String roleName = roleNameMap.getOrDefault("name", null);
        if(roleName == null) {
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setCode(ApiStatusCode.BAD_ARGUMENT);
            errorDTO.setMessage(ApiErrorMessageBuilder.getInstance()
                    .badParameter("name", "not exists").build());
            return mapToJson(errorDTO);
        } else if(roleRepository.findByName(roleName).isPresent()) {
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setCode(ApiStatusCode.OBJECT_EXISTS);
            errorDTO.setMessage(ApiErrorMessageBuilder.getInstance()
                    .badParameter("name", "exists")
                    .build());
            return mapToJson(errorDTO);

        } else {
            return mapToJson(roleRepository.insert(new Role(roleName)));
        }
    }

    @RequestMapping(value = "/{roleId}", method = RequestMethod.DELETE)
    public String deleteRole(@PathVariable("roleId") String RoleId) {
        return mapToJson(roleRepository.findById(RoleId).orElse(null));
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String deleteRoles() {
        String rolesJson = mapToJson(roleRepository.findAll());
        roleRepository.deleteAll();
        return rolesJson;
    }

    @RequestMapping(value = "/{roleId}", method = RequestMethod.GET)
    @ResponseBody
    public String getRole(@PathVariable("roleId") String roleId) {
        Role role = roleRepository.findById(roleId).orElse(null);
        return mapToJson(role);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String updateRole(@RequestBody Role role) {
        if(role.getName() == null || role.getName().equals("")) {
            return mapToJson(new ErrorDTO(ApiStatusCode.BAD_ARGUMENT, ApiErrorMessageBuilder.getInstance()
                    .badParameter("name", "Cannot create update role by setting empty name")
                    .build()));
        } else {
            return mapToJson(roleRepository.save(role));
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getRoles() {
        return mapToJson(roleRepository.findAll());
    }
}
