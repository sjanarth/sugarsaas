package com.sugarsaas.api.signup;

import com.sugarsaas.api.attachment.Attachment;
import com.sugarsaas.api.attachment.AttachmentService;
import com.sugarsaas.api.identity.role.RoleRepository;
import com.sugarsaas.api.identity.role.SugarSeededRoles;
import com.sugarsaas.api.identity.user.User;
import com.sugarsaas.api.identity.user.UserRepository;
import com.sugarsaas.api.preference.Preference;
import com.sugarsaas.api.preference.PreferenceService;
import com.sugarsaas.api.tag.Tag;
import com.sugarsaas.api.tag.TagService;
import com.sugarsaas.api.tenancy.tenancygroup.TenancyGroup;
import com.sugarsaas.api.tenancy.tenancygroup.TenancyGroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

@Slf4j
@RestController
@RequestMapping("${spring.data.rest.base-uri:sugar}")
@CrossOrigin(origins="http://localhost:3000")
public class SignupController
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TenancyGroupRepository tenancyGroupRepository;

    @Autowired
    private PreferenceService preferenceService;

    @Autowired
    private TagService tagService;

    @Autowired
    private AttachmentService attachmentService;

    @PostMapping("/signup")
    public ResponseEntity<User> create(@RequestBody SignupParams params) throws URISyntaxException {
        log.info ("Signing up : {}", params);
        // Create a new user
        User user = new User();
        user.setFirstName(params.getFirstName());
        user.setLastName(params.getLastName());
        user.setEmail(params.getEmail());
        user.setActive(true);
        log.info("  -> Setting up user "+user);
        user.setPassWord(params.getPassWord());
        user.setRoles(Collections.singleton(roleRepository.findByName(SugarSeededRoles.TENANCY_GROUP_ADMIN.name).get()));
        log.info("  -> Setting up roles "+user.getRoles());
        // Create a new tenancy group
        TenancyGroup tenancyGroup = new TenancyGroup();
        tenancyGroup.setShortName(params.getAccountName());
        tenancyGroup.setLongName(params.getAccountName());
        tenancyGroup.setEmail(params.getEmail());
        tenancyGroup.setActive(true);
        log.info("  -> Setting up tenancy-group "+tenancyGroup);
        // Link them
        user.setTenancyGroups(new HashSet<>(Arrays.asList(tenancyGroup)));
        User result = userRepository.save(user);
        return ResponseEntity
                // TODO: make this generic
                .created(new URI("/sugar/users/" +result.getId()))
                .body(result);
    }

    @PostMapping("/test")
    public ResponseEntity<Collection<Preference>> testUserPreferences(
            //@RequestParam("email") String email,
            @RequestParam("image") MultipartFile image)  {
        log.info("Testing now");
        User user = userRepository.findById(5).get(); // .findByEmail("sjanarth@gmail.com").get();
        log.info("  -> Found user "+user);
        Collection<Preference> preferences = preferenceService.getPreferences(user);
        log.info("  -> Found {} preferences", preferences.size());
        preferences.stream().forEach(p -> log.info("    -> "+p.toString()));
        Collection<Tag> tags = tagService.getTags(user);
        log.info("  -> Found {} tags", tags.size());
        tags.stream().forEach(t -> log.info("    -> "+t.toString()));
        /*
        try {
            log.info ("  -> Loading attachment");
            Attachment attachment = attachmentService.addAttachment(user, "Avatar", image);
            log.info ("  -> done loading attachment");
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        Collection<Attachment> attachments = attachmentService.getAttachments(user);
        log.info("  -> Found {} attachments", attachments.size());
        attachments.stream().forEach(t -> log.info("    -> "+t.toString()));
        return ResponseEntity.ok(preferences);
    }
}