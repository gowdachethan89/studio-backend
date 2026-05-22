package Photo_Studio.studio.service;

import Photo_Studio.studio.entity.User;
import Photo_Studio.studio.model.LoginRequestDTO;
import Photo_Studio.studio.model.LoginResponseDTO;
import Photo_Studio.studio.model.UserRequestDTO;
import Photo_Studio.studio.repository.UserRepository;
import Photo_Studio.studio.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository repo, PasswordEncoder encoder, JwtUtil jwtUtil) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    public void register(UserRequestDTO dto) {

        User user = new User();

        user.setUsername(dto.getUsername());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());
        user.setCreatedAt(LocalDateTime.now());

        repo.save(user);
    }

    public LoginResponseDTO login(LoginRequestDTO req) {

        User user = repo.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user);

        return new LoginResponseDTO(token);
    }
}
