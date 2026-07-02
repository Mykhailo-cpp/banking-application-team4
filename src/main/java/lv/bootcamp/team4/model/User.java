package lv.bootcamp.team4.model;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class User {
    private String id;
    private String username;
    private String email;
    private Role role;
}
