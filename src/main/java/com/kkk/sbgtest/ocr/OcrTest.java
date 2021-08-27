package com.kkk.sbgtest.ocr;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.Tesseract1;
import org.junit.Test;
import sun.tools.jar.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class OcrTest {
    @Test
    public void test() {
        try {
            Tesseract tesseract = new Tesseract();
//            tesseract.setLanguage("deu");
            tesseract.setOcrEngineMode(1);
//            tesseract.setDatapath("d://data//");
            tesseract.setDatapath("data");
            tesseract.setLanguage("eng");
            File lFile = new File("d://data//ocr_abc.png");
            BufferedImage image = ImageIO.read(new FileInputStream(lFile));

            String result = tesseract.doOCR(lFile);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test2() {
        try {
            File imageFile = new File("d:/data/ocr_abc.png");
//            Tesseract instance = Tesseract.getInstance(); // JNA Interface Mapping

            Tesseract1 instance = new Tesseract1();
            instance.setDatapath("d:/data");
            instance.setLanguage("eng");
            String result = instance.doOCR(imageFile);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
