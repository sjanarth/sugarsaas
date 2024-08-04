#CREATE DATABASE IF NOT EXISTS sugar;
#USE sugar;

CREATE TABLE IF NOT EXISTS sugar_users (
    id INT(11) UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(64) NOT NULL,
    active BIT(1) NOT NULL DEFAULT 1,
    time_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    time_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sugar_privileges (
    id INT(11) UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) UNIQUE NOT NULL,
    description VARCHAR(255),
    seeded BIT(1) NOT NULL DEFAULT 0,
    time_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    time_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sugar_user_privileges (
    user_id INT(11) UNSIGNED NOT NULL,
    privilege_id INT(11) UNSIGNED NOT NULL,
    PRIMARY KEY (user_id, privilege_id),
    CONSTRAINT userprivilege_user FOREIGN KEY (user_id) REFERENCES sugar_users (id),
    CONSTRAINT userprivilege_privilege FOREIGN KEY (privilege_id) REFERENCES sugar_privileges (id)
);

CREATE TABLE IF NOT EXISTS sugar_roles (
    id INT(11) UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(127) UNIQUE NOT NULL,
    description VARCHAR(255),
    seeded BIT(1) NOT NULL DEFAULT 0,
    time_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    time_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sugar_role_privileges (
    role_id INT(11) UNSIGNED NOT NULL,
    privilege_id INT(11) UNSIGNED NOT NULL,
    PRIMARY KEY (role_id, privilege_id),
    CONSTRAINT roleprivilege_role FOREIGN KEY (role_id) REFERENCES sugar_roles (id),
    CONSTRAINT roleprivilege_privilege FOREIGN KEY (privilege_id) REFERENCES sugar_privileges (id)
);

CREATE TABLE IF NOT EXISTS sugar_user_roles (
    user_id INT(11) UNSIGNED NOT NULL,
    role_id INT(11) UNSIGNED NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT userrole_user FOREIGN KEY (user_id) REFERENCES sugar_users (id),
    CONSTRAINT userrole_role FOREIGN KEY (role_id) REFERENCES sugar_roles (id)
);

CREATE TABLE IF NOT EXISTS sugar_tenancy_groups (
    id INT(11) UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    shortname VARCHAR(64) UNIQUE NOT NULL,
    longname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    active BIT(1) NOT NULL DEFAULT 1,
    time_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    time_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sugar_tenancy_group_users (
    user_id INT(11) UNSIGNED NOT NULL,
    tenancy_group_id INT(11) UNSIGNED NOT NULL,
    PRIMARY KEY (user_id, tenancy_group_id),
    CONSTRAINT tenancy_group_user_user FOREIGN KEY (user_id) REFERENCES sugar_users (id),
    CONSTRAINT tenancy_group_user_tg FOREIGN KEY (tenancy_group_id) REFERENCES sugar_tenancy_groups (id)
);

CREATE TABLE IF NOT EXISTS sugar_tenancies (
    id INT(11) UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    shortname VARCHAR(64) UNIQUE NOT NULL,
    longname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    active BIT(1) NOT NULL DEFAULT 1,
    tenancy_group_id INT(11) UNSIGNED NOT NULL,
    time_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    time_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT tenancy_tenancy_group FOREIGN KEY (tenancy_group_id) REFERENCES sugar_tenancy_groups (id)
);

CREATE TABLE IF NOT EXISTS sugar_tenancy_users (
    user_id INT(11) UNSIGNED NOT NULL,
    tenancy_id INT(11) UNSIGNED NOT NULL,
    PRIMARY KEY (user_id, tenancy_id),
    CONSTRAINT tenancy_user_user FOREIGN KEY (user_id) REFERENCES sugar_users (id),
    CONSTRAINT tenancy_user_tenancy FOREIGN KEY (tenancy_id) REFERENCES sugar_tenancies (id)
);
/*
    CREATE TABLE IF NOT EXISTS sugar_refdata_languages (
        language_code VARCHAR(3) PRIMARY KEY,
        language_name VARCHAR(64) NOT NULL
    );

    CREATE TABLE IF NOT EXISTS sugar_refdata_currencies (
        currency_code VARCHAR(3) PRIMARY KEY,
        currency_name VARCHAR(64) NOT NULL
    );

    CREATE TABLE IF NOT EXISTS sugar_refdata_countries (
        country_code VARCHAR(3) PRIMARY KEY,
        country_name VARCHAR(64) NOT NULL
    );

    CREATE TABLE IF NOT EXISTS sugar_refdata_country_subs (
        id INT(11) UNSIGNED PRIMARY KEY AUTO_INCREMENT,
        country_code VARCHAR(3) NOT NULL,
        subdivision_code VARCHAR(4) NOT NULL,
        subdivision_name VARCHAR(64) NOT NULL,
        subdivision_type VARCHAR(32) NOT NULL
    );

    CREATE TABLE IF NOT EXISTS sugar_refdata_country_cities (
        geonameid INT(11) UNSIGNED PRIMARY KEY,
        sub_id INT(11) UNSIGNED NOT NULL,
        city_name VARCHAR(128) NOT NULL,
        //CONSTRAINT cities_country FOREIGN KEY (country_code) REFERENCES  sugar_refdata_countries (country_code),
        CONSTRAINT city_subdiv FOREIGN KEY (sub_id) REFERENCES sugar_refdata_country_subs (id)
    );
*/

