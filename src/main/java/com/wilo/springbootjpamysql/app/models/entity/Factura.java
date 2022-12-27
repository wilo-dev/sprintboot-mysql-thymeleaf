package com.wilo.springbootjpamysql.app.models.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "facturas")
public class Factura implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String description;
    private String observacion;

    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;

    @Column(name = "create_at")
    @Temporal(TemporalType.DATE)
    private Date createAt;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "factura_id")
    private List<ItemFactura> items;

    // generar la fecha de creacion
    @PrePersist
    public void prePersist() {
        createAt = new Date();
    }


//    public Factura(Long id, String description, String observacion,
//                   Date createAt, Client client, List<ItemFactura> items) {
//        this.id = id;
//        this.description = description;
//        this.observacion = observacion;
//        this.createAt = createAt;
//        this.client = client;
//        this.items = items;
//    }

    public Factura() {
        this.items = new ArrayList<ItemFactura>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<ItemFactura> getItems() {
        return items;
    }

    // guarda todos los items
    public void setItems(List<ItemFactura> items) {
        this.items = items;
    }

    // guarda un solo item
    public void addItemFactura(ItemFactura item) {
        items.add(item);
    }

    public Double getRoundTwoDecimal(Double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    public Double getSubtotal() {
        Double cantidad = 0.0;
        Double subtotal = 0.0;

        int size = items.size();

        for (int i = 0; i < size; i++) {
            cantidad += items.get(i).calcularImporte();
//            subtotal = Math.round(cantidad * 100.0) / 100.0;
            subtotal = getRoundTwoDecimal(cantidad);
        }

        return subtotal;
    }

    public Double getIva() {
        Double iva = 0.0;
        iva = getRoundTwoDecimal(getSubtotal() * 0.12);
        return iva;
    }

    public Double getTotal() {
        Double total = 0.0;
        total = getRoundTwoDecimal(getSubtotal() + getIva());
        return total;
    }

    private static final long serialVersionUID = 1L;

}

