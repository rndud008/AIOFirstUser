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
public class Product {

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

    @Column(updatable = false)
    private LocalDate createdAt;
    @Column(insertable = false)
    private LocalDate updatedAt;


    public void changeProduct(ProductDetailDTO productDetailDTO){
        this.productName = productDetailDTO.getProductName();
        this.consumerPrice = productDetailDTO.getConsumerPrice();
        this.sellPrice = productDetailDTO.getSellPrice();

        List<String> productImgFileNames = productDetailDTO.getProductImgFileNames();
        List<String> productDescriptionImgFileNames = productDetailDTO.getProductDescriptionImgFileNames();

        clearProductImageList();
        if(productImgFileNames != null && !productImgFileNames.isEmpty()){
            productImgFileNames.forEach(uploadName -> addProductImgsString(uploadName));
        }

        clearProductImageDesccriptionList();
        if(productDescriptionImgFileNames != null && !productDescriptionImgFileNames.isEmpty()){
            productDescriptionImgFileNames.forEach(uploadName -> addProductDescriptionImgsString(uploadName));
        }

        clearStatusList();
        productDetailDTO.getProductStatuses().forEach(status ->
                changeStatus(ProductStatus.valueOf(status)));


    }

    public void changeStatus(ProductStatus productStatus){
        this.productStatuses.add(productStatus);

    }

    public void addProductImgs(ProductImg img){
        img.setOrd(productImgs.size());
        productImgs.add(img);
    }

    public void addProductDescriptionImgs(ProductDescriptionImg img){
        img.setOrd(productDescriptionImgs.size());
        productDescriptionImgs.add(img);
    }

    public void addProductImgsString(String fileName){
        ProductImg productImg = ProductImg.builder()
                .fileName(fileName)
                .build();

        addProductImgs(productImg);
    }

    public void addProductDescriptionImgsString(String fileName){
        ProductDescriptionImg productDescriptionImg = ProductDescriptionImg.builder()
                .descriptionFileName(fileName)
                .build();

        addProductDescriptionImgs(productDescriptionImg);
    }

    public void clearProductImageList(){
        this.productImgs.clear();
    }
    public void clearProductImageDesccriptionList(){
        this.productDescriptionImgs.clear();
    }
    public void clearStatusList(){
        this.productStatuses.clear();
    }

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDate.now();
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedAt = LocalDate.now();
    }
}
