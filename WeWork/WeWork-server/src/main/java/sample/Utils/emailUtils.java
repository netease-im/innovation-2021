package sample.Utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Date;
import java.util.Properties;

public class emailUtils {
    private static final String email="planNote@163.com";
    private static final String password="IBXZQKIMMKDNVLUU";
    private static final String mail_transport_protocol="smtp";
    private static final String mail_smtp_host="smtp.163.com";
    private static final String mail_smtp_auth="true";
    private static final String mail_smtp_ssl="true";
    private static final String mail_smtp_port="465";
    private static final Properties props=new Properties();
    private static final Session session;

    static {
        props.setProperty("mail.transport.protocol",mail_transport_protocol);
        props.setProperty("mail.smtp.host",mail_smtp_host);
        props.setProperty("mail.smtp.auth",mail_smtp_auth);
        props.setProperty("mail.smtp.port",mail_smtp_port);
        props.setProperty("mail.smtp.ssl.enable",mail_smtp_ssl);
        session=Session.getInstance(props);
    }

    public static MimeMessage getMsg(String content,String recipient){
        MimeMessage msg=new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(email));
            msg.setRecipients(Message.RecipientType.TO,recipient);
            msg.setSubject("MyWork:计划进度提醒","utf-8");
            msg.setContent(content,"text/html;charset=utf-8");
            msg.setSentDate(new Date());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public static void sendEmail(String content, String recipient){
        MimeMessage msg=getMsg(content, recipient);
        try {
            Transport trans=session.getTransport();
            trans.connect(email,password);
            trans.sendMessage(msg,msg.getAllRecipients());

            trans.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
