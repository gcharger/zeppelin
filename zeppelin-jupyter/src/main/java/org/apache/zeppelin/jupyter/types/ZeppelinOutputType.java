package org.apache.zeppelin.jupyter.types;

public enum ZeppelinOutputType {
  TEXT("TEXT"),
  HTML("HTML"),
  TABLE("TABLE")
  ;

  private final String type;
  private ZeppelinOutputType(final String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return type;
  }
}
