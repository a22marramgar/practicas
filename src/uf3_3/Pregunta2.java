package uf3_3;

import java.io.*;
import java.util.logging.*;

import static utils.UIUtilities.*;

class Clients {
	int Codi;
	String Nom;
	String Cognoms;
	int DiaNaixement;
	int MesNaixement;
	int AnyNaixement;
	String AdreçaPostal;
	String eMail;
	boolean VIP;
}

public class Pregunta2 {

	private static final String NOM_FITXER = "./ClientsBinari.bin";

	public static void main(String[] args) {
		int opcio;
		do {
			opcio = Menu();
			switch (opcio) {
			case 1:
				AnyadirCliente(true);
				break;
			case 2:
				// consultaPerPosicio(f1);
				break;
			case 3:
				LeerClientesCodigo();
				break;
			case 4:
				// modificarClient(f1);
				break;
			case 5:
				// borrarClient(f1);
				break;
			case 6:
				LeerFichero();
				break;
			case 7:

				break;

			default:
				break;
			}
		} while (opcio != 7);
	}

	private static int Menu() {
		System.out.println("Menu:");
		System.out.println("1. Alta d'un client");
		System.out.println("2. Consulta d'un client per posicio");
		System.out.println("3. Consulta d'un client per codi");
		System.out.println("4. Modificar un client");
		System.out.println("5. Borrar un client");
		System.out.println("6. Llistat de tots els clients");
		System.out.println("7. Sortir");
		int opcio = escollirOpcio(1, 7);
		return opcio;
	}

	public static void EscribirDatosCliente(Clients c) {
		System.out.println("Codi: " + c.Codi);
		System.out.println("Nom: " + c.Nom);
		System.out.println("Cognom: " + c.Cognoms);
		System.out.println("Adreça: " + c.AdreçaPostal);
		System.out.println("Dia Naixement: " + c.DiaNaixement);
		System.out.println("Mes Naixement: " + c.MesNaixement);
		System.out.println("Any Naixement: " + c.AnyNaixement);
		System.out.println("e-mail: " + c.eMail);
		System.out.println("VIP: " + c.VIP);

	}

	public static Clients FormatearFicheroCliente(String str) {
		Clients cli;

		if (!str.equals("")) {
			cli = new Clients();

			cli.Codi = Integer.parseInt(str.substring(0, 4).trim());
			cli.Nom = str.substring(4, 24).trim();
			cli.Cognoms = str.substring(24, 64).trim();
			cli.DiaNaixement = Integer.parseInt(str.substring(64, 66).trim());
			cli.MesNaixement = Integer.parseInt(str.substring(66, 68).trim());
			cli.AnyNaixement = Integer.parseInt(str.substring(68, 72).trim());
			cli.AdreçaPostal = str.substring(72, 122).trim();
			cli.eMail = str.substring(122, 162).trim();
		} else
			cli = null;
		return cli;
	}

	public static void LeerClientesCodigo() {
		System.out.print("Introduce el código del cliente a buscar: ");
		int codigoBuscar = llegirInt();

		// Creamos el enlace con el fichero en el disco para leer
		BufferedReader buf = AbrirFicheroLectura(NOM_FITXER, true);

		String linea = LeerLinea(buf);
		Clients cli = FormatearFicheroCliente(linea);
		while (cli != null && cli.Codi != codigoBuscar) {
			linea = LeerLinea(buf);
			cli = FormatearFicheroCliente(linea);
		}

		if (cli != null && cli.Codi == codigoBuscar)
			EscribirDatosCliente(cli);

		CerrarFichero(buf);
	}

	public static void AnyadirCliente(boolean blnAnyadir) {
		// Creamos el enlace con el fichero en el disco
		PrintWriter pw = AbrirFicheroEscritura(NOM_FITXER, true, blnAnyadir);

		Clients cli = PedirDatosCliente();
		String linea = FormatearClienteFichero(cli);
		EscribirLinea(pw, linea);

		CerrarFichero(pw);
	}

	public static Clients PedirDatosCliente() {
		Clients c = new Clients();
		c.Codi = llegirInt("Codi: ");
		if (c.Codi != 0) {
			c.Nom = llegirString("Nom: ");
			c.Cognoms = llegirString("Cognoms: ");
			c.DiaNaixement = llegirInt("Dia de naixement: ");
			c.MesNaixement = llegirInt("Mes de naixement: ");
			c.AnyNaixement = llegirInt("Any de naixement: ");
			c.AdreçaPostal = llegirString("Adreça: ");
			c.eMail = llegirString("e-mail: ");
			System.out.println("VIP?");
			System.out.println("1. Si");
			System.out.println("2. No");
			if (escollirOpcio(1, 2) == 1) {
				c.VIP = true;
			} else {
				c.VIP = false;
			}
		} else {
			c = null;
		}
		return c;
	}

