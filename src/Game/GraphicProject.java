package Game;

import com.sun.opengl.util.*;
import javax.media.opengl.*;
import javax.swing.*;
import java.awt.*;

public class GraphicProject extends JFrame {

    public static void main(String[] args) {

        new GraphicProject();
    }

    public GraphicProject() {
        AnimGLEventListener listener = new AnimGLEventListener();
        GLCanvas glcanvas = new GLCanvas();
        glcanvas.addGLEventListener(listener);
        glcanvas.addKeyListener(listener);
        getContentPane().add(glcanvas, BorderLayout.CENTER);
        Animator animator = new FPSAnimator(glcanvas, 60);
        animator.start();
        configureWindow();
        glcanvas.requestFocus();
    }

    private void configureWindow() {
        setTitle(" T-REX GAME :D ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
    }
}
