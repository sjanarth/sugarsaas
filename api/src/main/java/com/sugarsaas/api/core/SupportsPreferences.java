package com.sugarsaas.api.core;

import com.sugarsaas.api.preference.Preference;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SupportsFeature(Preference.class)
public @interface SupportsPreferences
{
}