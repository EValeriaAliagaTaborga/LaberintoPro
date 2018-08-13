import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Random;

public class Tablero extends JPanel {

    Agente ag = new Agente();
    Random rand = new Random();

    Obstaculos obs = new Obstaculos();
    Meta meta = new Meta();

    int n;
    private int numobs;
    private Elementos[][] mapa;
    File file;


    //NOMBRE DEL FILE PARA ABRIR O CREAR
    String fileName = "C:\\TEMP\\camino.txt";


    public int getN() {
        return n;
    }

    public Elementos[][] getMapa() {
        return mapa;
    }

    public Tablero(int n, int numobs, Elementos[][] mapa, File file) {
        this.n = n;
        this.mapa = mapa;
        this.numobs = numobs;
        this.file = file;
    }

    int x = 10;
    int y = 10;

    int w = 0;
    int h = 0;

    public void calculate(){
        x = 10;
        y = 10;

        if (n < 10) {
            w = 90;
            h = 90;
        } else if(n == 10){
            w = 70;
            h = 70;
        } else if(n>10 && n <=30){
            w = 30;
            h = 30;
        } else if(n>30 && n<=60){
            w = 15;
            h = 15;
        } else if (n>60 && n<=80){
            w = 10;
            h = 10;
        } else if (n>80 && n<=100){
            w = 9;
            h = 9;
        }
    }

