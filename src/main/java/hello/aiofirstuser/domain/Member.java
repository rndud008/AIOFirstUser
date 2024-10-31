package hello.aiofirstuser.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"addresses","roles"})
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String username;
    private String nickname;
    private String password;
    private String email;
    private String provider;
    private int phoneNumber;

    @Column(updatable = false)
    private LocalDate createdAt;
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "member_role",joinColumns = @JoinColumn(name = "member_id"))
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private List<Role> roles= new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Address> addresses = new ArrayList<>();

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDate.now();
    }


    public void addRole(Role role){
        roles.add(role);
    }

    public void clearRole(){
        roles.clear();
    }

    public void addAddress(Address address){
        addresses.add(address);
    }

//    public void modifyAddress(Address address){
//
//    }

}
