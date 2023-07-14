package com.orientechnologies.orient.core.storage.cluster.v2;

import com.orientechnologies.orient.core.storage.cache.OCacheEntry;
import com.orientechnologies.orient.core.storage.impl.local.OAbstractPaginatedStorage;
import com.orientechnologies.orient.core.storage.impl.local.paginated.atomicoperations.OAtomicOperation;
<<<<<<< HEAD
import com.orientechnologies.orient.core.storage.impl.local.paginated.atomicoperations.OAtomicOperationsManager;
=======
>>>>>>> develop
import com.orientechnologies.orient.core.storage.impl.local.paginated.base.ODurableComponent;
import com.orientechnologies.orient.core.storage.impl.local.paginated.base.ODurablePage;
import java.io.IOException;

public final class FreeSpaceMap extends ODurableComponent {

  public static final String DEF_EXTENSION = ".fsm";

  static final int NORMALIZATION_INTERVAL =
      (int) Math.floor(ODurablePage.MAX_PAGE_SIZE_BYTES / 256.0);

  private long fileId;

  public FreeSpaceMap(
      OAbstractPaginatedStorage storage, String name, String extension, String lockName) {
    super(storage, name, extension, lockName);
  }

  public boolean exists(final OAtomicOperation atomicOperation) {
    return isFileExists(atomicOperation, getFullName());
  }

  public void create(final OAtomicOperation atomicOperation) throws IOException {
    fileId = addFile(atomicOperation, getFullName());
    init(atomicOperation);
  }

  public void open(final OAtomicOperation atomicOperation) throws IOException {
    fileId = openFile(atomicOperation, getFullName());
  }

