/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.meng.wikipedia.profiler.pagecount.raw.db;

import dev.meng.wikipedia.profiler.db.SQLDB;
import java.sql.SQLException;

/**
 *
 * @author xumeng
 */
public class PagecountRawDB extends SQLDB{

    public PageTable PAGE;
    public ViewTable VIEW;
    public SummaryTable SUMMARY;
    
    public PagecountRawDB(String filename) {
        super(filename);
        
        PAGE = new PageTable(this.connection);
        this.tables.add(PAGE);
        
        VIEW = new ViewTable(this.connection);
        this.tables.add(VIEW);     
        
        SUMMARY = new SummaryTable(this.connection);
        this.tables.add(SUMMARY);           
    }    
}
