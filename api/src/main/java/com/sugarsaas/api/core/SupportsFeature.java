package com.sugarsaas.api.core;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SupportsFeature
{
    Class value();
    String id() default "id";
    String type() default "";
}
