package uk.co.v2systems.framework.utils;

/**
 * Created by PBU10 on 09/03/2016.
 */
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class ResizeImage {

    public static boolean resizeImage(String imagePath, int width, int height, String extension, String fileName, String destiny) throws IOException {
        StringBuilder nomeImagem = new StringBuilder();
        InputStream imageStream = new FileInputStream(imagePath);;
        nomeImagem.append(fileName);
        nomeImagem.append("_");
        nomeImagem.append(width);
        nomeImagem.append("x");
        nomeImagem.append(height);
        nomeImagem.append(".");
        nomeImagem.append(extension.toLowerCase());
        try {
            BufferedImage imagem = ImageIO.read(imageStream);
            Image image =  imagem.getScaledInstance(imagem.getWidth(),imagem.getHeight(),Image.SCALE_SMOOTH);

            BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = newImg.createGraphics();
            //g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, Image.SCALE_SMOOTH);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            //g.drawImage(imagem, 0, 0, width, height, null);
            g.drawImage(image, 0, 0, width, height, null);
            ImageIO.write(newImg, extension.toUpperCase(), new File(destiny + nomeImagem));
        } catch (IOException ex) {
            return false;
        } finally {
            imageStream.close();
        }
        return true;
    }
}

