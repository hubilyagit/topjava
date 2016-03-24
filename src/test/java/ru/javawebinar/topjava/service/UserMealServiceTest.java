package ru.javawebinar.topjava.service;

import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.ADMIN_MEAL_1;
import static ru.javawebinar.topjava.MealTestData.MATCHER;
import static ru.javawebinar.topjava.MealTestData.USER_MEAL_2;

/**
 * Created by ilya on 24.03.2016.
 */
public class UserMealServiceTest {

    private static ConfigurableApplicationContext appCtx;
    protected static UserMealService service;
    private static DbPopulator populator;

    @BeforeClass
    public static void setUp() throws Exception {
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml", "spring/spring-db.xml");
        System.out.println("\n" + Arrays.toString(appCtx.getBeanDefinitionNames()) + "\n");
        service = appCtx.getBean(UserMealService.class);
        populator = appCtx.getBeanFactory().getBean(DbPopulator.class);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        appCtx.close();
    }

    @Before
    public void beforeTest(){
           populator.execute();
    }

    @Test
    public void get() throws Exception {

    }

    @Test()
    public void delete() throws Exception {
        service.delete(100002,100000);
        MATCHER.assertCollectionEquals(Arrays.asList(USER_MEAL_2),service.getAll(100000));
    }

    @Test
    public void getBetweenDates() throws Exception {
    }

    @Test
    public void getBetweenDateTimes() throws Exception {

    }

    @Test
    public void getAll() throws Exception {

    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void save() throws Exception {

    }
}