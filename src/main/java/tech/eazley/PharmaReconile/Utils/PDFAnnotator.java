package tech.eazley.PharmaReconile.Utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationTextMarkup;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

@Component
public class PDFAnnotator extends PDFTextStripper
{
    private String[] criteria = {};

    public PDFAnnotator() throws IOException {
        super();
    }

    public void setCriteria(String[] criteria) {
        this.criteria = criteria;
    }

    @Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
        boolean isFound = false;

        float posXInit1  = 0,
                posXEnd1   = 0,
                posYInit1  = 0,
                posYEnd1   = 0,
                width1     = 0,
                height1    = 0,
                fontHeight1 = 0;

        //String[] criteria = {this.criteria[0]};
        for (int i = 0; i < criteria.length; i++) {
            if (string.contains(criteria[i])) {
                isFound = true;
            }
        }
        if (isFound) {

            for(TextPosition textPosition:textPositions) {

                posXInit1 = textPositions.get(0).getXDirAdj();
                posXEnd1  = textPositions.get(textPositions.size() - 1).getXDirAdj() + textPositions.get(textPositions.size() - 1).getWidth();
                posYInit1 = textPositions.get(0).getPageHeight() - textPositions.get(0).getYDirAdj();
                posYEnd1  = textPositions.get(0).getPageHeight() - textPositions.get(textPositions.size() - 1).getYDirAdj();
                width1    = textPositions.get(0).getWidthDirAdj();
                height1   = textPositions.get(0).getHeightDir();

            }


            float quadPoints[] = {posXInit1, posYEnd1 + height1 + 2, posXEnd1, posYEnd1 + height1 + 2, posXInit1, posYInit1 - 2, posXEnd1, posYEnd1 - 2};

            List<PDAnnotation> annotations = document.getPage(this.getCurrentPageNo() - 1).getAnnotations();
            PDAnnotationTextMarkup highlight = new PDAnnotationTextMarkup(PDAnnotationTextMarkup.SUB_TYPE_HIGHLIGHT);

            PDRectangle position = new PDRectangle();
            position.setLowerLeftX(posXInit1);
            position.setLowerLeftY(posYEnd1);
            position.setUpperRightX(posXEnd1);
            position.setUpperRightY(posYEnd1 + height1);

            highlight.setRectangle(position);

            // quadPoints is array of x,y coordinates in Z-like order (top-left, top-right, bottom-left,bottom-right)
            // of the area to be highlighted

            highlight.setQuadPoints(quadPoints);

            PDColor yellow = new PDColor(new float[]{1, 1, 1 / 255F}, PDDeviceRGB.INSTANCE);
            highlight.setColor(yellow);
            annotations.add(highlight);
        }
    }


    public byte[] run(byte[] clientData) throws IOException {
        PDDocument document = null;
        try {
            document = PDDocument.load(clientData);
            setSortByPosition( true );

            setStartPage( 0 );
            setEndPage( document.getNumberOfPages() );

            Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
            writeText(document, dummy);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            document.save(byteArrayOutputStream);

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if( document != null ) {
                document.close();
            }
        }
        return null;
    }

}
