package za.co.wethinkcode.gadgethomeserver.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import za.co.wethinkcode.gadgethomeserver.models.domain.UserDto;
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
        za.co.wethinkcode.gadgethomeserver.models.database.User user = userRepository.findUserByUserName(userName);
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(user.getRole()));

        return new User(user.getUserName(), user.getPassword(), authorityList);
    }

    public UserDetails createUserDetails(UserDto userDto) throws Exception {

        if(userRepository.existsByUserName(userDto.getUserName())) 
            throw new Exception("Username already exists");

        if(userRepository.existsByEmail(userDto.getEmail())) 
            throw new Exception("Email already exits");

        List<GrantedAuthority> authorityList = new ArrayList<>();

        za.co.wethinkcode.gadgethomeserver.models.database.User user = 
            new za.co.wethinkcode.gadgethomeserver.models.database.User();

        user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUserName(userDto.getUserName());
        user.setNumber(userDto.getNumber());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());

        userRepository.save(user);

        authorityList.add(new SimpleGrantedAuthority(userDto.getRole()));

        return new User(userDto.getUserName(), userDto.getPassword(), authorityList);
    }
}
