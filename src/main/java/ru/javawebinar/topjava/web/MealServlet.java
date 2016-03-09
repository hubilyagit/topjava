package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;
import ru.javawebinar.topjava.util.UserMealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by alik72908 on 07.03.2016.
 */
public class MealServlet extends HttpServlet {



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<UserMeal> list = UserMealsUtil.mapa.entrySet()
                .stream()
                .sorted((a,b) -> a.getValue().getDateTime().compareTo(b.getValue().getDateTime()))
                .map(Map.Entry :: getValue)
                .collect(Collectors.toList());
        List<UserMealWithExceed> utilList = UserMealsUtil.getFilteredMealWithStreams(list,2000);
        request.setAttribute("list", utilList);
        request.getRequestDispatcher("/WEB-INF/view/loop.jsp").forward(request,response);
    }
}
