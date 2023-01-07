package za.co.wethinkcode.gadgethomeserver.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import za.co.wethinkcode.gadgethomeserver.models.domain.UserDto;
import za.co.wethinkcode.gadgethomeserver.repository.UserRepository;
import za.co.wethinkcode.gadgethomeserver.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserDetailsService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<za.co.wethinkcode.gadgethomeserver.models.database.User> user = userRepository.findById(userName);
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(user.get().getRole()));

        return new User(user.get().getUserName(), user.get().getPassword(), authorityList);
    }

    public UserDetails createUserDetails(UserDto userDto) throws Exception {

        if(userRepository.existsByUserName(userDto.getUserName())) 
            throw new Exception("Username already exists");

        if(userRepository.existsByEmail(userDto.getEmail())) 
            throw new Exception("Email already exits");

        List<GrantedAuthority> authorityList = new ArrayList<>();

        za.co.wethinkcode.gadgethomeserver.models.database.User user = userMapper.toEntity(userDto);

        user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));

        userRepository.save(user);

        authorityList.add(new SimpleGrantedAuthority(userDto.getRole()));

        return new User(userDto.getUserName(), userDto.getPassword(), authorityList);
    }

    public UserDto getUserDto(String username) {
        Optional<za.co.wethinkcode.gadgethomeserver.models.database.User> user = userRepository.findById(username);

        if(user.isEmpty()) {
            return null;
        } else {
            return userMapper.toDto(user.get());
        }
    }

    public za.co.wethinkcode.gadgethomeserver.models.database.User getUser(String username) {
        Optional<za.co.wethinkcode.gadgethomeserver.models.database.User> user = userRepository.findById(username);

        return user.orElse(null);
    }

    public UserDto saveDeviceToContact(UserDto userDto) {
        za.co.wethinkcode.gadgethomeserver.models.database.User user = userRepository.findById(userDto.getUserName()).orElseThrow();
        if(Objects.equals(user.getDeviceId(), userDto.getDeviceId())) {
            return userDto;
        } else {
            user.setDeviceId(userDto.getDeviceId());
            return userMapper.toDto(userRepository.save(user));
        }
    }
}