  private void init(final OAtomicOperation atomicOperation) throws IOException {
<<<<<<< HEAD
    final OCacheEntry firstLevelCacheEntry = addPage(atomicOperation, fileId);
    try {
      final FreeSpaceMapPage page = new FreeSpaceMapPage(firstLevelCacheEntry);
      page.init();
    } finally {
      releasePageFromWrite(atomicOperation, firstLevelCacheEntry);
=======
    try (final OCacheEntry firstLevelCacheEntry = addPage(atomicOperation, fileId)) {
      final FreeSpaceMapPage page = new FreeSpaceMapPage(firstLevelCacheEntry);
      page.init();
>>>>>>> develop
    }
  }

  public int findFreePage(final int requiredSize) throws IOException {
    final int normalizedSize = requiredSize / NORMALIZATION_INTERVAL + 1;

<<<<<<< HEAD
    final OAtomicOperation atomicOperation = OAtomicOperationsManager.getCurrentOperation();
    final int localSecondLevelPageIndex;
    final OCacheEntry firstLevelEntry = loadPageForRead(atomicOperation, fileId, 0, true);
    try {
=======
    final OAtomicOperation atomicOperation = atomicOperationsManager.getCurrentOperation();
    final int localSecondLevelPageIndex;

    try (final OCacheEntry firstLevelEntry = loadPageForRead(atomicOperation, fileId, 0)) {
>>>>>>> develop
      final FreeSpaceMapPage page = new FreeSpaceMapPage(firstLevelEntry);
      localSecondLevelPageIndex = page.findPage(normalizedSize);
      if (localSecondLevelPageIndex < 0) {
        return -1;
      }
<<<<<<< HEAD
    } finally {
      releasePageFromRead(atomicOperation, firstLevelEntry);
    }

    final int secondLevelPageIndex = localSecondLevelPageIndex + 1;
    final OCacheEntry leafEntry =
        loadPageForRead(atomicOperation, fileId, secondLevelPageIndex, true);
    try {
      final FreeSpaceMapPage page = new FreeSpaceMapPage(leafEntry);
      return page.findPage(normalizedSize)
          + localSecondLevelPageIndex * FreeSpaceMapPage.CELLS_PER_PAGE;
    } finally {
      releasePageFromRead(atomicOperation, leafEntry);
=======
    }

    final int secondLevelPageIndex = localSecondLevelPageIndex + 1;
    try (final OCacheEntry leafEntry =
        loadPageForRead(atomicOperation, fileId, secondLevelPageIndex)) {
      final FreeSpaceMapPage page = new FreeSpaceMapPage(leafEntry);
      return page.findPage(normalizedSize)
          + localSecondLevelPageIndex * FreeSpaceMapPage.CELLS_PER_PAGE;
>>>>>>> develop
    }
  }

  public void updatePageFreeSpace(
      final OAtomicOperation atomicOperation, final int pageIndex, final int freeSpace)
      throws IOException {

    assert pageIndex >= 0;
    assert freeSpace < ODurablePage.MAX_PAGE_SIZE_BYTES;

    final int normalizedSpace = freeSpace / NORMALIZATION_INTERVAL;
    final int secondLevelPageIndex = 1 + pageIndex / FreeSpaceMapPage.CELLS_PER_PAGE;

    final long filledUpTo = getFilledUpTo(atomicOperation, fileId);

    for (int i = 0; i < secondLevelPageIndex - filledUpTo + 1; i++) {
<<<<<<< HEAD
      final OCacheEntry cacheEntry = addPage(atomicOperation, fileId);
      try {
        final FreeSpaceMapPage page = new FreeSpaceMapPage(cacheEntry);
        page.init();
      } finally {
        releasePageFromWrite(atomicOperation, cacheEntry);
=======
      try (final OCacheEntry cacheEntry = addPage(atomicOperation, fileId)) {
        final FreeSpaceMapPage page = new FreeSpaceMapPage(cacheEntry);
        page.init();
>>>>>>> develop
      }
    }

    final int maxFreeSpaceSecondLevel;
    final int localSecondLevelPageIndex = pageIndex % FreeSpaceMapPage.CELLS_PER_PAGE;
<<<<<<< HEAD
    final OCacheEntry leafEntry =
        loadPageForWrite(atomicOperation, fileId, secondLevelPageIndex, true, true);
    try {
      final FreeSpaceMapPage page = new FreeSpaceMapPage(leafEntry);
      maxFreeSpaceSecondLevel =
          page.updatePageMaxFreeSpace(localSecondLevelPageIndex, normalizedSpace);
    } finally {
      releasePageFromWrite(atomicOperation, leafEntry);
    }

    final OCacheEntry firstLevelCacheEntry =
        loadPageForWrite(atomicOperation, fileId, 0, true, true);
    try {
      final FreeSpaceMapPage page = new FreeSpaceMapPage(firstLevelCacheEntry);
      page.updatePageMaxFreeSpace(secondLevelPageIndex - 1, maxFreeSpaceSecondLevel);
    } finally {
      releasePageFromWrite(atomicOperation, firstLevelCacheEntry);
=======
    try (final OCacheEntry leafEntry =
        loadPageForWrite(atomicOperation, fileId, secondLevelPageIndex, true)) {

      final FreeSpaceMapPage page = new FreeSpaceMapPage(leafEntry);
      maxFreeSpaceSecondLevel =
          page.updatePageMaxFreeSpace(localSecondLevelPageIndex, normalizedSpace);
    }

    try (final OCacheEntry firstLevelCacheEntry =
        loadPageForWrite(atomicOperation, fileId, 0, true); ) {
      final FreeSpaceMapPage page = new FreeSpaceMapPage(firstLevelCacheEntry);
      page.updatePageMaxFreeSpace(secondLevelPageIndex - 1, maxFreeSpaceSecondLevel);
>>>>>>> develop
    }
  }

  public void delete(OAtomicOperation atomicOperation) throws IOException {
    deleteFile(atomicOperation, fileId);
  }

  void rename(final String newName) throws IOException {
    writeCache.renameFile(fileId, newName + getExtension());
    setName(newName);
  }
}
