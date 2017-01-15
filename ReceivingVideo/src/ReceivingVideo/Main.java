package ReceivingVideo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
	public static final String filename = "F:\\Video\\";
	public static Date dNow;
	public static SimpleDateFormat ft = new SimpleDateFormat("yyyy_MM_dd'at'hh_mm_ss_a");
	static int port = 6668;
	// static Socket socket;
	static ServerSocket serverSocket;

	

	public static void main(String arg[]) {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (true) {

			Socket socket;
			try {
				socket = serverSocket.accept();
				InputStream in = socket.getInputStream();
				new Thread(new Runnable() {
					@Override
					public void run() {
						dNow = new Date();
						File file = new File(filename + ft.format(dNow) + ".mp4");
						FileOutputStream fos = null;
						try {
							fos = new FileOutputStream(file);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						try {
							byte[] buffer = new byte[16 * 1024];
							int count;
							while ((count = in.read(buffer)) > 0) {
								fos.write(buffer, 0, count);
							}
							fos.close();
							socket.close();
						} catch (IOException ex) {
							try {
								if (fos != null) fos.close();
								if (socket != null) socket.close();
							} catch (IOException ex1) {
								ex1.printStackTrace();
							}
							ex.printStackTrace();
						}
					}

				}).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}