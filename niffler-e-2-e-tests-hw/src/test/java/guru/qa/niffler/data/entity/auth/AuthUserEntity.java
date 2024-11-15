package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.mapper.AuthAuthorityMapper;
import guru.qa.niffler.model.rest.AuthAuthorityJson;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.*;

import static jakarta.persistence.FetchType.EAGER;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "\"user\"")
public class AuthUserEntity implements Serializable {

    @ToString.Include
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;

    @ToString.Include
    @Column(nullable = false, unique = true)
    private String username;

    @ToString.Include
    @Column(nullable = false)
    private String password;

    @ToString.Include
    @Column(nullable = false)
    private Boolean enabled;

    @ToString.Include
    @Column(name = "account_non_expired", nullable = false)
    private Boolean accountNonExpired;

    @ToString.Include
    @Column(name = "account_non_locked", nullable = false)
    private Boolean accountNonLocked;

    @ToString.Include
    @Column(name = "credentials_non_expired", nullable = false)
    private Boolean credentialsNonExpired;

    @Builder.Default
    @ToString.Include
    @OneToMany(fetch = EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private List<AuthAuthorityEntity> authorities = new ArrayList<>();

    public void addAuthorities(AuthAuthorityEntity... authorities) {
        for (AuthAuthorityEntity authority : authorities) {
            this.authorities.add(authority);
            authority.setUser(this);
        }
    }

    public void addAuthorities(AuthAuthorityJson... authorities) {
        Arrays.stream(authorities)
                .map(new AuthAuthorityMapper()::toEntity)
                .forEach(authority -> {
                    authority.setUser(this);
                    this.authorities.add(authority);
                });
    }

    public void removeAuthority(AuthAuthorityEntity authority) {
        this.authorities.remove(authority);
        authority.setUser(null);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        AuthUserEntity that = (AuthUserEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

}