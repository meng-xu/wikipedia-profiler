/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.meng.wikipedia.profiler.config;

/**
 *
 * @author xumeng
 */
public class DBConfig extends PropertiesBasedConfig{

    public String PAGECOUNT_RAW;
    public String PAGECOUNT_CONSOLIDATED;
    public String METADATA;
    
    public DBConfig(String filename) {
        super(filename);
    }
    
}
