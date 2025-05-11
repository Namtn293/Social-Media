package com.namtn.media.core.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class CommonUtil {
    public static final String DIACRITIC_CHARACTERS =
            "áàảãạâấầẩẫậăắằẳẵặ" + // a
                    "éèẻẽẹêếềểễệ" + // e
                    "íìỉĩị" + // i
                    "óòỏõọôốồổỗộơớờởỡợ" + // o
                    "úùủũụưứừửữự" + // u
                    "ýỳỷỹỵ" + // y
                    "đ"; // d

    public static final String NON_DIACRITIC_CHARACTERS =
            "aaaaaaaaaaaaaaaaa" +
                    "eeeeeeeeeeee" +
                    "iiiiii" +
                    "ooooooooooooooooooo" +
                    "uuuuuuuuuuu" +
                    "yyyyy" +
                    "dd";

    public static boolean listIsNullOrEmpty(Collection collection){
        return collection.isEmpty() || collection==null;
    }

    //delete space
    public static void trimObj(Object obj) throws IllegalAccessException{
        Field[] fields=obj.getClass().getDeclaredFields();
        for (Field field:fields){
            field.setAccessible(true);
            if (field.getType() == String.class) {
                String value = (String) field.get(obj);
                if (value!=null){
                    String trimmedValue = value.trim();
                    field.set(obj,trimmedValue);
                }
            }
        }
    }

    //replace letter
    public static String replaceAccent(String input){
        StringBuilder output=new StringBuilder(input.length());
        for (char c:input.toCharArray()){
            int index = DIACRITIC_CHARACTERS.indexOf(c);
            if (index>0){
                output.append(NON_DIACRITIC_CHARACTERS.charAt(index));
            } else {
                output.append(c);
            }
        }
        return output.toString();
    }

    public static String deleteAllWhileSpace(String s){
        return s.replace("\\s+","");
    }

    public static String handleFullSearch(String s){
        deleteAllWhileSpace(s);
        s=s.toLowerCase();
        replaceAccent(s);
        return s;
    }

    public static LocalDateTime convertDateToLocalDateTime(Date date){
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
