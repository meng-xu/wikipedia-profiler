/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.meng.wikipedia.profiler.pagecount.consolidated.db;

import dev.meng.wikipedia.profiler.db.SQLColumnAttribute;
import dev.meng.wikipedia.profiler.db.SQLDataType;
import dev.meng.wikipedia.profiler.db.SQLTableDefinition;

/**
 *
 * @author xumeng
 */
public enum View implements SQLTableDefinition{
    
    PAGE_ID(SQLDataType.INTEGER, new SQLColumnAttribute[]{ SQLColumnAttribute.PRIMARY_KEY}),
    TIMESTAMP(SQLDataType.LONG, new SQLColumnAttribute[]{SQLColumnAttribute.PRIMARY_KEY}),
    FREQUENCY(SQLDataType.LONG, new SQLColumnAttribute[]{}),
    SIZE(SQLDataType.LONG, new SQLColumnAttribute[]{});

    private SQLDataType type;
    private SQLColumnAttribute[] attributes;
    
    private View(SQLDataType type, SQLColumnAttribute[] attributes){
        this.type = type;
        this.attributes = attributes;
    }

    @Override
    public SQLDataType getType() {
        return type;
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public SQLColumnAttribute[] getAttributes() {
        return this.attributes;
    }
}
