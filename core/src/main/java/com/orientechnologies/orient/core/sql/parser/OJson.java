/* Generated By:JJTree: Do not edit this line. OJson.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

import com.orientechnologies.orient.core.command.OCommandContext;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.record.impl.ODocumentHelper;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultInternal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class OJson extends SimpleNode {

  protected List<OJsonItem> items = new ArrayList<OJsonItem>();

  public OJson(int id) {
    super(id);
  }

  public OJson(OrientSql p, int id) {
    super(p, id);
  }

  public void toString(Map<Object, Object> params, StringBuilder builder) {
    builder.append("{");
    boolean first = true;
    for (OJsonItem item : items) {
      if (!first) {
        builder.append(", ");
      }
      item.toString(params, builder);

      first = false;
    }
    builder.append("}");
  }

  public void toGenericStatement(StringBuilder builder) {
    builder.append("{");
    boolean first = true;
    for (OJsonItem item : items) {
      if (!first) {
        builder.append(", ");
      }
      item.toGenericStatement(builder);

      first = false;
    }
    builder.append("}");
  }

  public ODocument toDocument(OIdentifiable source, OCommandContext ctx) {
    String className = getClassNameForDocument(ctx);
    ODocument doc;
    if (className != null) {
      doc = new ODocument(className);
    } else {
      doc = new ODocument();
    }
    for (OJsonItem item : items) {
      String name = item.getLeftValue();
      if (name == null) {
        continue;
      }
      Object value;
      if (item.right.value instanceof OJson) {
        value = ((OJson) item.right.value).toDocument(source, ctx);
      } else {
        value = item.right.execute(source, ctx);
      }
      doc.field(name, value);
    }

    return doc;
  }

  private ODocument toDocument(OResult source, OCommandContext ctx, String className) {
    ODocument retDoc = new ODocument(className);
    for (OJsonItem item : items) {
      String name = item.getLeftValue();
      if (name == null
          || ODocumentHelper.getReservedAttributes().contains(name.toLowerCase(Locale.ENGLISH))) {
        continue;
      }
      Object value = item.right.execute(source, ctx);
      retDoc.field(name, value);
    }
    return retDoc;
  }

  /**
   * choosing return type is based on existence of @class and @type field in JSON
   *
   * @param source
   * @param ctx
   * @return
   */
  public Object toObjectDetermineType(OResult source, OCommandContext ctx) {
    String className = getClassNameForDocument(ctx);
    String type = getTypeForDocument(ctx);
    if (className != null || (type != null && "d".equalsIgnoreCase(type))) {
      return toDocument(source, ctx, className);
    } else {
      return toMap(source, ctx);
    }
  }

  public Map<String, Object> toMap(OIdentifiable source, OCommandContext ctx) {
    Map<String, Object> doc = new LinkedHashMap<String, Object>();
    for (OJsonItem item : items) {
      String name = item.getLeftValue();
      if (name == null) {
        continue;
      }
      Object value = item.right.execute(source, ctx);
      doc.put(name, value);
    }

    return doc;
  }

  public Map<String, Object> toMap(OResult source, OCommandContext ctx) {
    Map<String, Object> doc = new LinkedHashMap<String, Object>();
    for (OJsonItem item : items) {
      String name = item.getLeftValue();
      if (name == null) {
        continue;
      }
      Object value = item.right.execute(source, ctx);
      doc.put(name, value);
    }

    return doc;
  }

  private String getClassNameForDocument(OCommandContext ctx) {
    for (OJsonItem item : items) {
      String left = item.getLeftValue();
      if (left != null && left.toLowerCase(Locale.ENGLISH).equals("@class")) {
        return "" + item.right.execute((OResult) null, ctx);
      }
    }
    return null;
  }

  private String getTypeForDocument(OCommandContext ctx) {
    for (OJsonItem item : items) {
      String left = item.getLeftValue();
      if (left != null && left.toLowerCase(Locale.ENGLISH).equals("@type")) {
        return "" + item.right.execute((OResult) null, ctx);
      }
    }
    return null;
  }

  public boolean needsAliases(Set<String> aliases) {
    for (OJsonItem item : items) {
      if (item.needsAliases(aliases)) {
        return true;
      }
    }
    return false;
  }

  public boolean isAggregate() {
    for (OJsonItem item : items) {
      if (item.isAggregate()) {
        return true;
      }
    }
    return false;
  }

  public OJson splitForAggregation(AggregateProjectionSplit aggregateSplit, OCommandContext ctx) {
    if (isAggregate()) {
      OJson result = new OJson(-1);
      for (OJsonItem item : items) {
        result.items.add(item.splitForAggregation(aggregateSplit, ctx));
      }
      return result;
    } else {
      return this;
    }
  }

  public OJson copy() {
    OJson result = new OJson(-1);
    result.items = items.stream().map(x -> x.copy()).collect(Collectors.toList());
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    OJson oJson = (OJson) o;

    if (items != null ? !items.equals(oJson.items) : oJson.items != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return items != null ? items.hashCode() : 0;
  }

  public void extractSubQueries(SubQueryCollector collector) {
    for (OJsonItem item : items) {
      item.extractSubQueries(collector);
    }
  }

  public boolean refersToParent() {
    for (OJsonItem item : items) {
      if (item.refersToParent()) {
        return true;
      }
    }
    return false;
  }

  public OResult serialize() {
    OResultInternal result = new OResultInternal();
    if (items != null) {
      result.setProperty(
          "items", items.stream().map(x -> x.serialize()).collect(Collectors.toList()));
    }
    return result;
  }

  public void deserialize(OResult fromResult) {

    if (fromResult.getProperty("items") != null) {
      List<OResult> ser = fromResult.getProperty("items");
      items = new ArrayList<>();
      for (OResult r : ser) {
        OJsonItem exp = new OJsonItem();
        exp.deserialize(r);
        items.add(exp);
      }
    }
  }

  public void addItem(OJsonItem item) {
    this.items.add(item);
  }

  public boolean isCacheable() {
    return false; // TODO optimize
  }
}
/* JavaCC - OriginalChecksum=3beec9f6db486de944498588b51a505d (do not edit this line) */
