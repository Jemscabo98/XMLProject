/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlproject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.file.Files;

/**
 *
 * @author Eduardo Montoya
 */
public class PruebaExperimentalPseudoCliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, IOException {
        DatagramSocket udpSocket = new DatagramSocket();
        // Creates a ByteArrayOutputStream with default size
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        File myFile = new File("..\\XMLProject\\src\\file\\person.xml");  
        
        out.write(Files.readAllBytes(myFile.toPath()));
        
        byte buffer[] = out.toByteArray();
                
        DatagramPacket packet = 
                new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), 1001);
        
        udpSocket.send(packet);
        udpSocket.close();
    }
    
}
