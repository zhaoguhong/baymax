package com.zhaoguhong.baymax;

import com.zhaoguhong.baymax.mail.MailService;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class EmailTest {

  @Autowired
  private MailService mailService;

  private String toMail = "aaa@qq.com";

  @BeforeEach
  public void init() {
//    JavaMailSenderImpl sender = ContextHolder.getBean("mailSender");
//    //设置邮箱主机,163邮箱为smtp.163.com，qq为smtp.qq.com
//    sender.setHost("smtp.qq.com");
//    sender.setUsername("aaaaaa@qq.com");
//    // 授权码
//    sender.setPassword("11111111");
  }

  /**
   * 测试一个简单的邮件发送
   */
  @Test
  public void testSimpleMail() {
    mailService.sendSimleMail(toMail, "测试", "测试内容");
  }

  @Test
  public void testHtmlMail() {
    String content = "<html>\n" +
        "<body>\n" +
        "    <h3>hello world ! 这是一封html邮件!</h3>\n" +
        "</body>\n" +
        "</html>";
    mailService.sendHtmlMail(toMail, "test html mail", content);
  }

  @Test
  public void testAttachmentMail() {
    String content = "<html>\n" +
        "<body>\n" +
        "    <h3>hello world ! 这是一封带附件的html邮件!</h3>\n" +
        "</body>\n" +
        "</html>";
    mailService.sendAttachmentMail(toMail, "test html mail", content,
        "/Users/my/Downloads/test.xlsx");
  }

  @Test
  public void testMailByTemplate() {
    Map<String, Object> emailMap = new HashMap<>();
    emailMap.put("name", "baymax");
    mailService.sendMailByTemplate(toMail, "测试哈", "test.ftl", emailMap);

  }

}

