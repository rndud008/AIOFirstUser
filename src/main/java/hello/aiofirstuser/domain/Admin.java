package hello.aiofirstuser.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString(exclude = "roles")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long id;

    private String username;
    private String password;
    private String nickname;

    @Column(updatable = false)
    private LocalDate createdAt;
    
    @ElementCollection
    @CollectionTable(name = "admin_role" , joinColumns = @JoinColumn(name = "admin_id"))
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private List<Role> roles = new ArrayList<>();

}
