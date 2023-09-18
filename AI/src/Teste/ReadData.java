package Teste;

import com.fazecast.jSerialComm.SerialPort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadData {
    public static void main(String[] args) {
        SerialPort comPort = SerialPort.getCommPort("ttyACM0");

        comPort.setBaudRate(115200);
        comPort.setNumDataBits(8);
        comPort.setParity(SerialPort.NO_PARITY);
        comPort.setNumStopBits(1);

        char[][] board = new char[8][8];

        if (comPort.openPort()) {
            System.out.println("Porta serial aberta com sucesso.");
            comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);
            BufferedReader in = new BufferedReader(new InputStreamReader(comPort.getInputStream()));
            System.out.println("Porta serial aberta com sucesso. 111");
            while (true){
                String msg = null;
                try {
                    while((msg = in.readLine()) != null);
                    System.out.println("Porta serial aberta com sucesso. 2222");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(msg);

                if(msg.length() >= 64){
                    int idx = 0;
                    for (int i = 0; i < board.length; i++) {
                        for (int j = 0; j < board[i].length; j++) {
                            board[i][j] = msg.charAt(idx);
                            idx++;
                        }
                    }
                }

                System.out.println("\n----------------");
                for (int i = 0; i < board.length; i++) {
                    for (int j = 0; j < board[i].length; j++) {
                        System.out.print(board[i][j]);
                    }
                    System.out.println();
                }
                System.out.println("\n----------------");
            }
            //comPort.closePort();
            //System.out.println("v");
            // Crie uma matriz 8x8 para armazenar os dados


        } else {
            System.err.println("Erro ao abrir a porta serial.");
        }
    }

}
