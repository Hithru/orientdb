package com.orientechnologies.orient.server.distributed;

import static org.junit.Assert.assertEquals;

import com.orientechnologies.orient.core.db.ODatabaseSession;
<<<<<<< HEAD
import com.orientechnologies.orient.core.db.ODatabaseType;
=======
>>>>>>> develop
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
<<<<<<< HEAD
import com.orientechnologies.orient.server.OServer;
=======
import com.orientechnologies.orient.setup.TestSetup;
import com.orientechnologies.orient.setup.TestSetupUtil;
import com.orientechnologies.orient.setup.configs.SimpleDServerConfig;
>>>>>>> develop
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SimpleUpdatePasswordDistributedIT {

<<<<<<< HEAD
  private OServer server0;
  private OServer server1;
  private OServer server2;
  private OrientDB remote;
  private ODatabaseSession session;

  @Before
  public void before() throws Exception {
    server0 = OServer.startFromClasspathConfig("orientdb-simple-dserver-config-0.xml");
    server1 = OServer.startFromClasspathConfig("orientdb-simple-dserver-config-1.xml");
    server2 = OServer.startFromClasspathConfig("orientdb-simple-dserver-config-2.xml");
    remote = new OrientDB("remote:localhost", "root", "test", OrientDBConfig.defaultConfig());
    remote.create("test", ODatabaseType.PLOCAL);
=======
  private OrientDB remote;
  private ODatabaseSession session;
  private SimpleDServerConfig config;
  private TestSetup setup;

  @Before
  public void before() throws Exception {
    config = new SimpleDServerConfig();
    setup = TestSetupUtil.create(config);
    setup.setup();

    remote =
        setup.createRemote(
            SimpleDServerConfig.SERVER0, "root", "test", OrientDBConfig.defaultConfig());
    remote.execute(
        "create database ? plocal users(admin identified by 'admin' role admin)", "test");
>>>>>>> develop
    session = remote.open("test", "admin", "admin");
  }

  @Test
  public void test() {
    session.command("update OUser set password='other' where name='admin'").close();
    session.close();
    session = remote.open("test", "admin", "other");
    try (OResultSet result = session.command("select  from OUser  where name='admin'")) {
      assertEquals(result.next().getProperty("name"), "admin");
    }
    session.close();
  }

  @After
  public void after() throws InterruptedException {
<<<<<<< HEAD
    remote.drop("test");
    remote.close();

    server0.shutdown();
    server1.shutdown();
    server2.shutdown();
=======
    try {
      remote.drop("test");
      remote.close();
    } finally {
      setup.teardown();
    }
>>>>>>> develop
    ODatabaseDocumentTx.closeAll();
  }
}
