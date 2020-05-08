package org.home.forex.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "quote")
@Getter @Setter
public class QuoteEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String symbol;
    private Long timestamp;
    private Double open;
    private Double close;
    private Double high;
    private Double low;
}
