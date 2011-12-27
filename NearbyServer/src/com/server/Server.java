package com.server;

import java.io.*;
import java.net.*;
import java.util.*;

import com.data.*;

public class Server {
	public final static int ECHO_PORT = 6000;
	private ServerSocket serverSock;	//메인 서버소켓
	private Socket clientSock;			//새로운 클라이언트를 받을 소켓
	public static HashMap<String, UserInfo> allUserInfo = new HashMap<String, UserInfo>();
	public static HashMap<String, RoomInfo> allRoomInfo = new HashMap<String, RoomInfo>();

	public static void main(String[] args) {
		Server server = new Server();
	}
	
	public Server() {
		try {
			//서버소켓을 생성하여 ECHO_PORT번 포트와 결합(bind)시킨다.
			serverSock = new ServerSocket(ECHO_PORT);
			System.out.println("***** Server Open *****");
			
			try {
				while (true) {
					System.out.println("- Server is listening......");
					clientSock = serverSock.accept();
					System.out.println("- Client is found!");
					
					//클라이언트와 연결된 소켓(clientSock)을 파라메터로 담아 클라이언트와 통신할 스레드(client) 실행
					ConThread client = new ConThread(clientSock);
					client.start();
				}
			} catch (IOException e) {
				clientSock.close();
				System.out.println("Client socket is died: " + e);
			} catch (NullPointerException e) {
				clientSock.close();
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}	
}