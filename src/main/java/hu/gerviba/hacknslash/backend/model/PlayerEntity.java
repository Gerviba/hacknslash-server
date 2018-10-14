package hu.gerviba.hacknslash.backend.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.context.annotation.Profile;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Profile(ConfigProfile.GAME_SERVER)
@SuppressWarnings("serial")
@Entity
@Table(name = "players")
@NoArgsConstructor
@ToString
public class PlayerEntity implements Serializable {

    @Id
    @Column
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    @Transient
    private String name;

    @Column
    @Getter
    @Setter
    private Long exp;

    @Transient
    @Getter
    @Setter
    private Integer level;

    @Column
    @Getter
    @Setter
    private Integer pvpKills;

    @Column
    @Getter
    @Setter
    private Integer pvpDeaths;

    @Column
    @Getter
    @Setter
    private Integer statStrength;

    @Column
    @Getter
    @Setter
    private Integer statAgility;

    @Column
    @Getter
    @Setter
    private Integer statMagic;

    @Transient
    @Getter
    @Setter
    private double hp;

    @Transient
    @Getter
    @Setter
    private double mana;

    @Column
    @Getter
    @Setter
    private String map;

    @Transient
    @Getter
    @Setter
    private double x;

    @Transient
    @Getter
    @Setter
    private double y;

    public PlayerEntity(String id) {
        this.id = id;
    }

}
