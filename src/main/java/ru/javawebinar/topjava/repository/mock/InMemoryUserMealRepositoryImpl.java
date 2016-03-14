package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.repository.UserMealRepository;
import ru.javawebinar.topjava.util.UserMealsUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * GKislin
 * 15.09.2015.
 */
@Repository
public class InMemoryUserMealRepositoryImpl implements UserMealRepository {
    private Map<Integer,Map<Integer,UserMeal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        repository.put(0, new ConcurrentHashMap<>());
        UserMealsUtil.MEAL_LIST.forEach(v -> save(v,0));
    }

    @Override
    public UserMeal save(UserMeal userMeal,int userId) {
        if (userMeal.isNew()) {
            userMeal.setId(counter.incrementAndGet());
        }
        if(isMine(userMeal.getId(),userId)){
            repository.get(userId).put(userMeal.getId(),userMeal);
            return userMeal;
        }
        else return null;
    }

    @Override
    public boolean delete(int id,int userId) {
       return repository.get(userId).remove(id)!=null;
    }

    @Override
    public UserMeal get(int id,int userId) {
        return repository.get(userId).get(id);
    }

    private boolean isMine(int id,int userId){
        for (Map.Entry<Integer,Map<Integer,UserMeal>> v : repository.entrySet()){
            for (Map.Entry<Integer,UserMeal> v2 : v.getValue().entrySet()){
                if(v2.getKey()==id && v.getKey()!=userId)return false;
            }
        }
        return true;
    }

    @Override
    public Collection<UserMeal> getAll(int userId) {
        Collection<UserMeal> collection = repository.get(userId).values();
       return collection.stream().sorted((v1,v2) -> v1.getDateTime().compareTo(v2.getDateTime())).collect(Collectors.toCollection(ArrayList :: new));
    }
}

