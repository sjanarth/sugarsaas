package com.sugarsaas.api.identity;

import com.sugarsaas.api.core.AbstractTest;
import com.sugarsaas.api.identity.user.User;
import com.sugarsaas.api.identity.user.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest extends AbstractTest<User>
{
    @Autowired
    UserRepository userRepository;

    @Override
    protected String getJsonForCreate() {
        return "{\"firstName\":\"TestFirstName\",\"lastName\":\"TestLastName\",\"email\":\"test@test.com\",\"passWord\":\"test\"}";
    }

    @Override
    protected User lookupEntity() {
        return userRepository.findByEmail("test@test.com").get();
    }

    @Override
    protected void update(User user) {
        user.setFirstName("TestFirstName2");
        user.setLastName("TestLastName2");
        user.setPassWord("test");
    }

    @Override
    protected void checkUpdate(ResultActions resultActions) {
        try {
            resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("TestFirstName2"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("TestLastName2"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}