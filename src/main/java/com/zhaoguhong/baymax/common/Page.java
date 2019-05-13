package com.zhaoguhong.baymax.common;

import java.util.List;

/**
 * 分页
 */
public class Page<T> {

  private int pageNo;// 第几页
  private int pageSize;// 页宽
  private int pageCount;// 一共的页数
  private int totalCount;// 记录条数
  private List<T> entityList;// 查询出来的对象实体

  public Page() {
    pageSize = 20;
    pageNo = 1;
  }

  public Page(int pageNo, int pageSize) {
    super();
    this.pageNo = pageNo < 1 ? 1 : pageNo;
    this.pageSize = pageSize < 1 ? 20 : pageSize;
  }

  public int getPageNo() {
    return pageNo;
  }

  public void setPageNo(int pageNo) {
    this.pageNo = pageNo;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public int getPageCount() {

    return pageCount;
  }

  public void setPageCount(int pageCount) {
    this.pageCount = pageCount;
  }

  public int getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(int totalCount) {
    pageCount = (int) Math.ceil((double) totalCount / pageSize);
    this.totalCount = totalCount;
  }

  public int getFirstEntityIndex() {
    return (pageNo - 1) * pageSize;
  }

  public List<T> getEntityList() {
    return entityList;
  }

  public void setEntityList(List<T> entityList) {
    this.entityList = entityList;
  }

  @Override
  public String toString() {
    return "Page [pageNo=" + pageNo + ", pageSize=" + pageSize + ", pageCount=" + pageCount
        + ", totalCount="
        + totalCount + ", entityList="
        + entityList + "]";
  }

}
