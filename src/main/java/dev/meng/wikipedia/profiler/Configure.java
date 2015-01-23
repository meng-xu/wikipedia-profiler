/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.meng.wikipedia.profiler;

import dev.meng.wikipedia.profiler.config.DBConfig;
import dev.meng.wikipedia.profiler.config.DaemonConfig;
import dev.meng.wikipedia.profiler.config.DataConfig;
import dev.meng.wikipedia.profiler.config.LogConfig;
import dev.meng.wikipedia.profiler.config.MetadataConfig;
import dev.meng.wikipedia.profiler.config.PagecountConfig;

/**
 *
 * @author xumeng
 */
public class Configure {
    public static final DBConfig DB = new DBConfig("config/db.properties");
    public static final DataConfig DATA = new DataConfig("config/data.properties");
    public static final LogConfig LOG = new LogConfig("config/log.properties");
    public static final DaemonConfig DAEMON = new DaemonConfig("config/daemon.properties");
    public static final PagecountConfig PAGECOUNT = new PagecountConfig("config/pagecount.properties");
    public static final MetadataConfig METADATA = new MetadataConfig("config/metadata.properties");
}