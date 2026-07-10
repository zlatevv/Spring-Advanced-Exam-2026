package bg.springadvancedexam.mainapp.dto.user;

import bg.springadvancedexam.mainapp.model.enums.Role;

public record UpdateRoleRequest(
        Role role
) {
}
