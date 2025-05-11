package com.charity.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CollectionBox {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(
            name = "box_currencies",
            joinColumns = @JoinColumn(name = "box_id")
    )
    private List<Money> currencies = new ArrayList<>();
}
