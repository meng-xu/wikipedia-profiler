/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.meng.wikipedia.profiler.pagecount.raw;

import dev.meng.wikipedia.profiler.Configure;
import dev.meng.wikipedia.profiler.DB;
import dev.meng.wikipedia.profiler.log.LogHandler;
import dev.meng.wikipedia.profiler.log.LogLevel;
import dev.meng.wikipedia.profiler.pagecount.PagecountUtils;
import dev.meng.wikipedia.profiler.pagecount.raw.db.Page;
import dev.meng.wikipedia.profiler.pagecount.raw.db.Summary;
import dev.meng.wikipedia.profiler.pagecount.raw.db.View;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author meng
 */
public class Parser {
    public Parser(){
        LogHandler.register(this, Configure.LOG.PAGECOUNT_PARSE);
    }
            
    public void parse(GregorianCalendar start, GregorianCalendar end){
        GregorianCalendar current = start;
        while(current.getTimeInMillis()<=end.getTimeInMillis()){
            parse(current);
            current.add(GregorianCalendar.HOUR_OF_DAY, 1);
        }
    }
    
    public void parse(GregorianCalendar timestamp){
        Path filepath = PagecountUtils.timestampToFilepath(timestamp);
        LogHandler.log(this, LogLevel.INFO, filepath.getFileName()+" parsing");      
        
        try {
            Scanner input = new Scanner(new GZIPInputStream(new FileInputStream(filepath.toFile())), StandardCharsets.UTF_8.name());
            
            Map<String, Map<Summary, Object>> summary = new HashMap<>();
            List<Map<Page, Object>> pageRecords = new LinkedList<>();
            List<Map<View, Object>> viewRecords = new LinkedList<>();
            
            long timestampValue = timestamp.getTimeInMillis();
            
            while(input.hasNext()){
                String line = input.nextLine();
                StringTokenizer tokenizer = new StringTokenizer(line);
                try{
                    String lang = tokenizer.nextToken();
                    String title = tokenizer.nextToken();
                    long frequency = Long.parseLong(tokenizer.nextToken());
                    long size = Long.parseLong(tokenizer.nextToken());

                    if(!lang.contains(".")){
                        if(frequency>Configure.PAGECOUNT.FREQUENCY_THRESHOLD){
                            Map<Page, Object> pageRecord = new HashMap<>();
                            pageRecord.put(Page.LANG, lang);
                            pageRecord.put(Page.TITLE, title);
                            pageRecords.add(pageRecord);
                            
                            Map<View, Object> viewRecord = new HashMap<>();
                            viewRecord.put(View.PAGE_ID, pageRecord);
                            viewRecord.put(View.TIMESTAMP, timestampValue);
                            viewRecord.put(View.FREQUENCY, frequency);
                            viewRecord.put(View.SIZE, size);                           
                            viewRecords.add(viewRecord);
                        }

                        Map<Summary, Object> summaryRecord = summary.get(lang);
                        if(summaryRecord==null){
                            summaryRecord = new HashMap<>();
                            summaryRecord.put(Summary.LANG, lang);
                            summaryRecord.put(Summary.TIMESTAMP, timestampValue);
                            summaryRecord.put(Summary.FREQUENCY, 0L);
                            summaryRecord.put(Summary.SIZE, 0L);
                        }
                        summaryRecord.put(Summary.FREQUENCY, (long)summaryRecord.get(Summary.FREQUENCY)+frequency);
                        summaryRecord.put(Summary.SIZE, (long)summaryRecord.get(Summary.SIZE)+size);                    
                    }
                } catch(NoSuchElementException ex){
                    LogHandler.log(this, LogLevel.WARN, "Unrecognized line: "+line);
                }
            }
            
            DB.PAGECOUNT_RAW.SUMMARY.insertOrIgnoreBatch(new LinkedList<Map<Summary, Object>>(summary.values()));
            DB.PAGECOUNT_RAW.PAGE.insertOrIgnoreBatch(pageRecords);
            
            List<Map<Page, Object>> updatedPageRecords = DB.PAGECOUNT_RAW.PAGE.retrieveAll();
            
            Map<String, Map<Page, Object>> updatedPageRecordMap = new HashMap<>();
            
            for(Map<Page, Object> updatedPageRecord : updatedPageRecords){
                updatedPageRecordMap.put(updatedPageRecord.get(Page.LANG)+"/"+updatedPageRecord.get(Page.TITLE), updatedPageRecord);
            }

            for(Map<View, Object> viewRecord : viewRecords){
                Map<Page, Object> pageRecord = (Map<Page, Object>)viewRecord.get(View.PAGE_ID);
                Map<Page, Object> updatedPageRecord = updatedPageRecordMap.get(pageRecord.get(Page.LANG)+"/"+pageRecord.get(Page.TITLE));
                if(updatedPageRecord!=null && updatedPageRecord.get(Page.LANG).equals(pageRecord.get(Page.LANG)) && updatedPageRecord.get(Page.TITLE).equals(pageRecord.get(Page.TITLE))){
                    viewRecord.put(View.PAGE_ID, updatedPageRecord.get(Page.ID));
                } else{
                    LogHandler.log(this, LogLevel.WARN, "Error handling data: "+pageRecord.get(Page.LANG)+" "+pageRecord.get(Page.TITLE)+" "+viewRecord.get(View.FREQUENCY)+" "+viewRecord.get(View.SIZE)+" "+updatedPageRecord);
                    viewRecords.remove(viewRecord);
                }
            }
            
            DB.PAGECOUNT_RAW.VIEW.insertOrIgnoreBatch(viewRecords);
            
        } catch (FileNotFoundException ex) {
            LogHandler.log(this, LogLevel.ERROR, ex);
        } catch (IOException ex) {
            LogHandler.log(this, LogLevel.ERROR, ex);
        } catch (SQLException ex) {
            LogHandler.log(this, LogLevel.ERROR, ex);
        }
    }
}