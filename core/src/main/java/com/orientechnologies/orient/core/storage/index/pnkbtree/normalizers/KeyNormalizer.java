package com.orientechnologies.orient.core.storage.index.pnkbtree.normalizers;

public interface KeyNormalizer {
  int normalizedSize(Object key);

  int normalize(final Object key, final int offset, final byte[] stream);
}