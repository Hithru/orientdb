package com.orientechnologies.orient.core.storage.index.sbtreebonsai.global.btree;

public final class PagePathItemUnit {

  private final long pageIndex;
  private final int itemIndex;

  public PagePathItemUnit(final long pageIndex, final int itemIndex) {
    this.pageIndex = pageIndex;
    this.itemIndex = itemIndex;
  }

  public long getPageIndex() {
    return pageIndex;
  }

  public int getItemIndex() {
    return itemIndex;
  }
}
