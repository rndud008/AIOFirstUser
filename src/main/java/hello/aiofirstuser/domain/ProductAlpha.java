package hello.aiofirstuser.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ProductAlpha {

    private String sizeColorPrice;
    private int alphaPrice;
}

