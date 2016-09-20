/**
* Author: João Pedro Dias
* Redes de Computadores 2016/2017 - FCT UNL
* Turno P1
*/

import java.io.*;

public class FileCreate {
	
	public static void main(String[] args) throws IOException {
		
		if( args.length != 2 ) {
			System.err.printf("usage: java FileCreate filename filesize(in bytes)\n") ;
			System.exit(0);
		}
		
		String filename = args[0];
		int size = 0;
		try {
			size = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
	        System.err.printf("argument " + args[1] + " must be an integer\n");
	        System.exit(0);
		}
		
		File f = new File(filename);
		if ( f.exists() && f.canRead() ) {
			System.err.printf("Can't create file "+filename+ "because it already exists\n");
		    System.exit(0);
		} else {
			FileOutputStream out = new FileOutputStream(filename);
			byte[] buffer = new byte[size];
			out.write(buffer);
			out.flush();
			out.close();
		}
		
	}

}
