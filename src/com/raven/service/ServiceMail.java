    package com.raven.service;

    import com.raven.model.ModelMessage;
    import java.util.Properties;
    import javax.mail.Message;
    import javax.mail.MessagingException;
    import javax.mail.PasswordAuthentication;
    import javax.mail.Session;
    import javax.mail.Transport;
    import javax.mail.internet.InternetAddress;
    import javax.mail.internet.MimeMessage;

    public class ServiceMail {

        public ModelMessage sendMain(String toEmail, String code) {
            ModelMessage ms = new ModelMessage(false, "");
            String from = "eswasthya724@gmail.com";
            Properties prop = new Properties();
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", "587");
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.starttls.enable", "true");
            String username = "eswasthya724@gmail.com";
            String password = "ydzl uahk ucnt qich";    //  Your email password here
            Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
                message.setSubject("OTP Verification");
                message.setText("Hello user,\nThank you for joining E-swasthya! To enhance the security of your account and provide you personalized health insights, we require a quick verification.\nPlease enter the OTP provided below on the login page to verify your identity. If you haven't received the email, kindly check your spam folder.\nOTP: " + code + "\nThank you for choosing E-swasthya for your wellness journey.\nBest regards,\nTeam E-swasthya");
    ;
                Transport.send(message);
                ms.setSuccess(true);
            } catch (MessagingException e) {
                if (e.getMessage().equals("Invalid Addresses")) {
                    ms.setMessage("Invalid email");
                } else {
                    ms.setMessage("Error");
                    e.printStackTrace();
                    
                }
            }
            return ms;
        }
    }
