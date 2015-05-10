/**
 * Autogenerated by Thrift Compiler (0.9.0)
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoteInterpreterContext implements org.apache.thrift.TBase<RemoteInterpreterContext, RemoteInterpreterContext._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("RemoteInterpreterContext");

  private static final org.apache.thrift.protocol.TField PARAGRAPH_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("paragraphId", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField PARAGRAPH_TITLE_FIELD_DESC = new org.apache.thrift.protocol.TField("paragraphTitle", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField PARAGRAPH_TEXT_FIELD_DESC = new org.apache.thrift.protocol.TField("paragraphText", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField CONFIG_FIELD_DESC = new org.apache.thrift.protocol.TField("config", org.apache.thrift.protocol.TType.STRING, (short)4);
  private static final org.apache.thrift.protocol.TField GUI_FIELD_DESC = new org.apache.thrift.protocol.TField("gui", org.apache.thrift.protocol.TType.STRING, (short)5);
  private static final org.apache.thrift.protocol.TField RUNNERS_FIELD_DESC = new org.apache.thrift.protocol.TField("runners", org.apache.thrift.protocol.TType.STRING, (short)6);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new RemoteInterpreterContextStandardSchemeFactory());
    schemes.put(TupleScheme.class, new RemoteInterpreterContextTupleSchemeFactory());
  }

  public String paragraphId; // required
  public String paragraphTitle; // required
  public String paragraphText; // required
  public String config; // required
  public String gui; // required
  public String runners; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    PARAGRAPH_ID((short)1, "paragraphId"),
    PARAGRAPH_TITLE((short)2, "paragraphTitle"),
    PARAGRAPH_TEXT((short)3, "paragraphText"),
    CONFIG((short)4, "config"),
    GUI((short)5, "gui"),
    RUNNERS((short)6, "runners");

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
        case 1: // PARAGRAPH_ID
          return PARAGRAPH_ID;
        case 2: // PARAGRAPH_TITLE
          return PARAGRAPH_TITLE;
        case 3: // PARAGRAPH_TEXT
          return PARAGRAPH_TEXT;
        case 4: // CONFIG
          return CONFIG;
        case 5: // GUI
          return GUI;
        case 6: // RUNNERS
          return RUNNERS;
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
    tmpMap.put(_Fields.PARAGRAPH_ID, new org.apache.thrift.meta_data.FieldMetaData("paragraphId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PARAGRAPH_TITLE, new org.apache.thrift.meta_data.FieldMetaData("paragraphTitle", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PARAGRAPH_TEXT, new org.apache.thrift.meta_data.FieldMetaData("paragraphText", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.CONFIG, new org.apache.thrift.meta_data.FieldMetaData("config", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.GUI, new org.apache.thrift.meta_data.FieldMetaData("gui", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.RUNNERS, new org.apache.thrift.meta_data.FieldMetaData("runners", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(RemoteInterpreterContext.class, metaDataMap);
  }

  public RemoteInterpreterContext() {
  }

  public RemoteInterpreterContext(
    String paragraphId,
    String paragraphTitle,
    String paragraphText,
    String config,
    String gui,
    String runners)
  {
    this();
    this.paragraphId = paragraphId;
    this.paragraphTitle = paragraphTitle;
    this.paragraphText = paragraphText;
    this.config = config;
    this.gui = gui;
    this.runners = runners;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public RemoteInterpreterContext(RemoteInterpreterContext other) {
    if (other.isSetParagraphId()) {
      this.paragraphId = other.paragraphId;
    }
    if (other.isSetParagraphTitle()) {
      this.paragraphTitle = other.paragraphTitle;
    }
    if (other.isSetParagraphText()) {
      this.paragraphText = other.paragraphText;
    }
    if (other.isSetConfig()) {
      this.config = other.config;
    }
    if (other.isSetGui()) {
      this.gui = other.gui;
    }
    if (other.isSetRunners()) {
      this.runners = other.runners;
    }
  }

  public RemoteInterpreterContext deepCopy() {
    return new RemoteInterpreterContext(this);
  }

  @Override
  public void clear() {
    this.paragraphId = null;
    this.paragraphTitle = null;
    this.paragraphText = null;
    this.config = null;
    this.gui = null;
    this.runners = null;
  }

  public String getParagraphId() {
    return this.paragraphId;
  }

  public RemoteInterpreterContext setParagraphId(String paragraphId) {
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

  public String getParagraphTitle() {
    return this.paragraphTitle;
  }

  public RemoteInterpreterContext setParagraphTitle(String paragraphTitle) {
    this.paragraphTitle = paragraphTitle;
    return this;
  }

  public void unsetParagraphTitle() {
    this.paragraphTitle = null;
  }

  /** Returns true if field paragraphTitle is set (has been assigned a value) and false otherwise */
  public boolean isSetParagraphTitle() {
    return this.paragraphTitle != null;
  }

  public void setParagraphTitleIsSet(boolean value) {
    if (!value) {
      this.paragraphTitle = null;
    }
  }

  public String getParagraphText() {
    return this.paragraphText;
  }

  public RemoteInterpreterContext setParagraphText(String paragraphText) {
    this.paragraphText = paragraphText;
    return this;
  }

  public void unsetParagraphText() {
    this.paragraphText = null;
  }

  /** Returns true if field paragraphText is set (has been assigned a value) and false otherwise */
  public boolean isSetParagraphText() {
    return this.paragraphText != null;
  }

  public void setParagraphTextIsSet(boolean value) {
    if (!value) {
      this.paragraphText = null;
    }
  }

  public String getConfig() {
    return this.config;
  }

  public RemoteInterpreterContext setConfig(String config) {
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

  public RemoteInterpreterContext setGui(String gui) {
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

  public String getRunners() {
    return this.runners;
  }

  public RemoteInterpreterContext setRunners(String runners) {
    this.runners = runners;
    return this;
  }

  public void unsetRunners() {
    this.runners = null;
  }

  /** Returns true if field runners is set (has been assigned a value) and false otherwise */
  public boolean isSetRunners() {
    return this.runners != null;
  }

  public void setRunnersIsSet(boolean value) {
    if (!value) {
      this.runners = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case PARAGRAPH_ID:
      if (value == null) {
        unsetParagraphId();
      } else {
        setParagraphId((String)value);
      }
      break;

    case PARAGRAPH_TITLE:
      if (value == null) {
        unsetParagraphTitle();
      } else {
        setParagraphTitle((String)value);
      }
      break;

    case PARAGRAPH_TEXT:
      if (value == null) {
        unsetParagraphText();
      } else {
        setParagraphText((String)value);
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

    case RUNNERS:
      if (value == null) {
        unsetRunners();
      } else {
        setRunners((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case PARAGRAPH_ID:
      return getParagraphId();

    case PARAGRAPH_TITLE:
      return getParagraphTitle();

    case PARAGRAPH_TEXT:
      return getParagraphText();

    case CONFIG:
      return getConfig();

    case GUI:
      return getGui();

    case RUNNERS:
      return getRunners();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case PARAGRAPH_ID:
      return isSetParagraphId();
    case PARAGRAPH_TITLE:
      return isSetParagraphTitle();
    case PARAGRAPH_TEXT:
      return isSetParagraphText();
    case CONFIG:
      return isSetConfig();
    case GUI:
      return isSetGui();
    case RUNNERS:
      return isSetRunners();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof RemoteInterpreterContext)
      return this.equals((RemoteInterpreterContext)that);
    return false;
  }

  public boolean equals(RemoteInterpreterContext that) {
    if (that == null)
      return false;

    boolean this_present_paragraphId = true && this.isSetParagraphId();
    boolean that_present_paragraphId = true && that.isSetParagraphId();
    if (this_present_paragraphId || that_present_paragraphId) {
      if (!(this_present_paragraphId && that_present_paragraphId))
        return false;
      if (!this.paragraphId.equals(that.paragraphId))
        return false;
    }

    boolean this_present_paragraphTitle = true && this.isSetParagraphTitle();
    boolean that_present_paragraphTitle = true && that.isSetParagraphTitle();
    if (this_present_paragraphTitle || that_present_paragraphTitle) {
      if (!(this_present_paragraphTitle && that_present_paragraphTitle))
        return false;
      if (!this.paragraphTitle.equals(that.paragraphTitle))
        return false;
    }

    boolean this_present_paragraphText = true && this.isSetParagraphText();
    boolean that_present_paragraphText = true && that.isSetParagraphText();
    if (this_present_paragraphText || that_present_paragraphText) {
      if (!(this_present_paragraphText && that_present_paragraphText))
        return false;
      if (!this.paragraphText.equals(that.paragraphText))
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

    boolean this_present_runners = true && this.isSetRunners();
    boolean that_present_runners = true && that.isSetRunners();
    if (this_present_runners || that_present_runners) {
      if (!(this_present_runners && that_present_runners))
        return false;
      if (!this.runners.equals(that.runners))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public int compareTo(RemoteInterpreterContext other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    RemoteInterpreterContext typedOther = (RemoteInterpreterContext)other;

    lastComparison = Boolean.valueOf(isSetParagraphId()).compareTo(typedOther.isSetParagraphId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetParagraphId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.paragraphId, typedOther.paragraphId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetParagraphTitle()).compareTo(typedOther.isSetParagraphTitle());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetParagraphTitle()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.paragraphTitle, typedOther.paragraphTitle);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetParagraphText()).compareTo(typedOther.isSetParagraphText());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetParagraphText()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.paragraphText, typedOther.paragraphText);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetConfig()).compareTo(typedOther.isSetConfig());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetConfig()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.config, typedOther.config);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetGui()).compareTo(typedOther.isSetGui());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetGui()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.gui, typedOther.gui);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetRunners()).compareTo(typedOther.isSetRunners());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetRunners()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.runners, typedOther.runners);
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
    StringBuilder sb = new StringBuilder("RemoteInterpreterContext(");
    boolean first = true;

    sb.append("paragraphId:");
    if (this.paragraphId == null) {
      sb.append("null");
    } else {
      sb.append(this.paragraphId);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("paragraphTitle:");
    if (this.paragraphTitle == null) {
      sb.append("null");
    } else {
      sb.append(this.paragraphTitle);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("paragraphText:");
    if (this.paragraphText == null) {
      sb.append("null");
    } else {
      sb.append(this.paragraphText);
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
    sb.append("runners:");
    if (this.runners == null) {
      sb.append("null");
    } else {
      sb.append(this.runners);
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

  private static class RemoteInterpreterContextStandardSchemeFactory implements SchemeFactory {
    public RemoteInterpreterContextStandardScheme getScheme() {
      return new RemoteInterpreterContextStandardScheme();
    }
  }

  private static class RemoteInterpreterContextStandardScheme extends StandardScheme<RemoteInterpreterContext> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, RemoteInterpreterContext struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // PARAGRAPH_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.paragraphId = iprot.readString();
              struct.setParagraphIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // PARAGRAPH_TITLE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.paragraphTitle = iprot.readString();
              struct.setParagraphTitleIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // PARAGRAPH_TEXT
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.paragraphText = iprot.readString();
              struct.setParagraphTextIsSet(true);
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
          case 6: // RUNNERS
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.runners = iprot.readString();
              struct.setRunnersIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, RemoteInterpreterContext struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.paragraphId != null) {
        oprot.writeFieldBegin(PARAGRAPH_ID_FIELD_DESC);
        oprot.writeString(struct.paragraphId);
        oprot.writeFieldEnd();
      }
      if (struct.paragraphTitle != null) {
        oprot.writeFieldBegin(PARAGRAPH_TITLE_FIELD_DESC);
        oprot.writeString(struct.paragraphTitle);
        oprot.writeFieldEnd();
      }
      if (struct.paragraphText != null) {
        oprot.writeFieldBegin(PARAGRAPH_TEXT_FIELD_DESC);
        oprot.writeString(struct.paragraphText);
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
      if (struct.runners != null) {
        oprot.writeFieldBegin(RUNNERS_FIELD_DESC);
        oprot.writeString(struct.runners);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class RemoteInterpreterContextTupleSchemeFactory implements SchemeFactory {
    public RemoteInterpreterContextTupleScheme getScheme() {
      return new RemoteInterpreterContextTupleScheme();
    }
  }

  private static class RemoteInterpreterContextTupleScheme extends TupleScheme<RemoteInterpreterContext> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, RemoteInterpreterContext struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetParagraphId()) {
        optionals.set(0);
      }
      if (struct.isSetParagraphTitle()) {
        optionals.set(1);
      }
      if (struct.isSetParagraphText()) {
        optionals.set(2);
      }
      if (struct.isSetConfig()) {
        optionals.set(3);
      }
      if (struct.isSetGui()) {
        optionals.set(4);
      }
      if (struct.isSetRunners()) {
        optionals.set(5);
      }
      oprot.writeBitSet(optionals, 6);
      if (struct.isSetParagraphId()) {
        oprot.writeString(struct.paragraphId);
      }
      if (struct.isSetParagraphTitle()) {
        oprot.writeString(struct.paragraphTitle);
      }
      if (struct.isSetParagraphText()) {
        oprot.writeString(struct.paragraphText);
      }
      if (struct.isSetConfig()) {
        oprot.writeString(struct.config);
      }
      if (struct.isSetGui()) {
        oprot.writeString(struct.gui);
      }
      if (struct.isSetRunners()) {
        oprot.writeString(struct.runners);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, RemoteInterpreterContext struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(6);
      if (incoming.get(0)) {
        struct.paragraphId = iprot.readString();
        struct.setParagraphIdIsSet(true);
      }
      if (incoming.get(1)) {
        struct.paragraphTitle = iprot.readString();
        struct.setParagraphTitleIsSet(true);
      }
      if (incoming.get(2)) {
        struct.paragraphText = iprot.readString();
        struct.setParagraphTextIsSet(true);
      }
      if (incoming.get(3)) {
        struct.config = iprot.readString();
        struct.setConfigIsSet(true);
      }
      if (incoming.get(4)) {
        struct.gui = iprot.readString();
        struct.setGuiIsSet(true);
      }
      if (incoming.get(5)) {
        struct.runners = iprot.readString();
        struct.setRunnersIsSet(true);
      }
    }
  }

}

