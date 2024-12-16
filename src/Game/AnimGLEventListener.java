package Game;

import Texture.TextureReader;
import Texture.AnimListener;
import java.awt.event.*;
import java.io.IOException;
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;

public class AnimGLEventListener extends AnimListener{
    int maxWidth = 100;
    int maxHeight = 100;
    int selectedOption = 0;

    // Animation-related variables
    private int currentFrame = 0; // Current frame index
    private int frameDelay = 4;   // Number of display calls before moving to the next frame
    private int frameCounter = 0; // Counter to manage frame delay
    private float bounceTime = 0; // Tracks time for bouncing
    private float bounceSpeed = 0.1f; // Speed of the bounce
    private float bounceAmplitude = 2.0f; // Height of the bounce


    public AnimGLEventListener() {
    }

    String[] textureNames = { "frame_00_delay-0.08s.png","frame_01_delay-0.08s.png","frame_02_delay-0.08s.png","frame_03_delay-0.08s.png","frame_04_delay-0.08s.png","frame_05_delay-0.08s.png","frame_06_delay-0.08s.png","frame_07_delay-0.08s.png","frame_08_delay-0.08s.png","frame_09_delay-0.08s.png",
            "frame_10_delay-0.08s.png","frame_11_delay-0.08s.png","frame_12_delay-0.08s.png","frame_13_delay-0.08s.png", "frame_14_delay-0.08s.png" , "frame_15_delay-0.08s.png" , "frame_16_delay-0.08s.png" , "frame_17_delay-0.08s.png" , "frame_18_delay-0.08s.png" , "frame_19_delay-0.08s.png",
            "frame_20_delay-0.08s.png" , "frame_21_delay-0.08s.png" , "frame_22_delay-0.08s.png" , "frame_23_delay-0.08s.png" , "frame_24_delay-0.08s.png" , "frame_25_delay-0.08s.png" , "frame_26_delay-0.08s.png" , "frame_27_delay-0.08s.png" , "frame_28_delay-0.08s.png" , "frame_29_delay-0.08s.png" ,
            "frame_30_delay-0.08s.png", "frame_31_delay-0.08s.png" , "frame_32_delay-0.08s.png" , "frame_33_delay-0.08s.png" , "frame_34_delay-0.08s.png" , "frame_35_delay-0.08s.png" , "frame_36_delay-0.08s.png" , "frame_37_delay-0.08s.png" , "frame_38_delay-0.08s.png" , "frame_39_delay-0.08s.png" ,
            "frame_40_delay-0.08s.png" , "frame_41_delay-0.08s.png" , "frame_42_delay-0.08s.png" , "frame_43_delay-0.08s.png" , "frame_44_delay-0.08s.png" , "frame_45_delay-0.08s.png" , "frame_46_delay-0.08s.png" , "frame_47_delay-0.08s.png" , "frame_48_delay-0.08s.png" , "frame_49_delay-0.08s.png" ,
            "frame_50_delay-0.08s.png" , "frame_51_delay-0.08s.png" , "frame_52_delay-0.08s.png" , "frame_53_delay-0.08s.png" , "frame_54_delay-0.08s.png" , "frame_55_delay-0.08s.png" , "frame_56_delay-0.08s.png" , "frame_57_delay-0.08s.png" , "frame_58_delay-0.08s.png" , "frame_59_delay-0.08s.png" ,
            "frame_60_delay-0.08s.png" ,"start-game.png","select.png","one-player.png","two-players.png","exit.png","T-ReX-GAME.png" ,"T-rexBG.png"
    };
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


        // Animation logic
        frameCounter++;
        if (frameCounter >= frameDelay) {
            currentFrame = (currentFrame + 1) % (textureNames.length - 7); // Exclude background and overlay
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

        // Draw the T-ReX-GAME.png overlay with bouncing effect
        DrawSprite(gl, 44, (int) (68 + bounceOffset), textures.length - 2, 0.5f, 0.35f);

        DrawMenu(gl);

    }
// drawscore
    public void drawscore(GL gl) {
        gl.glColor3f(0.0f, 0.0f, 0.0f); // Set color to black
        gl.glRasterPos2f(0.4f, 0.8f); // Set text position (x, y)
        // Use a library like FreeType for advanced font rendering
        // For a simple example, you can use the built-in font rendering functions (if available)
        // but they might be limited.
       // Font font = new Font("Arial", Font.BOLD, 20); // Assuming you have a Font class
        String Score = "Score: " + score;
        for (char c:Score.toCharArray()) {
            GLUT glut =new GLUT();
            glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_18, c);
        }
    }
    // Timer
    public void Timer(GL gl) {
        gl.glColor3f(0.0f, 0.0f, 0.0f); // Set color to black
        gl.glRasterPos2f(0.4f, 0.7f); // Set text position (x, y)
        // Use a library like FreeType for advanced font rendering
        // For a simple example, you can use the built-in font rendering functions (if available)
        // but they might be limited.
        // Font font = new Font("Arial", Font.BOLD, 20); // Assuming you have a Font class
        String Time = "Time: " + time;
        for (char c:Time.toCharArray()) {
            GLUT glut =new GLUT();
            glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_18, c);
        }
    }




    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // Up arrow key moves the selection up
        if (key == KeyEvent.VK_UP) {
            selectedOption = (selectedOption - 1 + 3) % 3; // Wrap around
        }

        // Down arrow key moves the selection down
        if (key == KeyEvent.VK_DOWN) {
            selectedOption = (selectedOption + 1) % 3; // Wrap around
        }

        // Enter key (or Space) to select an option
        if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_SPACE) {
            handleMenuSelection(); // Implement this method to handle the current selection
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {

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
                break;
            case 1:
                System.out.println("2-Players Mode Selected");
                break;
            case 2:
                System.out.println("Exit Selected");
                System.exit(0);
                break;
        }
    }


    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
}
