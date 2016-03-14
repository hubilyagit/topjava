package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.LoggedUser;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.service.UserMealService;
import ru.javawebinar.topjava.service.UserMealServiceImpl;
import ru.javawebinar.topjava.util.TimeUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GKislin
 * 06.03.2015.
 */
@Controller
public class UserMealRestController {

    @Autowired
    private UserMealService service;


    public List<UserMeal> getAll(){
        return service.getAll(LoggedUser.id());
    }

    public List<UserMeal> getFiltered(LocalDate from, LocalTime fromT,LocalDate to,LocalTime toT){
        LocalDateTime ldfrom = LocalDateTime.of(from,fromT);
        LocalDateTime ldto = LocalDateTime.of(to,toT);
        return getAll().stream()
                .filter(v -> TimeUtil.isBetween(v.getDateTime(),ldfrom,ldto))
                .collect(Collectors.toList());
    }

    public UserMeal getById(int id){
      return  service.get(id,LoggedUser.id());
    }

    public void delete(int id){
        service.delete(id,LoggedUser.id());
    }

    public UserMeal save(UserMeal u){
        return service.save(u, LoggedUser.id());
    }



}
