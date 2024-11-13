package hello.aiofirstuser.domain;

import hello.aiofirstuser.dto.product.ProductDetailDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"productImgs", "productAlphas", "productStatuses","productDescriptionImgs"})
@Getter
@Builder
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;
    private String productName;

    private String size;
    private String color;
    private int stockQuantity;

    private int consumerPrice;
    private int sellPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ElementCollection
    @CollectionTable(name = "product_description_img", joinColumns = @JoinColumn(name = "product_id"))
    @Builder.Default
    private List<ProductDescriptionImg> productDescriptionImgs = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "product_img",joinColumns = @JoinColumn(name = "product_id"))
    @Builder.Default
    private List<ProductImg> productImgs = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name="product_alpha",joinColumns = @JoinColumn(name = "product_id"))
    @Builder.Default
    private List<ProductAlpha> productAlphas = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "product_status",joinColumns = @JoinColumn(name = "product_id"))
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private List<ProductStatus> productStatuses = new ArrayList<>();

}
