package com.wilo.springbootjpamysql.app.view.style;

import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;

import java.awt.*;

public class FacturaPdfViewStyle {

    public PdfPCell headerCell1(String title, int r, int g, int b) {
        PdfPCell cell = new PdfPCell(new Phrase(title));
        cell.setBackgroundColor(new Color(r, g, b));
        cell.setPadding(8f);
        return cell;
    }

}
