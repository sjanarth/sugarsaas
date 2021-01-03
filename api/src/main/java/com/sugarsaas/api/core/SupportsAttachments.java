package com.sugarsaas.api.core;

import com.sugarsaas.api.attachment.Attachment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SupportsFeature(Attachment.class)
public @interface SupportsAttachments
{
}
