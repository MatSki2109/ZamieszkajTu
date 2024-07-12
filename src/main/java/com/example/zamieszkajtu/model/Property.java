package com.example.zamieszkajtu.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "properties")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titleProperty;

    private String categoryProperty;

    private String city;

    private String address;

    private double price;

    @Column(columnDefinition = "TEXT")
    private String description;
    private String imageName;



}
