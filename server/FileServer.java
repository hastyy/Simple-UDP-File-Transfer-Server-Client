/**
* Author: João Pedro Dias
* Redes de Computadores 2016/2017 - FCT UNL
* Turno P1
*/

import java.net.*;
import java.io.*;

public class FileServer {
	
static final int PORT = 8000 ;
static final int BLOCKSIZE = 1024 ; // buffer for file copy - 1 KByte
	
	public static void main(String[] args) throws Exception {
		
		// create input / output UDP socket
		DatagramSocket socket = new DatagramSocket( PORT ) ;
		
		try {
			
			for(;;) { // server endless loop
				
				// wait for an incoming datagram
				System.out.println("Waiting for a connection...");
				byte[] buffer = new byte[65536] ;
				DatagramPacket request = new DatagramPacket( buffer, buffer.length ) ;
				
				// receive datagram with filename
				socket.receive( request ) ;
				System.out.println("connection received.");
				byte[] requestData = request.getData();
				int requestLength = request.getLength() ;
				String filename = new String( requestData, 0, requestLength);
				
				// prepare to receive file
				FileOutputStream out = new FileOutputStream (filename);
				byte[] fileBuffer = new byte[BLOCKSIZE];
				long byteCount = 0;
				int blockCount = 0;
				int n;
				
				// receive blocks
				System.out.printf("Starting file transfer...\n");
				for(;;) {
					DatagramPacket block = new DatagramPacket(buffer, buffer.length);
					socket.receive(block);
					n = block.getLength();
					if ( n > 0 )
						out.write(fileBuffer,0,n);
					else
						break;
					byteCount += n;
					blockCount += 1;
					System.out.printf("\t[+] %d bytes received \n",  n );
				} 
				
				out.close();
				
				// end message
				System.out.println("File received successfully! (" + byteCount + " bytes , " + blockCount + " blocks)\n\n\n");
				
			}
			
		} finally {
			
			// close input / output UDP socket
			socket.close();
			
		}
		
	}

}
