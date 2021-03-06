package httpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class MyServerSocket {
	private String host = "127.0.0.1";
	private int port = 8888;
	public ServerSocket serverSocket;
	
	/*
	serverSocket 专门用于监听。
	当客户端发来一个连接请求的时候，serverSocket就accept()，并创建一个新的 clientSocket = serverSocket.accept();
	这个新的clientSocket用于和客户端进行通信，而原来的serverSocket则继续进行监听。
	*/
	
	public MyServerSocket() {
		try {
			serverSocket = new ServerSocket(port, 5, InetAddress.getByName(host));
			System.out.println(serverSocket.getLocalPort());
			
			// 快速重用socket, 否则端口会被占用一段时间, https://docs.oracle.com/javase/7/docs/api/java/net/StandardSocketOions.html
			serverSocket.setReuseAddress(true);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MyServerSocket testServerSocket = new MyServerSocket();
		testServerSocket.test4();
		testServerSocket.close();
	}
	
	public void test4() {
		System.out.println("start test4---");
		try {
			Socket clientSocket = serverSocket.accept();
			System.out.println("local socket: " + clientSocket.getLocalAddress() + ":" + clientSocket.getLocalPort() + "\t" + clientSocket.getLocalSocketAddress());
			System.out.println("remote socket: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + "\t" + clientSocket.getRemoteSocketAddress());
			
			readFromSocket(clientSocket);
			
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("end test4---");
	}
	
	private void readFromSocket(Socket clientSocket) {
		try {
			InputStream is = clientSocket.getInputStream();
			InputStreamReader isReader = new InputStreamReader(is, Charset.forName("UTF-8"));
			BufferedReader bufReader = new BufferedReader(isReader);
			
			String line = "";				
			while( true ) {
				line = bufReader.readLine(); // readLine会自动去掉\r\n, 相当于rstrip("\r\n")
				if( line.equals("q") == true ) {
					break;
				}else {
					System.out.println(line);
				}
			}
			bufReader.close();
			isReader.close();
			is.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Socket getClientSocket() {
		try {
			return serverSocket.accept();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void close() {
		try {
			serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
