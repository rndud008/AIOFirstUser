package hello.aiofirstuser.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductDescriptionImg {
    private String descriptionFileName;
    private int ord;

    public void setOrd(int ord) {
        this.ord = ord;
    }
}
