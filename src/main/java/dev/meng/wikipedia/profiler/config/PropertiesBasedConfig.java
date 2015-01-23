/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.meng.wikipedia.profiler.config;

import dev.meng.wikipedia.profiler.log.LogHandler;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 *
 * @author xumeng
 */
public class PropertiesBasedConfig {

    private Properties properties;
    
    public PropertiesBasedConfig(String filename) {
        try {
            
            properties = new Properties();
            properties.load(new FileInputStream(filename));

            for(Field field : this.getClass().getDeclaredFields()){
                String value = properties.getProperty(field.getName());
                if(field.getType()==String.class){
                    field.set(this, properties.get(field.getName()));
                } else if(field.getType()==int.class){
                    field.set(this, Integer.parseInt(value));
                } else if(field.getType()==double.class){
                    field.set(this, Double.parseDouble(value));
                } else if(field.getType()==boolean.class){
                    field.set(this, Boolean.parseBoolean(value));
                } else if(field.getType()==long.class){
                    field.set(this, Long.parseLong(value));
                } 
            }
        } catch (IOException ex) {
            LogHandler.console(this, ex);
        } catch (IllegalArgumentException ex) {
            LogHandler.console(this, ex);
        } catch (IllegalAccessException ex) {
            LogHandler.console(this, ex);
        }
    }
    
    public String getValue(String key){
        return properties.getProperty(key);
    }
    
    public void list(){
        properties.list(System.out);
    }
}
