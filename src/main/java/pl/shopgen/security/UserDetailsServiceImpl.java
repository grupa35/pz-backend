package pl.shopgen.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.shopgen.models.User;
import pl.shopgen.repositories.UserRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Service
@Qualifier("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(username);

        if(!optionalUser.isPresent())
        {
            throw new UsernameNotFoundException(username);
        }
        User user = optionalUser.get();

        org.springframework.security.core.userdetails.User userSpring =  new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                user.isCredentialsNonExpired(),
                user.isAccountNonExpired(),
                user.isAccountNonLocked(),
                getAuthority("ROLE_" + user.getRole().getName())
        );

        return userSpring;
    }

    private Collection<? extends GrantedAuthority> getAuthority(String role)
    {
        return Arrays.asList(new SimpleGrantedAuthority(role));
    }

}
