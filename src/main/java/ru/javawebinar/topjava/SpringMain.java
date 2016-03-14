package ru.javawebinar.topjava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.web.meal.UserMealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;
import ru.javawebinar.topjava.web.user.ProfileRestController;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * User: gkislin
 * Date: 22.08.2014
 */
@Component
public class SpringMain {


    public static void main(String[] args) {


        // java 7 Automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println(Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            System.out.println(adminUserController.create(new User(0, "userName", "email", "password", Role.ROLE_ADMIN)));

            UserMealRestController controller = appCtx.getBean(UserMealRestController.class);
            System.out.println(controller.getAll());
            System.out.println();
            System.out.println(controller.getById(2));
            controller.delete(2);
            System.out.println(controller.getAll());
            controller.save(new UserMeal(LocalDateTime.now(),"dedede",200));
            System.out.println(controller.getFiltered(controller.getById(3).getDateTime().toLocalDate(),controller.getById(3).getDateTime().toLocalTime(),controller.getById(5).getDateTime().toLocalDate(),controller.getById(5).getDateTime().toLocalTime()));



        }
    }
}
