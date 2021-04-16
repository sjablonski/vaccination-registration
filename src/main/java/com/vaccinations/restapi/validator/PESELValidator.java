package com.vaccinations.restapi.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PESELValidator implements ConstraintValidator<PESEL, String> {
    private final byte[] PESEL = new byte[11];

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value.length() != 11){
            return false;
        } else {
            for (int i = 0; i < 11; i++){
                PESEL[i] = Byte.parseByte(value.substring(i, i+1));
            }
            return (checkSum() && checkMonth() && checkDay());
        }
    }

    private boolean checkSum() {
        int[] weight = new int[]{1, 3, 7, 9, 1, 3, 7, 9, 1, 3};
        int sum = 0;

        for (int i = 0; i < weight.length; i++) {
            sum += PESEL[i] * weight[i];
        }
        sum = sum % 10;

        return (10 - sum) % 10 == PESEL[10];
    }

    private boolean checkMonth() {
        int month = getBirthMonth();

        return month > 0 && month < 13;
    }

    private boolean checkDay() {
        int year = getBirthYear();
        int month = getBirthMonth();
        int day = getBirthDay();
        if ((day >0 && day < 32) &&
                (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12)) {
            return true;
        }
        else if ((day >0 && day < 31) && (month == 4 || month == 6 || month == 9 || month == 11)) {
            return true;
        }
        else return (day > 0 && day < 30 && leapYear(year)) || (day > 0 && day < 29 && !leapYear(year));
    }

    private int getBirthYear() {
        int year;
        int month;
        year = 10 * PESEL[0];
        year += PESEL[1];
        month = 10 * PESEL[2];
        month += PESEL[3];
        if (month > 80 && month < 93) {
            year += 1800;
        }
        else if (month > 0 && month < 13) {
            year += 1900;
        }
        else if (month > 20 && month < 33) {
            year += 2000;
        }
        else if (month > 40 && month < 53) {
            year += 2100;
        }
        else if (month > 60 && month < 73) {
            year += 2200;
        }
        return year;
    }

    private int getBirthMonth() {
        int month;
        month = 10 * PESEL[2];
        month += PESEL[3];
        if (month > 80 && month < 93) {
            month -= 80;
        }
        else if (month > 20 && month < 33) {
            month -= 20;
        }
        else if (month > 40 && month < 53) {
            month -= 40;
        }
        else if (month > 60 && month < 73) {
            month -= 60;
        }
        return month;
    }


    private int getBirthDay() {
        int day;
        day = 10 * PESEL[4];
        day += PESEL[5];
        return day;
    }

    private boolean leapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }
}
