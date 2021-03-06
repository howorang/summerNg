package dmcs.summer.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        dmcs.summer.user.User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));

        return User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(getAuthorities(user))
                .build();


    }

    private String[] getAuthorities(dmcs.summer.user.User user) {
        return user.getRoles()
                .stream()
                .flatMap(role -> role.getPrivileges().stream())
                .map(Enum::name)
                .toArray(String[]::new);
    }
}
