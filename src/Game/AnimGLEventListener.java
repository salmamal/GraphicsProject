package Game;

import Texture.TextureReader;
import Texture.AnimListener;
import com.sun.opengl.util.GLUT;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.security.Key;
import java.util.BitSet;
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;

public class AnimGLEventListener extends AnimListener{
    int maxWidth = 100;
    int maxHeight = 100;
    int selectedOption = 0;

    // dino&tree-related variables
    int dinoIndex1=61;
    int dinoIndex2=68;


    int treeIndex = 65;
    int x1, y1; // tress
    boolean isJump1 = false;
    int jumpy1 =6;
    boolean isJump2 = false;
    int jumpy2 =6;
//    boolean isDown1=false;
//    boolean isDown2=false;
    boolean GameOver = false;
    int treeSpeed = 3;
    int x =20, y = 60;
    int x2=27,y2=13; // player2

    // Animation-related variables
    private int currentFrame = 0; // Current frame index
    private int frameDelay = 3;   // Number of display calls before moving to the next frame
    private int frameCounter = 0; // Counter to manage frame delay
    private float bounceTime = 0; // Tracks time for bouncing
    private float bounceSpeed = 0.1f; // Speed of the bounce
    private float bounceAmplitude = 2.0f; // Height of the bounce

    boolean isGameStarted1=false;
    int dinoRate1=0;
    int dinoMaxRate1=3;
    int dinoRate2=0;
    int dinoMaxRate2=6;
    boolean isGameStarted2=false;
    private GLUT glut;

    int score = 0; // نقاط اللاعب
    int scoreIncrement = 1;


    public AnimGLEventListener() {
    }

    String[] textureNames = { "frame_00_delay-0.08s.png","frame_01_delay-0.08s.png","frame_02_delay-0.08s.png","frame_03_delay-0.08s.png","frame_04_delay-0.08s.png","frame_05_delay-0.08s.png","frame_06_delay-0.08s.png","frame_07_delay-0.08s.png","frame_08_delay-0.08s.png","frame_09_delay-0.08s.png",
            "frame_10_delay-0.08s.png","frame_11_delay-0.08s.png","frame_12_delay-0.08s.png","frame_13_delay-0.08s.png", "frame_14_delay-0.08s.png" , "frame_15_delay-0.08s.png" , "frame_16_delay-0.08s.png" , "frame_17_delay-0.08s.png" , "frame_18_delay-0.08s.png" , "frame_19_delay-0.08s.png",
            "frame_20_delay-0.08s.png" , "frame_21_delay-0.08s.png" , "frame_22_delay-0.08s.png" , "frame_23_delay-0.08s.png" , "frame_24_delay-0.08s.png" , "frame_25_delay-0.08s.png" , "frame_26_delay-0.08s.png" , "frame_27_delay-0.08s.png" , "frame_28_delay-0.08s.png" , "frame_29_delay-0.08s.png" ,
            "frame_30_delay-0.08s.png", "frame_31_delay-0.08s.png" , "frame_32_delay-0.08s.png" , "frame_33_delay-0.08s.png" , "frame_34_delay-0.08s.png" , "frame_35_delay-0.08s.png" , "frame_36_delay-0.08s.png" , "frame_37_delay-0.08s.png" , "frame_38_delay-0.08s.png" , "frame_39_delay-0.08s.png" ,
            "frame_40_delay-0.08s.png" , "frame_41_delay-0.08s.png" , "frame_42_delay-0.08s.png" , "frame_43_delay-0.08s.png" , "frame_44_delay-0.08s.png" , "frame_45_delay-0.08s.png" , "frame_46_delay-0.08s.png" , "frame_47_delay-0.08s.png" , "frame_48_delay-0.08s.png" , "frame_49_delay-0.08s.png" ,
            "frame_50_delay-0.08s.png" , "frame_51_delay-0.08s.png" , "frame_52_delay-0.08s.png" , "frame_53_delay-0.08s.png" , "frame_54_delay-0.08s.png" , "frame_55_delay-0.08s.png" , "frame_56_delay-0.08s.png" , "frame_57_delay-0.08s.png" , "frame_58_delay-0.08s.png" , "frame_59_delay-0.08s.png" ,
            "frame_60_delay-0.08s.png" ,"playerOne-1.png","playerOne-2.png","playerOne-3.png","playerOne-1.png","rock4.png","rock5.png","rock6.png","playerTwo-2.png","playerTwo-3.png","playerTwo-1.png","start-game.png","select.png","one-player.png","two-players.png","exit.png","T-ReX-GAME.png" ,"T-rexBG.png"
    };//68 69 70
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    public int[] textures = new int[textureNames.length];

    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA,
                        texture[i].getWidth(),
                        texture[i].getHeight(),
                        GL.GL_RGBA,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels()
                );
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();

        gl.glColor3f(1.0f, 1.0f, 1.0f);
