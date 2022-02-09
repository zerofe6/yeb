package com.fz.server.converter;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: fanbo
 * @Date: 2022/01/04/8:45
 * @Description:日期格式转换
 */
@Component
public class DateConverter implements Converter<String,LocalDate> {

    @Override
    public LocalDate convert(String s) {
        try {
            if(s == null || s.replaceAll(" ","").equals("")){
                 return null;
            }
            return LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }
}
