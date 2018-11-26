package hu.gerviba.hacknslash.backend.pojo.game;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonMerge;

import lombok.Data;

@Data
public class MobPojo {

    private final long entityId;
    @JsonMerge
    private final MobTemplatePojo template;
    
    private List<Long> path;
    private double x;
    private double y;
    private int damage;
    private double health;
    
    
}
