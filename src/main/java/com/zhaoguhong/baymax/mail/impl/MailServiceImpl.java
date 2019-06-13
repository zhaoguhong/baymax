package com.zhaoguhong.baymax.mail.impl;

import com.zhaoguhong.baymax.mail.MailModel;
import com.zhaoguhong.baymax.mail.MailService;
import com.zhaoguhong.baymax.util.FreeMarkerUtil;
import java.io.File;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * 邮件发送
 *
 */
@Service
@Slf4j
public class MailServiceImpl implements MailService {

  @Autowired
  private JavaMailSender mailSender;

  @Value("${mail.sender:}")
  private String sender;

  @Override
  public void sendMail(MailModel mailModel) {
    MimeMessage message = mailSender.createMimeMessage();
    try {
      // true表示需要创建一个multipart message
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setFrom(sender);
      helper.setTo(mailModel.getReceiver());
      helper.setSubject(mailModel.getSubject());
      if (StringUtils.isNotEmpty(mailModel.getContent())) {
        helper.setText(mailModel.getContent());
      } else {
        helper.setText(mailModel.getHtmlContent(), true);
      }
      for (Map.Entry<String, File> entry : mailModel.getFiles().entrySet()) {
        helper.addAttachment(entry.getKey(), entry.getValue());
      }
      mailSender.send(message);
    } catch (MessagingException e) {
      log.error("发送邮件异常：{}", e);
    }
  }

  @Override
  public void sendSimleMail(String to, String subject, String content) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(sender);
    message.setTo(to);
    message.setSubject(subject);
    message.setText(content);
    mailSender.send(message);
  }

  @Override
  public void sendHtmlMail(String to, String subject, String content) {
    MailModel mailModel = new MailModel();
    mailModel.setReceiver(to);
    mailModel.setSubject(subject);
    mailModel.setHtmlContent(content);
    sendMail(mailModel);
  }

  @Override
  public void sendAttachmentMail(String to, String subject, String content, String filePath) {
    MailModel mailModel = new MailModel();
    mailModel.setReceiver(to);
    mailModel.setSubject(subject);
    mailModel.setContent(content);
    mailModel.addFile(filePath);
    sendMail(mailModel);
  }

  @Override
  public void sendAttachmentHtmlMail(String to, String subject, String content, String filePath) {
    MailModel mailModel = new MailModel();
    mailModel.setReceiver(to);
    mailModel.setSubject(subject);
    mailModel.setHtmlContent(content);
    mailModel.addFile(filePath);
    sendMail(mailModel);
  }

  @Override
  public void sendMailByTemplate(String to, String subject, String templateName,
      Map<String, Object> params) {
    String content = FreeMarkerUtil.generate(templateName, params);
    this.sendHtmlMail(to, subject, content);
  }

}
