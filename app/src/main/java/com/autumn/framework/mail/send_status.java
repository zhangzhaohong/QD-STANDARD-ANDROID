package com.autumn.framework.mail;
public class send_status
{
	
	public static int send_status;

	public static int send_status(){return send_status;};
	
	public static void init(){
		send_status = 0;
	}
	
	public static void send_success(){
		send_status = 1;
	}
	
	public static void send_fail(){
		send_status = 2;
	}
	
}
