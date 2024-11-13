package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.Product;
import hello.aiofirstuser.domain.ProductVariant;
import hello.aiofirstuser.dto.product.ProductDTO;
import hello.aiofirstuser.dto.product.ProductDetailDTO;
import hello.aiofirstuser.repository.CategoryRepository;
import hello.aiofirstuser.repository.ProductRepository;
import hello.aiofirstuser.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductDetailDTO getProductDTO(Long productId) {

        Optional<Product> result = productRepository.findById(productId);

        Product product = result.orElseThrow();
        List<ProductVariant> resultList = productVariantRepository.findByProductId(productId);

        ProductDetailDTO productDetailDTO = productDetailDTO(product);
        productDetailDTO.setColorSizePrice(colorSizePrice(resultList));

        List<String> sizePriorityOrder = Arrays.asList("S", "M", "L", "XL", "2XL");

        if (!resultList.isEmpty()) {

            List<String> sizeList = resultList
                    .stream().map(ProductVariant::getSize).distinct()
                    .sorted(Comparator.comparingInt(sizePriorityOrder::indexOf))
                    .toList();

            log.info("sizeList ={}", sizeList);
            String size = null;

            if (sizeList.size() > 1) {
                size = sizeList.get(0) + "~" + sizeList.get(sizeList.size() - 1);
                productDetailDTO.setSize(size);
            } else {
                productDetailDTO.setSize(sizeList.get(0));
            }

        }

        return productDetailDTO;
    }

    @Override
    public List<ProductDTO> getCategoryProductDTOS(Long id, boolean mainCategory) {
        List<Product> products = new ArrayList<>();

        if (mainCategory) {
            products = productRepository.findByCategoryDepNo(id);

        } else {
            products = productRepository.findByCategoryId(id);

        }

        List<ProductDTO> productDTOS = getProductDTOS(products);

        return productDTOS;
    }

    @Override
    public List<ProductDTO> getNewProductDTOS() {

        LocalDateTime twoWeekAgo = LocalDateTime.now().minusWeeks(2);
        List<Product> products = productRepository.findByCreatedAtAfter(twoWeekAgo);

        List<ProductDTO> productDTOS = new ArrayList<>();
        if (!products.isEmpty()) {
            productDTOS = getProductDTOS(products);
        }

        return productDTOS;
    }

    @Override
    public List<ProductDTO> getSearchProductDTOS(String search) {

        List<Product> products = productRepository.getProducts(search);
        List<ProductDTO> productDTOS = new ArrayList<>();
        if (!products.isEmpty()) {
            productDTOS = getProductDTOS(products);
        }

        return productDTOS;
    }

    private List<ProductDTO> getProductDTOS(List<Product> products) {
        List<ProductDTO> productDTOS = new ArrayList<>();

        List<String> sizePriorityOrder = Arrays.asList("S", "M", "L", "XL", "2XL");

        for (Product product : products) {
            ProductDTO productDTO = productDTO(product);
            List<ProductVariant> productVariants = productVariantRepository.findByProductId(product.getId());

            if (!productVariants.isEmpty()) {

                List<String> sizeList = productVariants
                        .stream().map(ProductVariant::getSize).distinct()
                        .sorted(Comparator.comparingInt(sizePriorityOrder::indexOf))
                        .toList();

                log.info("sizeList ={}", sizeList);
                String size = null;

                if (sizeList.size() > 1) {
                    size = sizeList.get(0) + "~" + sizeList.get(sizeList.size() - 1);
                    productDTO.setSize(size);
                } else {
                    productDTO.setSize(sizeList.get(0));
                }

            }

            productDTOS.add(productDTO);
        }
        return productDTOS;
    }

}
