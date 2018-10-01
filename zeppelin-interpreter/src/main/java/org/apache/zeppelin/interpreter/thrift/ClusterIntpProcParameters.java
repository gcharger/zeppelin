/**
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
/**
 * Autogenerated by Thrift Compiler (0.9.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.apache.zeppelin.interpreter.thrift;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.2)", date = "2018-9-28")
public class ClusterIntpProcParameters implements org.apache.thrift.TBase<ClusterIntpProcParameters, ClusterIntpProcParameters._Fields>, java.io.Serializable, Cloneable, Comparable<ClusterIntpProcParameters> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("ClusterIntpProcParameters");

  private static final org.apache.thrift.protocol.TField HOST_FIELD_DESC = new org.apache.thrift.protocol.TField("host", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField PORT_FIELD_DESC = new org.apache.thrift.protocol.TField("port", org.apache.thrift.protocol.TType.I32, (short)2);
  private static final org.apache.thrift.protocol.TField USER_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("userName", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField NOTE_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("noteId", org.apache.thrift.protocol.TType.STRING, (short)4);
  private static final org.apache.thrift.protocol.TField REPL_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("replName", org.apache.thrift.protocol.TType.STRING, (short)5);
  private static final org.apache.thrift.protocol.TField DEFAULT_INTERPRETER_SETTING_FIELD_DESC = new org.apache.thrift.protocol.TField("defaultInterpreterSetting", org.apache.thrift.protocol.TType.STRING, (short)6);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ClusterIntpProcParametersStandardSchemeFactory());
    schemes.put(TupleScheme.class, new ClusterIntpProcParametersTupleSchemeFactory());
  }

  public String host; // required
  public int port; // required
  public String userName; // required
  public String noteId; // required
  public String replName; // required
  public String defaultInterpreterSetting; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    HOST((short)1, "host"),
    PORT((short)2, "port"),
    USER_NAME((short)3, "userName"),
    NOTE_ID((short)4, "noteId"),
    REPL_NAME((short)5, "replName"),
    DEFAULT_INTERPRETER_SETTING((short)6, "defaultInterpreterSetting");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // HOST
          return HOST;
        case 2: // PORT
          return PORT;
        case 3: // USER_NAME
          return USER_NAME;
        case 4: // NOTE_ID
          return NOTE_ID;
        case 5: // REPL_NAME
          return REPL_NAME;
        case 6: // DEFAULT_INTERPRETER_SETTING
          return DEFAULT_INTERPRETER_SETTING;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __PORT_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.HOST, new org.apache.thrift.meta_data.FieldMetaData("host", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PORT, new org.apache.thrift.meta_data.FieldMetaData("port", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.USER_NAME, new org.apache.thrift.meta_data.FieldMetaData("userName", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.NOTE_ID, new org.apache.thrift.meta_data.FieldMetaData("noteId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.REPL_NAME, new org.apache.thrift.meta_data.FieldMetaData("replName", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.DEFAULT_INTERPRETER_SETTING, new org.apache.thrift.meta_data.FieldMetaData("defaultInterpreterSetting", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(ClusterIntpProcParameters.class, metaDataMap);
  }

  public ClusterIntpProcParameters() {
  }

  public ClusterIntpProcParameters(
    String host,
    int port,
    String userName,
    String noteId,
    String replName,
    String defaultInterpreterSetting)
  {
    this();
    this.host = host;
    this.port = port;
    setPortIsSet(true);
    this.userName = userName;
    this.noteId = noteId;
    this.replName = replName;
    this.defaultInterpreterSetting = defaultInterpreterSetting;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ClusterIntpProcParameters(ClusterIntpProcParameters other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetHost()) {
      this.host = other.host;
    }
    this.port = other.port;
    if (other.isSetUserName()) {
      this.userName = other.userName;
    }
    if (other.isSetNoteId()) {
      this.noteId = other.noteId;
    }
    if (other.isSetReplName()) {
      this.replName = other.replName;
    }
    if (other.isSetDefaultInterpreterSetting()) {
      this.defaultInterpreterSetting = other.defaultInterpreterSetting;
    }
  }

  public ClusterIntpProcParameters deepCopy() {
    return new ClusterIntpProcParameters(this);
  }

  @Override
  public void clear() {
    this.host = null;
    setPortIsSet(false);
    this.port = 0;
    this.userName = null;
    this.noteId = null;
    this.replName = null;
    this.defaultInterpreterSetting = null;
  }

  public String getHost() {
    return this.host;
  }

  public ClusterIntpProcParameters setHost(String host) {
    this.host = host;
    return this;
  }

  public void unsetHost() {
    this.host = null;
  }

  /** Returns true if field host is set (has been assigned a value) and false otherwise */
  public boolean isSetHost() {
    return this.host != null;
  }

  public void setHostIsSet(boolean value) {
    if (!value) {
      this.host = null;
    }
  }

  public int getPort() {
    return this.port;
  }

  public ClusterIntpProcParameters setPort(int port) {
    this.port = port;
    setPortIsSet(true);
    return this;
  }

  public void unsetPort() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __PORT_ISSET_ID);
  }

  /** Returns true if field port is set (has been assigned a value) and false otherwise */
  public boolean isSetPort() {
    return EncodingUtils.testBit(__isset_bitfield, __PORT_ISSET_ID);
  }

  public void setPortIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __PORT_ISSET_ID, value);
  }

  public String getUserName() {
    return this.userName;
  }

  public ClusterIntpProcParameters setUserName(String userName) {
    this.userName = userName;
    return this;
  }

  public void unsetUserName() {
    this.userName = null;
  }

  /** Returns true if field userName is set (has been assigned a value) and false otherwise */
  public boolean isSetUserName() {
    return this.userName != null;
  }

  public void setUserNameIsSet(boolean value) {
    if (!value) {
      this.userName = null;
    }
  }

  public String getNoteId() {
    return this.noteId;
  }

  public ClusterIntpProcParameters setNoteId(String noteId) {
    this.noteId = noteId;
    return this;
  }

  public void unsetNoteId() {
    this.noteId = null;
  }

  /** Returns true if field noteId is set (has been assigned a value) and false otherwise */
  public boolean isSetNoteId() {
    return this.noteId != null;
  }

  public void setNoteIdIsSet(boolean value) {
    if (!value) {
      this.noteId = null;
    }
  }

  public String getReplName() {
    return this.replName;
  }

  public ClusterIntpProcParameters setReplName(String replName) {
    this.replName = replName;
    return this;
  }

  public void unsetReplName() {
    this.replName = null;
  }

  /** Returns true if field replName is set (has been assigned a value) and false otherwise */
  public boolean isSetReplName() {
    return this.replName != null;
  }

  public void setReplNameIsSet(boolean value) {
    if (!value) {
      this.replName = null;
    }
  }

  public String getDefaultInterpreterSetting() {
    return this.defaultInterpreterSetting;
  }

  public ClusterIntpProcParameters setDefaultInterpreterSetting(String defaultInterpreterSetting) {
    this.defaultInterpreterSetting = defaultInterpreterSetting;
    return this;
  }

  public void unsetDefaultInterpreterSetting() {
    this.defaultInterpreterSetting = null;
  }

  /** Returns true if field defaultInterpreterSetting is set (has been assigned a value) and false otherwise */
  public boolean isSetDefaultInterpreterSetting() {
    return this.defaultInterpreterSetting != null;
  }

  public void setDefaultInterpreterSettingIsSet(boolean value) {
    if (!value) {
      this.defaultInterpreterSetting = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case HOST:
      if (value == null) {
        unsetHost();
      } else {
        setHost((String)value);
      }
      break;

    case PORT:
      if (value == null) {
        unsetPort();
      } else {
        setPort((Integer)value);
      }
      break;

    case USER_NAME:
      if (value == null) {
        unsetUserName();
      } else {
        setUserName((String)value);
      }
      break;

    case NOTE_ID:
      if (value == null) {
        unsetNoteId();
      } else {
        setNoteId((String)value);
      }
      break;

    case REPL_NAME:
      if (value == null) {
        unsetReplName();
      } else {
        setReplName((String)value);
      }
      break;

    case DEFAULT_INTERPRETER_SETTING:
      if (value == null) {
        unsetDefaultInterpreterSetting();
      } else {
        setDefaultInterpreterSetting((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case HOST:
      return getHost();

    case PORT:
      return Integer.valueOf(getPort());

    case USER_NAME:
      return getUserName();

    case NOTE_ID:
      return getNoteId();

    case REPL_NAME:
      return getReplName();

    case DEFAULT_INTERPRETER_SETTING:
      return getDefaultInterpreterSetting();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case HOST:
      return isSetHost();
    case PORT:
      return isSetPort();
    case USER_NAME:
      return isSetUserName();
    case NOTE_ID:
      return isSetNoteId();
    case REPL_NAME:
      return isSetReplName();
    case DEFAULT_INTERPRETER_SETTING:
      return isSetDefaultInterpreterSetting();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof ClusterIntpProcParameters)
      return this.equals((ClusterIntpProcParameters)that);
    return false;
  }

  public boolean equals(ClusterIntpProcParameters that) {
    if (that == null)
      return false;

    boolean this_present_host = true && this.isSetHost();
    boolean that_present_host = true && that.isSetHost();
    if (this_present_host || that_present_host) {
      if (!(this_present_host && that_present_host))
        return false;
      if (!this.host.equals(that.host))
        return false;
    }

    boolean this_present_port = true;
    boolean that_present_port = true;
    if (this_present_port || that_present_port) {
      if (!(this_present_port && that_present_port))
        return false;
      if (this.port != that.port)
        return false;
    }

    boolean this_present_userName = true && this.isSetUserName();
    boolean that_present_userName = true && that.isSetUserName();
    if (this_present_userName || that_present_userName) {
      if (!(this_present_userName && that_present_userName))
        return false;
      if (!this.userName.equals(that.userName))
        return false;
    }

    boolean this_present_noteId = true && this.isSetNoteId();
    boolean that_present_noteId = true && that.isSetNoteId();
    if (this_present_noteId || that_present_noteId) {
      if (!(this_present_noteId && that_present_noteId))
        return false;
      if (!this.noteId.equals(that.noteId))
        return false;
    }

    boolean this_present_replName = true && this.isSetReplName();
    boolean that_present_replName = true && that.isSetReplName();
    if (this_present_replName || that_present_replName) {
      if (!(this_present_replName && that_present_replName))
        return false;
      if (!this.replName.equals(that.replName))
        return false;
    }

    boolean this_present_defaultInterpreterSetting = true && this.isSetDefaultInterpreterSetting();
    boolean that_present_defaultInterpreterSetting = true && that.isSetDefaultInterpreterSetting();
    if (this_present_defaultInterpreterSetting || that_present_defaultInterpreterSetting) {
      if (!(this_present_defaultInterpreterSetting && that_present_defaultInterpreterSetting))
        return false;
      if (!this.defaultInterpreterSetting.equals(that.defaultInterpreterSetting))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_host = true && (isSetHost());
    list.add(present_host);
    if (present_host)
      list.add(host);

    boolean present_port = true;
    list.add(present_port);
    if (present_port)
      list.add(port);

    boolean present_userName = true && (isSetUserName());
    list.add(present_userName);
    if (present_userName)
      list.add(userName);

    boolean present_noteId = true && (isSetNoteId());
    list.add(present_noteId);
    if (present_noteId)
      list.add(noteId);

    boolean present_replName = true && (isSetReplName());
    list.add(present_replName);
    if (present_replName)
      list.add(replName);

    boolean present_defaultInterpreterSetting = true && (isSetDefaultInterpreterSetting());
    list.add(present_defaultInterpreterSetting);
    if (present_defaultInterpreterSetting)
      list.add(defaultInterpreterSetting);

    return list.hashCode();
  }

  @Override
  public int compareTo(ClusterIntpProcParameters other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetHost()).compareTo(other.isSetHost());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetHost()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.host, other.host);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetPort()).compareTo(other.isSetPort());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPort()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.port, other.port);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetUserName()).compareTo(other.isSetUserName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetUserName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.userName, other.userName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetNoteId()).compareTo(other.isSetNoteId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetNoteId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.noteId, other.noteId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetReplName()).compareTo(other.isSetReplName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetReplName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.replName, other.replName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetDefaultInterpreterSetting()).compareTo(other.isSetDefaultInterpreterSetting());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetDefaultInterpreterSetting()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.defaultInterpreterSetting, other.defaultInterpreterSetting);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("ClusterIntpProcParameters(");
    boolean first = true;

    sb.append("host:");
    if (this.host == null) {
      sb.append("null");
    } else {
      sb.append(this.host);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("port:");
    sb.append(this.port);
    first = false;
    if (!first) sb.append(", ");
    sb.append("userName:");
    if (this.userName == null) {
      sb.append("null");
    } else {
      sb.append(this.userName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("noteId:");
    if (this.noteId == null) {
      sb.append("null");
    } else {
      sb.append(this.noteId);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("replName:");
    if (this.replName == null) {
      sb.append("null");
    } else {
      sb.append(this.replName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("defaultInterpreterSetting:");
    if (this.defaultInterpreterSetting == null) {
      sb.append("null");
    } else {
      sb.append(this.defaultInterpreterSetting);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class ClusterIntpProcParametersStandardSchemeFactory implements SchemeFactory {
    public ClusterIntpProcParametersStandardScheme getScheme() {
      return new ClusterIntpProcParametersStandardScheme();
    }
  }

  private static class ClusterIntpProcParametersStandardScheme extends StandardScheme<ClusterIntpProcParameters> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, ClusterIntpProcParameters struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // HOST
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.host = iprot.readString();
              struct.setHostIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // PORT
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.port = iprot.readI32();
              struct.setPortIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // USER_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.userName = iprot.readString();
              struct.setUserNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // NOTE_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.noteId = iprot.readString();
              struct.setNoteIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // REPL_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.replName = iprot.readString();
              struct.setReplNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // DEFAULT_INTERPRETER_SETTING
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.defaultInterpreterSetting = iprot.readString();
              struct.setDefaultInterpreterSettingIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, ClusterIntpProcParameters struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.host != null) {
        oprot.writeFieldBegin(HOST_FIELD_DESC);
        oprot.writeString(struct.host);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(PORT_FIELD_DESC);
      oprot.writeI32(struct.port);
      oprot.writeFieldEnd();
      if (struct.userName != null) {
        oprot.writeFieldBegin(USER_NAME_FIELD_DESC);
        oprot.writeString(struct.userName);
        oprot.writeFieldEnd();
      }
      if (struct.noteId != null) {
        oprot.writeFieldBegin(NOTE_ID_FIELD_DESC);
        oprot.writeString(struct.noteId);
        oprot.writeFieldEnd();
      }
      if (struct.replName != null) {
        oprot.writeFieldBegin(REPL_NAME_FIELD_DESC);
        oprot.writeString(struct.replName);
        oprot.writeFieldEnd();
      }
      if (struct.defaultInterpreterSetting != null) {
        oprot.writeFieldBegin(DEFAULT_INTERPRETER_SETTING_FIELD_DESC);
        oprot.writeString(struct.defaultInterpreterSetting);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ClusterIntpProcParametersTupleSchemeFactory implements SchemeFactory {
    public ClusterIntpProcParametersTupleScheme getScheme() {
      return new ClusterIntpProcParametersTupleScheme();
    }
  }

  private static class ClusterIntpProcParametersTupleScheme extends TupleScheme<ClusterIntpProcParameters> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, ClusterIntpProcParameters struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetHost()) {
        optionals.set(0);
      }
      if (struct.isSetPort()) {
        optionals.set(1);
      }
      if (struct.isSetUserName()) {
        optionals.set(2);
      }
      if (struct.isSetNoteId()) {
        optionals.set(3);
      }
      if (struct.isSetReplName()) {
        optionals.set(4);
      }
      if (struct.isSetDefaultInterpreterSetting()) {
        optionals.set(5);
      }
      oprot.writeBitSet(optionals, 6);
      if (struct.isSetHost()) {
        oprot.writeString(struct.host);
      }
      if (struct.isSetPort()) {
        oprot.writeI32(struct.port);
      }
      if (struct.isSetUserName()) {
        oprot.writeString(struct.userName);
      }
      if (struct.isSetNoteId()) {
        oprot.writeString(struct.noteId);
      }
      if (struct.isSetReplName()) {
        oprot.writeString(struct.replName);
      }
      if (struct.isSetDefaultInterpreterSetting()) {
        oprot.writeString(struct.defaultInterpreterSetting);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, ClusterIntpProcParameters struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(6);
      if (incoming.get(0)) {
        struct.host = iprot.readString();
        struct.setHostIsSet(true);
      }
      if (incoming.get(1)) {
        struct.port = iprot.readI32();
        struct.setPortIsSet(true);
      }
      if (incoming.get(2)) {
        struct.userName = iprot.readString();
        struct.setUserNameIsSet(true);
      }
      if (incoming.get(3)) {
        struct.noteId = iprot.readString();
        struct.setNoteIdIsSet(true);
      }
      if (incoming.get(4)) {
        struct.replName = iprot.readString();
        struct.setReplNameIsSet(true);
      }
      if (incoming.get(5)) {
        struct.defaultInterpreterSetting = iprot.readString();
        struct.setDefaultInterpreterSettingIsSet(true);
      }
    }
  }

}

