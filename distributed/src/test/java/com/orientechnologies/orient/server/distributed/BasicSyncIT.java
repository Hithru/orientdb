package com.orientechnologies.orient.server.distributed;

import static org.junit.Assert.assertEquals;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.setup.*;
import com.orientechnologies.orient.setup.configs.SimpleDServerConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BasicSyncIT {

  private TestSetup setup;
  private SetupConfig config;
  private String server0, server1, server2;

  @Before
  public void before() throws Exception {
    config = new SimpleDServerConfig();
    server0 = SimpleDServerConfig.SERVER0;
    server1 = SimpleDServerConfig.SERVER1;
    server2 = SimpleDServerConfig.SERVER2;
    setup = TestSetupUtil.create(config);
    setup.setup();

    OrientDB remote = setup.createRemote(server0, "root", "test", OrientDBConfig.defaultConfig());
    remote.execute("create database test plocal users(admin identified by 'admin' role admin)");
    remote.close();
  }

  private void waitForDbOnlineStatus(String dbName) throws InterruptedException {
    server0
        .getDistributedManager()
        .waitUntilNodeOnline(server0.getDistributedManager().getLocalNodeName(), dbName);
    server1
        .getDistributedManager()
        .waitUntilNodeOnline(server1.getDistributedManager().getLocalNodeName(), dbName);
    server2
        .getDistributedManager()
        .waitUntilNodeOnline(server2.getDistributedManager().getLocalNodeName(), dbName);
  }

  @Test
  public void sync() throws InterruptedException {
    try (OrientDB remote = setup.createRemote(server0, OrientDBConfig.defaultConfig())) {
      try (ODatabaseSession session = remote.open("test", "admin", "admin")) {
        session.createClass("One");
        session.save(session.newElement("One"));
        session.save(session.newElement("One"));
      }
      setup.shutdownServer(server2);
      try (ODatabaseSession session = remote.open("test", "admin", "admin")) {
        session.save(session.newElement("One"));
      }
    }
<<<<<<< HEAD
    server0.shutdown();
    server1.shutdown();
    // Starting the servers in reverse shutdown order to trigger miss sync
    server0 = OServer.startFromClasspathConfig("orientdb-simple-dserver-config-0.xml");
    server1 = OServer.startFromClasspathConfig("orientdb-simple-dserver-config-1.xml");
    server2 = OServer.startFromClasspathConfig("orientdb-simple-dserver-config-2.xml");
    waitForDbOnlineStatus("test");
    // Test server 0
    try (OrientDB remote = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig())) {
      try (ODatabaseSession session = remote.open("test", "admin", "admin")) {
        assertEquals(session.countClass("One"), 3);
      }
    }
    // Test server 1
    try (OrientDB remote = new OrientDB("remote:localhost:2425", OrientDBConfig.defaultConfig())) {
      try (ODatabaseSession session = remote.open("test", "admin", "admin")) {
        assertEquals(session.countClass("One"), 3);
      }
    }
    // Test server 2
    try (OrientDB remote = new OrientDB("remote:localhost:2426", OrientDBConfig.defaultConfig())) {
      try (ODatabaseSession session = remote.open("test", "admin", "admin")) {
        assertEquals(session.countClass("One"), 3);
      }
    }
  }

  @Test
  public void reverseStartSync()
      throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException,
          InterruptedException {
    try (OrientDB remote = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig())) {
      try (ODatabaseSession session = remote.open("test", "admin", "admin")) {
        session.createClass("One");
        session.save(session.newElement("One"));
        session.save(session.newElement("One"));
      }
      server2.shutdown();
      try (ODatabaseSession session = remote.open("test", "admin", "admin")) {
        session.save(session.newElement("One"));
      }
    }
    server0.shutdown();
    server1.shutdown();
    // Starting the servers in reverse shutdown order to trigger miss sync
    server2 = OServer.startFromClasspathConfig("orientdb-simple-dserver-config-2.xml");
    server1 = OServer.startFromClasspathConfig("orientdb-simple-dserver-config-1.xml");
    server0 = OServer.startFromClasspathConfig("orientdb-simple-dserver-config-0.xml");
    waitForDbOnlineStatus("test");
=======
    setup.shutdownServer(server0);
    setup.shutdownServer(server1);

    setup.startServer(server0);
    setup.startServer(server1);
    setup.startServer(server2);
    TestSetup.waitForDbOnlineStatus(setup, "test");
>>>>>>> develop
    // Test server 0
    try (OrientDB remote = setup.createRemote(server0, OrientDBConfig.defaultConfig())) {
      try (ODatabaseSession session = remote.open("test", "admin", "admin")) {
        assertEquals(session.countClass("One"), 3);
      }
    }
    // Test server 1
    try (OrientDB remote = setup.createRemote(server1, OrientDBConfig.defaultConfig())) {
      try (ODatabaseSession session = remote.open("test", "admin", "admin")) {
        assertEquals(session.countClass("One"), 3);
      }
    }
    // Test server 2
    try (OrientDB remote = setup.createRemote(server2, OrientDBConfig.defaultConfig())) {
      try (ODatabaseSession session = remote.open("test", "admin", "admin")) {
        assertEquals(session.countClass("One"), 3);
      }
    }
  }

  @After
  public void after() {
    try {
      OrientDB remote = setup.createRemote(server0, "root", "test", OrientDBConfig.defaultConfig());
      remote.drop("test");
      remote.close();
    } finally {
      setup.teardown();
      ODatabaseDocumentTx.closeAll();
    }
  }
}
