package za.co.wethinkcode.gadgethomeserver.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import za.co.wethinkcode.gadgethomeserver.mapper.DeviceTokenMapper;
import za.co.wethinkcode.gadgethomeserver.models.database.DeviceToken;
import za.co.wethinkcode.gadgethomeserver.models.domain.DeviceTokenDto;
import za.co.wethinkcode.gadgethomeserver.models.domain.UserDto;
import za.co.wethinkcode.gadgethomeserver.repository.DeviceTokenRepository;
import za.co.wethinkcode.gadgethomeserver.repository.UserRepository;
import za.co.wethinkcode.gadgethomeserver.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    private final DeviceTokenRepository deviceTokenRepository;

    private final UserMapper userMapper;

    private final DeviceTokenMapper deviceTokenMapper;

    public UserDetailsService(UserRepository userRepository, DeviceTokenRepository deviceTokenRepository, UserMapper userMapper, DeviceTokenMapper deviceTokenMapper) {
        this.userRepository = userRepository;
        this.deviceTokenRepository = deviceTokenRepository;
        this.userMapper = userMapper;
        this.deviceTokenMapper = deviceTokenMapper;
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
            Optional<DeviceToken> deviceToken = deviceTokenRepository.findByUserUserName(username);
            za.co.wethinkcode.gadgethomeserver.models.database.User userEntity = user.get();
            deviceToken.ifPresent(userEntity::setDeviceToken);
            return userMapper.toDto(user.get());
        }
    }

    public UserDto saveDeviceToContact(DeviceTokenDto deviceTokenDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        za.co.wethinkcode.gadgethomeserver.models.database.User user = userRepository.findById(username).orElseThrow();
        UserDto userDto = userMapper.toDto(user);

        Optional<DeviceToken> deviceToken = deviceTokenRepository.findByUserUserName(user.getUserName());
        if(deviceToken.isPresent() && Objects.equals(deviceToken.get().getToken(), deviceTokenDto.getToken())) {
            userDto.setDeviceToken(deviceTokenMapper.toDto(deviceToken.get()));
        } else {
            deviceToken.ifPresent(deviceTokenRepository::delete);

            DeviceToken newDeviceToken = deviceTokenMapper.toEntity(deviceTokenDto);
            newDeviceToken.setUser(user);

            userDto.setDeviceToken(deviceTokenMapper.toDto(deviceTokenRepository.save(newDeviceToken)));
        }
        return userDto;
    }
}
