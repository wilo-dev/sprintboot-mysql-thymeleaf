package com.wilo.springbootjpamysql.app.view.xlsx;

import com.wilo.springbootjpamysql.app.models.entity.Factura;
import com.wilo.springbootjpamysql.app.models.entity.ItemFactura;
import org.apache.poi.ss.usermodel.*;
import org.springframework.context.support.MessageSourceAccessor;
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

        response.setHeader("Content-Disposition", "attachment; filename=\"\factura_view.xlsx\"");
        Factura factura = (Factura) model.get("factura");
        Sheet sheet = workbook.createSheet("facturaSpring");
        MessageSourceAccessor message = getMessageSourceAccessor();

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
        CellStyle theraderStyle = workbook.createCellStyle();
        theraderStyle.setBorderBottom(BorderStyle.MEDIUM);
        theraderStyle.setBorderTop(BorderStyle.MEDIUM);
        theraderStyle.setBorderRight(BorderStyle.MEDIUM);
        theraderStyle.setBorderLeft(BorderStyle.MEDIUM);
        theraderStyle.setFillForegroundColor(IndexedColors.GOLD.index);
        theraderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle tbodyStyle = workbook.createCellStyle();
        tbodyStyle.setBorderBottom(BorderStyle.THIN);
        tbodyStyle.setBorderTop(BorderStyle.THIN);
        tbodyStyle.setBorderRight(BorderStyle.THIN);
        tbodyStyle.setBorderLeft(BorderStyle.THIN);

        Row header = sheet.createRow(9);
        header.createCell(0).setCellValue("Producto");
        header.createCell(1).setCellValue("Precio");
        header.createCell(2).setCellValue("Cantidad");
        header.createCell(3).setCellValue("Total");

        header.getCell(0).setCellStyle(theraderStyle);
        header.getCell(1).setCellStyle(theraderStyle);
        header.getCell(2).setCellStyle(theraderStyle);
        header.getCell(3).setCellStyle(theraderStyle);

        int rownum = 10;
        for (ItemFactura item : factura.getItems()) {
            Row file = sheet.createRow(rownum++);

            cell = file.createCell(0);
            cell.setCellValue(item.getProduct().getName());
            cell.setCellStyle(tbodyStyle);

            cell = file.createCell(1);
            cell.setCellValue(item.getProduct().getPrecio());
            cell.setCellStyle(tbodyStyle);

            cell = file.createCell(2);
            cell.setCellValue(item.getCantidad());
            cell.setCellStyle(tbodyStyle);

            cell = file.createCell(3);
            cell.setCellValue(item.calcularImporte());
            cell.setCellStyle(tbodyStyle);
        }

        Row fileTotal = sheet.createRow(rownum);
        cell = fileTotal.createCell(2);
        cell.setCellValue("Subtotal: ");
        cell.setCellStyle(tbodyStyle);

        cell = fileTotal.createCell(3);
        cell.setCellValue(factura.getSubtotal());
        cell.setCellStyle(tbodyStyle);

        fileTotal = sheet.createRow(rownum + 1);
        cell = fileTotal.createCell(2);
        cell.setCellValue("I.V.A 12%: ");
        cell.setCellStyle(tbodyStyle);

        cell = fileTotal.createCell(3);
        cell.setCellValue(factura.getIva());
        cell.setCellStyle(tbodyStyle);

        fileTotal = sheet.createRow(rownum + 2);
        cell = fileTotal.createCell(2);
        cell.setCellValue("Total a pagar: ");
        cell.setCellStyle(tbodyStyle);


        cell = fileTotal.createCell(3);
        cell.setCellValue(factura.getTotal());
        cell.setCellStyle(tbodyStyle);


    }


}
