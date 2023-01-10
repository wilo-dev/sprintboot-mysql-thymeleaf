package com.wilo.springbootjpamysql.app.view.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.wilo.springbootjpamysql.app.models.entity.Factura;
import com.wilo.springbootjpamysql.app.models.entity.ItemFactura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.Locale;
import java.util.Map;

@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView {

    @Autowired
    private MessageSource messageSource; // metodo 1 d traduccion
    @Autowired
    private LocaleResolver localeResolver; // metodo 1 d traduccion


    public PdfPCell headerCell(String title, int r, int g, int b) {
        PdfPCell cell = new PdfPCell(new Phrase(title));
        cell.setBackgroundColor(new Color(r, g, b));
        cell.setPadding(8f);
        return cell;
    }

    public PdfPCell footerCell(String title, int r, int g, int b, int align) {
        PdfPCell cell = new PdfPCell(new Phrase(title));
        cell.setBackgroundColor(new Color(r, g, b));
        cell.setHorizontalAlignment(align);
        cell.setColspan(3);
        cell.setPadding(4f);
        return cell;
    }


    @Override
    protected void buildPdfDocument(Map<String, Object> model,
                                    Document document,
                                    PdfWriter writer,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {

        Factura factura = (Factura) model.get("factura");
        Locale locale = localeResolver.resolveLocale(request);
        MessageSourceAccessor message = getMessageSourceAccessor(); // metodo 2 d traduccion


        String title;

    /*
  ================================================================================
       table #1 datos del cliente
  ================================================================================
  ***/
        PdfPTable table1 = new PdfPTable(1);
        table1.setSpacingAfter(20);

        title = messageSource.getMessage("text.detalle.cliente", null, locale);
        table1.addCell(headerCell(title, 184, 218, 255));

        table1.addCell("Nombre: " + factura.getClient().getName().concat(factura.getClient().getFirst_name()));
        table1.addCell("Correo electrónico: " + factura.getClient().getEmail());

    /*
  ================================================================================
       table #1 datos de la factura
  ================================================================================
  ***/
        PdfPTable table2 = new PdfPTable(1);
        table2.setSpacingAfter(20);

        title = messageSource.getMessage("text.detalle.factura", null, locale);
        table2.addCell(headerCell(title, 195, 230, 203));

        assert message != null;
        table2.addCell(message.getMessage("text.detalle.factura.folio") + ": #".concat(String.valueOf(factura.getId())));
        table2.addCell(message.getMessage("text.detalle.factura.descripcion") + ": ".concat(factura.getDescription()));
        table2.addCell(message.getMessage("text.detalle.factura.fecha") + ": " + factura.getCreateAt());

        // guardar
        document.add(table1);
        document.add(table2);

            /*
  ================================================================================
       table #3 Items de la facura
  ================================================================================
  ***/

        PdfPTable table3 = new PdfPTable(4);
        table3.setSpacingAfter(20);
        table3.setWidths(new float[]{3.5f, 1, 1, 1});

        title = messageSource.getMessage("text.detalle.items.producto", null, locale);
        table3.addCell(headerCell(title, 236, 240, 241));

        title = messageSource.getMessage("text.detalle.items.precio", null, locale);
        table3.addCell(headerCell(title, 236, 240, 241));

        title = messageSource.getMessage("text.detalle.items.cantidad", null, locale);
        table3.addCell(headerCell(title, 236, 240, 241));

        title = messageSource.getMessage("text.detalle.items.total", null, locale);
        table3.addCell(headerCell(title, 236, 240, 241));

        // recorremos cada item
        for (ItemFactura item : factura.getItems()) {
            table3.addCell(item.getProduct().getName());
            table3.addCell(item.getProduct().getPrecio().toString());

            PdfPCell cell4 = new PdfPCell(new Phrase(item.getCantidad().toString()));
            cell4.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table3.addCell(cell4);
            table3.addCell(item.calcularImporte().toString());
        }

        // footer
//        PdfPCell cell1 = new PdfPCell(new Phrase("Subtotal "));
//        cell1.setColspan(3);
//        cell1.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
//        table3.addCell(cell1);
        table3.addCell(footerCell("Subtotal ", 255, 255, 255, 2));
        table3.addCell(factura.getSubtotal().toString());


        table3.addCell(footerCell("I.V.A 12% ", 255, 255, 255, 2));
        table3.addCell(factura.getIva().toString());


        table3.addCell(footerCell("Total a pagar", 254, 164, 127, 2));
        String totalPagar = factura.getTotal().toString();
        table3.addCell(footerCell(totalPagar, 254, 164, 127, 3));


        document.add(table3);
    }

}
