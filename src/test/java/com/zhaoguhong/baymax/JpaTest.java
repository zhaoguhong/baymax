package com.zhaoguhong.baymax;

import com.zhaoguhong.baymax.common.Page;
import com.zhaoguhong.baymax.demo.entity.Demo;
import com.zhaoguhong.baymax.demo.repository.DemoRepository;
import com.zhaoguhong.baymax.jpa.JpaDao;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class JpaTest {

  @PersistenceContext
  private EntityManager entityManager;

  @Resource
  private JpaDao jpaDao;

  @Resource
  private DemoRepository demoRepository;

  @Test
  public void testSpringDataJpa() {
    System.out.println("--------------");
    Demo demo = new Demo();
    demo.setUserName("baymax");
    demo.setAge(10);
    demo.setTitle("test");
    demoRepository.saveEntity(demo);
    log.info("测试新增:" + demo);
    log.info("测试getOne:" + demoRepository.getOne(1L));
    log.info("测试findByUserName:" + demoRepository.findByUserName("baymax"));
  }

  @Test
  public void testJpaDao() {
    System.out.println("--------------");
    log.info("测试get:" + jpaDao.get(Demo.class, 1L));
    String jpql = "from Demo where id =?";
    log.info("测试find占位符:" + jpaDao.find(jpql, 1L));

    jpql = "from Demo where id =:id";
    Map<String, Object> param = new HashMap<>();
    param.put("id", 1L);
    log.info("测试find命名参数:" + jpaDao.find(jpql, param));

    jpql = "from Demo ";
    Page page = new Page<Demo>();
    log.info("测试分页:" + jpaDao.find(page, jpql, new HashMap<>()));
  }


}

