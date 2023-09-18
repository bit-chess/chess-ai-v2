package Teste;

import com.fazecast.jSerialComm.SerialPort;

public class SerialTest {
    public static void main(String[] args) {
        System.out.println("Hello world");
        
        SerialPort[] ports = SerialPort.getCommPorts();

        for (SerialPort port: ports) {
            System.out.println(port.getSystemPortName());
        }
    }

}
