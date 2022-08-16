package za.co.wethinkcode.gadgethomeserver.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import za.co.wethinkcode.gadgethomeserver.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    final UserRepository userRepository;

    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        za.co.wethinkcode.gadgethomeserver.models.User user = userRepository.findUserByUserName(userName);
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("USER_ROLE"));

        return new User(user.getUserName(), user.getPassword(), authorityList);
    }

    public UserDetails createUserDetails(String userName, String password) {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("USER_ROLE"));
        return new User(userName, password, authorityList);
    }
}