//
        // تمكين المزج للتعامل مع الشفافية
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
//
        // Animation logic
        frameCounter++;
        if (frameCounter >= frameDelay) {
            currentFrame = (currentFrame + 1) % 60; // Exclude background and overlay //60 is images of frame
            frameCounter = 0; // Reset the counter
        }

        // Increment time for bounce effect
        bounceTime += bounceSpeed;

        // Draw animation in the upper half
        DrawSprite(gl, 45, 25, currentFrame, 0.7f, 0.5f); // Bottom area
        // Draw background (bottom layer)
        DrawBackground(gl);

        // Draw animation in the lower half
        DrawSprite(gl, 45, 68, currentFrame, 0.7f, 0.4f); // Top area


        // Calculate bounce offset for the overlay
        float bounceOffset = (float) Math.sin(bounceTime) * bounceAmplitude;




        if (isGameStarted1){
            DrawPlayerOne(gl,dinoIndex1);
        }
        else if(isGameStarted2){
            DrawPlayerTwo(gl,dinoIndex1);
        }
        else {
            // Draw the T-ReX-GAME.png overlay with bouncing effect
            DrawSprite(gl, 44, (int) (68 + bounceOffset), textures.length - 2, 0.5f, 0.35f);
            DrawMenu(gl);
        }


        dinoRate1++;
        if(dinoRate1>=dinoMaxRate1) {
            dinoRate1=0;
//            if(!isDown1)
            dinoIndex1 = (dinoIndex1 == 62) ? 63 : 62;// Toggle between bird1 (4) and bird2 (5)

//            else if(isDown1){
//                dinoIndex1 = (dinoIndex1 == 68) ? 69 : 68;
//            }

        }

        dinoRate2++;
        if(dinoRate2>=dinoMaxRate2) {
            dinoRate2=0;
//            if(!isDown2)
                dinoIndex2 = (dinoIndex2 == 68) ? 69 : 68;// Toggle between bird1 (4) and bird2 (5)

//            else if(isDown2){
//                dinoIndex2 = (dinoIndex2 == 68) ? 69 : 68;
//            }

        }
        score += scoreIncrement;
        DrawScore(gl,score);

//        if(selectedOption==0){
//            DrawPlayerOne(gl, dinoIndex);
//        }
//        dinoIndex++;
    }

    private void DrawScore(GL gl, int score) {
        String scoreText = "Score: " + score;

        // إعداد النص للرسم
        gl.glColor3f(0.0f, 0.0f, 0.0f); // لون النص (أسود)
        gl.glRasterPos2f(-0.95f, 0.85f); // موقع النص في الشاشة

        for (char c : scoreText.toCharArray()) {
            GLUT glut=new GLUT();
            glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_18, c); // طباعة كل حرف
        }
    }






    @Override
    public void keyTyped(KeyEvent e) {

    }

    public BitSet keyBits = new BitSet(256);



    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        keyBits.set(key);


//        if(key == KeyEvent.VK_SPACE){
//            if (!isJump) {
//                isJump = true;
//            }
//            dinoIndex=3;
//        }

        // Up arrow key moves the selection up
        if (key == KeyEvent.VK_UP) {
            selectedOption = (selectedOption - 1 + 3) % 3; // Wrap around
        }

        // Down arrow key moves the selection down
         if (key == KeyEvent.VK_DOWN) {
            selectedOption = (selectedOption + 1) % 3;// Wrap around
//             isDown1=true;


        }

//        if (key == KeyEvent.VK_S) {
//            isDown2=true;
//        }

        // Enter key (or Space) to select an option
         if (key == KeyEvent.VK_ENTER) {

            handleMenuSelection();// Implement this method to handle the current selection

        }
        if (key==KeyEvent.VK_SPACE) {
            if (!isJump1) {
                isJump1 = true;
            }
            dinoIndex1=64;
        }
        if (key==KeyEvent.VK_W) {
            if (!isJump2) {
                isJump2 = true;
            }
            dinoIndex2=70;
        }
//         if (key == KeyEvent.VK_SPACE) {
//            if (!isJump) {
//                isJump = true;
//            }
//            dinoIndex=64;
//        }
//         if(key == KeyEvent.VK_S){
//            if(!isDown){
//                isDown=true;
//            }
////            index 9 & 10 => dino-duck
//            dinoIndex = (dinoIndex == 68) ? 69 : 68;
//        }
//        else {
//            if (isDown){
//                isDown=false;
//            }
////            index 1&2 =>dino-run
//            dinoIndex=(dinoIndex == 62)?63:62;
//        }
        }


    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        keyBits.clear(key);

