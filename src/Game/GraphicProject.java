package Game;

import com.sun.opengl.util.*;
import javax.media.opengl.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        Animator animator = new FPSAnimator(glcanvas, 30);
        JButton startButton = new JButton("Start Game");
        startButton.setFocusable(false);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!animator.isAnimating()) {
                    animator.start();
                    startButton.setText("Stop Game");
                } else {
                    animator.stop();
                    startButton.setText("Start Game");
                }
            }
        });


        JPanel controlPanel = new JPanel();
        controlPanel.add(startButton);

        getContentPane().add(glcanvas, BorderLayout.CENTER);
        getContentPane().add(controlPanel, BorderLayout.SOUTH);
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
