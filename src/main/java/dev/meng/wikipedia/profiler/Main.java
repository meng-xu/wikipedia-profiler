/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.meng.wikipedia.profiler;

import dev.meng.wikipedia.profiler.analyze.Analyzer;
import dev.meng.wikipedia.profiler.log.LogHandler;
import dev.meng.wikipedia.profiler.pagecount.consolidated.Consolidator;
import dev.meng.wikipedia.profiler.pagecount.raw.Downloader;
import dev.meng.wikipedia.profiler.pagecount.raw.Parser;
import dev.meng.wikipedia.profiler.util.StringUtils;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author meng
 */
public class Main {
    
    public static void init(){
        Configure.DB.list();
        Scanner input = new Scanner(System.in);
        String line = input.nextLine();
        StringTokenizer tokenizer = new StringTokenizer(line);
        while (tokenizer.hasMoreTokens()) {
            String name = tokenizer.nextToken();
            String value = Configure.DB.getValue(name);
            if(value==null){
                System.out.println("invalid choice");
            } else if(value.equals(Configure.DB.PAGECOUNT_RAW)){
                try {
                    DB.PAGECOUNT_RAW.init();
                } catch (SQLException ex) {
                    LogHandler.console(Main.class, ex);
                }
            } else if (value.equals(Configure.DB.PAGECOUNT_CONSOLIDATED)) {
                try {
                    DB.PAGECOUNT_CONSOLIDATED.init();
                } catch (SQLException ex) {
                    LogHandler.console(Main.class, ex);
                }
            } else if (value.equals(Configure.DB.METADATA)){
                try {
                    DB.METADATA.init();
                } catch (SQLException ex) {
                    LogHandler.console(Main.class, ex);
                }
            }
        }

    }
    
    public static void download(){
        try {
            Scanner input = new Scanner(System.in);
            System.out.print("Start timestamp (format "+Configure.PAGECOUNT.TIMESTAMP_FORMAT+"): ");
            GregorianCalendar start = StringUtils.parseTimestamp(input.next(), Configure.PAGECOUNT.TIMESTAMP_FORMAT);
            System.out.print("End timestamp (format "+Configure.PAGECOUNT.TIMESTAMP_FORMAT+"): ");
            GregorianCalendar end = StringUtils.parseTimestamp(input.next(), Configure.PAGECOUNT.TIMESTAMP_FORMAT);
            Downloader downloader = new Downloader();
            downloader.download(start, end);
        } catch (ParseException ex) {
            LogHandler.console(Main.class, ex);
        }
    }
    
    public static void parse(){
        try {
            Scanner input = new Scanner(System.in);
            System.out.print("Start timestamp (format "+Configure.PAGECOUNT.TIMESTAMP_FORMAT+"): ");
            GregorianCalendar start = StringUtils.parseTimestamp(input.next(), Configure.PAGECOUNT.TIMESTAMP_FORMAT);
            System.out.print("End timestamp (format "+Configure.PAGECOUNT.TIMESTAMP_FORMAT+"): ");
            GregorianCalendar end = StringUtils.parseTimestamp(input.next(), Configure.PAGECOUNT.TIMESTAMP_FORMAT);
            Parser parser = new Parser();
            parser.parse(start, end);
        } catch (ParseException ex) {
            LogHandler.console(Main.class, ex);
        }
    }
    
    public static void consolidate(){
        Consolidator consolidator = new Consolidator();
        consolidator.consolidate();
    }
    
    public static void analyze(){
        try {
            Analyzer analyzer = new Analyzer();
            
            Scanner input = new Scanner(System.in);
            System.out.print("Lang: ");
            String lang = input.next();
            System.out.print("Start timestamp (format " + Configure.PAGECOUNT.TIMESTAMP_FORMAT + "): ");
            GregorianCalendar start = StringUtils.parseTimestamp(input.next(), Configure.PAGECOUNT.TIMESTAMP_FORMAT);
            System.out.print("End timestamp (format " + Configure.PAGECOUNT.TIMESTAMP_FORMAT + "): ");
            GregorianCalendar end = StringUtils.parseTimestamp(input.next(), Configure.PAGECOUNT.TIMESTAMP_FORMAT);
            System.out.print("Limit: ");
            int limit = Integer.parseInt(input.next());
            
            analyzer.analyze(lang, start, end, limit);
        } catch (ParseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void clean(){
        Scanner input = new Scanner(System.in);
        System.out.print("Module (data, db, log, result): ");
        String module = input.next();
        
        List<String> values;
        String line;
        StringTokenizer tokenizer;
        
        switch(module){
            case "db":
                Configure.DB.list();
                values = new LinkedList<>();
                input.nextLine();
                line = input.nextLine();
                tokenizer = new StringTokenizer(line);
                while(tokenizer.hasMoreTokens()){
                    String name = tokenizer.nextToken();
                    String value = Configure.DB.getValue(name);
                    if(value==null){
                        System.out.println(name+" is not valid, ignoring");
                    } else{
                        values.add(value);
                    }
                }
                Cleaner.cleanDB(values);
                break;
            case "log":
                Configure.LOG.list();
                values = new LinkedList<>();
                input.nextLine();
                line = input.nextLine();
                tokenizer = new StringTokenizer(line);
                while(tokenizer.hasMoreTokens()){
                    String name = tokenizer.nextToken();
                    String value = Configure.LOG.getValue(name);
                    if(value==null){
                        System.out.println(name+" is not valid, ignoring");
                    } else{
                        values.add(value);
                    }
                }
                Cleaner.cleanLog(values);
                break; 
            case "data":
                Configure.DATA.list();
                values = new LinkedList<>();
                input.nextLine();
                line = input.nextLine();
                tokenizer = new StringTokenizer(line);
                while(tokenizer.hasMoreTokens()){
                    String name = tokenizer.nextToken();
                    String value = Configure.DATA.getValue(name);
                    if(value==null){
                        System.out.println(name+" is not valid, ignoring");
                    } else{
                        values.add(value);
                    }
                }
                Cleaner.cleanData(values);
                break;     
            default:
                System.out.println("Module not recognized");
                break;
        }
    }
    
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        while(true){
            System.out.print("Command (init, download, parse, consolidate, analyze, clean, exit): ");
            String command = input.next();
            switch(command){
                case "init":
                    init();
                    break;
                case "download":
                    download();
                    break;
                case "parse":
                    parse();
                    break;
                case "consolidate":
                    consolidate();
                    break;
                case "analyze":
                    analyze();
                    break;
                case "clean":
                    clean();
                    break;
                case "exit":
                    System.exit(0);
                    break;
            }
        }
    }
}