//        if (key == KeyEvent.VK_DOWN) {
//            isDown1 = false;
//        }
//        if (key == KeyEvent.VK_S) {
//            isDown2 = false;
//        }
    }
    public void DrawSprite(GL gl,int x, int y, int index, float scaleX , float scaleY){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated( x/(maxWidth/2.0) - 0.9, y/(maxHeight/2.0) - 0.9, 0);
        gl.glScaled(scaleX, scaleY, 1);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }
    public void DrawBackground(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[textures.length - 1]);

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);

    }
    public void DrawPlayerOne(GL gl, int index){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);

        DrawSprite(gl, x, y, dinoIndex1, 0.2f, 0.2f);
        if(x1>15 && x1<75) {
            DrawSprite(gl, x1, 55, treeIndex, 0.09f, 0.09f);
        }

        //collosion
        if (checkCollision(x, y, x1, y1)) {
            GameOver = true;
            System.out.println("Collision detected! Game Over.");
            return;
        }

        //tree
        x1 -= treeSpeed;
        if(x1<0){
            x1=maxWidth-10;
            y1=27;
            treeIndex = (int)(Math.random()*3)+65;
        }

        //dinojump
        if (isJump1) {
            y += jumpy1;
            jumpy1 -= 1;
            if (y <= 60) {
                y = 60;
                isJump1 = false;
                jumpy1 = 6;
            }
        }

    }
    public void DrawPlayerTwo(GL gl, int index){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);

        DrawSprite(gl, x, y, dinoIndex1, 0.2f, 0.2f);
        DrawSprite(gl, x2, y2, dinoIndex2, 0.2f, 0.2f);


        if(x1>15 && x1<75) {
            DrawSprite(gl, x1, 55, treeIndex, 0.09f, 0.09f);

        }
        if(x1>20 && x1<67){
            DrawSprite(gl, x1, 8, treeIndex, 0.09f, 0.09f);
        }

        //collosion
        if (checkCollision(x, y, x1, y1)) {
            GameOver = true;
            System.out.println("Collision detected! Game Over.");
            return;
        }

        //tree
        x1 -= treeSpeed;
        if(x1<0){
            x1=maxWidth-10;
            y1=27;
            treeIndex = (int)(Math.random()*3)+65;
        }

        //dinojump
        if (isJump1) {
            y += jumpy1;
            jumpy1 -= 1;
            if (y <= 60) {
                y = 60;
                isJump1 = false;
                jumpy1 = 6;
            }
        }
        if (isJump2) {
            y2 += jumpy2;
            jumpy2 -= 1;
            if (y2 <= 13) {
                y2 = 13;
                isJump2 = false;
                jumpy2 = 6;
            }
        }

    }
    private boolean checkCollision(double x1, double y1, double x2, double y2) {

        double dinoWidth = 10;
        double dinoHeight = 12;


        double obstacleWidth = 8;
        double obstacleHeight = 10;


        boolean collisionX = x1 < x2 + obstacleWidth && x1 + dinoWidth > x2;
        boolean collisionY = y1 < y2 + obstacleHeight && y1 + dinoHeight > y2;

        return collisionX && collisionY;
    }

    public void DrawMenu(GL gl) {
        // Draw "Start Game"
        int startGameX = 45;
        int startGameY = 33;
        float startGameScaleX = (float) (2.5 * 0.2f);
        float startGameScaleY = (float) (2.5 * 0.05f);
        DrawSprite(gl, startGameX, startGameY, textures.length - 7, startGameScaleX, startGameScaleY);

        // Draw "1-Player"
        int onePlayerX = 45;  // Adjust X position for centering
        int onePlayerY = startGameY - 10;  // 8 units below Start Game
        float onePlayerScaleX = 0.25f; // Adjust horizontal scale
        float onePlayerScaleY = 0.05f; // Adjust vertical scale
        DrawSprite(gl, onePlayerX, onePlayerY, textures.length - 5, onePlayerScaleX, onePlayerScaleY);

        // Draw "2-Players"
        int twoPlayersX = 45;
        int twoPlayersY = onePlayerY - 8; // 8 units below 1-Player
        float twoPlayersScaleX = 0.25f; // Adjust horizontal scale
        float twoPlayersScaleY = 0.05f; // Adjust vertical scale
        DrawSprite(gl, twoPlayersX, twoPlayersY, textures.length - 4, twoPlayersScaleX, twoPlayersScaleY);
        // Draw "Exit"
        int exitX = 45;
        int exitY = twoPlayersY - 8; // 8 units below 2-Players
        float exitScaleX = 0.5f * 0.25f; // Adjust horizontal scale
        float exitScaleY = 0.75f * 0.05f; // Adjust vertical scale
        DrawSprite(gl, exitX, exitY, textures.length - 3, exitScaleX, exitScaleY);

        // Draw the select next to the current option (1-Player, 2-Players, Exit)
        int arrowX = 30;
        int arrowY = onePlayerY;

        // Adjust arrow Y based on the selected option (1-Player = 0, 2-Players = 1, Exit = 2)
        switch (selectedOption) {
            case 0: // 1-Player
                arrowY = onePlayerY;
                break;
            case 1: // 2-Players
                arrowY = twoPlayersY;
                break;
            case 2: // Exit
                arrowY = exitY;
                break;
        }

        DrawSprite(gl, arrowX, arrowY, textures.length - 6, 0.05f, 0.05f); // select.png
    }

    public void handleMenuSelection() {
        switch (selectedOption) {
            case 0:
                System.out.println("1-Player Mode Selected");
                startGame1();

                break;
            case 1:
                System.out.println("2-Players Mode Selected");
                startGame2();
                break;
            case 2:
                System.out.println("Exit Selected");
                System.exit(0);
                break;
        }
    }
    public void startGame1() {
        isGameStarted1 = true;
    }
    public void startGame2() {
        isGameStarted2 = true;
    }




    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
}
