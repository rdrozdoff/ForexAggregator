package org.home.forex.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(exclude = {"timestamp"})
public class Quote {
    private String symbol;
    private Long timestamp;
    private Double open;
    private Double close;
    private Double high;
    private Double low;
}
