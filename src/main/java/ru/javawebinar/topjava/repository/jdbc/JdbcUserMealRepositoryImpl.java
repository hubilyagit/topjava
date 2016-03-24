package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.repository.UserMealRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * User: gkislin
 * Date: 26.08.2014
 */

@Repository
public class JdbcUserMealRepositoryImpl implements UserMealRepository {


    public static class BeanRowMapper implements RowMapper{
        @Override
        public Object mapRow(ResultSet resultSet, int i) throws SQLException {
            return  new UserMeal(resultSet.getInt("id"),resultSet.getTimestamp("datetime").toLocalDateTime(),resultSet.getString("description"),resultSet.getInt("calories"));
        }
    }
    public static final BeanRowMapper MAPPER = new BeanRowMapper();

    @Autowired
    private JdbcTemplate template;

    @Override
    public UserMeal save(UserMeal userMeal, int userId) {
        if(userMeal.isNew()){
            KeyHolder kh = new GeneratedKeyHolder();
            int i = template.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement pst = connection.prepareStatement("Insert into meals(datetime,calories,description,user_id)" +
                            " VALUES (?,?,?,?)");
                    pst.setTimestamp(1, Timestamp.valueOf(userMeal.getDateTime()));
                    pst.setInt(2,userMeal.getCalories());
                    pst.setString(3,userMeal.getDescription());
                    pst.setInt(4,userId);
                    return pst;
                }
            },kh);
            userMeal.setId((Integer)kh.getKey());
        }
        else{
            template.update("UPDATE meals SET user_id = ?,description = ?,calories = ?,datetime = ? WHERE id = ?",
                    userId,userMeal.getDescription(),userMeal.getCalories(),Timestamp.valueOf(userMeal.getDateTime()),userMeal.getId());
        }

        return userMeal;
    }

    @Override
    public boolean delete(int id, int userId) {
        return template.update("DELETE FROM meals WHERE id = ? AND user_id = ? ", id, userId) != 0;
    }

    @Override
    public UserMeal get(int id, int userId) {
        return (UserMeal) template.queryForObject("SELECT * FROM meals WHERE id = ? AND user_ID = ? ",new Object[] {id,userId} ,MAPPER);
    }

    @Override
    public List<UserMeal> getAll(int userId) {
        return template.query("SELECT * FROM meals WHERE user_ID = ? ORDER by  datetime DESC ",MAPPER,userId);
    }

    @Override
    public List<UserMeal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return template.query("SELECT * FROM meals WHERE datetime > ? AND datetime < ? ORDER BY datetime DESC ",new Object[] {Timestamp.valueOf(startDate),Timestamp.valueOf(endDate)},MAPPER);
    }
}
