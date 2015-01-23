/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.meng.wikipedia.profiler;

import dev.meng.wikipedia.profiler.metadata.db.MetadataDB;
import dev.meng.wikipedia.profiler.pagecount.consolidated.db.PagecountConsolidatedDB;
import dev.meng.wikipedia.profiler.pagecount.raw.db.PagecountRawDB;

/**
 *
 * @author xumeng
 */
public class DB {
    public static final PagecountRawDB PAGECOUNT_RAW = new PagecountRawDB(Configure.DB.PAGECOUNT_RAW);
    public static final PagecountConsolidatedDB PAGECOUNT_CONSOLIDATED = new PagecountConsolidatedDB(Configure.DB.PAGECOUNT_CONSOLIDATED);
    public static final MetadataDB METADATA = new MetadataDB(Configure.DB.METADATA);
}
