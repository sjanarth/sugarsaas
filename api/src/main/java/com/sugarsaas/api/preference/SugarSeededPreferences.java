package com.sugarsaas.api.preference;

public enum SugarSeededPreferences implements SeededPreference
{
    EnableSso(SugarSeededPreferenceTypes.TENANCYGROUP, "EnableSso", "Enable Single Sign on", "Sugar", "true"),
    MaxUsers(SugarSeededPreferenceTypes.TENANCYGROUP, "MaxUsers", "Maximum number of users in a tenancy", "Sugar", "10000"),
    MaxTenancies(SugarSeededPreferenceTypes.TENANCYGROUP, "MaxTenancies", "Maximum number of tenancies in a tenancy group", "Sugar", "5");

    private SeededPreferenceType type;
    private String name;
    private String description;
    private String origin;
    private String value;
    SugarSeededPreferences(SeededPreferenceType type, String name, String description, String origin, String value) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.origin = origin;
        this.value = value;
    }

    @Override
    public SeededPreferenceType getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
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
    public String getValue()    {
        return value;
    }
}
