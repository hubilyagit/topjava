package ru.javawebinar.topjava.repository.mock;

import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by alik72908 on 13.03.2016.
 */
public class InMemoryUserRepositoryImpl implements UserRepository {

    private Map<Integer,User> map = new ConcurrentHashMap<>();

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public User save(User user) {
        if(user.isNew()){
            user.setId(atomicInteger.getAndIncrement());
            map.put(user.getId(),user);
        }
        map.put(user.getId(),user);
        return user;
    }

    @Override
    public boolean delete(int id) {
       return  map.remove(id)!=null;
    }

    @Override
    public User get(int id) {
        return map.get(id);
    }

    @Override
    public User getByEmail(String email) {
        return (User) map.entrySet().stream().filter(e -> e.getValue().getEmail().equals(email)).findAny().orElse(null);
    }

    @Override
    public List<User> getAll() {
        return map.entrySet().stream()
                              .map(Map.Entry :: getValue)
                              .sorted((v1,v2) -> v1.getName().compareTo(v2.getName()))
                               .collect(Collectors.toList());
    }
}
