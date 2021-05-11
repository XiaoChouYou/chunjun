/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dtstack.flinkx.connector.gbase.sink;

import com.dtstack.flinkx.connector.gbase.converter.GbaseRawTypeConverter;
import com.dtstack.flinkx.connector.jdbc.sink.JdbcOutputFormat;
import com.dtstack.flinkx.util.TableUtil;

import org.apache.flink.table.types.logical.LogicalType;
import org.apache.flink.table.types.logical.RowType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * @author tiezhu
 * @since 2021/5/10 3:02 下午
 */
public class GbaseOutputFormat extends JdbcOutputFormat {

    protected static final Logger LOG = LoggerFactory.getLogger(GbaseOutputFormat.class);

    @Override
    protected void openInternal(int taskNumber, int numTasks) {
        super.openInternal(taskNumber, numTasks);
        try {
            LogicalType rowType =
                    TableUtil.createRowType(
                            fullColumn, fullColumnType, GbaseRawTypeConverter::apply);
            setRowConverter(jdbcDialect.getColumnConverter((RowType) rowType));
        } catch (SQLException e) {
            LOG.error("", e);
        }
    }

    @Override
    protected String getTableName() {
        String table = jdbcConf.getTable();
        String[] split = table.split("\\.");
        if (split.length > 1) {
            table = split[1];
        }
        return table;
    }
}
