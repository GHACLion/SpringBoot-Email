package com.lion.mail.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * 邮件处理器类，所有接口已通过测试
 */
@RestController
@RequestMapping(value = "/email")
public class EmailController {

    @Resource
    private JavaMailSender jms;

    /**
     * 作为资源使用
     * 从配置文件中获取邮件发送者的信息
     */
    @Value("${spring.mail.username}")
    private String from;

    @Resource
    private TemplateEngine templateEngine;

    /**
     * 发送通用格式的邮件
     * @return
     */
    @RequestMapping(value = "/sendSimpleEmail")
    public String sendSimpleEmail(){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            //把邮件发送者信息放入邮件内容
            message.setFrom(from);
            //设置收件人邮箱, 群发多个收件人时，使用String[] 数组
            message.setTo("2453256688@qq.com");
            //设置邮件标题
            message.setSubject("一封来自良哥的简单邮件");
            //设置内容
            message.setText("使用Spring Boot 发送的简单邮件。");
            //调用Java的邮件发送器
            jms.send(message);
            return "发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * 发送 HTML 格式的邮件
     * @return
     */
    @RequestMapping(value = "/sendHtmlEmail")
    public String sendHtmlEmail(){
        MimeMessage message = null;
        try {
            message = jms.createMimeMessage();
            // 创建 MimeMessageHelper 对象时，会抛出异常，处理方法有两种，向上抛出或捕获，这里使用 try/catch捕获
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            //把邮件发送者信息放入邮件内容
            helper.setFrom(from);
            //设置收件人邮箱, 群发多个收件人时，使用String[] 数组
            helper.setTo("2453256688@qq.com");
            //设置邮件标题
            helper.setSubject("一封来自良哥的HTML格式邮件");
            //带HTML格式的内容
            StringBuffer sb = new StringBuffer("<p style='color:#42b983'>使用Spring Boot发送HTML格式邮件。</p>");
            helper.setText(sb.toString(), true);
            //调用Java的邮件发送器
            jms.send(message);
            return "发送成功";
        } catch (Exception e) {
            e.printStackTrace();
           return e.getMessage();
        }
    }

    /**
     * 发送带附件的邮件
     * @return
     */
    @RequestMapping(value = "/sendAttachmentsMail")
    public String sendAttachmentsMail(){
        MimeMessage message = null;
        try {
            message = jms.createMimeMessage();
            // 创建 MimeMessageHelper 对象时，会抛出异常，处理方法有两种，向上抛出或捕获，这里使用 try/catch捕获
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            // 把邮件发送者信息放入邮件内容
            helper.setFrom(from);
            //设置收件人邮箱, 群发多个收件人时，使用String[] 数组
            helper.setTo("2453256688@qq.com");
            //设置邮件标题
            helper.setSubject("一封来自良哥的带附件邮件");
            //设置内容
            helper.setText("详情见附件内容");
            //传入附件：注意：附件的地址填写文件在计算机上的目录地址
            FileSystemResource file = new FileSystemResource(new File("E:/MyProject/MySpringAll/22.SpringBoot-Email/src/main/resources/static/file/项目文档.docx"));
            helper.addAttachment("项目文档.docx", file);
            //调用Java的邮件发送器
            jms.send(message);
            return "发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * 发送附带静态资源的邮件
     * @return
     */
    @RequestMapping(value = "/sendInlineMail")
    public String sendInlineMail(){
        MimeMessage message = null;
        try {
            message = jms.createMimeMessage();
            // 创建 MimeMessageHelper 对象时，会抛出异常，处理方法有两种，向上抛出或捕获，这里使用 try/catch捕获
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            // 把邮件发送者信息放入邮件内容
            helper.setFrom(from);
            //设置收件人邮箱, 群发多个收件人时，使用String[] 数组
            helper.setTo("2453256688@qq.com");
            //设置邮件标题
            helper.setSubject("一封来自良哥的带静态资源的邮件");
            //设置内容
            helper.setText("<html><body>博客背景图：<img src='cid:img'/></body></html>", true);
            //传入附件
            FileSystemResource file = new FileSystemResource(new File("E:/MyProject/MySpringAll/22.SpringBoot-Email/src/main/resources/static/img/sunshine.png"));
            //传入静态资源
            helper.addInline("img", file);
            //调用Java的邮件发送器
            jms.send(message);
            return "发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * 发送模板邮件
     * @param code
     * @return
     */
    @RequestMapping(value = "/sendTemplateEmail")
    public String sendTemplateEmail(String code){
        MimeMessage message = null;
        try {
            message = jms.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo("2453256688@qq.com");
            helper.setSubject("一封来自良哥的模板邮件");
            //处理邮件模板
            Context context = new Context();
            context.setVariable("code", code);
            String template = templateEngine.process("emailTemplate", context);
            helper.setText(template);
            jms.send(message);
            return "发送成功";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
