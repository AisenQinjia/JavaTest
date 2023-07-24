package org.example.zhc.quartz;

import lombok.Data;

import java.io.Serializable;

@Data
public class TimerConfig implements Serializable {
    /**
     * 单次不循环
     */
    public final static Integer TIMER_TYPE_ONE_TIME = 1;
    /**
     * 每年
     */
    public final static Integer TIMER_TYPE_EVERY_YEAR = 5;
    /**
     * 每月
     */
    public final static Integer TIMER_TYPE_EVERY_MONTH = 4;
    /**
     *
     */
    public final static Integer TIMER_TYPE_EVERY_WEEK = 3;
    /**
     * 每日
     */
    public final static Integer TIMER_TYPE_EVERY_DAY = 2;
    Integer timerType;
    Long timerStartTime;
    Long timerEndTime;
    Integer year;
    Integer month;
    Integer monthDay;
    Integer week;
    Integer hour;
    Integer minute;
    Integer second;
    Long oneTimeStamp; // 单次时间戳
    public TimerConfig() {
    }
    public TimerConfig(Integer timerType, Long timerStartTime, Long timerEndTime, Integer year, Integer month, Integer monthDay, Integer week, Integer hour, Integer minute, Integer second, Long oneTimeStamp) {
        this.timerType = timerType;
        this.timerStartTime = timerStartTime;
        this.timerEndTime = timerEndTime;
        this.year = year;
        this.month = month;
        this.monthDay = monthDay;
        this.week = week;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.oneTimeStamp = oneTimeStamp;
    }
    public String paramCheck(){
        if(!typeValid()){
            return "timerType is invalid";
        }
        if(TIMER_TYPE_ONE_TIME.equals(timerType) && !timerOnceValid()){
            return "timerOnce is invalid";
        }
        if(TIMER_TYPE_EVERY_YEAR.equals(timerType) && !timerYearValid()){
            return "timerYear is invalid";
        }
        if(TIMER_TYPE_EVERY_MONTH.equals(timerType) && !timerMonthValid()){
            return "timerMonth is invalid";
        }
        if(TIMER_TYPE_EVERY_WEEK.equals(timerType) && !timerWeekValid()){
            return "timerWeek is invalid";
        }
        if(TIMER_TYPE_EVERY_DAY.equals(timerType) && !timerDayValid()){
            return "timerDay is invalid";
        }
        //开始时间和结束时间如果都有的话，结束时间必须大于开始时间
        if(timerStartTime!=null && timerEndTime!=null && timerEndTime<=timerStartTime){
            return "结束时间 must be greater than 开始时间";
        }
        return "";
    }

    public boolean timerOnceValid(){
        return oneTimeValid();
    }

    public boolean timerYearValid(){
        return monthValid() && monthDayValid() && hourValid() && minuteValid() && secondValid();
    }

    public boolean timerMonthValid(){
        return monthDayValid() && hourValid() && minuteValid() && secondValid();
    }

    public boolean timerWeekValid(){
        return weekValid() && hourValid() && minuteValid() && secondValid();
    }

    public boolean timerDayValid(){
        return hourValid() && minuteValid() && secondValid();
    }

    public boolean typeValid(){
        return timerType!=null && timerType>0 && timerType<6;
    }
    public boolean yearValid(){
        return year!=null && year>1970;
    }


    public boolean monthValid(){
        return month!=null && month>0 && month<13;
    }


    public boolean monthDayValid(){
        return monthDay!=null && monthDay>0 && monthDay<32;
    }

    public boolean weekValid(){
        return week!=null && week>0 && week<8;
    }

    public boolean hourValid(){
        return hour!=null && hour>=0 && hour<24;
    }

    public boolean minuteValid(){
        return minute!=null && minute>=0 && minute<60;
    }

    public boolean secondValid(){
        return second!=null && second>=0 && second<60;
    }
    public boolean oneTimeValid(){
        return oneTimeStamp!=null && oneTimeStamp>=0 && timerStartTime==null && timerEndTime==null;
    }

    public long fetchTypeOnceTime(){
        return oneTimeStamp;
    }

    public static TimerConfig everyDay(int hour,int minute,int second){
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setTimerType(TIMER_TYPE_EVERY_DAY);
        timerConfig.setHour(hour);
        timerConfig.setMinute(minute);
        timerConfig.setSecond(second);
        return timerConfig;
    }

    public static TimerConfig onetime(long time){
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setTimerType(TIMER_TYPE_ONE_TIME);
        timerConfig.setOneTimeStamp(time);
        return timerConfig;
    }
}
