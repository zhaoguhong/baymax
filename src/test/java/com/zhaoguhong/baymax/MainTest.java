package com.zhaoguhong.baymax;

/**
 * @author guhong
 * @date 2019/5/13
 */
public class MainTest {

  public static void main(String[] args) {
    String hql = "select  dd ? + ?";
    hql = hql.replace("?","?variable");
    for (int i = 1; hql.contains("?variable"); i++) {
      hql =  hql.replaceFirst("\\?variable","?"+i);
    }
    System.out.print(hql);
  }

}
