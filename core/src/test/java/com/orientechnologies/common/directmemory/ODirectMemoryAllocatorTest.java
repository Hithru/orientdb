package com.orientechnologies.common.directmemory;

import com.orientechnologies.common.directmemory.ODirectMemoryAllocator.Intention;
import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import java.nio.ByteBuffer;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ODirectMemoryAllocatorTest {
  @BeforeClass
  public static void beforeClass() {
    OGlobalConfiguration.DIRECT_MEMORY_TRACK_MODE.setValue(true);
  }

  @AfterClass
  public static void afterClass() {
    OGlobalConfiguration.DIRECT_MEMORY_TRACK_MODE.setValue(false);
  }

  @Test
  public void testAllocateDeallocate() {
    final ODirectMemoryAllocator directMemoryAllocator = new ODirectMemoryAllocator();
<<<<<<< HEAD
    final OPointer pointer = directMemoryAllocator.allocate(42, -1, false, Intention.TEST);
=======
    final OPointer pointer = directMemoryAllocator.allocate(42, false, Intention.TEST);
>>>>>>> develop
    Assert.assertNotNull(pointer);

    Assert.assertEquals(42, directMemoryAllocator.getMemoryConsumption());

    final ByteBuffer buffer = pointer.getNativeByteBuffer();
    Assert.assertEquals(42, buffer.capacity());
    directMemoryAllocator.deallocate(pointer);

    Assert.assertEquals(0, directMemoryAllocator.getMemoryConsumption());
  }

  @Test
  public void testNegativeOrZeroIsPassedToAllocate() {
    final ODirectMemoryAllocator directMemoryAllocator = new ODirectMemoryAllocator();
    try {
<<<<<<< HEAD
      directMemoryAllocator.allocate(0, -1, false, Intention.TEST);
=======
      directMemoryAllocator.allocate(0, false, Intention.TEST);
>>>>>>> develop
      Assert.fail();
    } catch (IllegalArgumentException e) {
      Assert.assertTrue(true);
    }

    try {
<<<<<<< HEAD
      directMemoryAllocator.allocate(-1, -1, false, Intention.TEST);
=======
      directMemoryAllocator.allocate(-1, false, Intention.TEST);
>>>>>>> develop
      Assert.fail();
    } catch (IllegalArgumentException e) {
      Assert.assertTrue(true);
    }
  }

  @Test
  public void testNullValueIsPassedToDeallocate() {
    final ODirectMemoryAllocator directMemoryAllocator = new ODirectMemoryAllocator();
    try {
      directMemoryAllocator.deallocate(null);
      Assert.fail();
    } catch (IllegalArgumentException e) {
      Assert.assertTrue(true);
    }
  }
}
