package com.orientechnologies.common.directmemory;

import com.kenai.jffi.MemoryIO;
import com.orientechnologies.common.directmemory.ODirectMemoryAllocator.Intention;
<<<<<<< HEAD
import java.lang.ref.WeakReference;
=======
import java.lang.ref.SoftReference;
>>>>>>> develop
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class OPointer {

  private final long pointer;
  private final int size;
  private final Intention intention;

<<<<<<< HEAD
  private WeakReference<ByteBuffer> byteBuffer;
=======
  private SoftReference<ByteBuffer> byteBuffer;
>>>>>>> develop
  private int hash = 0;

  OPointer(long pointer, int size, Intention intention) {
    this.pointer = pointer;
    this.size = size;
    this.intention = intention;
  }

  public void clear() {
    MemoryIO.getInstance().setMemory(pointer, size, (byte) 0);
  }

  public ByteBuffer getNativeByteBuffer() {
    ByteBuffer buffer;
    if (byteBuffer == null) {
      buffer = createNativeBuffer();
<<<<<<< HEAD
      byteBuffer = new WeakReference<>(buffer);
=======
      byteBuffer = new SoftReference<>(buffer);
>>>>>>> develop
    } else {
      buffer = byteBuffer.get();
      if (buffer == null) {
        buffer = createNativeBuffer();
<<<<<<< HEAD
        byteBuffer = new WeakReference<>(buffer);
=======
        byteBuffer = new SoftReference<>(buffer);
>>>>>>> develop
      }
    }

    return buffer;
  }

  long getNativePointer() {
    return pointer;
  }

  int getSize() {
    return size;
  }

  Intention getIntention() {
    return intention;
  }

  private ByteBuffer createNativeBuffer() {
    return MemoryIO.getInstance().newDirectByteBuffer(pointer, size).order(ByteOrder.nativeOrder());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    OPointer pointer1 = (OPointer) o;

    if (pointer != pointer1.pointer) {
      return false;
    }
    return size == pointer1.size;
  }

  @Override
  public int hashCode() {
    if (hash != 0) {
      return hash;
    }

    int result = (int) (pointer ^ (pointer >>> 32));
    result = 31 * result + size;

    hash = result;
    return hash;
  }
}
