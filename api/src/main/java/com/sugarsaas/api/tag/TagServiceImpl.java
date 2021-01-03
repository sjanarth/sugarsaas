package com.sugarsaas.api.tag;

import com.sugarsaas.api.core.AbstractNoSQLServiceImpl;
import com.sugarsaas.api.core.SupportsTags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@Slf4j
@Service
public class TagServiceImpl extends AbstractNoSQLServiceImpl implements TagService
{
    @Autowired
    private TagRepository tagRepository;

    public Collection<Tag> getTags(Object target)   {
        //log.info("getTags called for "+target);
        Optional<Object> entityId = getEntityId(target, SupportsTags.class);
        Optional<String> entityType = getEntityType(target, SupportsTags.class);
        if (entityId.isPresent() && entityType.isPresent()) {
            //log.info("  -> entityId={},entityType={}", entityId.get(), entityType.get());
            return tagRepository.findByEntityIdAndType((Integer) entityId.get(), entityType.get());
        }
        return new HashSet<>();
    }
}