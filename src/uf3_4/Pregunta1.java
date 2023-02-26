package uf3_4;

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

public class Pregunta1 {

    private static final String NOM_FITXER = "./ClientsBinari.bin";
    public static final int TAMANY_LONG = 8;
    public static final String NOM_FTX_CLIENTS_IDXPOS = "./clientes.idx_pos";

    public static void main(String[] args) {
        int opcio;
        do {
            opcio = Menu();
            switch (opcio) {
                case 1:
                    GrabarClientesBinario();
                    break;
                case 2:
                    LeerClientesBinarioPorPosicion();
                    break;
                case 3:
                    LeerClientesBinarioPorCodigo();
                    break;
                case 4:
                    ModificarClienteBinarioPorPosicion();
                    break;
                case 5:
                    BorrarClienteBinarioPorPosicion();
                    break;
                case 6:
                    LeerClientesBinario();
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
        System.out.println("Adreca: " + c.AdreçaPostal);
        System.out.println("Dia Naixement: " + c.DiaNaixement);
        System.out.println("Mes Naixement: " + c.MesNaixement);
        System.out.println("Any Naixement: " + c.AnyNaixement);
        System.out.println("e-mail: " + c.eMail);
        System.out.println("VIP: " + c.VIP);

    }

    public static Clients FormatearFicheroCliente(String str) {
        Clients cli;

        if (str != null) {
            cli = new Clients();

            cli.Codi = Integer.parseInt(str.substring(0, 4).trim());
            cli.Nom = str.substring(4, 24).trim();
            cli.Cognoms = str.substring(24, 64).trim();
            cli.DiaNaixement = Integer.parseInt(str.substring(64, 66).trim());
            cli.MesNaixement = Integer.parseInt(str.substring(66, 68).trim());
            cli.AnyNaixement = Integer.parseInt(str.substring(68, 72).trim());
            cli.AdreçaPostal = str.substring(72, 122).trim();
            cli.eMail = str.substring(122, 162).trim();
            if (str.substring(162, 166).equals("true")) {
                cli.VIP = true;
            } else {
                cli.VIP = false;
            }
        } else {
            cli = null;
        }
        return cli;
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
        result += String.format("%-1s", cli.VIP);

        return result;
    }

    /**
     * Funcion que abre un fichero y, opcionalmente, lo crea si no existe
     *
     * @param nomFichero Nombre del fichero a abrir
     * @param crear Si lo que queremos crear en el caso que no exista
     * @return File con el fichero que se ha abierto o null si no existe o no se
     * ha podido crear
     */
    public static File AbrirFichero(String nomFichero, boolean crear) {
        File result = null;

        result = new File(nomFichero);

        if (!result.exists()) {
            if (crear) {
                try {
                    result.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(Pregunta1.class.getName()).log(Level.SEVERE, null, ex);
                    result = null;
                }
            } else {
                result = null;
            }
        }

        return result;
    }

    public static void GrabarClientesBinario()
    {
        File f = AbrirFichero(NOM_FITXER, true);
        DataOutputStream dos = AbrirFicheroEscrituraBinario(true, f);
        
        Clients cli = PedirDatosCliente();

        GrabarIndiceClientePosicion(f.length());
        GrabarDatosClienteBinario(dos, cli, f);        
        
        CerrarFicheroBinario(dos);
        
    }
    

    public static DataOutputStream AbrirFicheroEscrituraBinario(boolean blnAnyadir, File f) {
        DataOutputStream dos = null;

        if (f != null) {
            // Declarar el writer para poder escribir en el fichero¡
            FileOutputStream writer;
            try {
                writer = new FileOutputStream(f, blnAnyadir);
                // PrintWriter para poder escribir más comodamente
                dos = new DataOutputStream(writer);
            } catch (IOException ex) {
                Logger.getLogger(Pregunta1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return dos;
    }

    public static void CerrarFicheroBinario(DataOutputStream dos) {
        try {
            dos.flush();
            dos.close();
        } catch (IOException ex) {
            Logger.getLogger(Pregunta1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void CerrarFicheroBinario(DataInputStream dis) {
        try {
            dis.close();
        } catch (IOException ex) {
            Logger.getLogger(Pregunta1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void GrabarDatosClienteBinario(DataOutputStream dos, Clients cli, File f) {
        try {
            dos.writeInt(cli.Codi);
            dos.writeUTF(cli.Nom);
            dos.writeUTF(cli.Cognoms);
            dos.writeInt(cli.DiaNaixement);
            dos.writeInt(cli.MesNaixement);
            dos.writeInt(cli.AnyNaixement);
            dos.writeUTF(cli.AdreçaPostal);
            dos.writeUTF(cli.eMail);
            dos.writeBoolean(cli.VIP);
        } catch (IOException ex) {
            Logger.getLogger(Pregunta1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public static void GrabarDatosClienteBinario(RandomAccessFile raf, Clients cli, File f) {
        try {
            raf.writeInt(cli.Codi);
            raf.writeUTF(cli.Nom);
            raf.writeUTF(cli.Cognoms);
            raf.writeInt(cli.DiaNaixement);
            raf.writeInt(cli.MesNaixement);
            raf.writeInt(cli.AnyNaixement);
            raf.writeUTF(cli.AdreçaPostal);
            raf.writeUTF(cli.eMail);
            raf.writeBoolean(cli.VIP);
        } catch (IOException ex) {
            Logger.getLogger(Pregunta1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public static DataInputStream AbrirFicheroLecturaBinario(File f) {
        DataInputStream dis = null;

        if (f != null) {
            // Declarar el writer para poder escribir en el fichero¡
            FileInputStream reader;
            try {
                reader = new FileInputStream(f);
                // PrintWriter para poder escribir más comodamente
                dis = new DataInputStream(reader);
            } catch (IOException ex) {
                Logger.getLogger(Pregunta1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return dis;
    }

    public static void LeerClientesBinario() {
        File f = AbrirFichero(NOM_FITXER, true);
        DataInputStream dis = AbrirFicheroLecturaBinario(f);

        Clients cli = LeerDatosClienteBinario(dis);
        while (cli != null) {
            EscribirDatosCliente(cli);
            cli = LeerDatosClienteBinario(dis);
        }

        CerrarFicheroBinario(dis);
    }

    public static Clients LeerDatosClienteBinario(DataInputStream dis) {
        Clients cli = new Clients();

        try {
            cli.Codi = dis.readInt();
            cli.Nom = dis.readUTF();
            cli.Cognoms = dis.readUTF();
            cli.DiaNaixement = dis.readInt();
            cli.MesNaixement = dis.readInt();
            cli.AnyNaixement = dis.readInt();
            cli.AdreçaPostal = dis.readUTF();
            cli.eMail = dis.readUTF();
            cli.VIP = dis.readBoolean();

        } catch (IOException ex) {
            cli = null;
        }
        return cli;
    }

    public static Clients LeerDatosClienteBinario(RandomAccessFile raf) {
        Clients cli = new Clients();

        try {
            cli.Codi = raf.readInt();
            cli.Nom = raf.readUTF();
            cli.Cognoms = raf.readUTF();
            cli.DiaNaixement = raf.readInt();
            cli.MesNaixement = raf.readInt();
            cli.AnyNaixement = raf.readInt();
            cli.AdreçaPostal = raf.readUTF();
            cli.eMail = raf.readUTF();
            cli.VIP = raf.readBoolean();

        } catch (IOException ex) {
            cli = null;
        }
        return cli;
    }

    public static void GrabarIndiceClientePosicion(long posicion) {

        File f = AbrirFichero(NOM_FTX_CLIENTS_IDXPOS, true);
        DataOutputStream dos = AbrirFicheroEscrituraBinario(true, f);

        try {
            dos.writeLong(posicion);
        } catch (IOException ex) {
            Logger.getLogger(Pregunta1.class.getName()).log(Level.SEVERE, null, ex);
        }

        CerrarFicheroBinario(dos);
    }

    public static void LeerClientesBinarioPorPosicion() {
        try {
            System.out.print("Número del registro al que quieres acceder: ");
            int numero = llegirInt();

            long posicion_indice = (numero - 1) * TAMANY_LONG;
            RandomAccessFile raf = new RandomAccessFile(NOM_FTX_CLIENTS_IDXPOS, "r");
            raf.seek(posicion_indice);
            long posicion_datos = raf.readLong();
            raf.close();

            RandomAccessFile rafCliente = new RandomAccessFile(NOM_FITXER, "r");
            rafCliente.seek(posicion_datos);

            Clients c = LeerDatosClienteBinario(rafCliente);
            EscribirDatosCliente(c);
            rafCliente.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Pregunta1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Pregunta1.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void LeerClientesBinarioPorCodigo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private static void ModificarClienteBinarioPorPosicion() {
        try {
            int posicionBuscar = llegirInt();
            long posicion_indice = (posicionBuscar - 1) * TAMANY_LONG;
            RandomAccessFile raf = new RandomAccessFile(NOM_FTX_CLIENTS_IDXPOS, "r");
            raf.seek(posicion_indice);
            long posicion_datos = raf.readLong();
            raf.close();

            File f = AbrirFichero(NOM_FITXER, true);
            RandomAccessFile rafCliente = new RandomAccessFile(NOM_FITXER, "rw");
            rafCliente.seek(posicion_datos);

            Clients c = PedirDatosCliente();
            GrabarDatosClienteBinario(rafCliente, c, f);    
            rafCliente.close();
        } catch (IOException ex) {
            Logger.getLogger(Pregunta1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void BorrarClienteBinarioPorPosicion() {
        try {
            int posicionBuscar = llegirInt();
            long posicion_indice = (posicionBuscar - 1) * TAMANY_LONG;
            RandomAccessFile raf = new RandomAccessFile(NOM_FTX_CLIENTS_IDXPOS, "rw");
            raf.seek(posicion_indice);
            long posicion_datos = raf.readLong();
            raf.close();

            RandomAccessFile rafCliente = new RandomAccessFile(NOM_FITXER, "r");
            rafCliente.seek(posicion_datos);

            Clients c = SetClientNull(rafCliente);
            rafCliente.close();
        } catch (IOException ex) {
            Logger.getLogger(Pregunta1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Clients SetClientNull(RandomAccessFile rafCliente) {
        Clients cli = new Clients();

        cli.Codi = 0;
        cli.Nom = null;
        cli.Cognoms = null;
        cli.DiaNaixement = 0;
        cli.MesNaixement = 0;
        cli.AnyNaixement = 0;
        cli.AdreçaPostal = null;
        cli.eMail = null;
        cli.VIP = false;
        return cli;
    }

    public static void ModificarIndiceClientePosicion(long posicion, long posicion_indice) {

        try {
            RandomAccessFile raf = new RandomAccessFile(NOM_FTX_CLIENTS_IDXPOS, "rw");
            raf.seek(posicion_indice);
            raf.writeLong(posicion);
            raf.close();
        } catch (IOException ex) {
            Logger.getLogger(Pregunta1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
