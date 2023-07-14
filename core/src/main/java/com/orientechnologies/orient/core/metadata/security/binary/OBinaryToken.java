package com.orientechnologies.orient.core.metadata.security.binary;

<<<<<<< HEAD
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.metadata.security.jwt.OBinaryTokenPayload;
import com.orientechnologies.orient.core.metadata.security.jwt.OTokenMetaInfo;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class OBinaryTokenPayloadImpl implements OBinaryTokenPayload {
  private String userName;
  private String database;
  private long expiry;
  private ORID userRid;
  private String databaseType;
  private short protocolVersion;
  private String serializer;
  private String driverName;
  private String driverVersion;
  private boolean serverUser;

  @Override
  public String getDatabase() {
    return database;
  }

  public void setDatabase(String database) {
    this.database = database;
  }

  @Override
  public long getExpiry() {
    return expiry;
  }

  public void setExpiry(long expiry) {
    this.expiry = expiry;
  }

  @Override
  public ORID getUserRid() {
    return userRid;
  }

  public void setUserRid(ORID rid) {
    this.userRid = rid;
  }

  @Override
  public String getDatabaseType() {
    return databaseType;
  }

  public void setDatabaseType(String databaseType) {
    this.databaseType = databaseType;
  }

  @Override
  public short getProtocolVersion() {
    return protocolVersion;
  }

  public void setProtocolVersion(short protocolVersion) {
    this.protocolVersion = protocolVersion;
  }

  @Override
  public String getSerializer() {
    return serializer;
  }

  public void setSerializer(String serializer) {
    this.serializer = serializer;
  }

  @Override
  public String getDriverName() {
    return driverName;
  }

  public void setDriverName(String driverName) {
    this.driverName = driverName;
  }

  @Override
  public String getDriverVersion() {
    return driverVersion;
  }

  public void setDriverVersion(String driverVersion) {
    this.driverVersion = driverVersion;
  }

  @Override
  public boolean isServerUser() {
    return serverUser;
  }

  public void setServerUser(boolean serverUser) {
    this.serverUser = serverUser;
=======
import com.orientechnologies.orient.core.db.ODatabaseDocumentInternal;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.metadata.security.OToken;
import com.orientechnologies.orient.core.metadata.security.OUser;
import com.orientechnologies.orient.core.metadata.security.jwt.OBinaryTokenPayload;
import com.orientechnologies.orient.core.metadata.security.jwt.OTokenHeader;
import com.orientechnologies.orient.core.record.impl.ODocument;

public class OBinaryToken implements OToken {

  private boolean valid;
  private boolean verified;
  private OTokenHeader header;
  private OBinaryTokenPayload payload;

  @Override
  public boolean getIsVerified() {
    return verified;
  }

  @Override
  public void setIsVerified(boolean verified) {
    this.verified = verified;
  }

  @Override
  public boolean getIsValid() {
    return valid;
  }

  @Override
  public void setIsValid(boolean valid) {
    this.valid = valid;
>>>>>>> develop
  }

  @Override
  public String getUserName() {
<<<<<<< HEAD
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  @Override
  public void serialize(DataOutputStream output, OTokenMetaInfo serializer)
      throws UnsupportedEncodingException, IOException {
    String toWrite = this.getDatabase();
    OBinaryTokenSerializer.writeString(output, toWrite);
    if (this.getDatabaseType() == null) output.writeByte(-1);
    else output.writeByte(serializer.getDbTypeID(this.getDatabaseType()));
    ORID id = this.getUserRid();
    if (id == null) {
      output.writeShort(-1);
      output.writeLong(-1);
    } else {
      output.writeShort(id.getClusterId());
      output.writeLong(id.getClusterPosition());
    }
    output.writeLong(this.getExpiry());
    output.writeBoolean(this.isServerUser());
    if (this.isServerUser()) {
      OBinaryTokenSerializer.writeString(output, this.getUserName());
    }
    output.writeShort(this.getProtocolVersion());
    OBinaryTokenSerializer.writeString(output, this.getSerializer());
    OBinaryTokenSerializer.writeString(output, this.getDriverName());
    OBinaryTokenSerializer.writeString(output, this.getDriverVersion());
  }

  @Override
  public String getPayloadType() {
    return "OrientDB";
=======
    return payload.getUserName();
  }

  @Override
  public OUser getUser(ODatabaseDocumentInternal db) {
    if (this.payload.getUserRid() != null) {
      ODocument result = db.load(new ORecordId(this.payload.getUserRid()), "roles:1");
      if (result != null && result.getClassName().equals(OUser.CLASS_NAME)) {
        return new OUser(result);
      }
    }
    return null;
  }

  @Override
  public String getDatabase() {
    return this.payload.getDatabase();
  }

  @Override
  public String getDatabaseType() {
    return this.getPayload().getDatabaseType();
  }

  @Override
  public ORID getUserId() {
    return this.getPayload().getUserRid();
  }

  public OTokenHeader getHeader() {
    return header;
  }

  public void setHeader(OTokenHeader header) {
    this.header = header;
  }

  @Override
  public void setExpiry(long expiry) {
    getPayload().setExpiry(expiry);
  }

  @Override
  public long getExpiry() {
    return payload.getExpiry();
  }

  public short getProtocolVersion() {
    return payload.getProtocolVersion();
  }

  public String getSerializer() {
    return payload.getSerializer();
  }

  public String getDriverName() {
    return payload.getDriverName();
  }

  public String getDriverVersion() {
    return payload.getDriverVersion();
  }

  public boolean isServerUser() {
    return getPayload().isServerUser();
  }

  @Override
  public boolean isNowValid() {
    long now = System.currentTimeMillis();
    return getExpiry() > now;
>>>>>>> develop
  }

  @Override
  public boolean isCloseToExpire() {
    long now = System.currentTimeMillis();
    return getExpiry() - 120000 <= now;
  }
<<<<<<< HEAD
=======

  public OBinaryTokenPayload getPayload() {
    return payload;
  }

  public void setPayload(OBinaryTokenPayload payload) {
    this.payload = payload;
  }
>>>>>>> develop
}
