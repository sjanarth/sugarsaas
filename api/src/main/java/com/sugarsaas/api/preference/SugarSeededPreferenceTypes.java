package com.sugarsaas.api.preference;

public enum SugarSeededPreferenceTypes implements SeededPreferenceType
{
    USER ("USER", "User level preference", "Sugar", 1),
    TENANCY ("TENANCY", "Tenancy level preference", "Sugar", 2),
    TENANCYGROUP("TENANCYGROUP", "User level preference", "Sugar", 3);

    private String type;
    private String description;
    private String origin;
    private Integer precedence;

    SugarSeededPreferenceTypes(String type, String description, String origin, Integer precedence) {
        this.type = type;
        this.description = description;
        this.origin = origin;
        this.precedence = precedence;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getOrigin() {
        return origin;
    }

    @Override
    public Integer getPrecedence() {
        return precedence;
    }
}
