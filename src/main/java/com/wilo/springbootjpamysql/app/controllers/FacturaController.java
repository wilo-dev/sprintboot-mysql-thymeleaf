package com.wilo.springbootjpamysql.app.controllers;

import com.wilo.springbootjpamysql.app.models.entity.Client;
import com.wilo.springbootjpamysql.app.models.entity.Factura;
import com.wilo.springbootjpamysql.app.models.entity.ItemFactura;
import com.wilo.springbootjpamysql.app.models.entity.Product;
import com.wilo.springbootjpamysql.app.models.services.IClientService;
import com.wilo.springbootjpamysql.app.models.services.facturaService.IFacturaService;
import com.wilo.springbootjpamysql.app.models.services.productService.IProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {

    private final Logger logger = LoggerFactory.getLogger(FacturaController.class);


    @Autowired
    private IClientService clientService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IFacturaService facturaService;


      /*
   ================================================================================
                                       CREATE
   ================================================================================
   ***/

    @GetMapping("/form/{clientId}")
    public String create(@PathVariable(value = "clientId") Long clientId,
                         Map<String, Object> model, RedirectAttributes flash) {
        Client client = clientService.findById(clientId);

        // validar si existe client
        if (client == null) {
            flash.addFlashAttribute("error", "El cliente no existe en la DB ");
            return "redirect:/list";
        }

        Factura factura = new Factura();
        factura.setClient(client);

        model.put("factura", factura);
        model.put("title", "Crear factura");
        return "factura/form";
    }


   /*
   ================================================================================
                                       SAVE ARRAY
   ================================================================================
   **/

    //    @PostMapping("/factura/form/{id}")
    @PostMapping("/form")
    public String save(@Valid Factura factura,
                       BindingResult result,
                       Model model,
                       @RequestParam(name = "item_id[]", required = false) Long[] itemId,
                       @RequestParam(name = "cantidad[]", required = false) Integer[] cantidad,
                       RedirectAttributes flash,
                       SessionStatus status) {

        // mostrar msn de error en la vista
        if (result.hasErrors()) {
            model.addAttribute("title", "Crear factura");
            return "factura/form";
        }

        // validamos la cantidad y itemId
        if (itemId == null || itemId.length == 0) {
            model.addAttribute("title", "Crear factura");
            model.addAttribute("error", "Error: La factura no puede estar vacía");
            return "factura/form";
        }

//        logger.info("itemId: {}, cantidad: {}", itemId, cantidad);

        // recorro los item del producto
        for (int i = 0; i < itemId.length; i++) {
            Product product = facturaService.findProductById(itemId[i]);

            ItemFactura itemProduct = new ItemFactura();
            itemProduct.setCantidad(cantidad[i]);
            itemProduct.setProduct(product);
            factura.addItemFactura(itemProduct);

            logger.info("id: {}, cantidad: {}", itemId[i].toString(), cantidad[i].toString());
        }

        // validar q product no este vacio
//        if (!factura.getItems().isEmpty()) {
//
//        }

        facturaService.save(factura);
        status.setComplete();
        flash.addFlashAttribute("success", "Factura creada con exito");
        return "redirect:/ver/" + factura.getClient().getId();
    }


   /*
   ================================================================================
                                       SEARCH
   ================================================================================
   ***/

    @GetMapping(value = "/load-product/{term}", produces = {"application/json"})
    public @ResponseBody List<Product> loadProduct(@PathVariable String term) {
        return productService.findProductByName(term);
    }

   /*
   ================================================================================
                                       show invoice by id
   ================================================================================
   **/

    @GetMapping("/ver/{id}")
    public String List(@PathVariable("id") Long id, Model model,
                       RedirectAttributes flash) {

//        Factura factura = facturaService.findFacturaById(id); // trae uno por uno las consultas
        Factura factura = facturaService.fetchFacturaByIdWithClientWithItemWithProduct(id);

        // verify that the invoice exists
        if (factura == null) {
            flash.addFlashAttribute("error", "La factura no existe en la DB");
            return "redirect:/list";
        }
        model.addAttribute("factura", factura);
        model.addAttribute("title", "Invoice detail ".concat(factura.getDescription()));
        return "factura/ver";
    }

       /*
   ================================================================================
                                      DELETE FACTURA
   ================================================================================
   **/

    @GetMapping("/delete/{id}")
    public String deleteFactura(@PathVariable("id") Long id, RedirectAttributes flash) {
        Factura factura = facturaService.findFacturaById(id);

        if (factura != null) {
            facturaService.deleteFactura(id);
            flash.addFlashAttribute("success", "Factura eliminada con exito");
            return "redirect:/ver/" + factura.getClient().getId();
        }
        flash.addFlashAttribute("error", "La factura no existe");
        return "redirect:/list";
    }


}

/*
BindingResult result.- me permite comprobar si hubieron errores en la factura
* **/
