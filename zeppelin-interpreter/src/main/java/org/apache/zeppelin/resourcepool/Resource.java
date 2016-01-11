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
package org.apache.zeppelin.resourcepool;

import java.io.Serializable;

/**
 * Information and reference to the resource
 */
public class Resource {
  private final Object r;
  private final boolean serializable;
  private final ResourceId resourceId;
  private final String className;


  /**
   * Create local resource
   * @param resourceId
   * @param r must not be null
   */
  Resource(ResourceId resourceId, Object r) {
    this.r = r;
    this.resourceId = resourceId;
    this.serializable = r instanceof Serializable;
    this.className = r.getClass().getName();
  }

  /**
   * Create remote object
   * @param resourceId
   */
  Resource(ResourceId resourceId, boolean serializable, String className) {
    this.r = null;
    this.resourceId = resourceId;
    this.serializable = serializable;
    this.className = className;
  }

  public ResourceId getResourceId() {
    return resourceId;
  }

  public String getClassName() {
    return className;
  }

  /**
   *
   * @return null when this is remote resource and not serializable.
   */
  public Object get() {
    if (isLocal()) {
      return r;
    } else if (isSerializable()){
      return r;
    } else {
      return null;
    }
  }

  public boolean isSerializable() {
    return serializable;
  }

  /**
   * if it is remote object
   * @return
   */
  public boolean isRemote() {
    return !isLocal();
  }

  /**
   * Whether it is locally accessible or not
   * @return
   */
  public boolean isLocal() {
    return r != null;
  }
}
