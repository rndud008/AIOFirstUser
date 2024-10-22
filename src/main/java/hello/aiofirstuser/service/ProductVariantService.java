package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.ProductVariant;

public interface ProductVariantService {

    ProductVariant getProductVariant(Long productId, String color, String size);

}
