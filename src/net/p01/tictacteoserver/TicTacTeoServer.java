package net.p01.tictacteoserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TicTacTeoServer {
	final static int PORT = 9000;
	public static String[] parsePacket(String line) {
		String[] params = line.split("\\|");
		return params;
	}
	
	public static void main(String[] args) {
		int[] maps= {-1,-1,-1,-1,-1,-1,-1,-1,-1};//맵초기화
		try {
			//소켓 생성, 스트림 개방
			ServerSocket serverSocket = new ServerSocket(PORT);
			
			System.out.println("Wait client....");
			Socket conSocket = serverSocket.accept();
			
			InetAddress inetAddr = conSocket.getInetAddress();
			System.out.println("Connect "+ inetAddr.getHostAddress());
			
			OutputStream out =conSocket.getOutputStream();
			OutputStreamWriter outW = new OutputStreamWriter(out);
			PrintWriter pw = new PrintWriter(outW);
			
			InputStream in = conSocket.getInputStream();
			InputStreamReader inR = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(inR); // 자동적으로 개행문자를 기준으로 데이터를 보냄.
			
			while(true) {
				//클라이언트가 보내는 전체 패킷을 수신
				String line = br.readLine();
				if(line == null) {
					System.out.println("Disconnect Client!");
					break;
				}
				System.out.println("Recieved Data : "+line);
				String[] params = parsePacket(line);
				System.out.println(params[0]);
				if (params[0].equals("S")) {
					//params[1]의 이름으로 파일출력
					String filename=params[1];
					System.out.println("Save File :"+filename);
					Writer wOut=new FileWriter(filename);
					BufferedWriter bOut = new BufferedWriter(wOut);
					PrintWriter pOut = new PrintWriter(bOut);
					pOut.println(params[2]);
					pOut.close();
					System.out.println("File Saved!");
					pw.println();// 클라이언트로 결과를 전송
					pw.flush();
				}else if(params[0].equals("L")) {
					//params[1]의 이름으로 파일 입력.
					try {
					Reader rIn= new FileReader(params[1]);
					BufferedReader bIn = new BufferedReader(rIn);
					String logs=bIn.readLine();
					System.out.println(logs);
					String packet=String.format("L|%s",logs);
					pw.println(packet);// 클라이언트로 결과를 전송
					pw.flush();
					}catch(IOException e) {
						String packet=String.format("E");
						pw.println(packet);// 클라이언트로 결과를 전송
						pw.flush();
					}
					System.out.println("Send Logs");
				}
				
				
			}
			pw.close();
			br.close();
					
		} catch (IOException e) {
			System.out.println("Client is Killed!");
		}
	}
}