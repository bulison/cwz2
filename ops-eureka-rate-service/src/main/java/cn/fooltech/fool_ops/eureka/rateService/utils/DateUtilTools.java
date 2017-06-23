package cn.fooltech.fool_ops.eureka.rateService.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 2017/3/30.
 */
public class DateUtilTools {

    /**
     * String转化为Date，转换失败返回null
     * @param strDate
     * @return
     */
    public static Date string2Date(String strDate) {
        Date date;
        if (strDate==null) return null;
        if(strDate.trim()=="") return null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try{
            date=formatter.parse(strDate);
        }catch (Exception e){
            date=null;
        }
        return date;
    }
}
