package com.sugarsaas.api.preference;

import com.sugarsaas.api.core.AbstractNoSQLServiceImpl;
import com.sugarsaas.api.core.SupportsPreferences;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@Slf4j
@Service
public class PreferenceServiceImpl extends AbstractNoSQLServiceImpl implements PreferenceService
{
    @Autowired
    private PreferenceRepository preferenceRepository;

    public Collection<Preference> getPreferences(Object target)   {
        //log.info("getPreferences called for "+target);
        Optional<Object> entityId = getEntityId(target, SupportsPreferences.class);
        Optional<String> entityType = getEntityType(target, SupportsPreferences.class);
        if (entityId.isPresent() && entityType.isPresent()) {
            //log.info("  -> entityId={},entityType={}", entityId.get(), entityType.get());
            return preferenceRepository.findByEntityIdAndType((Integer) entityId.get(), entityType.get());
        }
        return new HashSet<>();
    }
}