package com.orientechnologies.orient.server.distributed;

<<<<<<< HEAD:distributed/src/main/java/com/orientechnologies/orient/server/distributed/impl/ODistributedResponseManagerFactory.java
import com.orientechnologies.orient.server.distributed.ODistributedRequest;
import com.orientechnologies.orient.server.distributed.ODistributedResponseManager;
=======
>>>>>>> develop:server/src/main/java/com/orientechnologies/orient/server/distributed/ODistributedResponseManagerFactory.java
import com.orientechnologies.orient.server.distributed.task.ORemoteTask;
import java.util.Collection;
import java.util.Set;

public interface ODistributedResponseManagerFactory {

  ODistributedResponseManager newResponseManager(
      ODistributedRequest iRequest,
      Collection<String> iNodes,
      ORemoteTask task,
      Set<String> nodesConcurToTheQuorum,
      int availableNodes,
      int expectedResponses,
      int quorum,
      boolean groupByResponse,
      boolean waitLocalNode);
}
