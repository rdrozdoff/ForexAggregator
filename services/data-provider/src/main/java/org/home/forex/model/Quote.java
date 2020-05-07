package org.home.forex.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Quote {
    private String symbol;
    private Long timestamp;
    private Double open;
    private Double close;
    private Double high;
    private Double low;
}
