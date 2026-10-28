package Polizas.Marcas;

import java.awt.event.MouseAdapter;
import java.util.List;
import javax.swing.JOptionPane;

public class MarcasControl {

    private MarcasVista mv;
    private MarcasModelo mm;
    private List<Marcas> lista;
    private int idSeleccionado = -1;   // NUEVO

    public MarcasControl(MarcasVista marcasVista) {
        this.mv = marcasVista;
        this.mm = new MarcasModelo();
        marcasEventos();
    }

    public void marcasEventos() {
        // CONSULTAR
        mv.btn1.addActionListener(e -> {
            lista = mm.read();
            mv.actualizarVista(lista);
        });

        // AGREGAR
        mv.btn_a.addActionListener(e -> {
            String nombre = mv.txt1.getText().trim();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(mv, "El nombre no puede estar vacio");
                return;
            }

            lista = mm.read();
            boolean existe = lista.stream()
                    .anyMatch(m -> m.getNombre().trim().equalsIgnoreCase(nombre));

            if (existe) {
                JOptionPane.showMessageDialog(mv, "La marca \"" + nombre + "\" ya existe");
                return;
            }

            Marcas marca = new Marcas(0, nombre);
            int filas = mm.create(marca);
            JOptionPane.showMessageDialog(mv, filas > 0 ? "Marca agregada correctamente" : "No se pudo agregar la marca");
            limpiar();   // NUEVO (antes: actualizarVista + setText)
        });

        // NUEVO: ACTUALIZAR
        mv.btn_u.addActionListener(e -> {
            if (idSeleccionado == -1) {
                JOptionPane.showMessageDialog(mv, "Selecciona una marca de la tabla");
                return;
            }
            String nombre = mv.txt1.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(mv, "El nombre no puede estar vacio");
                return;
            }

            lista = mm.read();
            boolean existe = lista.stream()
                    .anyMatch(m -> m.getId() != idSeleccionado
                            && m.getNombre().trim().equalsIgnoreCase(nombre));
            if (existe) {
                JOptionPane.showMessageDialog(mv, "La marca \"" + nombre + "\" ya existe");
                return;
            }

            int filas = mm.update(new Marcas(idSeleccionado, nombre));
            JOptionPane.showMessageDialog(mv, filas > 0 ? "Marca actualizada correctamente" : "No se pudo actualizar");
            limpiar();
        });

        // NUEVO: ELIMINAR
        mv.btn_d.addActionListener(e -> {
            if (idSeleccionado == -1) {
                JOptionPane.showMessageDialog(mv, "Selecciona una marca de la tabla");
                return;
            }
            int r = JOptionPane.showConfirmDialog(mv,
                    "¿Eliminar la marca \"" + mv.txt1.getText() + "\"?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (r != JOptionPane.YES_OPTION) return;

            int filas = mm.delete(new Marcas(idSeleccionado, ""));
            JOptionPane.showMessageDialog(mv, filas > 0 ? "Marca eliminada correctamente" : "No se pudo eliminar");
            limpiar();
        });

        // CLIC EN LA TABLA (modificado: ahora guarda el id)
        mv.tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int fila = mv.tabla.getSelectedRow();
                if (fila == -1) return;
                idSeleccionado = Integer.parseInt(mv.marcasModelo.getValueAt(fila, 0).toString());
                mv.txt1.setText((String) mv.marcasModelo.getValueAt(fila, 1));
            }
        });

        mv.tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {   // corregido con "!"
                int fila = mv.tabla.getSelectedRow();
                if (fila != -1) {
                    System.out.println("Registro Fila: " + fila + " Valor: " + mv.tabla.getValueAt(fila, 1));
                }
            }
        });

        mv.cmbMarcas.addActionListener(e -> {
            Marcas m = (Marcas) mv.cmbMarcas.getSelectedItem();
        });
    }

    // NUEVO: refresca la tabla y deja todo como al inicio
    private void limpiar() {
        mv.actualizarVista(mm.read());
        mv.txt1.setText("");
        idSeleccionado = -1;
        mv.tabla.clearSelection();
    }

}
