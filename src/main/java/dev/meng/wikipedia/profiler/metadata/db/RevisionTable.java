/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.meng.wikipedia.profiler.metadata.db;

import dev.meng.wikipedia.profiler.db.SQLTable;
import java.sql.Connection;

/**
 *
 * @author xumeng
 */
public class RevisionTable extends SQLTable{

    public RevisionTable(Connection database) {
        super(Revision.class, database);
    }
}
