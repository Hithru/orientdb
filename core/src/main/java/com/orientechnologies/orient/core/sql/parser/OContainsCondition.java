/* Generated By:JJTree: Do not edit this line. OContainsCondition.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

import com.orientechnologies.common.collection.OMultiValue;
import com.orientechnologies.orient.core.command.OCommandContext;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultInternal;
import com.orientechnologies.orient.core.sql.operator.OQueryOperatorEquals;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OContainsCondition extends OBooleanExpression {

  protected OExpression left;
  protected OExpression right;
  protected OBooleanExpression condition;

  public OContainsCondition(int id) {
    super(id);
  }

  public OContainsCondition(OrientSql p, int id) {
    super(p, id);
  }

  public boolean execute(Object left, Object right) {
    if (left instanceof Collection) {
      if (right instanceof Collection) {
        if (((Collection) right).size() == 1) {
          Object item = ((Collection) right).iterator().next();
          if (item instanceof OResult && ((OResult) item).getPropertyNames().size() == 1) {
            Object propValue =
                ((OResult) item).getProperty(((OResult) item).getPropertyNames().iterator().next());
            if (((Collection) left).contains(propValue)) {
              return true;
            }
          }
          if (((Collection) left).contains(item)) {
            return true;
          }
          if (item instanceof OResult) {
            item = ((OResult) item).getElement().orElse(null);
          }
          if (item instanceof OIdentifiable && ((Collection) left).contains(item)) {
            return true;
          }
        }

        if (OMultiValue.contains(left, right)) {
          return true;
        }
        return false;
      }
      if (right instanceof Iterable) {
        right = ((Iterable) right).iterator();
      }
      if (right instanceof Iterator) {
        Iterator iterator = (Iterator) right;
        while (iterator.hasNext()) {
          Object next = iterator.next();
          if (!((Collection) left).contains(next)) {
            return false;
          }
        }
      }
      for (Object o : (Collection) left) {
        if (equalsInContainsSpace(o, right)) {
          return true;
        }
      }
      return false;
    }

    Iterator leftIterator = null;
    if (left instanceof Iterable) {
      leftIterator = ((Iterable) left).iterator();
    } else if (left instanceof Iterator) {
      leftIterator = (Iterator) left;
    }
    if (leftIterator != null) {
      if (!(right instanceof Iterable)) {
        right = Collections.singleton(right);
      }
      right = ((Iterable) right).iterator();

      Iterator rightIterator = (Iterator) right;
      while (rightIterator.hasNext()) {
        Object leftItem = rightIterator.next();
        boolean found = false;
        while (leftIterator.hasNext()) {
          Object rightItem = leftIterator.next();
          if ((leftItem != null && leftItem.equals(rightItem))
              || (leftItem == null && rightItem == null)) {
            found = true;
            break;
          }
        }

        if (!found) {
          return false;
        }

        // here left iterator should go from beginning, that can be done only for iterable
        // if left at input is iterator result can be invalid
        // TODO what if left is Iterator!!!???, should we make temporary Collection , to be able to
        // iterate from beginning
        if (left instanceof Iterable) {
          leftIterator = ((Iterable) left).iterator();
        }
      }
      return true;
    }
    return false;
  }

  private boolean equalsInContainsSpace(Object left, Object right) {
    if (left == null && right == null) {
      return true;
    } else {
      return OQueryOperatorEquals.equals(left, right);
    }
  }

  @Override
  public boolean evaluate(OIdentifiable currentRecord, OCommandContext ctx) {
    Object leftValue = left.execute(currentRecord, ctx);
    if (right != null) {
      Object rightValue = right.execute(currentRecord, ctx);
      return execute(leftValue, rightValue);
    } else {
      if (!OMultiValue.isMultiValue(leftValue)) {
        return false;
      }
      Iterator<Object> iter = OMultiValue.getMultiValueIterator(leftValue);
      while (iter.hasNext()) {
        Object item = iter.next();
        if (item instanceof OIdentifiable && condition.evaluate((OIdentifiable) item, ctx)) {
          return true;
        } else if (item instanceof OResult && condition.evaluate((OResult) item, ctx)) {
          return true;
        }
      }
      return false;
    }
  }

  @Override
  public boolean evaluate(OResult currentRecord, OCommandContext ctx) {
    if (left.isFunctionAny()) {
      return evaluateAny(currentRecord, ctx);
    }

    if (left.isFunctionAll()) {
      return evaluateAllFunction(currentRecord, ctx);
    }

    Object leftValue = left.execute(currentRecord, ctx);
    if (right != null) {
      Object rightValue = right.execute(currentRecord, ctx);
      return execute(leftValue, rightValue);
    } else {
      if (!OMultiValue.isMultiValue(leftValue)) {
        return false;
      }
      Iterator<Object> iter = OMultiValue.getMultiValueIterator(leftValue);
      while (iter.hasNext()) {
        Object item = iter.next();
        if (item instanceof OIdentifiable && condition.evaluate((OIdentifiable) item, ctx)) {
          return true;
        } else if (item instanceof OResult && condition.evaluate((OResult) item, ctx)) {
          return true;
        } else if (item instanceof Map) {
          OResultInternal res = new OResultInternal();
          ((Map<String, Object>) item)
              .entrySet()
              .forEach(x -> res.setProperty(x.getKey(), x.getValue()));
          if (condition.evaluate(res, ctx)) {
            return true;
          }
        }
      }
      return false;
    }
  }

  private boolean evaluateAny(OResult currentRecord, OCommandContext ctx) {
    if (right != null) {
      for (String s : currentRecord.getPropertyNames()) {
        Object leftVal = currentRecord.getProperty(s);
        Object rightValue = right.execute(currentRecord, ctx);
        if (execute(leftVal, rightValue)) {
          return true;
        }
      }
      return false;
    } else {
      for (String s : currentRecord.getPropertyNames()) {
        Object leftValue = currentRecord.getProperty(s);

        if (!OMultiValue.isMultiValue(leftValue)) {
          continue;
        }
        Iterator<Object> iter = OMultiValue.getMultiValueIterator(leftValue);
        while (iter.hasNext()) {
          Object item = iter.next();
          if (item instanceof OIdentifiable && condition.evaluate((OIdentifiable) item, ctx)) {
            return true;
          } else if (item instanceof OResult && condition.evaluate((OResult) item, ctx)) {
            return true;
          } else if (item instanceof Map) {
            OResultInternal res = new OResultInternal();
            ((Map<String, Object>) item)
                .entrySet()
                .forEach(x -> res.setProperty(x.getKey(), x.getValue()));
            if (condition.evaluate(res, ctx)) {
              return true;
            }
          }
        }
      }
      return false;
    }
  }

  private boolean evaluateAllFunction(OResult currentRecord, OCommandContext ctx) {
    if (right != null) {
      for (String s : currentRecord.getPropertyNames()) {
        Object leftVal = currentRecord.getProperty(s);
        Object rightValue = right.execute(currentRecord, ctx);
        if (!execute(leftVal, rightValue)) {
          return false;
        }
      }
      return true;
    } else {
      for (String s : currentRecord.getPropertyNames()) {
        Object leftValue = currentRecord.getProperty(s);

        if (!OMultiValue.isMultiValue(leftValue)) {
          return false;
        }
        Iterator<Object> iter = OMultiValue.getMultiValueIterator(leftValue);
        boolean found = false;
        while (iter.hasNext()) {
          Object item = iter.next();
          if (item instanceof OIdentifiable && condition.evaluate((OIdentifiable) item, ctx)) {
            found = true;
            break;
          } else if (item instanceof OResult && condition.evaluate((OResult) item, ctx)) {
            found = true;
            break;
          } else if (item instanceof Map) {
            OResultInternal res = new OResultInternal();
            ((Map<String, Object>) item)
                .entrySet()
                .forEach(x -> res.setProperty(x.getKey(), x.getValue()));
            if (condition.evaluate(res, ctx)) {
              found = true;
              break;
            }
          }
        }
        if (!found) {
          return false;
        }
      }
      return true;
    }
  }

  public void toString(Map<Object, Object> params, StringBuilder builder) {
    left.toString(params, builder);
    builder.append(" CONTAINS ");
    if (right != null) {
      right.toString(params, builder);
    } else if (condition != null) {
      builder.append("(");
      condition.toString(params, builder);
      builder.append(")");
    }
  }

  @Override
  public void toGenericStatement(StringBuilder builder) {
    left.toGenericStatement(builder);
    builder.append(" CONTAINS ");
    if (right != null) {
      right.toGenericStatement(builder);
    } else if (condition != null) {
      builder.append("(");
      condition.toGenericStatement(builder);
      builder.append(")");
    }
  }

  @Override
  public boolean supportsBasicCalculation() {
    if (!left.supportsBasicCalculation()) {
      return false;
    }
    if (!right.supportsBasicCalculation()) {
      return false;
    }
    if (!condition.supportsBasicCalculation()) {
      return false;
    }

    return true;
  }

  @Override
  protected int getNumberOfExternalCalculations() {
    int total = 0;
    if (condition != null) {
      total += condition.getNumberOfExternalCalculations();
    }
    if (!left.supportsBasicCalculation()) {
      total++;
    }
    if (right != null && !right.supportsBasicCalculation()) {
      total++;
    }
    return total;
  }

  @Override
  protected List<Object> getExternalCalculationConditions() {
    List<Object> result = new ArrayList<Object>();

    if (condition != null) {
      result.addAll(condition.getExternalCalculationConditions());
    }
    if (!left.supportsBasicCalculation()) {
      result.add(left);
    }
    if (right != null && !right.supportsBasicCalculation()) {
      result.add(right);
    }
    return result;
  }

  @Override
  public boolean needsAliases(Set<String> aliases) {
    if (left != null && left.needsAliases(aliases)) {
      return true;
    }
    if (right != null && right.needsAliases(aliases)) {
      return true;
    }
    if (condition != null && condition.needsAliases(aliases)) {
      return true;
    }
    return false;
  }

  @Override
  public OContainsCondition copy() {
    OContainsCondition result = new OContainsCondition(-1);
    result.left = left == null ? null : left.copy();
    result.right = right == null ? null : right.copy();
    result.condition = condition == null ? null : condition.copy();
    return result;
  }

  @Override
  public void extractSubQueries(SubQueryCollector collector) {
    if (left != null) {
      left.extractSubQueries(collector);
    }
    if (right != null) {
      right.extractSubQueries(collector);
    }
    if (condition != null) {
      condition.extractSubQueries(collector);
    }
  }

  @Override
  public boolean refersToParent() {
    if (left != null && left.refersToParent()) {
      return true;
    }
    if (right != null && right.refersToParent()) {
      return true;
    }
    if (condition != null && condition.refersToParent()) {
      return true;
    }
    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    OContainsCondition that = (OContainsCondition) o;

    if (left != null ? !left.equals(that.left) : that.left != null) return false;
    if (right != null ? !right.equals(that.right) : that.right != null) return false;
    if (condition != null ? !condition.equals(that.condition) : that.condition != null)
      return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = left != null ? left.hashCode() : 0;
    result = 31 * result + (right != null ? right.hashCode() : 0);
    result = 31 * result + (condition != null ? condition.hashCode() : 0);
    return result;
  }

  @Override
  public List<String> getMatchPatternInvolvedAliases() {
    List<String> leftX = left == null ? null : left.getMatchPatternInvolvedAliases();
    List<String> rightX = right == null ? null : right.getMatchPatternInvolvedAliases();
    List<String> conditionX = condition == null ? null : condition.getMatchPatternInvolvedAliases();

    List<String> result = new ArrayList<String>();
    if (leftX != null) {
      result.addAll(leftX);
    }
    if (rightX != null) {
      result.addAll(rightX);
    }
    if (conditionX != null) {
      result.addAll(conditionX);
    }

    return result.size() == 0 ? null : result;
  }

  @Override
  public boolean isCacheable() {
    if (left != null && !left.isCacheable()) {
      return false;
    }
    if (right != null && !right.isCacheable()) {
      return false;
    }
    if (condition != null && !condition.isCacheable()) {
      return false;
    }
    return true;
  }
}
/* JavaCC - OriginalChecksum=bad1118296ea74860e88d66bfe9fa222 (do not edit this line) */
