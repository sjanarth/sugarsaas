package com.sugarsaas.api.loader;

import com.sugarsaas.api.identity.privilege.Privilege;
import com.sugarsaas.api.identity.privilege.PrivilegeRepository;
import com.sugarsaas.api.identity.privilege.SeededPrivilege;
import com.sugarsaas.api.identity.privilege.SugarSeededPrivileges;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

@Slf4j
@Order(11)
@Component
public class SeededPrivilegesLoader extends AbstractSeedDataLoader implements CommandLineRunner
{
    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Override
    public void run (String ... args) {
        /*
         * Scan the classpath to find all implementations of SeededPrivilege.
         *
         * The scan also forces static initializers in them to run which is
         * is important as it allows implementations a chance to modify the
         * SeedPrivileges provided by Sugar (ex: updating descriptions).
         *
         * However, while loading we load SugarSeededPrivileges before all
         * other implementations to support dependencies on SugarSeededPrivileges.
         */
        Set<Class<? extends SeededPrivilege>> seededPrivileges = scanProviders(SeededPrivilege.class);
        log.info("Processing "+SugarSeededPrivileges.class.getCanonicalName());
        loadSeededPrivileges(SugarSeededPrivileges.values());
        for (Class<? extends SeededPrivilege> cls : seededPrivileges) {
            if (!cls.getCanonicalName().equals(SugarSeededPrivileges.class.getCanonicalName())) {
                log.info("Processing " +cls.getCanonicalName());
                loadSeededPrivileges(cls.getEnumConstants());
            }
        }
    }

    private void loadSeededPrivileges(SeededPrivilege[] seededPrivileges)  {
        Map<String, Privilege> currentPrivileges = privilegeRepository.findAll().stream().collect(toMap(p -> p.getName(), p -> p));
        for (SeededPrivilege sp : seededPrivileges) {
            Privilege p = currentPrivileges.get(sp.getName());
            if (p == null)  {
                p = new Privilege();
                p.setName(sp.getName());
                p.setDescription(sp.getDescription());
                p.setSeeded(true);
                log.info("  Loading "+sp.getOrigin()+" Seeded Privilege "+sp.getName());
                privilegeRepository.saveAndFlush(p);
            } else if (!p.getDescription().equals(sp.getDescription()))  {
                p.setDescription(sp.getDescription());
                log.info("  Updating "+sp.getOrigin()+" Seeded Privilege "+sp.getName());
                privilegeRepository.saveAndFlush(p);
            }
        }
    }
}
