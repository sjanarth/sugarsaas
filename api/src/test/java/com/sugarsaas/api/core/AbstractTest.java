package com.sugarsaas.api.core;

import com.sugarsaas.api.SugarSaaSApplication;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

//@ExtendWith(SpringExtension.class)    // Couldn't get junit5 to work (autowired mvc is null)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes=SugarSaaSApplication.class)
@PropertySource("classpath:application.properties")
public abstract class AbstractTest
{
    @Autowired
    protected MockMvc mvc;
}
