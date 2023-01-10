package com.wilo.springbootjpamysql.app.view.xlsx;

import com.wilo.springbootjpamysql.app.models.entity.Factura;
import com.wilo.springbootjpamysql.app.models.entity.ItemFactura;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component("factura/ver.xlsx")
public class FacturaXlsxView extends AbstractXlsxView {
    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook,
                                      HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Factura factura = (Factura) model.get("factura");

        Sheet sheet = workbook.createSheet("facturaSpring");

  /*
  ================================================================================
       celda #1 datos del cliente, primera forma de crear
  ================================================================================
  **/
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Datos del cliente");

        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue(factura.getClient().getName() + " " + factura.getClient().getFirst_name());

        row = sheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue(factura.getClient().getEmail());

  /*
  ================================================================================
       celda #2 Datos de la factura, segunda forma de crear
  ================================================================================
  **/

        sheet.createRow(4).createCell(0).setCellValue("Datos de la factura");
        sheet.createRow(5).createCell(0).setCellValue("Folio: " + factura.getId());
        sheet.createRow(6).createCell(0).setCellValue("Descripcion: " + factura.getDescription());
        sheet.createRow(7).createCell(0).setCellValue("Fecha: " + factura.getCreateAt());


        /*
  ================================================================================
       celda #3 Detalles de la factura
  ================================================================================
  **/

        Row header = sheet.createRow(9);
        header.createCell(0).setCellValue("Producto");
        header.createCell(1).setCellValue("Precio");
        header.createCell(2).setCellValue("Cantidad");
        header.createCell(3).setCellValue("Total");

        int rownum = 10;
        for (ItemFactura item : factura.getItems()) {
            Row file = sheet.createRow(rownum++);
            file.createCell(0).setCellValue(item.getProduct().getName());
            file.createCell(1).setCellValue(item.getProduct().getPrecio());
            file.createCell(2).setCellValue(item.getCantidad());
            file.createCell(3).setCellValue(item.calcularImporte());
        }
    
        Row fileTotal = sheet.createRow(rownum);
        fileTotal.createCell(2).setCellValue("Subtotal: ");
        fileTotal.createCell(3).setCellValue(factura.getSubtotal());

        fileTotal = sheet.createRow(rownum + 1);
        fileTotal.createCell(2).setCellValue("I.V.A 12%: ");
        fileTotal.createCell(3).setCellValue(factura.getIva());

        fileTotal = sheet.createRow(rownum + 2);
        fileTotal.createCell(2).setCellValue("Total a pagar: ");
        fileTotal.createCell(3).setCellValue(factura.getTotal());


    }


}
