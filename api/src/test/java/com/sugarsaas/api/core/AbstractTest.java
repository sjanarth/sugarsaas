package com.sugarsaas.api.core;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sugarsaas.api.SugarSaaSApplication;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
//import org.junit.runner.RunWith;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.lang.reflect.ParameterizedType;

//@ExtendWith(SpringExtension.class)    // Couldn't get junit5 to work (autowired mvc is null)
//@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
//@RunWith(SpringRunner.class)
@SpringBootTest(classes=SugarSaaSApplication.class)
@PropertySource("classpath:application.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class AbstractTest<T extends AuditableEntity>
{
    @Autowired
    protected MockMvc mvc;

    protected abstract String getJsonForCreate();

    protected abstract T lookupEntity();

    protected abstract void update(T entity);
    protected abstract void checkUpdate(ResultActions resultActions);

    @Test
    @Order(10)
    public void testCreate() throws Exception {
        System.out.println("Running " + getClass().getSimpleName() + ".testCreate");
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
                .post(getApiPath())
                .content(getJsonForCreate())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(request)
                //.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        T entity = mapper.readValue(result.getResponse().getContentAsString(), getType());
        System.out.println("entity="+ entity);
    }

    @Test
    @Order(20)
    public void testUnique() {
        System.out.println("Running " + getClass().getSimpleName() + ".testUnique");
        T entity = lookupEntity();
        System.out.println("entity="+ entity);
        RequestBuilder request = MockMvcRequestBuilders
                .post(getApiPath())
                .content(getJsonForCreate())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        try {
            mvc.perform(request).andExpect(MockMvcResultMatchers.status().isConflict());
        } catch (Exception ex)  {
           org.junit.jupiter.api.Assertions.assertTrue(hasSQLIntegrityConstraintViolationException(ex));
        }
        System.out.println("entity="+ entity);
    }

    @Test
    @Order(30)
    public void testUpdate() throws Exception {
        System.out.println("Running " + getClass().getSimpleName() + ".testUpdate");
        T entity = lookupEntity();
        update(entity);
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
                        .put(getApiPathWithKey(entity), entity.getKey())
                        .content(new ObjectMapper().writeValueAsString(entity))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        checkUpdate(resultActions);
    }

    @Test
    @Order(40)
    public void testDelete() throws Exception {
        System.out.println("Running " + getClass().getSimpleName() + ".testDelete");
        T entity = lookupEntity();
        System.out.println("entity="+ entity);
        mvc.perform(MockMvcRequestBuilders
                        .delete(getApiPathWithKey(entity), entity.getKey())
                        .content(new ObjectMapper().writeValueAsString(entity))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    private String getApiPath()   {
        return "/" + getType().getSimpleName().toLowerCase() + "s";
    }

    private String getApiPathWithKey(T entity)    {
        return getApiPath() + "/{" + entity.getKeyColumnName() +"}";
    }

    @SuppressWarnings("unchecked")
    private Class<T> getType()   {
        return (Class<T>)
                ((ParameterizedType)getClass()
                        .getGenericSuperclass())
                        .getActualTypeArguments()[0];
    }

    private boolean hasSQLIntegrityConstraintViolationException (Throwable ex)   {
        //if (ex instanceof SQLIntegrityConstraintViolationException || ex instanceof ConstraintViolationException)
            //return true;
        if (ex instanceof PSQLException)    {
            PSQLException pex = (PSQLException) ex;
            // org.postgresql.util.PSQLException: ERROR: duplicate key value violates unique constraint
            if (pex.getErrorCode() == 23505)
                return true;
        }
        if (ex.getCause() != null)
            return hasSQLIntegrityConstraintViolationException (ex.getCause());
        return false;
    }
}