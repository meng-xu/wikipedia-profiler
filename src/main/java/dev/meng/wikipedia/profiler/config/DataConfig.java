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
public class DataConfig extends PropertiesBasedConfig{

    public String PAGECOUNT_DIR;
    
    public DataConfig(String filename) {
        super(filename);
    }
    
}
