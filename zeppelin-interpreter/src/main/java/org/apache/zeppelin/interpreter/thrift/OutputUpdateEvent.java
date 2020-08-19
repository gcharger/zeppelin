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
 * Autogenerated by Thrift Compiler (0.13.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.apache.zeppelin.interpreter.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.13.0)", date = "2020-07-28")
public class OutputUpdateEvent implements org.apache.thrift.TBase<OutputUpdateEvent, OutputUpdateEvent._Fields>, java.io.Serializable, Cloneable, Comparable<OutputUpdateEvent> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("OutputUpdateEvent");

  private static final org.apache.thrift.protocol.TField NOTE_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("noteId", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField PARAGRAPH_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("paragraphId", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField INDEX_FIELD_DESC = new org.apache.thrift.protocol.TField("index", org.apache.thrift.protocol.TType.I32, (short)3);
  private static final org.apache.thrift.protocol.TField TYPE_FIELD_DESC = new org.apache.thrift.protocol.TField("type", org.apache.thrift.protocol.TType.STRING, (short)4);
  private static final org.apache.thrift.protocol.TField DATA_FIELD_DESC = new org.apache.thrift.protocol.TField("data", org.apache.thrift.protocol.TType.STRING, (short)5);
  private static final org.apache.thrift.protocol.TField APP_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("appId", org.apache.thrift.protocol.TType.STRING, (short)6);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new OutputUpdateEventStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new OutputUpdateEventTupleSchemeFactory();

  public @org.apache.thrift.annotation.Nullable java.lang.String noteId; // required
  public @org.apache.thrift.annotation.Nullable java.lang.String paragraphId; // required
  public int index; // required
  public @org.apache.thrift.annotation.Nullable java.lang.String type; // required
  public @org.apache.thrift.annotation.Nullable java.lang.String data; // required
  public @org.apache.thrift.annotation.Nullable java.lang.String appId; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    NOTE_ID((short)1, "noteId"),
    PARAGRAPH_ID((short)2, "paragraphId"),
    INDEX((short)3, "index"),
    TYPE((short)4, "type"),
    DATA((short)5, "data"),
    APP_ID((short)6, "appId");

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
        case 1: // NOTE_ID
          return NOTE_ID;
        case 2: // PARAGRAPH_ID
          return PARAGRAPH_ID;
        case 3: // INDEX
          return INDEX;
        case 4: // TYPE
          return TYPE;
        case 5: // DATA
          return DATA;
        case 6: // APP_ID
          return APP_ID;
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
  private static final int __INDEX_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.NOTE_ID, new org.apache.thrift.meta_data.FieldMetaData("noteId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PARAGRAPH_ID, new org.apache.thrift.meta_data.FieldMetaData("paragraphId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.INDEX, new org.apache.thrift.meta_data.FieldMetaData("index", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.TYPE, new org.apache.thrift.meta_data.FieldMetaData("type", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.DATA, new org.apache.thrift.meta_data.FieldMetaData("data", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.APP_ID, new org.apache.thrift.meta_data.FieldMetaData("appId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(OutputUpdateEvent.class, metaDataMap);
  }

  public OutputUpdateEvent() {
  }

  public OutputUpdateEvent(
    java.lang.String noteId,
    java.lang.String paragraphId,
    int index,
    java.lang.String type,
    java.lang.String data,
    java.lang.String appId)
  {
    this();
    this.noteId = noteId;
    this.paragraphId = paragraphId;
    this.index = index;
    setIndexIsSet(true);
    this.type = type;
    this.data = data;
    this.appId = appId;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public OutputUpdateEvent(OutputUpdateEvent other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetNoteId()) {
      this.noteId = other.noteId;
    }
    if (other.isSetParagraphId()) {
      this.paragraphId = other.paragraphId;
    }
    this.index = other.index;
    if (other.isSetType()) {
      this.type = other.type;
    }
    if (other.isSetData()) {
      this.data = other.data;
    }
    if (other.isSetAppId()) {
      this.appId = other.appId;
    }
  }

  public OutputUpdateEvent deepCopy() {
    return new OutputUpdateEvent(this);
  }

  @Override
  public void clear() {
    this.noteId = null;
    this.paragraphId = null;
    setIndexIsSet(false);
    this.index = 0;
    this.type = null;
    this.data = null;
    this.appId = null;
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getNoteId() {
    return this.noteId;
  }

  public OutputUpdateEvent setNoteId(@org.apache.thrift.annotation.Nullable java.lang.String noteId) {
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

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getParagraphId() {
    return this.paragraphId;
  }

  public OutputUpdateEvent setParagraphId(@org.apache.thrift.annotation.Nullable java.lang.String paragraphId) {
    this.paragraphId = paragraphId;
    return this;
  }

  public void unsetParagraphId() {
    this.paragraphId = null;
  }

  /** Returns true if field paragraphId is set (has been assigned a value) and false otherwise */
  public boolean isSetParagraphId() {
    return this.paragraphId != null;
  }

  public void setParagraphIdIsSet(boolean value) {
    if (!value) {
      this.paragraphId = null;
    }
  }

  public int getIndex() {
    return this.index;
  }

  public OutputUpdateEvent setIndex(int index) {
    this.index = index;
    setIndexIsSet(true);
    return this;
  }

  public void unsetIndex() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __INDEX_ISSET_ID);
  }

  /** Returns true if field index is set (has been assigned a value) and false otherwise */
  public boolean isSetIndex() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __INDEX_ISSET_ID);
  }

  public void setIndexIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __INDEX_ISSET_ID, value);
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getType() {
    return this.type;
  }

  public OutputUpdateEvent setType(@org.apache.thrift.annotation.Nullable java.lang.String type) {
    this.type = type;
    return this;
  }

  public void unsetType() {
    this.type = null;
  }

  /** Returns true if field type is set (has been assigned a value) and false otherwise */
  public boolean isSetType() {
    return this.type != null;
  }

  public void setTypeIsSet(boolean value) {
    if (!value) {
      this.type = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getData() {
    return this.data;
  }

  public OutputUpdateEvent setData(@org.apache.thrift.annotation.Nullable java.lang.String data) {
    this.data = data;
    return this;
  }

  public void unsetData() {
    this.data = null;
  }

  /** Returns true if field data is set (has been assigned a value) and false otherwise */
  public boolean isSetData() {
    return this.data != null;
  }

  public void setDataIsSet(boolean value) {
    if (!value) {
      this.data = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getAppId() {
    return this.appId;
  }

  public OutputUpdateEvent setAppId(@org.apache.thrift.annotation.Nullable java.lang.String appId) {
    this.appId = appId;
    return this;
  }

  public void unsetAppId() {
    this.appId = null;
  }

  /** Returns true if field appId is set (has been assigned a value) and false otherwise */
  public boolean isSetAppId() {
    return this.appId != null;
  }

  public void setAppIdIsSet(boolean value) {
    if (!value) {
      this.appId = null;
    }
  }

  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case NOTE_ID:
      if (value == null) {
        unsetNoteId();
      } else {
        setNoteId((java.lang.String)value);
      }
      break;

    case PARAGRAPH_ID:
      if (value == null) {
        unsetParagraphId();
      } else {
        setParagraphId((java.lang.String)value);
      }
      break;

    case INDEX:
      if (value == null) {
        unsetIndex();
      } else {
        setIndex((java.lang.Integer)value);
      }
      break;

    case TYPE:
      if (value == null) {
        unsetType();
      } else {
        setType((java.lang.String)value);
      }
      break;

    case DATA:
      if (value == null) {
        unsetData();
      } else {
        setData((java.lang.String)value);
      }
      break;

    case APP_ID:
      if (value == null) {
        unsetAppId();
      } else {
        setAppId((java.lang.String)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case NOTE_ID:
      return getNoteId();

    case PARAGRAPH_ID:
      return getParagraphId();

    case INDEX:
      return getIndex();

    case TYPE:
      return getType();

    case DATA:
      return getData();

    case APP_ID:
      return getAppId();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case NOTE_ID:
      return isSetNoteId();
    case PARAGRAPH_ID:
      return isSetParagraphId();
    case INDEX:
      return isSetIndex();
    case TYPE:
      return isSetType();
    case DATA:
      return isSetData();
    case APP_ID:
      return isSetAppId();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof OutputUpdateEvent)
      return this.equals((OutputUpdateEvent)that);
    return false;
  }

  public boolean equals(OutputUpdateEvent that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_noteId = true && this.isSetNoteId();
    boolean that_present_noteId = true && that.isSetNoteId();
    if (this_present_noteId || that_present_noteId) {
      if (!(this_present_noteId && that_present_noteId))
        return false;
      if (!this.noteId.equals(that.noteId))
        return false;
    }

    boolean this_present_paragraphId = true && this.isSetParagraphId();
    boolean that_present_paragraphId = true && that.isSetParagraphId();
    if (this_present_paragraphId || that_present_paragraphId) {
      if (!(this_present_paragraphId && that_present_paragraphId))
        return false;
      if (!this.paragraphId.equals(that.paragraphId))
        return false;
    }

    boolean this_present_index = true;
    boolean that_present_index = true;
    if (this_present_index || that_present_index) {
      if (!(this_present_index && that_present_index))
        return false;
      if (this.index != that.index)
        return false;
    }

    boolean this_present_type = true && this.isSetType();
    boolean that_present_type = true && that.isSetType();
    if (this_present_type || that_present_type) {
      if (!(this_present_type && that_present_type))
        return false;
      if (!this.type.equals(that.type))
        return false;
    }

    boolean this_present_data = true && this.isSetData();
    boolean that_present_data = true && that.isSetData();
    if (this_present_data || that_present_data) {
      if (!(this_present_data && that_present_data))
        return false;
      if (!this.data.equals(that.data))
        return false;
    }

    boolean this_present_appId = true && this.isSetAppId();
    boolean that_present_appId = true && that.isSetAppId();
    if (this_present_appId || that_present_appId) {
      if (!(this_present_appId && that_present_appId))
        return false;
      if (!this.appId.equals(that.appId))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetNoteId()) ? 131071 : 524287);
    if (isSetNoteId())
      hashCode = hashCode * 8191 + noteId.hashCode();

    hashCode = hashCode * 8191 + ((isSetParagraphId()) ? 131071 : 524287);
    if (isSetParagraphId())
      hashCode = hashCode * 8191 + paragraphId.hashCode();

    hashCode = hashCode * 8191 + index;

    hashCode = hashCode * 8191 + ((isSetType()) ? 131071 : 524287);
    if (isSetType())
      hashCode = hashCode * 8191 + type.hashCode();

    hashCode = hashCode * 8191 + ((isSetData()) ? 131071 : 524287);
    if (isSetData())
      hashCode = hashCode * 8191 + data.hashCode();

    hashCode = hashCode * 8191 + ((isSetAppId()) ? 131071 : 524287);
    if (isSetAppId())
      hashCode = hashCode * 8191 + appId.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(OutputUpdateEvent other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetNoteId()).compareTo(other.isSetNoteId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetNoteId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.noteId, other.noteId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetParagraphId()).compareTo(other.isSetParagraphId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetParagraphId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.paragraphId, other.paragraphId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetIndex()).compareTo(other.isSetIndex());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetIndex()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.index, other.index);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetType()).compareTo(other.isSetType());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetType()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.type, other.type);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetData()).compareTo(other.isSetData());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetData()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.data, other.data);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetAppId()).compareTo(other.isSetAppId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetAppId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.appId, other.appId);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("OutputUpdateEvent(");
    boolean first = true;

    sb.append("noteId:");
    if (this.noteId == null) {
      sb.append("null");
    } else {
      sb.append(this.noteId);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("paragraphId:");
    if (this.paragraphId == null) {
      sb.append("null");
    } else {
      sb.append(this.paragraphId);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("index:");
    sb.append(this.index);
    first = false;
    if (!first) sb.append(", ");
    sb.append("type:");
    if (this.type == null) {
      sb.append("null");
    } else {
      sb.append(this.type);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("data:");
    if (this.data == null) {
      sb.append("null");
    } else {
      sb.append(this.data);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("appId:");
    if (this.appId == null) {
      sb.append("null");
    } else {
      sb.append(this.appId);
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
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class OutputUpdateEventStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public OutputUpdateEventStandardScheme getScheme() {
      return new OutputUpdateEventStandardScheme();
    }
  }

  private static class OutputUpdateEventStandardScheme extends org.apache.thrift.scheme.StandardScheme<OutputUpdateEvent> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, OutputUpdateEvent struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // NOTE_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.noteId = iprot.readString();
              struct.setNoteIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // PARAGRAPH_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.paragraphId = iprot.readString();
              struct.setParagraphIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // INDEX
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.index = iprot.readI32();
              struct.setIndexIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // TYPE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.type = iprot.readString();
              struct.setTypeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // DATA
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.data = iprot.readString();
              struct.setDataIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // APP_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.appId = iprot.readString();
              struct.setAppIdIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, OutputUpdateEvent struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.noteId != null) {
        oprot.writeFieldBegin(NOTE_ID_FIELD_DESC);
        oprot.writeString(struct.noteId);
        oprot.writeFieldEnd();
      }
      if (struct.paragraphId != null) {
        oprot.writeFieldBegin(PARAGRAPH_ID_FIELD_DESC);
        oprot.writeString(struct.paragraphId);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(INDEX_FIELD_DESC);
      oprot.writeI32(struct.index);
      oprot.writeFieldEnd();
      if (struct.type != null) {
        oprot.writeFieldBegin(TYPE_FIELD_DESC);
        oprot.writeString(struct.type);
        oprot.writeFieldEnd();
      }
      if (struct.data != null) {
        oprot.writeFieldBegin(DATA_FIELD_DESC);
        oprot.writeString(struct.data);
        oprot.writeFieldEnd();
      }
      if (struct.appId != null) {
        oprot.writeFieldBegin(APP_ID_FIELD_DESC);
        oprot.writeString(struct.appId);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class OutputUpdateEventTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public OutputUpdateEventTupleScheme getScheme() {
      return new OutputUpdateEventTupleScheme();
    }
  }

  private static class OutputUpdateEventTupleScheme extends org.apache.thrift.scheme.TupleScheme<OutputUpdateEvent> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, OutputUpdateEvent struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetNoteId()) {
        optionals.set(0);
      }
      if (struct.isSetParagraphId()) {
        optionals.set(1);
      }
      if (struct.isSetIndex()) {
        optionals.set(2);
      }
      if (struct.isSetType()) {
        optionals.set(3);
      }
      if (struct.isSetData()) {
        optionals.set(4);
      }
      if (struct.isSetAppId()) {
        optionals.set(5);
      }
      oprot.writeBitSet(optionals, 6);
      if (struct.isSetNoteId()) {
        oprot.writeString(struct.noteId);
      }
      if (struct.isSetParagraphId()) {
        oprot.writeString(struct.paragraphId);
      }
      if (struct.isSetIndex()) {
        oprot.writeI32(struct.index);
      }
      if (struct.isSetType()) {
        oprot.writeString(struct.type);
      }
      if (struct.isSetData()) {
        oprot.writeString(struct.data);
      }
      if (struct.isSetAppId()) {
        oprot.writeString(struct.appId);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, OutputUpdateEvent struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(6);
      if (incoming.get(0)) {
        struct.noteId = iprot.readString();
        struct.setNoteIdIsSet(true);
      }
      if (incoming.get(1)) {
        struct.paragraphId = iprot.readString();
        struct.setParagraphIdIsSet(true);
      }
      if (incoming.get(2)) {
        struct.index = iprot.readI32();
        struct.setIndexIsSet(true);
      }
      if (incoming.get(3)) {
        struct.type = iprot.readString();
        struct.setTypeIsSet(true);
      }
      if (incoming.get(4)) {
        struct.data = iprot.readString();
        struct.setDataIsSet(true);
      }
      if (incoming.get(5)) {
        struct.appId = iprot.readString();
        struct.setAppIdIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

