package org.acme.util;

import org.acme.entity.User;

import java.time.LocalDate;
import java.time.Period;

public class DateUtil {
    private DateUtil() {
    }


    public static Integer calculateAge(User user){
        return DateUtil.calculateAge(user.getBirthdate());
    }
    public static Integer calculateAge(LocalDate birthdayUser){
        return DateUtil.calculateAge(birthdayUser, LocalDate.now());
    }
    public static Integer calculateAge(LocalDate birthdayUser, LocalDate today){
        if( birthdayUser == null ) return null;
        Period period = Period.between(birthdayUser, today);
        return period.getYears();
    }


}
