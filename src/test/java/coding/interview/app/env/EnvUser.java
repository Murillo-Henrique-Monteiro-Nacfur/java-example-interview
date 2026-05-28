package coding.interview.app.env;

import coding.interview.app.api.entities.UserRoles;
import coding.interview.app.api.entities.Users;

import java.time.LocalDate;

public class EnvUser {
    public static Users getUsers(){
        return Users.builder()
                .id(1L)
                .name("User")
                .login("user@example.com")
                .password("password")
                .role(UserRoles.ADMIN)
                .birthDate(LocalDate.of(1990, 1, 1))
                .build();

    }
}
