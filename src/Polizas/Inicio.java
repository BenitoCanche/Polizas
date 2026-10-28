package Polizas;

import Polizas.General.Bienvenida;
import javax.swing.SwingUtilities;
import Polizas.General.Controlador;

public class Inicio {

    private Controlador controlador;
    private Bienvenida vista;

    public Inicio() {
        //Instanciar Bienvenida
        vista = new Bienvenida();
        controlador = new Controlador(vista);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Inicio::new);
    }

}
