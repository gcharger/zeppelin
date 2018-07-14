/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
package org.apache.zeppelin.cluster;

import io.atomix.copycat.Query;
import org.apache.zeppelin.cluster.meta.ClusterMetaType;

/**
 * Command to query a variable in cluster state machine
 */
public class GetCommand implements Query<Object> {
  private final ClusterMetaType type;
  private final String key;

  public GetCommand(ClusterMetaType type, String key){
    this.type = type;
    this.key = key;
  }

  public ClusterMetaType type(){
    return type;
  }

  public String key(){
    return key;
  }
}
