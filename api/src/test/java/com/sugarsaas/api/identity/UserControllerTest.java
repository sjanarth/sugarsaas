package com.sugarsaas.api.identity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sugarsaas.api.core.AbstractTest;
import com.sugarsaas.api.identity.user.User;
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
public class UserControllerTest extends AbstractTest
{
    private static User createdUser = null;
    private static final String TEST_USER_JSON =
            "{\"firstName\":\"TestFirstName\",\"lastName\":\"TestLastName\",\"email\":\"test@test.com\",\"passWord\":\"test\"}";

    @Test
    public void test1_testCreate() throws Exception {
        System.out.println("*** Running testCreate");
        /*
        User user = new User();
        user.setId(Integer.MAX_VALUE);
        user.setFirstName("TestFirstName");
        user.setLastName("TestLastName");
        user.setEmail("test@test.com");
        user.setPassWord("test");
        String jsonString = new ObjectMapper().writeValueAsString(user);        // won't pickup password
        */
        RequestBuilder request = MockMvcRequestBuilders
                                    .post("/api/user")
                                    .content(TEST_USER_JSON)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(request)
                                //.andDo(MockMvcResultHandlers.print())
                                .andExpect(MockMvcResultMatchers.status().isCreated())
                                .andReturn();
        createdUser = new ObjectMapper().readValue(result.getResponse().getContentAsString(), User.class);
        System.out.println("createdUser="+createdUser);
    }

    @Test
    public void test2_testCreateUnique() throws Exception {
        System.out.println("*** Running testCreateUnique");
        System.out.println("createdUser="+createdUser);
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/user")
                .content(TEST_USER_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        try {
            mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception ex)  {
            Assert.assertTrue(hasSQLIntegrityConstraintViolationException(ex));
        }
        System.out.println("createdUser="+createdUser);
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
        System.out.println("createdUser="+createdUser);
        createdUser.setFirstName("TestFirstName2");
        createdUser.setLastName("TestLastName2");
        mvc.perform(MockMvcRequestBuilders
                    .put("/api/user/{id}", createdUser.getId())
                    .content(new ObjectMapper().writeValueAsString(createdUser))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("TestFirstName2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("TestLastName2"));
    }

    @Test
    public void test4_testDelete() throws Exception {
        System.out.println("*** Running testDelete");
        System.out.println("createdUser="+createdUser);
        mvc.perform(MockMvcRequestBuilders
                .delete("/api/user/{id}", createdUser.getId())
                .content(new ObjectMapper().writeValueAsString(createdUser))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}