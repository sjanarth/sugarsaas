package com.sugarsaas.api.identity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sugarsaas.api.core.AbstractTest;
import com.sugarsaas.api.identity.privilege.Privilege;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.SQLIntegrityConstraintViolationException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PrivilegeControllerTest extends AbstractTest
{
    private static Privilege createdPrivilege = null;
    private static final String TEST_PRIVILEGE_JSON =
            "{\"name\":\"TestPrivilege\",\"description\":\"TestPrivilegeDescription\"}";

    @Test
    public void test1_testCreate() throws Exception {
        System.out.println("*** Running testCreate");
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/privilege")
                .content(TEST_PRIVILEGE_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(request)
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        createdPrivilege = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Privilege.class);
        System.out.println("createdPrivilege="+createdPrivilege);
    }

    @Test
    public void test2_testCreateUnique() throws Exception {
        System.out.println("*** Running testCreateUnique");
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/privilege")
                .content(TEST_PRIVILEGE_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        try {
            mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception ex)  {
            Assert.assertTrue(hasSQLIntegrityConstraintViolationException(ex));
        }
    }

    private boolean hasSQLIntegrityConstraintViolationException (Throwable ex)   {
        //System.out.println("***"+ex);
        if (ex instanceof SQLIntegrityConstraintViolationException)
            return true;
        if (ex.getCause() != null)
            return hasSQLIntegrityConstraintViolationException (ex.getCause());
        return false;
    }

    @Test
    public void test3_testUpdate() throws Exception {
        System.out.println("*** Running testUpdate");
        createdPrivilege.setName("TestPrivilege2");
        createdPrivilege.setDescription("TestPrivilegeDescription2");
        mvc.perform(MockMvcRequestBuilders
                .put("/api/privilege/{id}", createdPrivilege.getId())
                .content(new ObjectMapper().writeValueAsString(createdPrivilege))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("TestPrivilege2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("TestPrivilegeDescription2"));
    }

    @Test
    public void test4_testDelete() throws Exception {
        System.out.println("*** Running testDelete");
        System.out.println("createdPrivilege="+createdPrivilege);
        mvc.perform(MockMvcRequestBuilders
                .delete("/api/privilege/{id}", createdPrivilege.getId())
                .content(new ObjectMapper().writeValueAsString(createdPrivilege))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}