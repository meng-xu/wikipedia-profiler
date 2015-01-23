/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.meng.wikipedia.profiler.pagecount.consolidated;

import dev.meng.wikipedia.profiler.Configure;
import dev.meng.wikipedia.profiler.DB;
import dev.meng.wikipedia.profiler.log.LogHandler;
import dev.meng.wikipedia.profiler.log.LogLevel;
import dev.meng.wikipedia.profiler.metadata.Metadata;
import dev.meng.wikipedia.profiler.pagecount.consolidated.db.Page;
import dev.meng.wikipedia.profiler.pagecount.consolidated.db.View;
import dev.meng.wikipedia.profiler.util.AsciiToUnicodeFormat;
import dev.meng.wikipedia.profiler.util.CodecUtils;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author meng
 */
public class Consolidator {
    
    public Consolidator(){
        LogHandler.register(this, Configure.LOG.PAGECOUNT_CONSOLIDATE);
    }
    
    public void consolidate(){
        Metadata metadata = new Metadata();
        
        try {
            List<String> langs = DB.PAGECOUNT_RAW.PAGE.retrieveAllLangs();
            
            for(String lang : langs){
                LogHandler.log(this, LogLevel.INFO, "Consolidating "+lang);
                
                List<Map<Page, Object>> newPageRecords = new LinkedList<>();
                Map<String, List<Integer>> newToOldPageIdMap = new HashMap<>();
                
                List<String> titles = DB.PAGECOUNT_RAW.PAGE.retrieveTitlesByLang(lang);
                Map<String, Integer> oldTitleToOldIdMap = DB.PAGECOUNT_RAW.PAGE.retrieveTitleToIdMapByLang(lang);
                
                Map<String, List<String>> titleMap = new HashMap<>();
                for(String title : titles){
                    String urlDecoded = title;
                    if(title.contains("%")){
                        urlDecoded = CodecUtils.asciiToUnicode(title, AsciiToUnicodeFormat.PERCENTAGE_HEX);
                    } 
                    String decoded = urlDecoded;
                    if(urlDecoded.contains("\\x")){
                        decoded = CodecUtils.asciiToUnicode(urlDecoded, AsciiToUnicodeFormat.SLASH_X_HEX);
                    }
                    List<String> encodedTitleList = titleMap.get(decoded);
                    if(encodedTitleList==null){
                        encodedTitleList = new LinkedList<>();
                    }
                    encodedTitleList.add(title);
                    titleMap.put(decoded, encodedTitleList);
                }
                
                List<String> decodedTitles = new LinkedList<>(titleMap.keySet());
                
                Map<String, String>[] queryResult = metadata.queryPageIds(lang, decodedTitles);
                Map<String, String> norms = queryResult[0];
                Map<String, String> pageIds = queryResult[1];
                
                for(String title : decodedTitles){
                    String normalizedTitle = norms.get(title);
                    String newPageId = pageIds.get(normalizedTitle);
                    if(newPageId!=null){
                        List<Integer> oldPageIdList = new LinkedList<>();
                        for(String oldTitle : titleMap.get(title)){
                            oldPageIdList.add(oldTitleToOldIdMap.get(oldTitle));
                        }
                        List<Integer> oldPageIds = newToOldPageIdMap.get(newPageId);
                        if(oldPageIds==null){
                            oldPageIds = new LinkedList<>();
                            
                            Map<Page, Object> pageRecord = new HashMap<>();
                            pageRecord.put(Page.PAGE_ID, newPageId);
                            pageRecord.put(Page.TITLE, normalizedTitle);
                            pageRecord.put(Page.LANG, lang);
                            newPageRecords.add(pageRecord);                            
                        } else{
                            LogHandler.log(this, LogLevel.WARN, "Mapping of same page ids: existing: "+oldPageIds.toString()+" and "+oldPageIdList.toString()+", new: "+newPageId);
                        }
                        oldPageIds.addAll(oldPageIdList);
                        newToOldPageIdMap.put(newPageId, oldPageIds);
                    }
                }
                
                DB.PAGECOUNT_CONSOLIDATED.PAGE.insertOrIgnoreBatch(newPageRecords);
                
                Map<String, Integer> newPageIdToIdMap = DB.PAGECOUNT_CONSOLIDATED.PAGE.retrievePageIdToIdMapByLang(lang);

                List<Map<View, Object>> newViewRecords = new LinkedList<>();
                
                for(String newPageId : newPageIdToIdMap.keySet()){
                    int newId = newPageIdToIdMap.get(newPageId);
                    Map<Long, Map<View, Object>> newViewRecordsById = new HashMap<>();
                
                    for(int oldId : newToOldPageIdMap.get(newPageId)){
                        List<Map<dev.meng.wikipedia.profiler.pagecount.raw.db.View, Object>> oldViewRecords = DB.PAGECOUNT_RAW.VIEW.retrieveByPageId(oldId);
                        for(Map<dev.meng.wikipedia.profiler.pagecount.raw.db.View, Object> oldViewRecord : oldViewRecords){
                            long timestamp = (long) oldViewRecord.get(dev.meng.wikipedia.profiler.pagecount.raw.db.View.TIMESTAMP);
                            Map<View, Object> newViewRecordById = newViewRecordsById.get(timestamp);
                            if(newViewRecordById==null){
                                newViewRecordById = new HashMap<>();
                                newViewRecordById.put(View.PAGE_ID, newId);
                                newViewRecordById.put(View.TIMESTAMP, timestamp);
                                newViewRecordById.put(View.FREQUENCY, 0L);
                                newViewRecordById.put(View.SIZE, 0L);
                            }
                            long newFrequency = (long)newViewRecordById.get(View.FREQUENCY) + ((Number)oldViewRecord.get(dev.meng.wikipedia.profiler.pagecount.raw.db.View.FREQUENCY)).longValue();
                            long newSize = (long)newViewRecordById.get(View.SIZE) + ((Number)oldViewRecord.get(dev.meng.wikipedia.profiler.pagecount.raw.db.View.SIZE)).longValue();
                            newViewRecordById.put(View.FREQUENCY, newFrequency);
                            newViewRecordById.put(View.SIZE, newSize);
                            newViewRecordsById.put(timestamp, newViewRecordById);
                        }
                    }   
                    newViewRecords.addAll(newViewRecordsById.values());
                }
                
                DB.PAGECOUNT_CONSOLIDATED.VIEW.insertOrIgnoreBatch(newViewRecords);
                
            }
        } catch (SQLException ex) {
            LogHandler.log(this, LogLevel.ERROR, ex);
        }
    }
}
