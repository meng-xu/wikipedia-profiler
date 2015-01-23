/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.meng.wikipedia.profiler.util;

import dev.meng.wikipedia.profiler.log.LogHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author xumeng
 */
public class StringUtils {
    
    public static String collectionToString(Collection collection, String delimeter){
        String result = "";
        if(!collection.isEmpty()){
            Iterator iterator = collection.iterator();
            result = result + iterator.next();
            while(iterator.hasNext()){
                result = result + delimeter + iterator.next();
            }
        }
        return result;
    }
    public static String arrayToString(Object[] collection, String delimeter){
        String result = "";
        if(collection.length!=0){
            result = result + collection[0];
            for(int i=1;i<collection.length;i++){
                result = result + delimeter + collection[i];
            }
        }
        return result;
    }
    public static String repetition(String base, String delimeter, int times){
        String result = "";
        if(times>0){
            result = result + base;
            for(int i=1;i<times;i++){
                result = result + delimeter + base;
            }
        }
        return result;
    }
    public static String formatTimestamp(GregorianCalendar date, String format){
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date.getTime());
    }
    public static GregorianCalendar parseTimestamp(String string, String format) throws ParseException {
        GregorianCalendar result = null;

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        result = (GregorianCalendar) GregorianCalendar.getInstance();
        result.setTime(formatter.parse(string));

        return result;
    }
    public static String replace(String string, Object... values){
        String result = string;
        for (Object value : values) {
            if(string.contains("?")){
                result = result.replaceFirst("\\?", value.toString());
            }
        }
        return result;
    }
    public static String mapToURLParameters(Map<String, Object> params){
        String result = "";
        Iterator it = params.entrySet().iterator();
        while (it.hasNext()) {
            try {
                Map.Entry<String, Object> pair = (Map.Entry) it.next();
                result += URLEncoder.encode(pair.getKey(), "utf-8") + "="
                        + URLEncoder.encode(pair.getValue().toString(), "utf-8");
                if (it.hasNext()) {
                    result += "&";
                }
            } catch (UnsupportedEncodingException ex) {
                LogHandler.console(StringUtils.class, ex);
            }
        }
        return result;
    }
    public static String inputStreamToString(InputStream input) throws IOException{
        return IOUtils.toString(input, "utf-8");
    }
    
    public static String URLEncode(String string){
        try {
            return URLEncoder.encode(string, "utf-8");
        } catch (UnsupportedEncodingException ex) {
            LogHandler.console(StringUtils.class, ex);
            return string;
        }
    }
    
    public static String URLDecode(String string){
        try {
            return URLDecoder.decode(string, "utf-8");
        } catch (UnsupportedEncodingException ex) {
            LogHandler.console(StringUtils.class, ex);
            return string;
        }
    }    
}
