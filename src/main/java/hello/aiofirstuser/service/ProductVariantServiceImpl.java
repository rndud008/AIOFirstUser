package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.ProductVariant;
import hello.aiofirstuser.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepository productVariantRepository;
    @Override
    public ProductVariant getProductVariant(Long productId, String color, String size) {
        return productVariantRepository.findByProductIdAndColorAndSize(productId,color,size);
    }
}
