package coding.interview.app.infrastructure.security.service;

import coding.interview.app.api.dto.authentication.UserAuthenticatedDTO;
import coding.interview.app.api.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserService UserService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        var user = UserService.findUserByLogin(login);
        return new UserAuthenticatedDTO(user.getId(), user.getName(), user.getPassword(),user.getRole());
    }
}