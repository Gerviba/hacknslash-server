package hu.gerviba.hacknslash.backend.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.context.annotation.Profile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import hu.gerviba.hacknslash.backend.packets.TelemetryUpdatePacket;
import lombok.Data;
import lombok.NoArgsConstructor;

@Profile(ConfigProfile.GAME_SERVER)
@SuppressWarnings("serial")
@Entity
@Table(name = "players")
@NoArgsConstructor
@Data
public class PlayerEntity implements Serializable {

    @Id
    @Column
    @JsonIgnore
    private String id;

    @Transient
    private String name;

    @Column
    @JsonIgnore
    private Long exp;

    @Transient
    @JsonIgnore
    private Integer level;

    @Column
    @JsonIgnore
    private Integer pvpKills;

    @Column
    @JsonIgnore
    private Integer pvpDeaths;

    @Column
    @JsonIgnore
    private Integer statStrength;

    @Column
    @JsonIgnore
    private Integer statAgility;

    @Column
    @JsonIgnore
    private Integer statMagic;

    @Transient
    private double hp;

    @Transient
    private double mana;

    @Column
    @JsonIgnore
    private String map;
    
    @Transient
    private long entityId;
    
    @Transient
    private double x;

    @Transient
    private double y;
    
    // TODO: Eztszámolja ki delta -> fokok alapján
    @Transient
    private int direction;
    
    // TODO: Eztszámolja ki delta -> fokok alapján
    @Transient
    private boolean walking;

    public PlayerEntity(String id) {
        this.id = id;
    }

    public void update(TelemetryUpdatePacket telemetry) {
        this.x = telemetry.getX();
        this.y = telemetry.getY();
        this.direction = telemetry.getDirection();
        this.walking = telemetry.isWalking();
    }

}
