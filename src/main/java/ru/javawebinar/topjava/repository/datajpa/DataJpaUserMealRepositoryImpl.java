package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.repository.UserMealRepository;
import ru.javawebinar.topjava.util.exception.ExceptionUtil;

import java.time.LocalDateTime;
import java.util.List;

/**
 * GKislin
 * 27.03.2015.
 */
@Repository
public class DataJpaUserMealRepositoryImpl implements UserMealRepository{

    @Autowired
    private ProxyUserMealRepository proxy;

    @Autowired
    private ProxyUserRepository userProxy;


    @Override
    @Transactional
    public UserMeal save(UserMeal userMeal, int userId) {
        if(userMeal.isNew()){
         userMeal.setUser(userProxy.findOne(userId));
            proxy.save(userMeal);
        }
        else{
            UserMeal pro = proxy.findOne(userMeal.getId());
            ExceptionUtil.check(pro.getUser().getId()==userId,"");
            userMeal.setUser(pro.getUser());
            proxy.save(userMeal);
        }
        return userMeal;
    }

    @Override
    public boolean delete(int id, int userId) {
        boolean an = proxy.delete(id,userId) !=0;
        ExceptionUtil.check(an,id);
        return an;
    }

    @Override
    @Transactional
    public UserMeal get(int id, int userId) {
        UserMeal um = proxy.findOne(id);
        ExceptionUtil.check(um.getUser().getId()==userId,"");
        return um;
    }

    @Override
    public List<UserMeal> getAll(int userId) {
        return proxy.getAll(userId);
    }

    @Override
    public List<UserMeal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return proxy.getAllBetween(userId,startDate,endDate);
    }
}
