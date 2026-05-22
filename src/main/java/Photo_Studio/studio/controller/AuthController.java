
package Photo_Studio.studio.controller;

import Photo_Studio.studio.entity.User;
import Photo_Studio.studio.model.ApiResponse;
import Photo_Studio.studio.model.LoginRequestDTO;
import Photo_Studio.studio.model.LoginResponseDTO;
import Photo_Studio.studio.model.UserRequestDTO;
import Photo_Studio.studio.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody UserRequestDTO user) {

        service.register(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse("User Registered"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO req,
            HttpServletResponse response) {

        LoginResponseDTO res = service.login(req);

        Cookie cookie = new Cookie("AUTH_TOKEN", res.getToken());
        cookie.setHttpOnly(true);   // 🔐 secure
        cookie.setSecure(false);    // ⚠️ true in production (HTTPS)
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);  // 1 hour
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure(false);

        response.addCookie(cookie);

        return ResponseEntity.ok(res); // optional: still return token
    }
}