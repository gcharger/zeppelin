/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.zeppelin.interpreter;

import org.apache.commons.lang.StringUtils;
import org.apache.zeppelin.tabledata.ColumnDef;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Type of interpreter result message
 */
public class MessageColumnTypes implements Serializable {
  List<ColumnDef.TYPE> columnTypes = new ArrayList<>();

  public MessageColumnTypes() {

  }

  public MessageColumnTypes(Collection<ColumnDef.TYPE> types) {
    columnTypes.addAll(types);
  }

  public List<ColumnDef.TYPE> getListOfColumnTypes() {
    return columnTypes;
  }

  public String toString() {
    if (columnTypes.isEmpty()) {
      return "Type is unknown";
    } else {
      return StringUtils.join(columnTypes, ", ");
    }
  }
}
