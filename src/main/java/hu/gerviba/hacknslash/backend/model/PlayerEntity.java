package hu.gerviba.hacknslash.backend.model;

import java.io.Serializable;
import java.security.Principal;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.context.annotation.Profile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import hu.gerviba.hacknslash.backend.item.Inventory;
import hu.gerviba.hacknslash.backend.item.InventoryConverter;
import hu.gerviba.hacknslash.backend.packets.TelemetryUpdatePacket;
import lombok.Data;
import lombok.NoArgsConstructor;

@Profile(ConfigProfile.GAME_SERVER)
@SuppressWarnings("serial")
@Entity
@Table(name = "players")
@NoArgsConstructor
@Data
public class PlayerEntity implements Serializable, Principal {

    @Id
    @Column
    @JsonIgnore
    private String id;

    @Transient
    private String name;
    
    @Transient
    private String sessionId;

    @Column
    @JsonIgnore
    private Integer exp;

    @Column
    @JsonIgnore
    private Integer maxHp;

    @Column
    @JsonIgnore
    private Integer maxMana;

    @Column
    @JsonIgnore
    private Integer money;
    
    @Column
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
    private volatile double hp;

    @Transient
    private volatile double mana;

    @Column
    @JsonIgnore
    private String map = "dungeon1";
    
    @Transient
    private long entityId;
    
    @Transient
    private volatile double x;

    @Transient
    private volatile double y;
    
    // TODO: Eztszámolja ki delta -> fokok alapján
    @Transient
    private volatile int direction;
    
    // TODO: Eztszámolja ki delta -> fokok alapján
    @Transient
    private volatile boolean walking;

    @Column
    private volatile String base;
    
    @Transient
    private volatile String weapon = "null";
    
    @Transient
    private volatile String helmet = "null";
    
    @Transient
    private volatile String armor = "null";
    
    @Transient
    private volatile String boots = "null";

    @Column
    @Lob
    @Convert(converter = InventoryConverter.class)
    private Inventory inventory;

    public PlayerEntity(String id) {
        this.id = id;
        this.hp = 100;
        this.maxHp = 100;
        this.mana = 20;
        this.maxMana = 30;
        this.exp = 0;
        this.money = 0;
        this.level = 1;
        this.base = "player_no1";
        this.weapon = "null";
        this.helmet = "null";
        this.armor = "null";
        this.boots = "null";
        this.map = "dungeon1";
        this.inventory = new Inventory(new HashMap<>());
    }

    public void update(TelemetryUpdatePacket telemetry) {
        this.x = telemetry.getX();
        this.y = telemetry.getY();
        this.direction = telemetry.getDirection();
        this.walking = telemetry.isWalking();
    }
    
    private static ConcurrentHashMap<Integer, Integer> EXPS_PER_LEVEL = new ConcurrentHashMap<>();
    
    public int getMaxExp() {
        if (!EXPS_PER_LEVEL.containsKey(level))
            EXPS_PER_LEVEL.put(level, 
                    (int) (((20 * (level + 1)) * (Math.log(level * 2) + 1) * 10) 
                    / (20 * level + Math.log(x * 2) * 10)));
        return EXPS_PER_LEVEL.get(level);
    }
    
    public void setMana(double mana) {
        this.mana = Math.min(mana, maxMana);
    }

    public void setHp(double hp) {
        this.hp = Math.min(hp, maxHp);
    }

    public void updateAppearance() {
        this.weapon = inventory.getWeapon();
        this.helmet = inventory.getHelmet();
        this.armor = inventory.getArmor();
        this.boots = inventory.getBoots();
    }
    
}
