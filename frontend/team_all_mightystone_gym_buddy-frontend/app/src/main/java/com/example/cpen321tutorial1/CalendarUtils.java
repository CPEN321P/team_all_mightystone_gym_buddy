package com.example.cpen321tutorial1;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarUtils {

    public static LocalDate selectedDate;

    public static ArrayList<LocalDate> daysInMonthArray() {
        ArrayList<LocalDate> daysInMonthArray = new ArrayList<>();

        YearMonth yearMonth = YearMonth.from(selectedDate);
        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate previousMonth = selectedDate.minusMonths(1);
        LocalDate nextMonth = selectedDate.plusMonths(1);

        YearMonth PreviousYearMonth = YearMonth.from(previousMonth);
        int PreviousDaysInMonth = PreviousYearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 1; i <= 42; i++){
            if (i <= dayOfWeek) {
                daysInMonthArray.add(
                        LocalDate.of(previousMonth.getYear(),
                        previousMonth.getMonth(),
                        PreviousDaysInMonth + i - dayOfWeek)
                );
            }
            else if (i > daysInMonth +  dayOfWeek){
                daysInMonthArray.add(LocalDate.of(nextMonth.getYear(),
                        nextMonth.getMonth(),
                        i - dayOfWeek - daysInMonth));
            }
            else{
                daysInMonthArray.add(LocalDate.of(selectedDate.getYear(),
                        selectedDate.getMonth(),
                        i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    public static String monthYearFromDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public static String monthDayFromDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d");
        return date.format(formatter);
    }

    public static String formattedTime(LocalTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        return time.format(formatter);
    }

    public static String formattedShortTime(LocalTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }


    public static ArrayList<LocalDate> daysInWeekArray(LocalDate selectedDate) {
        ArrayList<LocalDate> days = new ArrayList<>();
        LocalDate current = sundayForDate(selectedDate);
        LocalDate end = current.plusWeeks(1);

        while (current.isBefore(end)) {
            days.add(current);
            current = current.plusDays(1);
        }

        return days;
    }

    private static LocalDate sundayForDate(LocalDate selectedDate) {
        LocalDate oneWeekAgo = selectedDate.minusWeeks(1);
        while(selectedDate.isAfter(oneWeekAgo)){
            if(selectedDate.getDayOfWeek() == DayOfWeek.SUNDAY)
                return selectedDate;

            selectedDate.minusDays(1);
        }

        return null;
    }
}
