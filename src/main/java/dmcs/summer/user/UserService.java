package dmcs.summer.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    void createUser(RegisterUserDto registerUserDto) {
        if (userRepository.existsByUsername(registerUserDto.getUsername())) {
            throw new UserAlreadyExistsException();
        }
        User newUser = User.builder()
                .username(registerUserDto.getUsername())
                .password(passwordEncoder.encode(registerUserDto.getPassword()))
                .role(roleRepository.findByRoleName("ROLE_USER"))
                .build();
        userRepository.save(newUser);
    }

    UserInfoDto getCurrentUserInfo() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return UserInfoDto.fromUser(userRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException(userName)));
    }
}
