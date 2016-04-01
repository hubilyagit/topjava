package ru.javawebinar.topjava.repository.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.repository.UserMealRepository;
import ru.javawebinar.topjava.util.exception.ExceptionUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.time.LocalDateTime;
import java.util.List;

/**
 * User: gkisline
 * Date: 26.08.2014
 */

@Repository
public class JpaUserMealRepositoryImpl implements UserMealRepository {

    @PersistenceContext
    public EntityManager entityManager;

    @Override
    @Transactional
    public UserMeal save(UserMeal userMeal, int userId) {

        if(userMeal.isNew()){
            User ref = entityManager.getReference(User.class, userId);
            userMeal.setUser(ref);
            entityManager.persist(userMeal);
        }
        else {
            UserMeal proxy = entityManager.find(UserMeal.class,userMeal.getId());
            ExceptionUtil.check(proxy.getUser().getId()==userId,"This food belongs to another user");
            userMeal.setUser(proxy.getUser());
            entityManager.merge(userMeal);
        }
        return userMeal;
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
       boolean an = entityManager.createQuery("DELETE FROM UserMeal um WHERE um.id =:id and um.user.id =:userId")
                .setParameter("id",id)
                .setParameter("userId",userId)
                .executeUpdate()!=0;
        ExceptionUtil.check(an,id);
        return an;
    }

    @Override
    @Transactional
    public UserMeal get(int id, int userId) {
        UserMeal um = entityManager.find(UserMeal.class,id);
        ExceptionUtil.check(um.getUser().getId()==userId,"");
        return um;
    }

    @Override
    public List<UserMeal> getAll(int userId) {
        return entityManager.createQuery("SELECT um FROM UserMeal um WHERE um.user.id =:userId order by um.id desc ",UserMeal.class).setParameter("userId",userId).getResultList();
    }

    @Override
    public List<UserMeal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return entityManager.createQuery("select um from UserMeal um WHERE um.user.id =:userId and um.dateTime between :starttime and :endtime order by um.id desc ")
                .setParameter("userId",userId)
                .setParameter("starttime",startDate)
                .setParameter("endtime",endDate).getResultList();
    }
}