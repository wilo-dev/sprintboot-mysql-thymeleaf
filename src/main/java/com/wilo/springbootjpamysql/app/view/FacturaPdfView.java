package com.wilo.springbootjpamysql.app.view;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.wilo.springbootjpamysql.app.models.entity.Factura;
import com.wilo.springbootjpamysql.app.models.services.facturaService.IFacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView {

    @Autowired
    private IFacturaService facturaService;

    @Override
    protected void buildPdfDocument(Map<String, Object> model,
                                    Document document,
                                    PdfWriter writer,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {


        Factura factura = (Factura) model.get("factura");

        // creando pdf
        PdfPTable table = new PdfPTable(1);
        table.addCell("Datos del Cliente");
        table.addCell(factura.getClient().getName().concat(factura.getClient().getFirst_name()));
        table.addCell(factura.getClient().getEmail());

        PdfPTable table2 = new PdfPTable(1);
        table2.addCell("Datos de la Factura");
        table2.addCell("Folio".concat(String.valueOf(factura.getId())));
        table2.addCell("Descripción".concat(factura.getDescription()));
        table2.addCell("Fecha" + factura.getCreateAt());

        // guardar
        document.add(table);
        document.add(table2);

    }

}
