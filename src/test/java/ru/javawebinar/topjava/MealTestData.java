package ru.javawebinar.topjava;

import ru.javawebinar.topjava.matcher.ModelMatcher;
import ru.javawebinar.topjava.model.UserMeal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * GKislin
 * 13.03.2015.
 */
public class MealTestData {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final UserMeal USER_MEAL_1 = new UserMeal(100002,LocalDateTime.parse("2015-11-09 00:00",formatter),"dinner",200);
    public static final UserMeal USER_MEAL_2 = new UserMeal(100003,LocalDateTime.parse("2015-03-30 00:00",formatter),"завтрак",200);
    public static final UserMeal ADMIN_MEAL_1 = new UserMeal(100004,LocalDateTime.parse("2015-03-30 00:00",formatter),"завтрак",200);

    public static final ModelMatcher<UserMeal, String> MATCHER = new ModelMatcher<>(UserMeal::toString);

}
