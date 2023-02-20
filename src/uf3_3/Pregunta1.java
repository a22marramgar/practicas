package uf3_3;

import java.io.*;
import static utils.UIUtilities.*;

public class Pregunta1 {
	public static void main(String[] args) throws IOException {
		File f1 = new File("./arxiuBinari.bin");
		OutputStream os= new FileOutputStream(f1);
		int numeroAinsertar=llegirInt("Introdueix el numero que vols registrar: ");
		while(numeroAinsertar!=0) {
			String numerosAinsetar=numeroAinsertar+"";
			os.write(numerosAinsetar.getBytes(), 0, numerosAinsetar.length() );
			numeroAinsertar=llegirInt("Introdueix el numero que vols registrar: ");
		}
		os.flush();
		os.close();
	}
}
