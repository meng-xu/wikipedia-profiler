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
public class LogConfig extends PropertiesBasedConfig{

    public String PAGECOUNT_DOWNLOAD;
    public String PAGECOUNT_PARSE;
    public String PAGECOUNT_CONSOLIDATE;
    public String METADATA;
    
    public LogConfig(String filename) {
        super(filename);
    }
    
}
