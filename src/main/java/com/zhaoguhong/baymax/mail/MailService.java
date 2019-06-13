package com.zhaoguhong.baymax.mail;

import java.util.Map;

/**
 * 邮件发送
 *
 */
public interface MailService {

  /**
   * 发送邮件
   *
   * @param mailModel 邮件相关配置
   */
  void sendMail(MailModel mailModel);


  /**
   * 发送简单的邮件
   *
   * @param to 发送到的邮箱地址
   * @param subject 邮件标题
   * @param content 邮件内容
   */
  void sendSimleMail(String to, String subject, String content);

  /**
   * 发送html格式的邮件
   *
   * @param to 发送到的邮箱地址
   * @param subject 邮件标题
   * @param content 邮件内容(html格式)
   */
  void sendHtmlMail(String to, String subject, String content);

  /**
   * 发送带附件的邮件
   *
   * @param to 发送到的邮箱地址
   * @param subject 邮件标题
   * @param content 邮件内容
   * @param content 附件路径
   */
  void sendAttachmentMail(String to, String subject, String content, String path);

  /**
   * 发送带附件的html格式邮件
   *
   * @param to 发送到的邮箱地址
   * @param subject 邮件标题
   * @param content 邮件内容(html格式)
   * @param content 附件路径
   */
  void sendAttachmentHtmlMail(String to, String subject, String content, String path);
  
  /**
   * 根据模版发送简单邮件
   * @param to 收件邮箱地址
   * @param subject 邮件标题
   * @param templateName 模版名称
   * @param params 参数
   */
  void sendMailByTemplate(String to, String subject, String templateName,
      Map<String, Object> params);
  
}
