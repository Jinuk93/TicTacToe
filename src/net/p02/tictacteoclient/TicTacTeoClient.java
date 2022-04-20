package net.p02.tictacteoclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class TicTacTeoClient {
   final static String IP="127.0.0.1";
   final static int PORT = 9000;
   public static int gameWin(int[] maps) {//-1�̸� ���ھ���, �ƴϸ� �ش� ĭ���ڰ� ����. �ϵ��ڵ�~
      int result=-1;
      if (maps[0]!=-1 && maps[0]==maps[1] && maps[1]==maps[2])
         result= maps[0];
      else if(maps[3]!=-1 && maps[3]==maps[4]&&maps[4]==maps[5])
         result = maps[3];
      else if (maps[6] != -1 &&maps[6]==maps[7]&& maps[7]==maps[8])
         result = maps[6];
      else if (maps[0] != -1 && maps[0]==maps[3] && maps[3]==maps[6])
         result = maps[0];
      else if (maps[1]!= -1 && maps[1]==maps[4]&& maps[4]==maps[7])
         result = maps[1];
      else if (maps[2]!= -1 && maps[2]==maps[5]&& maps[5]==maps[8])
         result =maps[2];
      else if (maps[0]!= -1 && maps[0]==maps[4]&&maps[4]==maps[8])
         result = maps[0];
      else if (maps[2]!=-1 && maps[2]==maps[4]&& maps[4]==maps[6])
         result = maps[2];
      
      return result;
   }
   public static String[] parsePacket(String line) {
      String[] params = line.split("\\|");
      return params;
   }
   public static void replay(String logs) {
	   char[] cmap= {'0','|','1','|','2','\n','3','|','4','|','5','\n','6','|','7','|','8'};
	   char[] log=logs.toCharArray();
  	 System.out.println("-------------------");
	   System.out.println(cmap);
	   for(int i=0;i<logs.length();i++) {
		   int p=log[i]-'0';
		   cmap[2*p]=(i%2==0)?'O':'X';
		   System.out.printf("-- turn: %d --\n",i);
		   System.out.println(cmap);
		   System.out.println("-------------");
	   }
  	 System.out.println("-------------------");
	 System.out.println("\n    Replay End!\n");
  	 System.out.println("-------------------");
   }
   public static void main(String[] args) {
      try {
         //��Ʈ�� ����
         Socket socket = new Socket(IP,PORT);
         
         InputStreamReader ink = new InputStreamReader(System.in);
         BufferedReader keyboard = new BufferedReader(ink);
         
         OutputStream out = socket.getOutputStream();
         OutputStreamWriter outW = new OutputStreamWriter(out);
         PrintWriter pw = new PrintWriter(outW);
         
         InputStream in = socket.getInputStream();
         InputStreamReader inR = new InputStreamReader(in);
         BufferedReader br = new BufferedReader(inR);
         //�� �ʱ�ȭ
         boolean loop1=false;
         String packet;
         String line;
         while(true) {
        	 System.out.println("-------------------");
            System.out.println("        MENU");
            System.out.println("-------------------");
            System.out.println("1. New game");
            System.out.println("2. Replay game");
            System.out.println("3. Quit");
            System.out.println("-------------------");
            System.out.print("Input menu (1~3)>> ");
            int menu=-1;
            String me = keyboard.readLine();
            System.out.println('\n');
            try {
               menu=Integer.parseInt(me);
            }catch(NumberFormatException e) {
               System.out.println("���ڰ� �ƴմϴ�. ������ ���ڸ� �־��ּ���.");
               continue;
            }
            if(menu>3 ||menu<1) {
               System.out.println("������ ���ڸ� �־��ּ���.");
               continue;
            }
            if(menu==1) {
            //�Է¹ޱ�
            	String logs="";
               int[] maps= {-1,-1,-1,-1,-1,-1,-1,-1,-1};
               int turn=0;
               int player=0;
               char[] cmapd= {'0','|','1','|','2','\n','3','|','4','|','5','\n','6','|','7','|','8'};
               char[] cmap=cmapd.clone();
               System.out.println("Player 0's Turn!\n");
               System.out.println(cmap);
               while(true) {
                  System.out.print("input position (0~8)>>  ");
                  String cmd = keyboard.readLine();
                  int num=-1;
                  try {//�Է¿��� : ���ڰ� �ƴѰ��� �ԷµȰ��, quit�� ���ܸ� ���� ���� �ξ���.
                     num = Integer.parseInt(cmd);
                  }catch(NumberFormatException e) {
                     System.out.println("���ڰ� �ƴմϴ�. ������ ���ڸ� �־��ּ���.");
                     continue;
                  }
                  if (num<0|| num>8) {//0~8������ ��ȣ����, ���� ������ ����� �޼����� ������.
                     System.out.println("���ڰ� ���������� ������ϴ�. �ٽ� �Է��� �ֽʽÿ�.");
                     continue;
                  }
                  if (maps[num]!=-1) {// �̹� �ٸ� ����� ���� ������ ���.
                     System.out.println("�� ������ �ƴմϴ�. �ٽ� �Է��� �ֽʽÿ�.");
                     continue;
                  }

                  logs+=cmd;
                  maps[num]=player;
                  cmap[num*2]=(maps[num]==0)?'O':'X';
                  System.out.println(cmap);
                  int winner=gameWin(maps);
                  if(turn!=8 &&winner==-1) {
                     turn++;
                     player=turn%2;
                  }else {
                	  System.out.println("-------------------");
                     if(winner!=-1) {
                        System.out.printf("Player %d's Win!\n",player);
                     }else {
                        System.out.println("Draw!");
                     }
                     System.out.println("-------------------");
                     System.out.println("-------------------");
                     System.out.println("        MENU");
                     System.out.println("-------------------");
                     System.out.println("1. Save Replay And Back to Main");
                     System.out.println("2. New Game");
                     System.out.println("3. Back to Main");
                     System.out.println("4. Quit");
                     System.out.println("-------------------");
                     System.out.print("Input >> ");
                     while(true) {
	                     cmd = keyboard.readLine();
	                     try {//�Է¿��� : ���ڰ� �ƴѰ��� �ԷµȰ��, quit�� ���ܸ� ���� ���� �ξ���.
	                        num = Integer.parseInt(cmd);
	                     }catch(NumberFormatException e) {
	                        System.out.println("���ڰ� �ƴմϴ�. ������ ���ڸ� �־��ּ���.");
	                        continue;
	                     }
	                     if (num<0|| num>4) {//0~8������ ��ȣ����, ���� ������ ����� �޼����� ������.
	                        System.out.println("���ڰ� ���������� ������ϴ�. �ٽ� �Է��� �ֽʽÿ�.");
	                        continue;
	                     }
	                     break;
                     }//�޴��� 2�� �޾ƿ�
                     
                     if(num==1) {
                    	 //save
                    	 System.out.print("Save File Name Input >> ");
                    	 cmd = keyboard.readLine();
                    	 packet = String.format("S|%s|%s", cmd,logs);
                    	 pw.println(packet);
                         pw.flush();
                         System.out.println("Saving....");
                         line=br.readLine();
                         System.out.println("Saved!\n\n");
                         break;
                     }else if (num==2){
                    	 logs="";
                    	 Arrays.fill(maps, -1);
                    	 turn=0;
                    	 cmap=cmapd.clone();
                    	 player=0;
                    	 System.out.println("Player 0's Turn!\n");
                         System.out.println(cmap);
                    	 continue;
                     }else if(num==3) {
                    	 System.out.println("\n");
                    	 break;
                     }else {
                    	 loop1=true;
                    	 break;
                     }
                  }
               }
            }else if(menu==2) {
               //�ε��� ���� �����ͷ� ����
            	while(true) {
            		System.out.print("Pleas Input File Name >> ");
	            	String fileName=keyboard.readLine();
	            	packet=String.format("L|%s", fileName);
	            	pw.println(packet);
	                pw.flush();
	                String logPack= br.readLine();
	                String[] logs=parsePacket(logPack);
	                if(logs[0].equals("E")) {
	                	System.out.println("-------------------");
	                	System.out.println("\nWorng File Name!!\n");
	                	System.out.println("-------------------");
	                	continue;
	                }
	                System.out.println("Data Load!");
	                replay(logs[1]);
	                break;
            	}
            }
            else if(menu==3) {
               System.out.println("Client Ended!");
               break;
            }
            if (loop1) {
            	System.out.println("Client Ended!");
                break;
            }

         }
         pw.close();
         br.close();
         
      } catch (IOException e) {
    	  System.out.println("Server is Killed!");
      }
      
   }
}