package Communication;

import com.fazecast.jSerialComm.SerialPort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Serial {
    
    private final SerialPort comPort;
    private BufferedReader in;

    public Serial(String portName){
        this.comPort = SerialPort.getCommPort("ttyACM0");
    
        this.comPort.setBaudRate(115200);
        this.comPort.setNumDataBits(8);
        this.comPort.setParity(SerialPort.NO_PARITY);
        this.comPort.setNumStopBits(1);

        if(this.comPort.openPort()){
            this.comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);
            this.in = new BufferedReader(new InputStreamReader(this.comPort.getInputStream()));
        }
    }

    public boolean IsOpenPort(){
        return this.comPort.openPort();
    }

    public void readBoardFromSerial(){
        char[][] board = new char[8][8];

        try {
            String msg = this.in.readLine();
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

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void closePort(){
        this.comPort.closePort();
    }

    public static void main(String[] args){
        Serial s = new Serial("ttyACM0");

        if(s.IsOpenPort()){
            System.out.println("abriu");
            while(true){
                s.readBoardFromSerial();
            }
        }
    }

}
