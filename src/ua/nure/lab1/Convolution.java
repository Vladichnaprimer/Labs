package ua.nure.lab1;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.*;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
public class Convolution {
    public static void main(String[] argv) throws Exception {
        try {
            BufferedImage buff_original;
            buff_original = ImageIO.read(new File("mountain.jpg"));
            float val=1f/49f;
            float[]data={ val, val,val ,val, val, val, val, val, val,
                          val, val,val ,val, val, val, val, val, val,
                          val, val,val ,val, val, val, val, val, val,
                          val, val,val ,val, val, val, val, val, val,
                          val, val,val ,val, val, val, val, val, val,
                          val, val, val, val};
            Kernel kernel = new Kernel(7, 7,data);
            BufferedImageOp ConOp = new ConvolveOp(kernel);
            buff_original = ConOp.filter(buff_original, null);
            JPanel content = new JPanel();
            content.setLayout(new FlowLayout());

            content.add(new JLabel(new ImageIcon(buff_original)));
            JFrame f = new JFrame("Convolution Image ");
            f.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            f.setContentPane(content);
            f.pack();
            f.setVisible(true);
        } catch (IOException e) {
        }
    }

}
