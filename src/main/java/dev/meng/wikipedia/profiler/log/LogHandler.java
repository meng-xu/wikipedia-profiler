/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.meng.wikipedia.profiler.log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author meng
 */
public class LogHandler {
    private static Map<Object, Map<LogLevel, Logger>> loggers = new HashMap<>();
    private static Logger con = Logger.getLogger(LogHandler.class .getName());
    
    public static void console(Object obj, String message){
        con.log(Level.SEVERE, obj.getClass()+" - "+message);
    }
    
    public static void console(Object obj, Throwable exception){
        con.log(Level.SEVERE, obj.getClass()+" - ", exception);
    }
    
    public static void console(Class cls, String message){
        con.log(Level.SEVERE, cls+" - "+message);
    }
    
    public static void console(Class cls, Throwable exception){
        con.log(Level.SEVERE, cls+" - ", exception);
    }
    
    public static void log(Object obj, LogLevel level, String message){
        Logger logger = loggers.get(obj).get(level);
        if(logger==null){
            console(obj, message);
        } else{
            logger.log(Level.INFO, message);
        }
    }
    
    public static void log(Object obj, LogLevel level, Throwable exception){
        Logger logger = loggers.get(obj).get(level);
        if(logger==null){
            console(obj, exception);
        } else{
            logger.log(Level.INFO, null, exception);
        }
    }
    
    public static void register(Object obj, String filename){
        Map<LogLevel, Logger> loggerMap = new HashMap<>();
        for(LogLevel level : LogLevel.values()){
            Logger logger = createLogger(obj.getClass(), level, filename);
            loggerMap.put(level, logger);
        }

        loggers.put(obj, loggerMap);
    }
    
    private static Logger createLogger(Class cls, LogLevel level, String filename){
        Logger logger = null;
        try {
            logger = Logger.getLogger(cls.getName()+"-"+level.name().toLowerCase());
            FileHandler handler = new FileHandler(filename+"-"+level.name().toLowerCase()+".log");
            handler.setFormatter(new LogFormatter());
            logger.addHandler(handler);
            logger.setUseParentHandlers(false);
        } catch (IOException ex) {
            console(LogHandler.class, ex);
        } catch (SecurityException ex) {
            console(LogHandler.class, ex);
        }
        return logger;
    }
}
