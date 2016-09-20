/**
* Author: João Pedro Dias
* Redes de Computadores 2016/2017 - FCT UNL
* Turno P1
*/

import java.net.*;
import java.io.*;

public class FileClient {

	/**
	 * @param args
	 * usage: java FileClient address port filename [seconds]
     *
	 */

	static final int BLOCKSIZE = 1024 ; // buffer for file copy - 1 KByte

	public static void main(String[] args) throws Exception {

		// arguments check
		if( args.length < 3 || args.length > 4 ) {
			System.err.printf("usage: java FileCopy address port filename [seconds]\n") ;
			System.exit(0);
		}
		String server = args[0] ;
		int port = Integer.parseInt( args[1] ) ;
		InetAddress serverAddress = InetAddress.getByName( server ) ;
		String filename = args[2];
		int secondsToWait = 0;
		if ( args.length == 4) {
			try {
				secondsToWait = Integer.parseInt(args[3]);
			} catch (NumberFormatException e) {
		        System.err.printf("argument " + args[3] + " must be an integer\n");
		        System.exit(0);
			}
		}
		// does the file exist and is it readable ?
		File f = new File(filename);
		if ( f.exists() && f.canRead() ) {
			System.out.printf("file: "+filename+" Ok to copy\n"); 
		} else {
		    System.err.printf("Can't open from file "+filename+ "\n");
		    System.exit(0);
		}
		
		try {
			
			long milliSeconds = System.currentTimeMillis();
			FileInputStream in = new FileInputStream (filename);
			
			// create input / output UDP socket
			DatagramSocket socket = new DatagramSocket() ;
			
			// send file name
			byte [] fname = filename.getBytes();
			DatagramPacket flname = new DatagramPacket(fname, fname.length, serverAddress, port);
			socket.send(flname);
			
			byte[] fileBuffer = new byte[BLOCKSIZE];
			boolean finished = false;
			long byteCount = 0;
			int blockCount = 0;
			long speed = 0;
			int n;
			System.out.printf("File transfer started...\n");
			do {
				n = in.read(fileBuffer);
				if ( n == -1 ) n = 0;
//				System.out.printf("bytes read %d \n",  n );
				if ( n < BLOCKSIZE ) finished=true;  // no more bytes to read
				if ( secondsToWait > 0 ) Thread.sleep(secondsToWait * 1000);
				if ( n > 0 ) {
					DatagramPacket block = new DatagramPacket(fileBuffer, n, serverAddress, port ) ;
					socket.send(block);
					System.out.printf("\t[+] %d bytes sent \n",  n );
				}
				byteCount += n;
				blockCount += 1;
			 } while ( !finished );
			
			// send empty block
			DatagramPacket emptyBlock = new DatagramPacket("".getBytes(), 0, serverAddress, port);
			socket.send(emptyBlock);
			
			in.close();
			 
			//out.close();
			socket.close();
			 
			// compute time spent copying bytes
			milliSeconds = System.currentTimeMillis() - milliSeconds;
			speed = 1000 * 8 * Math.round( byteCount / milliSeconds );
			System.out.printf("File sent successfully!\n");
			System.out.printf("%d blocks and %d bytes copied, in %d milli seconds, at %d bps\n", 
					blockCount, byteCount, milliSeconds, speed );
		} catch (Exception e) {
			System.err.printf("Can't copy file\n");
	    	System.exit(0);
		}
		
	}

}
