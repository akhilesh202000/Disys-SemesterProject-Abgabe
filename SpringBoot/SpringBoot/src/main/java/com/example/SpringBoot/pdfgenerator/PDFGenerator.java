package com.example.SpringBoot.pdfgenerator;


import com.example.SpringBoot.entities.ChargingStationData;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.util.List;

public class PDFGenerator {

    public static void generatePdf(List<ChargingStationData> csdList) throws Exception {

        boolean isEmpty = csdList == null || csdList.isEmpty();

        if(isEmpty) {
            System.out.println("No PDF created because there is no Charging Station Data available for this customer");
            return;
        }
        // Creating a PdfWriter
        String dest = String.format("%s.pdf", csdList.get(0).customerId);
        PdfWriter writer = new PdfWriter(dest);

        // Creating a PdfDocument
        PdfDocument pdfDoc = new PdfDocument(writer);

        // Adding a new page
        pdfDoc.addNewPage();

        // Creating a Document
        Document document = new Document(pdfDoc);

        document.add(new Paragraph( "Customer ID: " + csdList.get(0).customerId));
        for(ChargingStationData csd : csdList) {
            document.add(new Paragraph(String.valueOf(csd)));
        }


        // Closing the document
        document.close();
        System.out.println("PDF Created");
    }
}

