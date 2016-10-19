/**
 * Autogenerated by Thrift Compiler (0.9.3)
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
@Generated(value = "Autogenerated by Thrift Compiler (0.9.3)", date = "2016-10-19")
public class RemoteInterpreterResult implements org.apache.thrift.TBase<RemoteInterpreterResult, RemoteInterpreterResult._Fields>, java.io.Serializable, Cloneable, Comparable<RemoteInterpreterResult> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("RemoteInterpreterResult");

  private static final org.apache.thrift.protocol.TField CODE_FIELD_DESC = new org.apache.thrift.protocol.TField("code", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField TYPE_FIELD_DESC = new org.apache.thrift.protocol.TField("type", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField MSG_FIELD_DESC = new org.apache.thrift.protocol.TField("msg", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField CONFIG_FIELD_DESC = new org.apache.thrift.protocol.TField("config", org.apache.thrift.protocol.TType.STRING, (short)4);
  private static final org.apache.thrift.protocol.TField GUI_FIELD_DESC = new org.apache.thrift.protocol.TField("gui", org.apache.thrift.protocol.TType.STRING, (short)5);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new RemoteInterpreterResultStandardSchemeFactory());
    schemes.put(TupleScheme.class, new RemoteInterpreterResultTupleSchemeFactory());
  }

  public String code; // required
  public String type; // required
  public String msg; // required
  public String config; // required
  public String gui; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    CODE((short)1, "code"),
    TYPE((short)2, "type"),
    MSG((short)3, "msg"),
    CONFIG((short)4, "config"),
    GUI((short)5, "gui");

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
        case 1: // CODE
          return CODE;
        case 2: // TYPE
          return TYPE;
        case 3: // MSG
          return MSG;
        case 4: // CONFIG
          return CONFIG;
        case 5: // GUI
          return GUI;
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
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.CODE, new org.apache.thrift.meta_data.FieldMetaData("code", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.TYPE, new org.apache.thrift.meta_data.FieldMetaData("type", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.MSG, new org.apache.thrift.meta_data.FieldMetaData("msg", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.CONFIG, new org.apache.thrift.meta_data.FieldMetaData("config", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.GUI, new org.apache.thrift.meta_data.FieldMetaData("gui", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(RemoteInterpreterResult.class, metaDataMap);
  }

  public RemoteInterpreterResult() {
  }

  public RemoteInterpreterResult(
    String code,
    String type,
    String msg,
    String config,
    String gui)
  {
    this();
    this.code = code;
    this.type = type;
    this.msg = msg;
    this.config = config;
    this.gui = gui;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public RemoteInterpreterResult(RemoteInterpreterResult other) {
    if (other.isSetCode()) {
      this.code = other.code;
    }
    if (other.isSetType()) {
      this.type = other.type;
    }
    if (other.isSetMsg()) {
      this.msg = other.msg;
    }
    if (other.isSetConfig()) {
      this.config = other.config;
    }
    if (other.isSetGui()) {
      this.gui = other.gui;
    }
  }

  public RemoteInterpreterResult deepCopy() {
    return new RemoteInterpreterResult(this);
  }

  @Override
  public void clear() {
    this.code = null;
    this.type = null;
    this.msg = null;
    this.config = null;
    this.gui = null;
  }

  public String getCode() {
    return this.code;
  }

  public RemoteInterpreterResult setCode(String code) {
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

  public String getType() {
    return this.type;
  }

  public RemoteInterpreterResult setType(String type) {
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

  public String getMsg() {
    return this.msg;
  }

  public RemoteInterpreterResult setMsg(String msg) {
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

  public String getConfig() {
    return this.config;
  }

  public RemoteInterpreterResult setConfig(String config) {
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

  public String getGui() {
    return this.gui;
  }

  public RemoteInterpreterResult setGui(String gui) {
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

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case CODE:
      if (value == null) {
        unsetCode();
      } else {
        setCode((String)value);
      }
      break;

    case TYPE:
      if (value == null) {
        unsetType();
      } else {
        setType((String)value);
      }
      break;

    case MSG:
      if (value == null) {
        unsetMsg();
      } else {
        setMsg((String)value);
      }
      break;

    case CONFIG:
      if (value == null) {
        unsetConfig();
      } else {
        setConfig((String)value);
      }
      break;

    case GUI:
      if (value == null) {
        unsetGui();
      } else {
        setGui((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case CODE:
      return getCode();

    case TYPE:
      return getType();

    case MSG:
      return getMsg();

    case CONFIG:
      return getConfig();

    case GUI:
      return getGui();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case CODE:
      return isSetCode();
    case TYPE:
      return isSetType();
    case MSG:
      return isSetMsg();
    case CONFIG:
      return isSetConfig();
    case GUI:
      return isSetGui();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof RemoteInterpreterResult)
      return this.equals((RemoteInterpreterResult)that);
    return false;
  }

  public boolean equals(RemoteInterpreterResult that) {
    if (that == null)
      return false;

    boolean this_present_code = true && this.isSetCode();
    boolean that_present_code = true && that.isSetCode();
    if (this_present_code || that_present_code) {
      if (!(this_present_code && that_present_code))
        return false;
      if (!this.code.equals(that.code))
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

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_code = true && (isSetCode());
    list.add(present_code);
    if (present_code)
      list.add(code);

    boolean present_type = true && (isSetType());
    list.add(present_type);
    if (present_type)
      list.add(type);

    boolean present_msg = true && (isSetMsg());
    list.add(present_msg);
    if (present_msg)
      list.add(msg);

    boolean present_config = true && (isSetConfig());
    list.add(present_config);
    if (present_config)
      list.add(config);

    boolean present_gui = true && (isSetGui());
    list.add(present_gui);
    if (present_gui)
      list.add(gui);

    return list.hashCode();
  }

  @Override
  public int compareTo(RemoteInterpreterResult other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetCode()).compareTo(other.isSetCode());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCode()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.code, other.code);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetType()).compareTo(other.isSetType());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetType()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.type, other.type);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMsg()).compareTo(other.isSetMsg());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMsg()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.msg, other.msg);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetConfig()).compareTo(other.isSetConfig());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetConfig()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.config, other.config);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetGui()).compareTo(other.isSetGui());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetGui()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.gui, other.gui);
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
    StringBuilder sb = new StringBuilder("RemoteInterpreterResult(");
    boolean first = true;

    sb.append("code:");
    if (this.code == null) {
      sb.append("null");
    } else {
      sb.append(this.code);
    }
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
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class RemoteInterpreterResultStandardSchemeFactory implements SchemeFactory {
    public RemoteInterpreterResultStandardScheme getScheme() {
      return new RemoteInterpreterResultStandardScheme();
    }
  }

  private static class RemoteInterpreterResultStandardScheme extends StandardScheme<RemoteInterpreterResult> {

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
          case 2: // TYPE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.type = iprot.readString();
              struct.setTypeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // MSG
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.msg = iprot.readString();
              struct.setMsgIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // CONFIG
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.config = iprot.readString();
              struct.setConfigIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // GUI
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.gui = iprot.readString();
              struct.setGuiIsSet(true);
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
      if (struct.type != null) {
        oprot.writeFieldBegin(TYPE_FIELD_DESC);
        oprot.writeString(struct.type);
        oprot.writeFieldEnd();
      }
      if (struct.msg != null) {
        oprot.writeFieldBegin(MSG_FIELD_DESC);
        oprot.writeString(struct.msg);
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
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class RemoteInterpreterResultTupleSchemeFactory implements SchemeFactory {
    public RemoteInterpreterResultTupleScheme getScheme() {
      return new RemoteInterpreterResultTupleScheme();
    }
  }

  private static class RemoteInterpreterResultTupleScheme extends TupleScheme<RemoteInterpreterResult> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, RemoteInterpreterResult struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetCode()) {
        optionals.set(0);
      }
      if (struct.isSetType()) {
        optionals.set(1);
      }
      if (struct.isSetMsg()) {
        optionals.set(2);
      }
      if (struct.isSetConfig()) {
        optionals.set(3);
      }
      if (struct.isSetGui()) {
        optionals.set(4);
      }
      oprot.writeBitSet(optionals, 5);
      if (struct.isSetCode()) {
        oprot.writeString(struct.code);
      }
      if (struct.isSetType()) {
        oprot.writeString(struct.type);
      }
      if (struct.isSetMsg()) {
        oprot.writeString(struct.msg);
      }
      if (struct.isSetConfig()) {
        oprot.writeString(struct.config);
      }
      if (struct.isSetGui()) {
        oprot.writeString(struct.gui);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, RemoteInterpreterResult struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(5);
      if (incoming.get(0)) {
        struct.code = iprot.readString();
        struct.setCodeIsSet(true);
      }
      if (incoming.get(1)) {
        struct.type = iprot.readString();
        struct.setTypeIsSet(true);
      }
      if (incoming.get(2)) {
        struct.msg = iprot.readString();
        struct.setMsgIsSet(true);
      }
      if (incoming.get(3)) {
        struct.config = iprot.readString();
        struct.setConfigIsSet(true);
      }
      if (incoming.get(4)) {
        struct.gui = iprot.readString();
        struct.setGuiIsSet(true);
      }
    }
  }

}

