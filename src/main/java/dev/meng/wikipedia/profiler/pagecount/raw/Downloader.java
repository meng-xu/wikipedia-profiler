/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.meng.wikipedia.profiler.pagecount.raw;

import dev.meng.wikipedia.profiler.Configure;
import dev.meng.wikipedia.profiler.log.LogHandler;
import dev.meng.wikipedia.profiler.log.LogLevel;
import dev.meng.wikipedia.profiler.pagecount.PagecountUtils;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.util.GregorianCalendar;

/**
 *
 * @author meng
 */
public class Downloader {
    public Downloader(){
        LogHandler.register(this, Configure.LOG.PAGECOUNT_DOWNLOAD);
    }
            
    public void download(GregorianCalendar start, GregorianCalendar end){
        GregorianCalendar current = start;
        while(current.getTimeInMillis()<=end.getTimeInMillis()){
            download(current);
            current.add(GregorianCalendar.HOUR_OF_DAY, 1);
        }
    }
    
    private void download(GregorianCalendar timestamp){
        Path filepath = PagecountUtils.timestampToFilepath(timestamp);
        if(!filepath.toFile().exists() || filepath.toFile().isDirectory()){
            LogHandler.log(this, LogLevel.INFO, filepath.getFileName()+" downloading");
            downloadWorker(0, timestamp, filepath);
        } else{
            LogHandler.log(this, LogLevel.INFO, filepath.getFileName()+" already exists in repository");
        }
    }

    private void downloadWorker(int probing, GregorianCalendar timestamp, Path filepath){
        GregorianCalendar probingTimestamp = (GregorianCalendar) timestamp.clone();
        probingTimestamp.add(GregorianCalendar.SECOND, probing);
        try{
            URL url = PagecountUtils.timestampToURL(probingTimestamp);
            try {
                InputStream input = url.openStream();
                ReadableByteChannel channel = Channels.newChannel(input);
                FileOutputStream output = new FileOutputStream(filepath.toFile());
                output.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
                output.close();
                channel.close();
            } catch (IOException ex) {
                if(probing==-60){
                    LogHandler.log(this, LogLevel.INFO, filepath.getFileName()+" downloading failed");
                } else if(probing>=0){
                    probing = probing + 1;
                } else{
                    probing = probing - 1;
                }
                try {
                    Thread.sleep(Configure.PAGECOUNT.HTTP_WAIT_TIME);
                } catch (InterruptedException ex1) {
                    LogHandler.log(this, LogLevel.ERROR, ex1);
                }
                downloadWorker(probing, timestamp, filepath);
            }
        } catch (MalformedURLException ex) {
            LogHandler.log(this, LogLevel.ERROR, ex);
        }
    }
}