	public static String FormatearClienteFichero(Clients cli) {
		String result = "";

		result += String.format("%-4s", cli.Codi);
		result += String.format("%-20s", cli.Nom);
		result += String.format("%-40s", cli.Cognoms);
		result += String.format("%-2s", cli.DiaNaixement);
		result += String.format("%-2s", cli.MesNaixement);
		result += String.format("%-4s", cli.AnyNaixement);
		result += String.format("%-50s", cli.AdreçaPostal);
		result += String.format("%-40s", cli.eMail);

		return result;
	}

	public static void LeerFichero() {
		// Creamos el enlace con el fichero en el disco
		BufferedReader buf = AbrirFicheroLectura(NOM_FITXER, true);

		String linea = LeerLinea(buf);
		while (linea != null) {
			System.out.println(linea);
			linea = LeerLinea(buf);
		}

		CerrarFichero(buf);

	}

	/**
	 * Funcion que abre un fichero y, opcionalmente, lo crea si no existe
	 *
	 * @param nomFichero Nombre del fichero a abrir
	 * @param crear      Si lo que queremos crear en el caso que no exista
	 * @return File con el fichero que se ha abierto o null si no existe o no se ha
	 *         podido crear
	 */
	public static File AbrirFichero(String nomFichero, boolean crear) {
		File result = null;

		result = new File(nomFichero);

		if (!result.exists()) {
			if (crear) {
				try {
					result.createNewFile();
				} catch (IOException ex) {
					Logger.getLogger(Pregunta2.class.getName()).log(Level.SEVERE, null, ex);
					result = null;
				}
			} else {
				result = null;
			}
		}

		return result;
	}

	/**
	 * Abre un fichero para lectura
	 *
	 * @param nomFichero Nombre del fichero
	 * @param crear      Indica si queremos crear el fichero o no, en el caso que no
	 *                   exista
	 * @return BufferedReader apuntando al fichero
	 */
	public static BufferedReader AbrirFicheroLectura(String nomFichero, boolean crear) {
		BufferedReader br = null;
		File f = AbrirFichero(nomFichero, crear);

		if (f != null) {
			// Declarar el reader para poder leer el fichero¡
			FileReader reader;
			try {
				reader = new FileReader(f);
				// Buffered reader para poder leer más comodamente
				br = new BufferedReader(reader);
			} catch (FileNotFoundException ex) {
				Logger.getLogger(Pregunta2.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		return br;
	}

	/**
	 * Abre un fichero para lectura
	 *
	 * @param nomFichero Nombre del fichero
	 * @param crear      Indica si queremos crear el fichero o no, en el caso que no
	 *                   exista
	 * @return BufferedReader apuntando al fichero
	 */
	public static PrintWriter AbrirFicheroEscritura(String nomFichero, boolean crear, boolean blnAnyadir) {
		PrintWriter pw = null;
		File f = AbrirFichero(nomFichero, crear);

		if (f != null) {
			// Declarar el writer para poder escribir en el fichero¡
			FileWriter writer;
			try {
				writer = new FileWriter(f, blnAnyadir);
				// PrintWriter para poder escribir más comodamente
				pw = new PrintWriter(writer);
			} catch (IOException ex) {
				Logger.getLogger(Pregunta2.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		return pw;
	}

	/**
	 * Cierra el fichero
	 *
	 * @param br fichero a cerrar
	 */
	public static void CerrarFichero(BufferedReader br) {
		try {
			br.close();
		} catch (IOException ex) {
			Logger.getLogger(Pregunta2.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Cierra el fichero
	 *
	 * @param pw fichero a cerrar
	 */
	public static void CerrarFichero(PrintWriter pw) {
		pw.flush();
		pw.close();
	}

	/**
	 * Lee una linea del fichero
	 *
	 * @param br BufferedReader con el fichero a leer
	 * @return String
	 */
	public static String LeerLinea(BufferedReader br) {
		String linea = null;

		try {
			linea = br.readLine();
		} catch (IOException ex) {
			Logger.getLogger(Pregunta2.class.getName()).log(Level.SEVERE, null, ex);
		}

		return linea;
	}

	/**
	 * Lee una linea del fichero
	 *
	 * @param pw PrintWrite con el fichero a leer
	 * @linea Linea a escribir
	 */
	public static void EscribirLinea(PrintWriter pw, String linea) {
		pw.println(linea);
	}

	public static void EscribirFichero(boolean blnAnyadir) {
		// Creamos el enlace con el fichero en el disco
		PrintWriter pw = AbrirFicheroEscritura(NOM_FITXER, true, blnAnyadir);

		String linea = llegirString();
		while (!linea.equals("")) {
			EscribirLinea(pw, linea);
			linea = llegirString();
		}

		CerrarFichero(pw);
	}

	public static void BorrarFichero(String filename) {
		File f = new File(filename);
		f.delete();
	}

}
