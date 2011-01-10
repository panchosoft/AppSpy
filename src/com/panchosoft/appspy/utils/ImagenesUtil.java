package com.panchosoft.appspy.utils;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.Map;
import javax.swing.ImageIcon;
import sun.awt.image.BufferedImageGraphicsConfig;

/**
 *
 * @author Panchosoft
 */
public class ImagenesUtil {

    private static ImagenesUtil instancia;

    private ImagenesUtil() {
    }

    public static ImagenesUtil getInstance() {
        if (instancia == null) {
            instancia = new ImagenesUtil();
        }
        return instancia;
    }
 public ImageIcon redimensionarImagen(ImageIcon in, int w, int h) {
        BufferedImage bi = new BufferedImage(in.getIconWidth(), in.getIconHeight(), BufferedImage.TYPE_INT_ARGB);

        bi.getGraphics().drawImage(in.getImage(), 0, 0, in.getIconWidth(), in.getIconHeight(), null);
        return resizeTrick(bi,w,h);
    }



    @SuppressWarnings("unchecked")
    public BufferedImage resize(BufferedImage image, int width, int height) {
        int type = image.getType() == 0? BufferedImage.TYPE_INT_ARGB : image.getType();
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.setComposite(AlphaComposite.Src);

        g.setRenderingHint(RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }

    @SuppressWarnings("unchecked")
    public BufferedImage createCompatibleImage(BufferedImage image) {
        GraphicsConfiguration gc = BufferedImageGraphicsConfig.getConfig(image);
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage result = gc.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
        Graphics2D g2 = result.createGraphics();
        g2.drawRenderedImage(image, null);
        g2.dispose();
        return result;
    }
    public ImageIcon resizeTrick(BufferedImage image, int width, int height) {
        image = createCompatibleImage(image);
        image = resize(image, 200, 200);
        image = ImagenesUtil.getInstance().blurImage(image);
        image = resize(image, width, height);
        return new ImageIcon(image);
    }

    @SuppressWarnings("unchecked")
    public BufferedImage blurImage(BufferedImage image) {
        float ninth = 1.0f / 9.0f;
        float[] blurKernel = {
            ninth, ninth, ninth,
            ninth, ninth, ninth,
            ninth, ninth, ninth
        };

        Map map = new java.util.HashMap();

        map.put(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        map.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        map.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        RenderingHints hints = new RenderingHints(map);
        BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, blurKernel), ConvolveOp.EDGE_NO_OP, hints);
        return op.filter(image, null);
    }
}
