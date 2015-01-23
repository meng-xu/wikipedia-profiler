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
public class DaemonConfig extends PropertiesBasedConfig{

    public String ASCII_TO_UNI_DAEMON;
    
    public DaemonConfig(String filename) {
        super(filename);
    }
    
}
