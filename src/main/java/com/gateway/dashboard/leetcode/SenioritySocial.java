package com.gateway.dashboard.leetcode;

import java.util.List;
import java.util.function.Function;

public class SenioritySocial {

    /**
     * Given a hierarchy of employees as a tree, every person has some seniority level based on their experience.
     * You want to get maxSeniorityPoint
     * If you are calling manager you cannot call their direct subordinates and vice versa.
     * @param id
     * @param subordinates
     * @param seniorityLevel
     */
    record Employee(
            Integer id,
            List<Employee> subordinates,
            Integer seniorityLevel
    ){}

    public static int maxSeniorityLevel(final Employee employee) {
        return Math.max(
                maxSeniorityLevel(employee, false) + employee.seniorityLevel,
                maxSeniorityLevel(employee, true)
        );
    }

    private static int maxSeniorityLevel(final Employee employee, final boolean include) {
        final List<Employee> sub = employee.subordinates;
        if(sub == null || sub.isEmpty()) {
            if (include)
                return employee.seniorityLevel;
            else
                return 0;
        }
        if(include){
            return employee.seniorityLevel +
                    sub.stream().map(e -> maxSeniorityLevel(e, false))
                            .mapToInt(Integer::intValue).sum();
        } else {
            return sub.stream().map(e -> maxSeniorityLevel(e, true))
                    .mapToInt(Integer::intValue).sum();
        }
    }

}
