package com.sugarsaas.api.loader;

import com.sugarsaas.api.preference.Preference;
import com.sugarsaas.api.preference.PreferenceRepository;
import com.sugarsaas.api.preference.SeededPreference;
import com.sugarsaas.api.preference.SugarSeededPreferences;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

@Slf4j
@Order(13)
@Component
public class SeededPreferencesLoader extends AbstractSeedDataLoader implements CommandLineRunner
{
    @Autowired
    PreferenceRepository preferenceRepository;

    @Override
    public void run (String ... args) {
        /*
         * Scan the classpath to find all implementations of SeededPreference.
         *
         * The scan also forces static initializers in them to run which is
         * is important as it allows implementations a chance to modify the
         * SeedPreferences provided by Sugar (ex: updating descriptions).
         *
         * However, while loading we load SugarSeededPreferences before all
         * other implementations to support dependencies on SugarSeededPreferences.
         */
        Set<Class<? extends SeededPreference>> seededPreferences = scanProviders(SeededPreference.class);
        log.info("Processing "+ SugarSeededPreferences.class.getCanonicalName());
        loadSeededPreferences(SugarSeededPreferences.values());
        for (Class<? extends SeededPreference> cls : seededPreferences) {
            if (!cls.getCanonicalName().equals(SugarSeededPreferences.class.getCanonicalName())) {
                log.info("Processing " +cls.getCanonicalName());
                loadSeededPreferences(cls.getEnumConstants());
            }
        }
    }

    private void loadSeededPreferences(SeededPreference[] seededPreferences)  {
        for (SeededPreference sp : seededPreferences)   {
            boolean createNew = true;
            Collection<Preference> preferences = preferenceRepository.findByTypeAndSeeded(sp.getType().toString(), true);
            for (Preference preference : preferences) {
                if (preference.getName().equals(sp.getName()))  {
                    if (hasChanged(sp, preference)) {
                        preference.setDescription(sp.getDescription());
                        preference.setValue(sp.getValue());
                        log.info("  Updating " + sp.getOrigin() + " Seeded Preference " + sp.getName());
                        preferenceRepository.save(preference);
                    }
                    createNew = false;
                    break;
                }
            }
            if (createNew)  {
                Preference preference = new Preference();
                preference.setEntityId(-1);
                preference.setType(sp.getType().toString());
                preference.setName(sp.getName());
                preference.setDescription(sp.getDescription());
                preference.setValue(sp.getValue());
                preference.setSeeded(true);
                log.info("  Loading {} Seeded Preference {} {}", sp.getOrigin(), sp.getType(), sp.getName());
                preferenceRepository.save(preference);
            }
        }
    }

    private boolean hasChanged(SeededPreference seededPreference, Preference preference)   {
        if (!seededPreference.getDescription().equals(preference.getDescription())) return true;
        if (!seededPreference.getValue().equals(preference.getValue())) return true;
        return false;
    }
}
