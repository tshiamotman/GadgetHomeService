package za.co.wethinkcode.gadgethomeserver.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import za.co.wethinkcode.gadgethomeserver.models.database.User;
import za.co.wethinkcode.gadgethomeserver.models.domain.RefreshToken;
import za.co.wethinkcode.gadgethomeserver.repository.RefreshTokenRepository;
import za.co.wethinkcode.gadgethomeserver.repository.UserRepository;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepo;
    private final UserRepository userRepo;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepo = refreshTokenRepository;
        this.userRepo = userRepository;
    }

    public RefreshToken createRefreshToken(String username) {
        // Optional<RefreshToken> token = this.refreshTokenRepo.findByUser(username);
        Optional<User> user = this.userRepo.findById(username);

        if(user.isEmpty()) return null; 

        // if(token.isEmpty()) return new RefreshToken(user.get());
        
        return new RefreshToken(user.get());
    }

    public Boolean verifyRefreshToken(String token, String username) {
        Optional<RefreshToken> refreshToken = this.refreshTokenRepo.findById(token);

        if(refreshToken.isPresent()) return username.equals(refreshToken.get().getUser());
        
        return false;
    }

    public void deleteRefreshToken(String username) {
        Optional<RefreshToken> refreshToken = this.refreshTokenRepo.findById(username);
        
        if(refreshToken.isPresent() && username.equals(refreshToken.get().getUser())) 
            this.refreshTokenRepo.delete(refreshToken.get());
        
    }
}
