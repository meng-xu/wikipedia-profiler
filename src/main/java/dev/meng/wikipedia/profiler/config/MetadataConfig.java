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
public class MetadataConfig extends PropertiesBasedConfig{

    public String API_ENDPOINT;
    public int REQUEST_LENGTH_MAX;
    public String TIMESTAMP_FORMAT;
    
    public MetadataConfig(String filename) {
        super(filename);
    }
    
}
