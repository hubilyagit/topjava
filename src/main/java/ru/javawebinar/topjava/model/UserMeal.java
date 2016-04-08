package ru.javawebinar.topjava.model;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * GKislin
 * 11.01.2015.
 */
@SuppressWarnings("JpaQlInspection")

@Entity
@Table(name = "MEALS", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id","date_time"}, name = "meals_unique_user_datetime_idx")})
public class UserMeal extends BaseEntity {

    @Column( name = "DATE_TIME" ,nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "CALORIES", nullable = false)
    protected int calories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "USER_ID")
    private User user;

    public UserMeal() {
    }

    public UserMeal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public UserMeal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserMeal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
