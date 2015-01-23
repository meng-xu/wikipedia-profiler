/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.meng.wikipedia.profiler.pagecount.raw.db;

import dev.meng.wikipedia.profiler.db.SQLTable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author xumeng
 */
public class PageTable extends SQLTable{

    public PageTable(Connection database) {
        super(Page.class, database);
    }
        
    public List<String> retrieveAllLangs() throws SQLException{
        List<String> result = new LinkedList<>();
        
        Map<Page, Object> criteria = new HashMap<>();
        
        List<Map<Page, Object>> records = this.selectDistinct(new Page[]{Page.LANG}, criteria);
        for(Map<Page, Object> record : records){
            result.add((String) record.get(Page.LANG));
        }
        
        return result;
    }
    
    public List<Map<Page, Object>> retrieveAll() throws SQLException{
        Map<Page, Object> criteria = new HashMap<>();
        return this.select(new Page[]{Page.ID, Page.LANG, Page.TITLE}, criteria);
    }
    
    public Map<String, Integer> retrieveTitleToIdMapByLang(String lang) throws SQLException{
        Map<String, Integer> result = new HashMap<>();
        
        Map<Page, Object> criteria = new HashMap<>();
        criteria.put(Page.LANG, lang);
        
        List<Map<Page, Object>> records = this.select(new Page[]{Page.ID, Page.TITLE}, criteria);
        for(Map<Page, Object> record : records){
            result.put((String)record.get(Page.TITLE), (int) record.get(Page.ID));
        }
        
        return result;
    }
    
    public List<String> retrieveTitlesByLang(String lang) throws SQLException{
        List<String> result = new LinkedList<>();
        
        Map<Page, Object> criteria = new HashMap<>();
        criteria.put(Page.LANG, lang);
        
        List<Map<Page, Object>> records = this.select(new Page[]{Page.TITLE}, criteria);
        for(Map<Page, Object> record : records){
            result.add((String) record.get(Page.TITLE));
        }
        
        return result;
    }
    
    public int retrieveIdByLangAndTitle(String lang, String title) throws SQLException{
        Map<Page, Object> criteria = new HashMap<>();
        criteria.put(Page.LANG, lang);
        criteria.put(Page.TITLE, title);
        
        List<Map<Page, Object>> result = this.select(new Page[]{Page.ID}, criteria);
        if(result.size()!=1){
            return -1;
        } else{
            return (int) result.get(0).get(Page.ID);
        }
    }
}
