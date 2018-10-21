package hu.gerviba.hacknslash.backend.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;

import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommandService {

    @Autowired 
    AutowireCapableBeanFactory beanFactory;
    
    @FunctionalInterface
    interface Command {
        public void handle(PlayerEntity player, String[] args);
    }
    
    private Map<String, Command> commands = new ConcurrentHashMap<>();
    
    @PostConstruct
    public void loadCommands() {
        registerListener(new PlayerCommandListener());
        registerListener(new DebugCommandListener());
    }

    public void registerListener(Object listener) {
        beanFactory.autowireBean(listener);
        log.info("Registering new listener: " + listener.getClass().getName());
        for (Method m : listener.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(CommandHandler.class) && 
                    m.getParameterTypes().length == 2 &&
                    m.getParameterTypes()[0].equals(PlayerEntity.class) &&
                    m.getParameterTypes()[1].equals(String[].class)) {
                commands.put("/" + m.getAnnotation(CommandHandler.class).value().toLowerCase(), 
                        (pl, args) -> {
                            try {
                                m.invoke(listener, pl, args);
                            } catch (IllegalAccessException 
                                    | IllegalArgumentException 
                                    | InvocationTargetException e) {
                                log.error("Failed to execute command: " 
                            + m.getAnnotation(CommandHandler.class).value().toLowerCase(), e);
                            }
                        });
                log.info("New command registered: /" 
                        + m.getAnnotation(CommandHandler.class).value().toLowerCase());
            }
        }
    }

    public boolean handle(PlayerEntity pe, String message) {
        String[] args = message.split(" ");
        if (commands.containsKey(args[0].toLowerCase())) {
            commands.get(args[0].toLowerCase()).handle(pe, args);
            return true;
        }
        return false;
    }
    
}

