package com.sugarsaas.api.preference;

public interface SeededPreference
{
    SeededPreferenceType getType();
    String getName();
    String getDescription();
    String getOrigin();
    String getValue();
}
