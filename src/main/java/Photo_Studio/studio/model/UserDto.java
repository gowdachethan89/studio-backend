package Photo_Studio.studio.model;

import Photo_Studio.studio.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {

    private Long id;
    private String username;
    private String role;
    private LocalDateTime createdAt;

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole().name();
        this.createdAt = user.getCreatedAt();
    }
}