    public void paint(Graphics g) {
        calculate();

        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;

        //PARA DIBUJAR GRILLA
        calculate();

        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++) {
                //SE DIBUJA LA GRILLA
                g2.setColor(Color.BLACK);
                g2.drawRect(x, y, w, h);
                x = x + w;
            }
            y = y + h;
            x = 10;
        }
    }

    public void creategame(){

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;

            while ((st = br.readLine()) != null){
                System.out.println(st);

                if(st.endsWith("0")){
                    System.out.println("SOY UN OBSTACULO");

                    //DETERMINAR POSICION DE LOS OBSTACULOS Y CREARLO

                    String[] partsobs = st.split(",");
                    int obsx = Integer.valueOf(partsobs[0]);
                    int obsy = Integer.valueOf(partsobs[1]);

                    //DETERMINAR POSICION
                    if(mapa[obsx][obsy] == null){
                        if(obsy == 0){
                            mapa[obsx][obsy] = obs;

                            mapa[obsx][obsy].setPosX(10 + obsx*w);
                            mapa[obsx][obsy].setPosY(10);

                            getGraphics().setColor(Color.GREEN);
                            getGraphics().fillRect(10 + obsx*w,10,w,h);

                        } else {

                            mapa[obsx][obsy] = obs;

                            mapa[obsx][obsy].setPosX(10 + w*obsx);
                            mapa[obsx][obsy].setPosY(10 + h*obsy);

                            getGraphics().setColor(Color.GREEN);
                            getGraphics().fillRect(10 + w*obsx,10 + h*obsy,w,h);
                        }

                    }

                } else if (st.endsWith("1")) {
                    System.out.println("SOY UN AGENTE");

                    String[] agparts = st.split(",");
                    int xpart1 = Integer.valueOf(agparts[0]);
                    int ypart2 = Integer.valueOf(agparts[1]);
                    int zpart3 = Integer.valueOf(agparts[2]);

                    int posYAg;

                    //AÑADIR EL AGENTE EN SU POSICIÓN ACTUAL EN LA MATRIZ DE ELEMENTOS
                    mapa[xpart1][ypart2] = ag;

                    //DETERMINAR POSICION AGENTE EN BASE A RANDOM
                    if (ypart2 == 0) {
                        posYAg = 10+h;
                        ag.setPosY(posYAg);
                        ag.setPosX(10);

                    } else {
                        posYAg = 10 + h*ypart2;
                        ag.setPosY(posYAg);
                        ag.setPosX(10);
                    }

                    //SE DIBUJA EL AGENTE EN LA GRILLA
                    getGraphics().setColor(Color.BLUE);
                    getGraphics().fillOval(10,posYAg,w,h);

                } else if (st.endsWith("2")) {
                    System.out.println("SOY UNA META");

                    String[] metparts = st.split(",");
                    int xmet = Integer.valueOf(metparts[0]);
                    int ymet = Integer.valueOf(metparts[1]);
                    int zmet = Integer.valueOf(metparts[2]);

                    //PARA DIBUJAR META OVALO ROJO EN POSICION
                    int posYM;

                    //ANADIR LA META EN SU POSICION ACTUAL EN LA MATRIZ DE ELEMENTOS
                    mapa[xmet][ymet] = meta;

                    //DETERMINAR POSICION META
                    if (ymet == 0) {
                        posYM = 10;
                        meta.setPosY(posYM);
                        meta.setPosX(10 + (n-1)*w);

                    } else {
                        posYM = 10 + h*ymet;
                        meta.setPosY(posYM);
                        meta.setPosX(10 + (n-1)*w);
                    }

                    getGraphics().setColor(Color.RED);
                    getGraphics().fillOval(meta.getPosX(),posYM,w,h);
                }

            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }



    boolean verDer = true;
    boolean verIz = true;
    boolean verUp = true;
    boolean verDw = true;

    int contDer = 0;
    int contIz = 0;
    int contUp = 0;
    int contDw = 0;

    public void game(){
        int matposAgX = (ag.getPosX()-10)/w;
        int matposAgY = (ag.getPosX()-10)/h;

        while(mapa[matposAgX][matposAgY] != meta) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            if (verDer) {
                movDer();

                if(contDer > 5){
                    verDer = false;
                    contDer = 0;
                }
            } else {
                if (verUp) {
                    movUp();

                    if(contUp > 5){
                        verUp = false;
                        contUp = 0;
                    }

                    movIz();
                    verDer = true;
                    verIz = true;
                } else {
                    if (verDw) {
                        movDw();

                        if(contDw > 5){
                            verDw = false;
                            contDw = 0;
                        }

                        verDer = true;
                        verIz = true;
                    } else {
                        if (verIz) {
                            movIz();
                            movIz();

                            if(contIz > 5){
                                verIz = false;
                                contIz = 0;
                                movDer();
                                movDw();
                            }

                            verDer = true;
                            verUp = true;
                            verDw = true;
                        } else {
                            break;
                        }
                    }
                }
            }
        }
    }


    public void movDer(){

        int xd = ag.getPosX();
        int yd = ag.getPosY();

        int verifx = ((xd + w) - 10)/w;
        int verify=0;

        if(n<=30){
            verify = ((yd + 10)/w);
        } else if (n>30 && n<=60){
            verify = ((yd + 10)/w)-1;
        } else {
            verify = ((yd + 10)/w)-2;
        }


        if(verifx > n-1 || mapa[verifx][verify] == obs) {
            //System.out.println("No se puede mover mas hacia Derecha");

            contDer++;

            verDer = false;
        } else if (mapa[verifx][verify] == meta){
            mapa[verifx-1][verify] = null;

            getGraphics().clearRect(xd,yd,w,h);
            getGraphics().drawRect(xd,yd,w,h);

            xd = xd + w;

            mapa[verifx][verify] = meta;

            getGraphics().fillOval(xd,yd,w,h);

            verDer = false;
            verUp = false;
            verIz = false;
            verDw = false;

            try {
                // Assume default encoding.
                FileWriter fileWriter =
                        new FileWriter(fileName, true);

                // Always wrap FileWriter in BufferedWriter.
                BufferedWriter bufferedWriter =
                        new BufferedWriter(fileWriter);

                bufferedWriter.write(verifx + "," + verify);
                bufferedWriter.newLine();

                // Always close files.
                bufferedWriter.close();

            } catch(IOException ex) {
                System.out.println("Error writing to file '"+  fileName + "'");
            }

            System.out.println("****************************");
            System.out.println("LLEGO A LA META!!");
            System.out.println("****************************");

        } else {

            mapa[verifx-1][verify] = null;

            getGraphics().clearRect(xd,yd,w,h);
            getGraphics().drawRect(xd,yd,w,h);

            xd = xd + w;
            ag.setPosX(xd);

            mapa[verifx][verify] = ag;

            try {
                // Assume default encoding.
                FileWriter fileWriter =
                        new FileWriter(fileName, true);

                // Always wrap FileWriter in BufferedWriter.
                BufferedWriter bufferedWriter =
                        new BufferedWriter(fileWriter);

                bufferedWriter.write(verifx + "," + verify);
                bufferedWriter.newLine();

                // Always close files.
                bufferedWriter.close();

            } catch(IOException ex) {
                System.out.println("Error writing to file '"+  fileName + "'");
            }

            System.out.println("Moviendo a casilla " + "[" + verifx + "]" + "[" + verify + "]");

            getGraphics().fillOval(xd,yd,w,h);
        }

    }


    public void movIz() {
        int xi = ag.getPosX();
        int yi = ag.getPosY();

        int verifx = ((xi - w) - 10) / w;
        int verify = 0;

        if (n <= 30) {
            verify = ((yi + 10) / w);
        } else if (n > 30 && n <= 60) {
            verify = ((yi + 10) / w) - 1;
        } else {
            verify = ((yi + 10) / w) - 2;
        }

        if (verifx < 0 || mapa[verifx][verify] == obs) {
            //System.out.println("No se puede mover mas hacia Izquierda");

            contIz++;

            verIz = false;

        } else {
            mapa[verifx + 1][verify] = null;

            getGraphics().clearRect(xi,yi,w,h);
            getGraphics().drawRect(xi,yi,w,h);

            xi = xi - w;
            ag.setPosX(xi);

            mapa[verifx][verify] = ag;

            try {
                // Assume default encoding.
                FileWriter fileWriter =
                        new FileWriter(fileName, true);

                // Always wrap FileWriter in BufferedWriter.
                BufferedWriter bufferedWriter =
                        new BufferedWriter(fileWriter);

                bufferedWriter.write(verifx + "," + verify);
                bufferedWriter.newLine();

                // Always close files.
                bufferedWriter.close();

            } catch(IOException ex) {
                System.out.println("Error writing to file '"+  fileName + "'");
            }

            System.out.println("Moviendo a casilla " + "[" + verifx + "]" + "[" + verify + "]");

            getGraphics().fillOval(xi,yi,w,h);
        }
    }


    public void movUp(){
        int xup = ag.getPosX();
        int yup = ag.getPosY();

        int verify = ((yup - h) - 10)/h;
        int verifx=0;

        if(n <= 30){
            verifx = ((xup + 10)/h);
        } else if (n > 30 && n<=60){
            verifx = ((xup + 10)/h)-1;
        } else {
            verifx = ((xup + 10)/h)-2;
        }


        if(verify < 0 || mapa[verifx][verify] == obs){
            //System.out.println("No se puede mover mas hacia Arriba");

            contUp++;

            verUp = false;

        } else {

            mapa[verifx][verify +1] = null;

            getGraphics().clearRect(xup,yup,w,h);
            getGraphics().drawRect(xup,yup,w,h);

            yup = yup - h;
            ag.setPosY(yup);

            mapa[verifx][verify] = ag;

            try {
                // Assume default encoding.
                FileWriter fileWriter =
                        new FileWriter(fileName, true);

                // Always wrap FileWriter in BufferedWriter.
                BufferedWriter bufferedWriter =
                        new BufferedWriter(fileWriter);

                bufferedWriter.write(verifx + "," + verify);
                bufferedWriter.newLine();

                // Always close files.
                bufferedWriter.close();

            } catch(IOException ex) {
                System.out.println("Error writing to file '"+  fileName + "'");
            }

            System.out.println("Moviendo a casilla " + "[" + verifx + "]" + "[" + verify + "]");

            getGraphics().fillOval(xup,yup,w,h);
        }
    }


    public void movDw(){
        int xdw = ag.getPosX();
        int ydw = ag.getPosY();

        int verify = ((ydw + h) - 10)/h;
        int verifx =  0;

        if(n<=30){
            verifx =((xdw + 10)/h);
        } else if(n>30 && n<=60){
            verifx =((xdw + 10)/h)-1;
        } else {
            verifx =((xdw + 10)/h)-2;
        }


        if (verify > n-1 || mapa[verifx][verify] == obs){
            //System.out.println("No se puede mover mas hacia Abajo");

            contDw++;

            verDw = false;

        } else {

            mapa[verifx][verify-1] = null;

            getGraphics().clearRect(xdw,ydw,w,h);
            getGraphics().drawRect(xdw,ydw,w,h);

            ydw = ydw + h;
            ag.setPosY(ydw);

            mapa[verifx][verify] = ag;

            try {
                // Assume default encoding.
                FileWriter fileWriter =
                        new FileWriter(fileName, true);

                // Always wrap FileWriter in BufferedWriter.
                BufferedWriter bufferedWriter =
                        new BufferedWriter(fileWriter);

                bufferedWriter.write(verifx + "," + verify);
                bufferedWriter.newLine();

                // Always close files.
                bufferedWriter.close();

            } catch(IOException ex) {
                System.out.println("Error writing to file '"+  fileName + "'");
            }

            System.out.println("Moviendo a casilla " + "[" + verifx + "]" + "[" + verify + "]");

            getGraphics().fillOval(xdw,ydw,w,h);
        }
    }



}
