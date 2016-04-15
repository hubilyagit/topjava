package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.LoggedUser;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.TimeUtil;
import ru.javawebinar.topjava.web.meal.UserMealRestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * User: gkislin
 * Date: 22.08.2014
 */
@Controller
public class RootController {
    @Autowired
    private UserService service;

    @Autowired
    private UserMealRestController mealcontroller;

    private static final Logger LOG = LoggerFactory.getLogger(RootController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String root() {
        return "index";
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String userList(Model model) {
        model.addAttribute("userList", service.getAll());
        return "userList";
    }

    @RequestMapping(value = "/meals",method = RequestMethod.GET)
    public String meals(@RequestParam(value = "action",required = false) String action,@RequestParam(value = "id",required = false) String id,Model mod){
      if(action==null){
          LOG.info("GetAll");
        mod.addAttribute("mealList",mealcontroller.getAll());
          return "mealList";
      }
        else if("delete".equals(action)){
          String paramId = Objects.requireNonNull(id, "parameter id  must not be null");
          int parId = Integer.parseInt(paramId);
          LOG.info("deleting {}",parId);
          mealcontroller.delete(parId);
          return "redirect:meals";
      }
        else{
          UserMeal userMeal = "create".equals(action) ? new UserMeal(LocalDateTime.now(),"",22) : mealcontroller.get(Integer.parseInt(id));
          mod.addAttribute("meal",userMeal);
          return "mealEdit";
      }
    }

    @RequestMapping(value = "/meals",method = RequestMethod.POST)
    public String mealsPost(HttpServletRequest request){
        String action = request.getParameter("action");
        if (action == null) {
            final UserMeal userMeal = new UserMeal(
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.valueOf(request.getParameter("calories")));

            if (request.getParameter("id").isEmpty()) {
                LOG.info("Create {}", userMeal);
                mealcontroller.create(userMeal);
            } else {
                LOG.info("Update {}", userMeal);
                mealcontroller.update(userMeal, Integer.parseInt(request.getParameter("id")));
            }
            return "redirect:meals";
        } else if (action.equals("filter")) {
            LocalDate startDate = TimeUtil.parseLocalDate(resetParam("startDate", request));
            LocalDate endDate = TimeUtil.parseLocalDate(resetParam("endDate", request));
            LocalTime startTime = TimeUtil.parseLocalTime(resetParam("startTime", request));
            LocalTime endTime = TimeUtil.parseLocalTime(resetParam("endTime", request));
            request.setAttribute("mealList", mealcontroller.getBetween(startDate, startTime, endDate, endTime));
            return "mealList";
        }
        return "redirect:meals";
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public String setUser(HttpServletRequest request) {
        int userId = Integer.valueOf(request.getParameter("userId"));
        LoggedUser.setId(userId);
        return "redirect:meals";
    }

    private String resetParam(String param, HttpServletRequest request) {
        String value = request.getParameter(param);
        request.setAttribute(param, value);
        return value;
    }
}
