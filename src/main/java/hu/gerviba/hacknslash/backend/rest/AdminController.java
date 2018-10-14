package hu.gerviba.hacknslash.backend.rest;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.gerviba.hacknslash.backend.ConfigProfile;

@Profile(ConfigProfile.GAME_SERVER)
@RestController
@RequestMapping("/api/admin/")
public class AdminController {

    // Require admin token
    // GET user list
    // GET user count
    // GET map list
    // POST message
    // POST teleport
    // POST kick user
    
}
