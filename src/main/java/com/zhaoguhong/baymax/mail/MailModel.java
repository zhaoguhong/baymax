package com.zhaoguhong.baymax.mail;

import com.google.common.collect.Maps;
import java.io.File;
import java.util.Map;
import lombok.Data;

@Data
public class MailModel {

    /**
     * 接收人
     */
    private String receiver;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String Content;

    /**
     * html格式邮件内容
     */
    private String htmlContent;

    /**
     * html格式邮件内容
     */
    private Map<String, File> files = Maps.newLinkedHashMap();

    public void addFile(File file) {
        addFile(file.getPath());
    }

    public void addFile(String filePath) {
        String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
        files.put(fileName, new File(filePath));
    }

}
