package com.orientechnologies.orient.core.storage.impl.local.paginated.wal.common;

import com.orientechnologies.orient.core.storage.impl.local.paginated.wal.OLogSequenceNumber;
import com.orientechnologies.orient.core.storage.impl.local.paginated.wal.OWALRecord;

public final class StartWALRecord implements OWALRecord {
<<<<<<< HEAD
  private volatile OperationIdLSN operationIdLSN;

  @Override
  public OperationIdLSN getOperationIdLSN() {
    return operationIdLSN;
  }

  @Override
  public OLogSequenceNumber getLsn() {
    return operationIdLSN.lsn;
  }

  @Override
  public void setOperationIdLsn(OLogSequenceNumber lsn, int operationId) {
    this.operationIdLSN = new OperationIdLSN(operationId, lsn);
=======
  private volatile OLogSequenceNumber logSequenceNumber;

  @Override
  public OLogSequenceNumber getLsn() {
    return logSequenceNumber;
  }

  @Override
  public void setLsn(OLogSequenceNumber lsn) {
    this.logSequenceNumber = lsn;
>>>>>>> develop
  }

  @Override
  public void setDistance(int distance) {}

  @Override
  public void setDiskSize(int diskSize) {}

  @Override
  public int getDistance() {
    return 0;
  }

  @Override
  public int getDiskSize() {
    return CASWALPage.RECORDS_OFFSET;
  }

  @Override
  public boolean trackOperationId() {
    return false;
  }
}
