package com.sugarsaas.api.tag;

import java.util.Collection;

public interface TagService
{
    Collection<Tag> getTags(Object entity);
}
