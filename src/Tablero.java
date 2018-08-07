import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class Tablero extends JPanel {

    Agente ag = new Agente();
    Random rand = new Random();

    Obstaculos obs = new Obstaculos();
    Meta meta = new Meta();

    int n;
    int porcentaje;
    Elementos[][] mapa;

    public int getN() {
        return n;
    }

    public Elementos[][] getMapa() {
        return mapa;
    }

    public Tablero(int n, int porcentaje, Elementos[][] mapa) {
        this.n = n;
        this.mapa = mapa;
        this.porcentaje = porcentaje;
    }

    int x = 10;
    int y = 10;

    int w = 0;
    int h = 0;



    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;

        //PARA DIBUJAR GRILLA

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

        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                //SE DIBUJA LA GRILLA
                if(j == 0) {
                    g2.setColor(Color.BLUE);
                    g2.fillRect(x, y, w, h);
                } else if (j == n-1){
                    g2.setColor(Color.RED);
                    g2.fillRect(x, y, w, h);

                    mapa[j][i] = meta;
                } else {
                    g2.setColor(Color.BLACK);
                    g2.drawRect(x,y,w,h);
                }
                x = x + w;
            }
            y = y + h;
            x = 10;
        }

        /*
        //PARA DIBUJAR META OVALO ROJO EN POSICION RANDOM ULTIMA FILA
        int randYposM = rand.nextInt(n);
        int posYM;

        //ANADIR LA META EN SU POSICION ACTUAL EN LA MATRIZ DE ELEMENTOS
        mapa[n-1][randYposM] = meta;

        //DETERMINAR POSICION META EN BASE A RANDOM
        if (randYposM == 0) {
            posYM = 10;
            meta.setPosY(posYM);
            meta.setPosX(10 + (n-1)*w);

        } else {
            posYM = 10 + h*randYposM;
            meta.setPosY(posYM);
            meta.setPosX(10 + (n-1)*w);
        }

        g2.setColor(Color.RED);
        g2.fillOval(meta.getPosX(),posYM,w,h);
        */


    }

    public void StartGame(){

        //PARA DIBUJAR AGENTE GRIS EN POSICION RANDOM DE LA PRIMERA FILA

        int randYpos = rand.nextInt(n);
        int posYAg;

        //AÑADIR EL AGENTE EN SU POSICIÓN ACTUAL EN LA MATRIZ DE ELEMENTOS

        mapa[0][randYpos] = ag;

        //DETERMINAR POSICION AGENTE EN BASE A RANDOM
        if (randYpos == 0) {
            posYAg = 10+h;
            ag.setPosY(posYAg);
            ag.setPosX(10);

        } else {
            posYAg = 10 + h*randYpos;
            ag.setPosY(posYAg);
            ag.setPosX(10);
        }


        //SE DIBUJA EL AGENTE EN LA GRILLA
        getGraphics().fillOval(10,posYAg,w,h);


        //DETERMINAR POSICION DE LOS OBSTACULOS Y CREARLOS
         int numobs = ((n*n*porcentaje)/100);
         int newn = n-2;

         for(int k = 0; k < numobs; k++){
             int randobsx = rand.nextInt(newn) + 1;
             int randobsy = rand.nextInt(n);

             //DETERMINAR POSICION EN BASE A RANDOM
             if(mapa[randobsx][randobsy] == null){
                 if(randobsy == 0){
                         mapa[randobsx][randobsy] = obs;

                         mapa[randobsx][randobsy].setPosX(10 + randobsx*w);
                         mapa[randobsx][randobsy].setPosY(10);

                         getGraphics().fillRect(10 + randobsx*w,10,w,h);

                 } else {

                         mapa[randobsx][randobsy] = obs;

                         mapa[randobsx][randobsy].setPosX(10 + w*randobsx);
                         mapa[randobsx][randobsy].setPosY(10 + h*randobsy);

                         getGraphics().fillRect(10 + w*randobsx,10 + h*randobsy,w,h);
                 }

             }
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

            System.out.println("Moviendo a casilla " + "[" + verifx + "]" + "[" + verify + "]");

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

            System.out.println("Moviendo a casilla " + "[" + verifx + "]" + "[" + verify + "]");

            getGraphics().fillOval(xdw,ydw,w,h);
        }
    }



}
