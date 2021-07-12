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
 * Autogenerated by Thrift Compiler (0.14.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.apache.zeppelin.interpreter.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.14.2)", date = "2021-07-12")
public class RemoteInterpreterResult implements org.apache.thrift.TBase<RemoteInterpreterResult, RemoteInterpreterResult._Fields>, java.io.Serializable, Cloneable, Comparable<RemoteInterpreterResult> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("RemoteInterpreterResult");

  private static final org.apache.thrift.protocol.TField CODE_FIELD_DESC = new org.apache.thrift.protocol.TField("code", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField MSG_FIELD_DESC = new org.apache.thrift.protocol.TField("msg", org.apache.thrift.protocol.TType.LIST, (short)2);
  private static final org.apache.thrift.protocol.TField CONFIG_FIELD_DESC = new org.apache.thrift.protocol.TField("config", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField GUI_FIELD_DESC = new org.apache.thrift.protocol.TField("gui", org.apache.thrift.protocol.TType.STRING, (short)4);
  private static final org.apache.thrift.protocol.TField NOTE_GUI_FIELD_DESC = new org.apache.thrift.protocol.TField("noteGui", org.apache.thrift.protocol.TType.STRING, (short)5);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new RemoteInterpreterResultStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new RemoteInterpreterResultTupleSchemeFactory();

  public @org.apache.thrift.annotation.Nullable java.lang.String code; // required
  public @org.apache.thrift.annotation.Nullable java.util.List<RemoteInterpreterResultMessage> msg; // required
  public @org.apache.thrift.annotation.Nullable java.lang.String config; // required
  public @org.apache.thrift.annotation.Nullable java.lang.String gui; // required
  public @org.apache.thrift.annotation.Nullable java.lang.String noteGui; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    CODE((short)1, "code"),
    MSG((short)2, "msg"),
    CONFIG((short)3, "config"),
    GUI((short)4, "gui"),
    NOTE_GUI((short)5, "noteGui");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // CODE
          return CODE;
        case 2: // MSG
          return MSG;
        case 3: // CONFIG
          return CONFIG;
        case 4: // GUI
          return GUI;
        case 5: // NOTE_GUI
          return NOTE_GUI;
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
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.CODE, new org.apache.thrift.meta_data.FieldMetaData("code", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.MSG, new org.apache.thrift.meta_data.FieldMetaData("msg", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, RemoteInterpreterResultMessage.class))));
    tmpMap.put(_Fields.CONFIG, new org.apache.thrift.meta_data.FieldMetaData("config", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.GUI, new org.apache.thrift.meta_data.FieldMetaData("gui", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.NOTE_GUI, new org.apache.thrift.meta_data.FieldMetaData("noteGui", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(RemoteInterpreterResult.class, metaDataMap);
  }

  public RemoteInterpreterResult() {
  }

  public RemoteInterpreterResult(
    java.lang.String code,
    java.util.List<RemoteInterpreterResultMessage> msg,
    java.lang.String config,
    java.lang.String gui,
    java.lang.String noteGui)
  {
    this();
    this.code = code;
    this.msg = msg;
    this.config = config;
    this.gui = gui;
    this.noteGui = noteGui;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public RemoteInterpreterResult(RemoteInterpreterResult other) {
    if (other.isSetCode()) {
      this.code = other.code;
    }
    if (other.isSetMsg()) {
      java.util.List<RemoteInterpreterResultMessage> __this__msg = new java.util.ArrayList<RemoteInterpreterResultMessage>(other.msg.size());
      for (RemoteInterpreterResultMessage other_element : other.msg) {
        __this__msg.add(new RemoteInterpreterResultMessage(other_element));
      }
      this.msg = __this__msg;
    }
    if (other.isSetConfig()) {
      this.config = other.config;
    }
    if (other.isSetGui()) {
      this.gui = other.gui;
    }
    if (other.isSetNoteGui()) {
      this.noteGui = other.noteGui;
    }
  }

  public RemoteInterpreterResult deepCopy() {
    return new RemoteInterpreterResult(this);
  }

  @Override
  public void clear() {
    this.code = null;
    this.msg = null;
    this.config = null;
    this.gui = null;
    this.noteGui = null;
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getCode() {
    return this.code;
  }

  public RemoteInterpreterResult setCode(@org.apache.thrift.annotation.Nullable java.lang.String code) {
    this.code = code;
    return this;
  }

  public void unsetCode() {
    this.code = null;
  }

  /** Returns true if field code is set (has been assigned a value) and false otherwise */
  public boolean isSetCode() {
    return this.code != null;
  }

  public void setCodeIsSet(boolean value) {
    if (!value) {
      this.code = null;
    }
  }

  public int getMsgSize() {
    return (this.msg == null) ? 0 : this.msg.size();
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.Iterator<RemoteInterpreterResultMessage> getMsgIterator() {
    return (this.msg == null) ? null : this.msg.iterator();
  }

  public void addToMsg(RemoteInterpreterResultMessage elem) {
    if (this.msg == null) {
      this.msg = new java.util.ArrayList<RemoteInterpreterResultMessage>();
    }
    this.msg.add(elem);
  }

  @org.apache.thrift.annotation.Nullable
  public java.util.List<RemoteInterpreterResultMessage> getMsg() {
    return this.msg;
  }

  public RemoteInterpreterResult setMsg(@org.apache.thrift.annotation.Nullable java.util.List<RemoteInterpreterResultMessage> msg) {
    this.msg = msg;
    return this;
  }

  public void unsetMsg() {
    this.msg = null;
  }

  /** Returns true if field msg is set (has been assigned a value) and false otherwise */
  public boolean isSetMsg() {
    return this.msg != null;
  }

  public void setMsgIsSet(boolean value) {
    if (!value) {
      this.msg = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getConfig() {
    return this.config;
  }

  public RemoteInterpreterResult setConfig(@org.apache.thrift.annotation.Nullable java.lang.String config) {
    this.config = config;
    return this;
  }

  public void unsetConfig() {
    this.config = null;
  }

  /** Returns true if field config is set (has been assigned a value) and false otherwise */
  public boolean isSetConfig() {
    return this.config != null;
  }

  public void setConfigIsSet(boolean value) {
    if (!value) {
      this.config = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getGui() {
    return this.gui;
  }

  public RemoteInterpreterResult setGui(@org.apache.thrift.annotation.Nullable java.lang.String gui) {
    this.gui = gui;
    return this;
  }

  public void unsetGui() {
    this.gui = null;
  }

  /** Returns true if field gui is set (has been assigned a value) and false otherwise */
  public boolean isSetGui() {
    return this.gui != null;
  }

  public void setGuiIsSet(boolean value) {
    if (!value) {
      this.gui = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getNoteGui() {
    return this.noteGui;
  }

  public RemoteInterpreterResult setNoteGui(@org.apache.thrift.annotation.Nullable java.lang.String noteGui) {
    this.noteGui = noteGui;
    return this;
  }

  public void unsetNoteGui() {
    this.noteGui = null;
  }

  /** Returns true if field noteGui is set (has been assigned a value) and false otherwise */
  public boolean isSetNoteGui() {
    return this.noteGui != null;
  }

  public void setNoteGuiIsSet(boolean value) {
    if (!value) {
      this.noteGui = null;
    }
  }

  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case CODE:
      if (value == null) {
        unsetCode();
      } else {
        setCode((java.lang.String)value);
      }
      break;

    case MSG:
      if (value == null) {
        unsetMsg();
      } else {
        setMsg((java.util.List<RemoteInterpreterResultMessage>)value);
      }
      break;

    case CONFIG:
      if (value == null) {
        unsetConfig();
      } else {
        setConfig((java.lang.String)value);
      }
      break;

    case GUI:
      if (value == null) {
        unsetGui();
      } else {
        setGui((java.lang.String)value);
      }
      break;

    case NOTE_GUI:
      if (value == null) {
        unsetNoteGui();
      } else {
        setNoteGui((java.lang.String)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case CODE:
      return getCode();

    case MSG:
      return getMsg();

    case CONFIG:
      return getConfig();

    case GUI:
      return getGui();

    case NOTE_GUI:
      return getNoteGui();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case CODE:
      return isSetCode();
    case MSG:
      return isSetMsg();
    case CONFIG:
      return isSetConfig();
    case GUI:
      return isSetGui();
    case NOTE_GUI:
      return isSetNoteGui();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof RemoteInterpreterResult)
      return this.equals((RemoteInterpreterResult)that);
    return false;
  }

  public boolean equals(RemoteInterpreterResult that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_code = true && this.isSetCode();
    boolean that_present_code = true && that.isSetCode();
    if (this_present_code || that_present_code) {
      if (!(this_present_code && that_present_code))
        return false;
      if (!this.code.equals(that.code))
        return false;
    }

    boolean this_present_msg = true && this.isSetMsg();
    boolean that_present_msg = true && that.isSetMsg();
    if (this_present_msg || that_present_msg) {
      if (!(this_present_msg && that_present_msg))
        return false;
      if (!this.msg.equals(that.msg))
        return false;
    }

    boolean this_present_config = true && this.isSetConfig();
    boolean that_present_config = true && that.isSetConfig();
    if (this_present_config || that_present_config) {
      if (!(this_present_config && that_present_config))
        return false;
      if (!this.config.equals(that.config))
        return false;
    }

    boolean this_present_gui = true && this.isSetGui();
    boolean that_present_gui = true && that.isSetGui();
    if (this_present_gui || that_present_gui) {
      if (!(this_present_gui && that_present_gui))
        return false;
      if (!this.gui.equals(that.gui))
        return false;
    }

    boolean this_present_noteGui = true && this.isSetNoteGui();
    boolean that_present_noteGui = true && that.isSetNoteGui();
    if (this_present_noteGui || that_present_noteGui) {
      if (!(this_present_noteGui && that_present_noteGui))
        return false;
      if (!this.noteGui.equals(that.noteGui))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetCode()) ? 131071 : 524287);
    if (isSetCode())
      hashCode = hashCode * 8191 + code.hashCode();

    hashCode = hashCode * 8191 + ((isSetMsg()) ? 131071 : 524287);
    if (isSetMsg())
      hashCode = hashCode * 8191 + msg.hashCode();

    hashCode = hashCode * 8191 + ((isSetConfig()) ? 131071 : 524287);
    if (isSetConfig())
      hashCode = hashCode * 8191 + config.hashCode();

    hashCode = hashCode * 8191 + ((isSetGui()) ? 131071 : 524287);
    if (isSetGui())
      hashCode = hashCode * 8191 + gui.hashCode();

    hashCode = hashCode * 8191 + ((isSetNoteGui()) ? 131071 : 524287);
    if (isSetNoteGui())
      hashCode = hashCode * 8191 + noteGui.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(RemoteInterpreterResult other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetCode(), other.isSetCode());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCode()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.code, other.code);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetMsg(), other.isSetMsg());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMsg()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.msg, other.msg);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetConfig(), other.isSetConfig());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetConfig()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.config, other.config);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetGui(), other.isSetGui());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetGui()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.gui, other.gui);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetNoteGui(), other.isSetNoteGui());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetNoteGui()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.noteGui, other.noteGui);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  @org.apache.thrift.annotation.Nullable
  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("RemoteInterpreterResult(");
    boolean first = true;

    sb.append("code:");
    if (this.code == null) {
      sb.append("null");
    } else {
      sb.append(this.code);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("msg:");
    if (this.msg == null) {
      sb.append("null");
    } else {
      sb.append(this.msg);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("config:");
    if (this.config == null) {
      sb.append("null");
    } else {
      sb.append(this.config);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("gui:");
    if (this.gui == null) {
      sb.append("null");
    } else {
      sb.append(this.gui);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("noteGui:");
    if (this.noteGui == null) {
      sb.append("null");
    } else {
      sb.append(this.noteGui);
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

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class RemoteInterpreterResultStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public RemoteInterpreterResultStandardScheme getScheme() {
      return new RemoteInterpreterResultStandardScheme();
    }
  }

  private static class RemoteInterpreterResultStandardScheme extends org.apache.thrift.scheme.StandardScheme<RemoteInterpreterResult> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, RemoteInterpreterResult struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // CODE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.code = iprot.readString();
              struct.setCodeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // MSG
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list10 = iprot.readListBegin();
                struct.msg = new java.util.ArrayList<RemoteInterpreterResultMessage>(_list10.size);
                @org.apache.thrift.annotation.Nullable RemoteInterpreterResultMessage _elem11;
                for (int _i12 = 0; _i12 < _list10.size; ++_i12)
                {
                  _elem11 = new RemoteInterpreterResultMessage();
                  _elem11.read(iprot);
                  struct.msg.add(_elem11);
                }
                iprot.readListEnd();
              }
              struct.setMsgIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // CONFIG
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.config = iprot.readString();
              struct.setConfigIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // GUI
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.gui = iprot.readString();
              struct.setGuiIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // NOTE_GUI
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.noteGui = iprot.readString();
              struct.setNoteGuiIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, RemoteInterpreterResult struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.code != null) {
        oprot.writeFieldBegin(CODE_FIELD_DESC);
        oprot.writeString(struct.code);
        oprot.writeFieldEnd();
      }
      if (struct.msg != null) {
        oprot.writeFieldBegin(MSG_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.msg.size()));
          for (RemoteInterpreterResultMessage _iter13 : struct.msg)
          {
            _iter13.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      if (struct.config != null) {
        oprot.writeFieldBegin(CONFIG_FIELD_DESC);
        oprot.writeString(struct.config);
        oprot.writeFieldEnd();
      }
      if (struct.gui != null) {
        oprot.writeFieldBegin(GUI_FIELD_DESC);
        oprot.writeString(struct.gui);
        oprot.writeFieldEnd();
      }
      if (struct.noteGui != null) {
        oprot.writeFieldBegin(NOTE_GUI_FIELD_DESC);
        oprot.writeString(struct.noteGui);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class RemoteInterpreterResultTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public RemoteInterpreterResultTupleScheme getScheme() {
      return new RemoteInterpreterResultTupleScheme();
    }
  }

  private static class RemoteInterpreterResultTupleScheme extends org.apache.thrift.scheme.TupleScheme<RemoteInterpreterResult> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, RemoteInterpreterResult struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetCode()) {
        optionals.set(0);
      }
      if (struct.isSetMsg()) {
        optionals.set(1);
      }
      if (struct.isSetConfig()) {
        optionals.set(2);
      }
      if (struct.isSetGui()) {
        optionals.set(3);
      }
      if (struct.isSetNoteGui()) {
        optionals.set(4);
      }
      oprot.writeBitSet(optionals, 5);
      if (struct.isSetCode()) {
        oprot.writeString(struct.code);
      }
      if (struct.isSetMsg()) {
        {
          oprot.writeI32(struct.msg.size());
          for (RemoteInterpreterResultMessage _iter14 : struct.msg)
          {
            _iter14.write(oprot);
          }
        }
      }
      if (struct.isSetConfig()) {
        oprot.writeString(struct.config);
      }
      if (struct.isSetGui()) {
        oprot.writeString(struct.gui);
      }
      if (struct.isSetNoteGui()) {
        oprot.writeString(struct.noteGui);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, RemoteInterpreterResult struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(5);
      if (incoming.get(0)) {
        struct.code = iprot.readString();
        struct.setCodeIsSet(true);
      }
      if (incoming.get(1)) {
        {
          org.apache.thrift.protocol.TList _list15 = iprot.readListBegin(org.apache.thrift.protocol.TType.STRUCT);
          struct.msg = new java.util.ArrayList<RemoteInterpreterResultMessage>(_list15.size);
          @org.apache.thrift.annotation.Nullable RemoteInterpreterResultMessage _elem16;
          for (int _i17 = 0; _i17 < _list15.size; ++_i17)
          {
            _elem16 = new RemoteInterpreterResultMessage();
            _elem16.read(iprot);
            struct.msg.add(_elem16);
          }
        }
        struct.setMsgIsSet(true);
      }
      if (incoming.get(2)) {
        struct.config = iprot.readString();
        struct.setConfigIsSet(true);
      }
      if (incoming.get(3)) {
        struct.gui = iprot.readString();
        struct.setGuiIsSet(true);
      }
      if (incoming.get(4)) {
        struct.noteGui = iprot.readString();
        struct.setNoteGuiIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

