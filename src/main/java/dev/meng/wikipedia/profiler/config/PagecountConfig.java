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
public class PagecountConfig extends PropertiesBasedConfig{

    public String TIMESTAMP_FORMAT;
    public String FILENAME_FORMAT;
    public int FREQUENCY_THRESHOLD;
    public long HTTP_WAIT_TIME;
    public String DATA_SOURCE;
    
    public PagecountConfig(String filename) {
        super(filename);
    }
    
}
