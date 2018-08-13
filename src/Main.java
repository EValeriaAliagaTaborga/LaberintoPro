import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/*EL PRESENTE PROGRAMA CREA UN LABERINTO DE n x n, AÑADIENDO UN AGENTE CON FORMA DE CÍRCULO COLOR GRIS Y OBSTÁCULOS
  * REPRESENTADOS POR CASILLAS MARCADAS ENTERAMENTE EN COLOR GRIS. PARA INICIAR EL JUEGO PRIMERO SE DEBEN CREAR ESTOS
  * ELEMENTOS CON EL BOTON "Crear elementos", Y POSTERIORMENTE EL BOTÓN "Start", PARA QUE EL AGENTE REALICE LA BÚSQUEDA
  * DE MANERA AUTOMÁTICA MOVIENDOSE CASILLA POR CASILLA PARTIENDO DE UNA CASILLA ALEATORIA EN LA COLUMNA 0.
  *
  * LA SIMULACIÓN TERMINA CUANDO EL AGENTE LLEGA A CUALQUIER CASILLA DE LA COLUMNA n-1. PUEDE DARSE EL CASO QUE EL
  * AGENTE SE TRABE Y NO SEPA COMO SALIR DEL LABERINTO YA QUE SU ALGORITMO DE BÚSQUEDA CARECE DE UNA INTELIGENCIA
  * ARTIFICIAL EFICIENTE.*/

/* AUTOR:
*
*       ERICKA VALERIA ALIAGA TABORGA
*       07/08/2018
* */

public class Main {

    public static void main(String[] args){
        JFrame forbuttons = new JFrame();
        JFrame frame = new JFrame();
        JPanel panelestupido = new JPanel();

        panelestupido.setSize(1100,1030);
        panelestupido.setVisible(true);
        panelestupido.setLayout(null);

        forbuttons.setContentPane(panelestupido);

        // muestra el cuadro de diálogo de archivos, para que el usuario pueda elegir el archivo a abrir
        JFileChooser selectorArchivos = new JFileChooser();
        selectorArchivos.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        // indica cual fue la accion de usuario sobre el jfilechooser
        int resultado = selectorArchivos.showOpenDialog(forbuttons);

        File archivo = selectorArchivos.getSelectedFile(); // obtiene el archivo seleccionado

        // muestra error si es inválido
        if ((archivo == null) || (archivo.getName().equals(""))) {
            JOptionPane.showMessageDialog(forbuttons, "Nombre de archivo inválido", "Nombre de archivo inválido", JOptionPane.ERROR_MESSAGE);
        } // fin de if


        int n1 = 20;
        int numobs = 0;

        File file = archivo;

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String st;
            while ((st = br.readLine()) != null){
                System.out.println(st);

                if(st.endsWith("0")){
                    System.out.println("SOY UN OBSTACULO");
                    numobs++;
                } else if (st.endsWith("1")) {
                    System.out.println("SOY UN AGENTE");
                } else if (st.endsWith("2")) {
                    System.out.println("SOY UNA META");
                }
            }
            System.out.println(n1);
            System.out.println(numobs);
        } catch (IOException ex){
            ex.printStackTrace();
        }

        Elementos[][] mapa = new Elementos[n1][n1];

        Tablero tb = new Tablero(n1, numobs, mapa,archivo);

        JButton showframe = new JButton("Run");
        showframe.setSize(100,50);
        showframe.setLocation(10, 80);
        showframe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(true);
                frame.setResizable(true);
            }
        });
        forbuttons.getContentPane().add(showframe);

        //*********************************************

        forbuttons.setBackground(Color.LIGHT_GRAY);
        forbuttons.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        forbuttons.setSize(200,200);

        forbuttons.setVisible(true);
        forbuttons.setResizable(false);

        //*********************************************

        JButton start = new JButton("Start");
        start.setSize(100,40);
        start.setLocation(920, 100);

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tb.crearobs();
                tb.game();
            }
        });

        frame.add(start);


        frame.add(tb);


        frame.setSize(1100,1030);
        frame.setBackground(Color.LIGHT_GRAY);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
