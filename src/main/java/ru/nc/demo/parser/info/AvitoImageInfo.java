package ru.nc.demo.parser.info;

import java.awt.image.BufferedImage;

public class AvitoImageInfo {
    private BufferedImage smallImage;
    private BufferedImage bigImage;

    public AvitoImageInfo(BufferedImage smallImage, BufferedImage bigImage) {
        this.smallImage = smallImage;
        this.bigImage = bigImage;
    }

    public BufferedImage getSmallImage() {
        return smallImage;
    }
    public void setSmallImage(BufferedImage smallImage) {
        this.smallImage = smallImage;
    }
    public BufferedImage getBigImage() {
        return bigImage;
    }
    public void setBigImage(BufferedImage bigImage) {
        this.bigImage = bigImage;
    }
}
