package cn.ilell.manage.service;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import cn.ilell.manage.utils.MyConfig;

/**
 * Created by WSL on 2017/1/9. 发送和接收消息服务
 */
public class MsgService {
	// 输出流
	private static Socket socket;
	private static BufferedReader in = null;
	private static DataOutputStream out = null;

	/**
	 * 连接到服务器
	 */
	public static void connectServer() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					socket = new Socket(MyConfig.SERVER_IP, MyConfig.PORT);
					out = new DataOutputStream(socket.getOutputStream());
					in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 发送消息到服务器
	 * 
	 * @param msg
	 */
	public static void sendMsgToServer(String msg) {
		try {
			if (null != out) {
				out.write(msg.getBytes());
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从服务器读取消息
	 */
	public static void recvMsgFromServer() {
		char[] b = new char[1024];
		int len = 0;
		while (true) {
			try {
				len = in.read(b);
				String str = new String(b, 0, len);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
