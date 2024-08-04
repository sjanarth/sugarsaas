package com.sugarsaas.api.identity;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sugarsaas.api.core.AbstractTest;
import com.sugarsaas.api.core.AuditableEntity;
import com.sugarsaas.api.identity.privilege.Privilege;
import com.sugarsaas.api.identity.privilege.PrivilegeRepository;
import com.sugarsaas.api.identity.user.UserRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.SQLIntegrityConstraintViolationException;

public class PrivilegeControllerTest extends AbstractTest<Privilege>
{
    @Autowired
    PrivilegeRepository privilegeRepository;

    @Override
    protected String getJsonForCreate() {
        return "{\"name\":\"TestPrivilege\",\"description\":\"TestPrivilegeDescription\"}";
    }

    @Override
    protected Privilege lookupEntity() {
        return privilegeRepository.findByName("TestPrivilege").get();
    }

    @Override
    protected void update(Privilege privilege) {
        //privilege.setName("TestPrivilege2");
        privilege.setDescription("TestPrivilegeDescription2");

    }

    @Override
    protected void checkUpdate(ResultActions resultActions) {
        try {
            // name is used to lookup for testDelete
            //resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("TestPrivilege2"))
            resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.description").value("TestPrivilegeDescription2"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}