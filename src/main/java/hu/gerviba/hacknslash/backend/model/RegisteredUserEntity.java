package hu.gerviba.hacknslash.backend.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.context.annotation.Profile;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Registered user entity
 * @author Gergely Szabó
 */
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("serial")
@Profile(ConfigProfile.AUTH_SERVER)
@Entity
@Table(name = "users")
public class RegisteredUserEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Getter
    private String id;

    @Column
    @Getter
    private String email;

    @Column
    @Getter
    @Size(max = 32)
    private String username;

    @Column
    @Getter
    private String password;

    @Column
    @Getter
    private String salt;

    @Column
    @Getter
    @Setter
    private boolean banned;

}
