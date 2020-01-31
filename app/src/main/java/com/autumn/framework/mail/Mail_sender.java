package com.autumn.framework.mail;
import com.autumn.framework.Log.LogUtil;

public class Mail_sender
{
	
	public static void sendMessage(final String title , final String msg) {

		/*****************************************************/
		LogUtil.i("开始发送邮件");
		// 这个类主要是设置邮件
		new Thread(new Runnable() {

				@Override
				public void run() {
					//send_status.init();
					// TODO Auto-generated method stub
					MailSenderInfo mailInfo = new MailSenderInfo();
					mailInfo.setMailServerHost("smtp.163.com");
					mailInfo.setMailServerPort("25");
					mailInfo.setValidate(true);
					mailInfo.setUserName("meternitycn@163.com");
					mailInfo.setPassword("hOng000814");// 您的邮箱密码
					mailInfo.setFromAddress("meternitycn@163.com");
					mailInfo.setToAddress("544901005@qq.com");
					mailInfo.setSubject(title);
					mailInfo.setContent(msg);
					// 这个类主要来发送邮件
					SimpleMailSender sms = new SimpleMailSender();
					boolean isSuccess = sms.sendTextMail(mailInfo);// 发送文体格式
					// sms.sendHtmlMail(mailInfo);//发送html格式
					//LogUtil.i(isSuccess+"");
					send_status info = new send_status();
					if (isSuccess) {
						info.send_success();
						//LogUtil.i("发送成功");
						return;
					} else {
						info.send_fail();
						//LogUtil.i("发送失败");
						return;
					}
				}
			}).start();
			
			
			}
			
			
}
