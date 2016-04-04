package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.UserMeal;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by ilya on 03.04.2016.
 */
public interface ProxyUserMealRepository extends JpaRepository<UserMeal,Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM UserMeal um WHERE um.id =:id and um.user.id=:userId")
     int delete(@Param("id")int id,@Param("userId")int userId);

    @Override
    @Transactional
    UserMeal save(UserMeal um);

    @Override
    @Transactional
    UserMeal findOne(Integer id);

    @Query("SELECT um FROM UserMeal um WHERE um.user.id =:userId order by um.id desc")
    List<UserMeal> getAll(@Param("userId")int userId);

    @Query("select um from UserMeal um WHERE um.user.id =:userId and um.dateTime between :starttime and :endtime order by um.id desc")
    List<UserMeal> getAllBetween(@Param("userId")int userId, @Param("starttime")LocalDateTime starttime,@Param("endtime")LocalDateTime endtime);


}
