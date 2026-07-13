package org.example.productjwt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.productjwt.enums.ProductCategory;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory category;

    private String brand;
    private int stocks;

    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems = new ArrayList<>();
}
