package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private static final int port = 1378;//PORT

	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			int clients = 0;
			Socket client;
			OneConnection connection;
			while (true) {
				client = serverSocket.accept();
				connection = new OneConnection(client);
				connection.start();
				clients++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
