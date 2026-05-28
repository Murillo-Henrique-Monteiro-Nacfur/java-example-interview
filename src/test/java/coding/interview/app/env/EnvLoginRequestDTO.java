package coding.interview.app.env;

import coding.interview.app.api.dto.authentication.LoginRequestDTO;

public class EnvLoginRequestDTO {

    public static LoginRequestDTO getLoginRequestDTO(){
        return LoginRequestDTO
                .builder()
                .login("user@test.com")
                .password("password123")
                .build();
    }
}
