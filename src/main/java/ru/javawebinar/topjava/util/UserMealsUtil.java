package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,11,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510),
                new UserMeal(LocalDateTime.of(2015, Month.JUNE, 30,10,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.JUNE, 30,10,3), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.JUNE, 30,11,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.JUNE, 30,12,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.JULY, 30,9,0), "Обед", 3000)
        );
        List<UserMeal> test = filledListFactory();
        long t1 = System.nanoTime();
       List<UserMealWithExceed> ll = getFilteredMealsWithExceeded(test, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
        System.out.println(System.nanoTime()-t1);
        t1 = System.nanoTime();
        ll = getFilteredMealWithStreams(test,LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
        System.out.println(System.nanoTime()-t1);
        t1 = System.nanoTime();
        ll = getFilteredMealWithStreams2(test,LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
        System.out.println(System.nanoTime()-t1);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed>  getFilteredMealsWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExceed> list = new ArrayList<>();
        int calcount = 0;
        boolean exceed;
        LocalDate currDay;
        for(int j = 0 ;j < mealList.size();j++){
            UserMeal um = mealList.get(j);
            currDay = um.getDateTime().toLocalDate();
            calcount += um.getCalories();
            if(j < mealList.size()-1)um = mealList.get(j+1);
            if(um.getDateTime().toLocalDate().compareTo(currDay)>0 || j == mealList.size()-1){
                    exceed = calcount>caloriesPerDay;
                        for (int i = j; i >= 0 ;i--){
                            if (mealList.get(i).getDateTime().toLocalDate().compareTo(currDay) < 0) break;
                            if(TimeUtil.isBetween(mealList.get(i).getDateTime().toLocalTime(),startTime,endTime)) {
                                list.add(convertor(mealList.get(i), exceed));
                            }
                        }
                calcount = 0;
            }
        }
        return list;
    }
    private static UserMealWithExceed convertor(UserMeal meal,boolean exceed){
        return new UserMealWithExceed(meal.getDateTime(),meal.getDescription(),meal.getCalories(),exceed);
    }

    public static List<UserMealWithExceed> getFilteredMealWithStreams(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay){
        ArrayList<UserMealWithExceed> list = new ArrayList<>();
         Map<LocalDate,Integer> map = mealList.stream().collect(Collectors.groupingBy(p -> p.getDateTime().toLocalDate(),Collectors.summingInt( UserMeal :: getCalories)));

        mealList.stream().filter(p-> TimeUtil.isBetween(p.getDateTime().toLocalTime(),startTime,endTime)).map(p -> {
            boolean exceed = map.get(p.getDateTime().toLocalDate()) > caloriesPerDay;
            return convertor(p,exceed);
        }).collect(Collectors.toCollection(()->list));

        return list;
    }

    public static List<UserMealWithExceed> getFilteredMealWithStreams2(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay){
        ArrayList<UserMealWithExceed> list  = new ArrayList<>();

       Map<LocalDate,List<UserMeal>> map =  mealList.stream().collect(Collectors.groupingBy(p -> p.getDateTime().toLocalDate()));

        map.forEach((v,h) -> {
            boolean exceed  =  h.stream().collect(Collectors.summingInt(UserMeal :: getCalories)) > caloriesPerDay;
            h.stream().filter(p -> TimeUtil.isBetween(p.getDateTime().toLocalTime(),startTime,endTime))
                      .map(p -> convertor(p,exceed)).collect(Collectors.toCollection(() -> list));
        });
        return list;
    }

    public static List<UserMeal> filledListFactory(){
        List<UserMeal> list = new ArrayList<>();
        LocalDateTime ld = LocalDateTime.of(2013,Month.APRIL,2,8,0);
        for (int i = 0; i <5000 ; i++) {
            list.add(new UserMeal(ld,"ddd",(int)(Math.random()*2000)));
            ld = ld.plusHours(4);
        }
        return list;
    }
}
