package hu.gerviba.hacknslash.backend.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.springframework.context.annotation.Profile;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Session user entity
 * @author Gergely Szabó
 */
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("serial")
@Profile(ConfigProfile.AUTH_SERVER)
@Entity
@Table(name = "sessions")
public class SessionUserEntity implements Serializable {

    @Id
    @Getter
    private String id;

    @Column
    @Getter
    @Size(max = 32)
    private String username;
    
    @Column
    @Getter
    @Size(max = 64)
    private String sessionId;

    @Column
    @Getter
    private String ip;
    
    @Column
    @Getter
    private long time;
    
}
