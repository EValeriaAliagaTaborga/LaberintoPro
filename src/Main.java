import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        JFrame frame = new JFrame();


        Scanner sc = new Scanner(System.in);
        System.out.println("Introduzca dos enteros para determinar la matriz de nxn y el porcentaje de obstaculos.");
        System.out.println("Ej.: 6 5 (Matriz 6x6 con 5 porciento de obstaculos)");

        int n1 = sc.nextInt();
        int porcen = sc.nextInt();
        Elementos[][] mapa = new Elementos[n1][n1];

        Tablero tb = new Tablero(n1, porcen, mapa);

        JButton crear = new JButton("Crear elementos");
        crear.setSize(150,40);
        crear.setLocation(920,10);

        crear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tb.StartGame();
            }
        });

        frame.getContentPane().add(crear);

        JButton start = new JButton("Start");
        start.setSize(100,40);
        start.setLocation(920, 100);

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tb.game();
            }
        });

        frame.getContentPane().add(start);


        frame.add(tb);



        /*
        JButton movD = new JButton("movD");
        movD.setPreferredSize(new Dimension(100,40));

        movD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tb.movDer();
            }
        });

        tb.add(movD);


        JButton movI = new JButton("movI");
        movI.setPreferredSize(new Dimension(100,40));

        movI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tb.movIz();
            }
        });

        tb.add(movI);


        JButton movUp = new JButton("movUp");
        movUp.setPreferredSize(new Dimension(100,40));

        movUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tb.movUp();
            }
        });

        tb.add(movUp);


        JButton movDw = new JButton("movDw");
        movDw.setPreferredSize(new Dimension(100,40));

        movDw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tb.movDw();
            }
        });

        tb.add(movDw);
        */


        frame.setSize(1100,1030);
        frame.setBackground(Color.LIGHT_GRAY);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(true);


    }

}
