/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.meng.wikipedia.profiler.pagecount.consolidated.db;

import dev.meng.wikipedia.profiler.db.SQLDB;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 *
 * @author xumeng
 */
public class PagecountConsolidatedDB extends SQLDB{

    public PageTable PAGE;
    public ViewTable VIEW;
    public SummaryTable SUMMARY;
    
    public PagecountConsolidatedDB(String filename) {
        super(filename);
        
        PAGE = new PageTable(this.connection);
        this.tables.add(PAGE);
        
        VIEW = new ViewTable(this.connection);
        this.tables.add(VIEW);     
        
        SUMMARY = new SummaryTable(this.connection);
        this.tables.add(SUMMARY);           
    }    
    
    public List<Map<String, Object>> getTopViewsByLangAndTimestamp(String lang, GregorianCalendar start, GregorianCalendar end, int limit) throws SQLException{
        String sql = "SELECT page.LANG, page.PAGE_ID, page.TITLE, sum(view.FREQUENCY) as FREQUENCY_SUM, sum(view.SIZE) as SIZE_SUM FROM view INNER JOIN page ON view.PAGE_ID=page.ID WHERE page.LANG=? AND view.TIMESTAMP>=? AND view.TIMESTAMP<=? GROUP BY view.PAGE_ID ORDER BY FREQUENCY_SUM DESC LIMIT ?";
        
        return select(sql, lang, start.getTimeInMillis(), end.getTimeInMillis(), limit);
    }        
    
}
