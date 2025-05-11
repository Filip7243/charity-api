package com.charity.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
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
            name = "box_moneys",
            joinColumns = @JoinColumn(name = "box_id")
    )
    private List<Money> moneys = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "fundraising_event_id")
    private FundraisingEvent fundraisingEvent;

    public CollectionBox(List<Money> moneys, FundraisingEvent fundraisingEvent) {
        this.moneys = moneys;
        this.fundraisingEvent = fundraisingEvent;
    }
}
