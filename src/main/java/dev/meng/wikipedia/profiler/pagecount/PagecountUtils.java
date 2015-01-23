/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.meng.wikipedia.profiler.pagecount;

import dev.meng.wikipedia.profiler.Configure;
import dev.meng.wikipedia.profiler.util.StringUtils;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.GregorianCalendar;

/**
 *
 * @author meng
 */
public class PagecountUtils {
    private static String timestampToName(GregorianCalendar timestamp){
        return StringUtils.replace(Configure.PAGECOUNT.FILENAME_FORMAT, StringUtils.formatTimestamp(timestamp, Configure.PAGECOUNT.TIMESTAMP_FORMAT));
    }
    public static Path timestampToFilepath(GregorianCalendar timestamp){
        String filename = timestampToName(timestamp);
        return Paths.get(Configure.DATA.PAGECOUNT_DIR, filename);
    }
    public static URL timestampToURL(GregorianCalendar timestamp) throws MalformedURLException{
        return new URL(Configure.PAGECOUNT.DATA_SOURCE+StringUtils.formatTimestamp(timestamp, "yyyy")+"/"+StringUtils.formatTimestamp(timestamp, "yyyy-MM")+"/"+timestampToName(timestamp));
    }
}
