package cn.com.play.controllers.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import static javax.print.attribute.ResolutionSyntax.DPI;

public class PdfConvertUtil {
    public static String covertPdfToImage() throws IOException {
        String imagePath = "/src/path/test.png";
        File image = new File(imagePath);

        String pdfpath = "test.pdf";

        convert(new String[]{pdfpath}, imagePath);

        String imageStream = encodeFileToBase64Binary(image);
        return imageStream;
    }


    public static void convert(String[] pdfPaths, String imagePath) throws IOException {
        int x = 0, y = 0, height = 0, pageWidth = 0;
        Map<BufferedImage, Integer> pageMap = new LinkedHashMap<>();
        for (int pdfIndex = 0; pdfIndex < pdfPaths.length; pdfIndex++) {
            try (PDDocument document = PDDocument.load(new File(pdfPaths[pdfIndex]))) {
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                for (int page = 0; page < document.getNumberOfPages(); ++page) {
                    BufferedImage pageBI = pdfRenderer.renderImageWithDPI(page, DPI, ImageType.RGB);
                    int pageHeight = pageBI.getHeight();
                    height += pageHeight;
                    pageWidth = Math.max(pageWidth, pageBI.getWidth());
                    pageMap.put(pageBI, pageHeight);
                }
            }
        }
        BufferedImage result = new BufferedImage(pageWidth, height, BufferedImage.TYPE_INT_RGB);
        for (BufferedImage page : pageMap.keySet()) {
            result.getGraphics().drawImage(page, x, y, null);
            y += pageMap.get(page);
        }
        ImageIO.write(result, "png", new File(imagePath));
    }

    public static String encodeFileToBase64Binary(File file){
        String encodedfile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = new String(Base64.getEncoder().encode(bytes), "UTF-8");
        } catch (Exception e) {
            log.error("Failed to transfer file to base64.", e);
        }

        return encodedfile;
    }
}
