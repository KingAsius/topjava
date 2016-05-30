package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        List<UserMealWithExceed> list = getFilteredMealsWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
        for (UserMealWithExceed mealWithExceed : list) System.out.println(mealWithExceed.toString());
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed>  getFilteredMealsWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExceed> exceedList = new ArrayList<>();
        mealList = mealList.stream().sorted((o1, o2) -> o1.getDateTime().compareTo(o2.getDateTime())).collect(Collectors.toList());

        for (int i = 0; i < mealList.size(); i++) {
            final UserMeal meal = mealList.get(i);
            List<UserMeal> list = mealList.stream().filter((p)->p.getDateTime().toLocalDate().isEqual(meal.getDateTime()
                    .toLocalDate())).collect(Collectors.toList());
            i += list.size() - 1;
            final int calories = list.parallelStream().mapToInt((m)-> m.getCalories()).sum();
            list.stream().filter((p)->p.getDateTime().toLocalTime().isAfter(startTime) && p.getDateTime().toLocalTime().isBefore(endTime))
                    .forEach(m->exceedList.add(new UserMealWithExceed(m.getDateTime(), m.getDescription(), m.getCalories(), calories > caloriesPerDay)));
        }
        return exceedList;
    }
}
