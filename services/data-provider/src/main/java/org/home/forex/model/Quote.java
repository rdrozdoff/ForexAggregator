package org.home.forex.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class Quote {
    private String symbol;
    private Long timestamp;
    private Double open;
    private Double close;
    private Double high;
    private Double low;
}
