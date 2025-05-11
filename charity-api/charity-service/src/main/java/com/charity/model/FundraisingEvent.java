package com.charity.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FundraisingEvent {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;

    private Money amount;

    public FundraisingEvent(String name, Money amount) {
        this.name = name;
        this.amount = amount;
    }
}
