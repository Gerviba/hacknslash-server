package hu.gerviba.hacknslash.backend.connection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.packets.ChatMessagePacket.MessageType;
import hu.gerviba.hacknslash.backend.packets.ItemChangeUpdatePacket;
import hu.gerviba.hacknslash.backend.packets.SkillPacket;
import hu.gerviba.hacknslash.backend.packets.SkillRequestPacket;
import hu.gerviba.hacknslash.backend.services.GlobalPacketService;
import hu.gerviba.hacknslash.backend.services.UserStorageService;
import hu.gerviba.hacknslash.backend.skill.HealthPotion;
import hu.gerviba.hacknslash.backend.skill.ManaPotion;
import hu.gerviba.hacknslash.backend.skill.Skill;

@Profile(ConfigProfile.GAME_SERVER)
@Controller
public class MapActionController {

    @Autowired
    UserStorageService users;
    
    @Autowired 
    AutowireCapableBeanFactory beanFactory;
    
    @Autowired
    GlobalPacketService packets;
    
    private Map<Integer, Skill> skills = new ConcurrentHashMap<>();
    
    @PostConstruct
    void init() {
        skills.put(101, new HealthPotion(101, 1000, 20));
        skills.put(102, new HealthPotion(102, 2500, 50));
        skills.put(103, new HealthPotion(103, 10000, 500));
        
        skills.put(201, new ManaPotion(201, 1000, 20));
        skills.put(202, new ManaPotion(202, 2500, 50));
        skills.put(203, new ManaPotion(203, 10000, 500));
        
        skills.values().forEach(beanFactory::autowireBean);
    }
    
    @MessageMapping("/skills")
    @SendTo("/topic/skills")
    SkillPacket skill(@Payload SkillRequestPacket skill, SimpMessageHeaderAccessor header) {
        PlayerEntity pe = users.getPlayer((String) header.getSessionAttributes()
                .get(HandshakeValidator.SESSION_ID_ATTRIBUTE));
        
        if (this.skills.containsKey(skill.getSkillUid()))
            this.skills.get(skill.getSkillUid()).apply(pe, pe.getX() / 64, pe.getY() / 64, pe.getDirection());
            
        return new SkillPacket(skill.getSkillUid(), pe.getDirection(), pe.getX() / 64, pe.getY() / 64);
    }
    
    @MessageMapping("/switch-item")
    void switchItem(@Payload ItemChangeUpdatePacket change, SimpMessageHeaderAccessor header) {
        PlayerEntity pe = users.getPlayer((String) header.getSessionAttributes()
                .get(HandshakeValidator.SESSION_ID_ATTRIBUTE));
        
        if (pe.getInventory().change(change.getSlotFrom(), change.getSlotTo())) {
            pe.updateAppearance();
        } else {
            packets.sendPrivateMessage(pe, MessageType.WARNING, "Invalid item swap!");
            packets.sendFullInventoryUpdate(pe);
        }
    }
    
}
