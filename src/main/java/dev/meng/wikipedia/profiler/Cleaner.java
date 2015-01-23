/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.meng.wikipedia.profiler;

import dev.meng.wikipedia.profiler.log.LogHandler;
import dev.meng.wikipedia.profiler.log.LogLevel;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author meng
 */
public class Cleaner {
    
    public static void cleanDB(List<String> values){
        for(String value : values){
            try {
                String[] splits = value.split(":");
                Path filepath = Paths.get(splits[splits.length-1]);
                Files.deleteIfExists(filepath);
            } catch (IOException ex) {
                LogHandler.console(Cleaner.class, ex);
            }
        }
    }
    
    public static void cleanData(List<String> values){
        for(String value : values){
            try {
                Path filepath = Paths.get(value);
                File file = filepath.toFile();
                if(file.exists()){
                    if(file.isFile()){
                        Files.delete(filepath);
                    } else if(file.isDirectory()){
                        FileUtils.deleteDirectory(file);
                    }
                }
            } catch (IOException ex) {
                LogHandler.console(Cleaner.class, ex);
            }
        }
    }
    
    public static void cleanLog(List<String> values){
        for (String value : values) {
            for (LogLevel level : LogLevel.values()) {
                try {
                    Path filepath = Paths.get(value + "-" + level.name().toLowerCase() + ".log");
                    Files.deleteIfExists(filepath);
                } catch (IOException ex) {
                    LogHandler.console(Cleaner.class, ex);
                }
            }
        }
    }
    
    public static void cleanResult(){
        
    }
}
