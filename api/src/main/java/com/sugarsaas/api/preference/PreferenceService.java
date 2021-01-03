package com.sugarsaas.api.preference;

import java.util.Collection;

public interface PreferenceService
{
    Collection<Preference> getPreferences(Object entity);
}
