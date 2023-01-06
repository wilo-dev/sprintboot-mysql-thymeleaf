package com.wilo.springbootjpamysql.app.view;

import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.wilo.springbootjpamysql.app.models.entity.Factura;
import com.wilo.springbootjpamysql.app.models.entity.ItemFactura;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.Map;

@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView {
    PdfPCell cell = null;

    public PdfPCell styleCell(String title, int r, int g, int b) {
        cell = new PdfPCell(new Phrase(title));
        cell.setBackgroundColor(new Color(r, g, b));
        cell.setPadding(8f);
        return cell;
    }


    @Override
    protected void buildPdfDocument(Map<String, Object> model,
                                    Document document,
                                    PdfWriter writer,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {


        Factura factura = (Factura) model.get("factura");

//         creando el diseño del pdf
        PdfPTable table = new PdfPTable(1);
        table.setSpacingAfter(20);

        table.addCell(styleCell("Datos del cliente", 184, 218, 255));
        table.addCell("Nombre: " + factura.getClient().getName().concat(factura.getClient().getFirst_name()));
        table.addCell("Correo electrónico: " + factura.getClient().getEmail());


        PdfPTable table2 = new PdfPTable(1);

        table2.setSpacingAfter(20);
        table2.addCell(styleCell("Datos de la factura", 195, 230, 203));
        table2.addCell("Folio: #".concat(String.valueOf(factura.getId())));
        table2.addCell("Descripción: ".concat(factura.getDescription()));
        table2.addCell("Fecha: " + factura.getCreateAt());

        // guardar
        document.add(table);
        document.add(table2);

        PdfPTable table3 = new PdfPTable(4);
        table3.setWidths(new float[]{3.5f, 1, 1, 1});
        table3.setSpacingAfter(20);
        table3.addCell("Producto");
        table3.addCell("Precio");
        table3.addCell("Cantidad");
        table3.addCell("Total");

        // recorremos cada item
        for (ItemFactura item : factura.getItems()) {
            table3.addCell(item.getProduct().getName());
            table3.addCell(item.getProduct().getPrecio().toString());

            cell = new PdfPCell(new Phrase(item.getCantidad().toString()));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table3.addCell(cell);
            table3.addCell(item.calcularImporte().toString());
        }

        // footer
        PdfPCell cell1 = new PdfPCell(new Phrase("Subtotal "));
        cell1.setColspan(3);
        cell1.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        table3.addCell(cell1);
        table3.addCell(factura.getSubtotal().toString());

        PdfPCell cell2 = new PdfPCell(new Phrase("I.V.A 12% "));
        cell2.setColspan(3);
        cell2.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        table3.addCell(cell2);
        table3.addCell(factura.getIva().toString());

        PdfPCell cell3 = new PdfPCell(new Phrase("Total a pagar "));
        cell3.setColspan(3);
        cell3.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        table3.addCell(cell3);
        table3.addCell(factura.getTotal().toString());

        document.add(table3);


    }

}
