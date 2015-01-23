/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.meng.wikipedia.profiler.analyze;

import dev.meng.wikipedia.profiler.DB;
import dev.meng.wikipedia.profiler.metadata.Metadata;
import dev.meng.wikipedia.profiler.metadata.PageInfo;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author meng
 */
public class Analyzer {
    public void analyze(String lang, GregorianCalendar start, GregorianCalendar end, int limit){
        try {
            List<Map<String, Object>> records = DB.PAGECOUNT_CONSOLIDATED.getTopViewsByLangAndTimestamp(lang, start, end, limit);
            
            Set<PageInfo> pages = new HashSet<>();
            for(Map<String, Object> record : records){
                PageInfo page = new PageInfo();
                page.setLang((String) record.get("LANG"));
                page.setPageId((String) record.get("PAGE_ID"));
                pages.add(page);
            }
            
            Metadata metadata = new Metadata();
            metadata.queryMetadata(pages, start, end);
            
        } catch (SQLException ex) {
            Logger.getLogger(Analyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
