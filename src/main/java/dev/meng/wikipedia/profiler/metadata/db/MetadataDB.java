/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.meng.wikipedia.profiler.metadata.db;

import dev.meng.wikipedia.profiler.db.SQLDB;

/**
 *
 * @author xumeng
 */
public class MetadataDB extends SQLDB{

    public PageTable PAGE;
    public FileTable FILE;
    public RevisionTable REVISION;
    public PageFileTable PAGE_FILE;
    
    public MetadataDB(String filename) {
        super(filename);
        
        PAGE = new PageTable(this.connection);
        this.tables.add(PAGE);
        
        FILE = new FileTable(this.connection);
        this.tables.add(FILE);     
        
        REVISION = new RevisionTable(this.connection);
        this.tables.add(REVISION);   
        
        PAGE_FILE = new PageFileTable(this.connection);
        this.tables.add(PAGE_FILE);           
    }    
}
