package coding.interview.app.api.dto.authentication;

import coding.interview.app.api.entities.UserRoles;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserAuthenticatedDTOTest {

    @Test
    @DisplayName("Should return correct granted authority for single role")
    void shouldReturnCorrectGrantedAuthorityForSingleRole() {
        UserAuthenticatedDTO userDto = new UserAuthenticatedDTO(1L, "user", "pass", UserRoles.ADMIN);

        Collection<? extends GrantedAuthority> authorities = userDto.getAuthorities();

        assertThat(authorities).isNotNull();
        assertThat(authorities).hasSize(1);
        assertThat(authorities.iterator().next().getAuthority()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    @DisplayName("Should return correct granted authorities for multiple roles")
    void shouldReturnCorrectGrantedAuthoritiesForMultipleRoles() {
        UserAuthenticatedDTO userDto = UserAuthenticatedDTO.builder()
                .authorities(List.of(UserRoles.ADMIN, UserRoles.AGENT))
                .build();

        Collection<? extends GrantedAuthority> authorities = userDto.getAuthorities();

        assertThat(authorities).isNotNull();
        assertThat(authorities).hasSize(2);
        assertThat(authorities.stream().map(GrantedAuthority::getAuthority))
                .containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_AGENT");
    }

    @Test
    @DisplayName("Should return empty collection when authorities list is empty")
    void shouldReturnEmptyCollectionWhenAuthoritiesListIsEmpty() {
        UserAuthenticatedDTO userDto = UserAuthenticatedDTO.builder()
                .authorities(Collections.emptyList())
                .build();

        Collection<? extends GrantedAuthority> authorities = userDto.getAuthorities();

        assertThat(authorities).isNotNull();
        assertThat(authorities).isEmpty();
    }

    @Test
    @DisplayName("Should throw NullPointerException when authorities list is null")
    void shouldThrowNullPointerExceptionWhenAuthoritiesListIsNull() {
        UserAuthenticatedDTO userDto = UserAuthenticatedDTO.builder()
                .authorities(null)
                .build();

        assertThatThrownBy(userDto::getAuthorities)
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should return correct username and password")
    void shouldReturnCorrectUsernameAndPassword() {
        UserAuthenticatedDTO userDto = new UserAuthenticatedDTO(1L, "testuser", "testpass", UserRoles.OPERATOR);

        assertThat(userDto.getUsername()).isEqualTo("testuser");
        assertThat(userDto.getPassword()).isEqualTo("testpass");
    }

    @Test
    @DisplayName("Should return true for all boolean flags when using default constructor")
    void shouldReturnTrueForAllBooleanFlagsWhenUsingDefaultConstructor() {
        UserAuthenticatedDTO userDto = new UserAuthenticatedDTO(1L, "user", "pass", UserRoles.AGENT);

        assertThat(userDto.isAccountNonExpired()).isTrue();
        assertThat(userDto.isAccountNonLocked()).isTrue();
        assertThat(userDto.isCredentialsNonExpired()).isTrue();
        assertThat(userDto.isEnabled()).isTrue();
    }

    @Test
    @DisplayName("Should return correct value for boolean flag when set by builder")
    void shouldReturnCorrectValueForBooleanFlagWhenSetByBuilder() {
        UserAuthenticatedDTO userDto = UserAuthenticatedDTO.builder()
                .isAccountNonLocked(false)
                .build();

        assertThat(userDto.isAccountNonLocked()).isFalse();
    }
}