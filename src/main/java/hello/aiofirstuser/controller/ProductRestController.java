package hello.aiofirstuser.controller;

import hello.aiofirstuser.dto.category.CategoryRequestDTO;
import hello.aiofirstuser.dto.product.ProductDTO;
import hello.aiofirstuser.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@Slf4j
public class ProductRestController {
    private final ProductService productService;

    @PostMapping("/category")
    public ResponseEntity<?> getFilterProducts(@RequestBody CategoryRequestDTO categoryRequestDTO){
        boolean filterCheck = categoryRequestDTO.getFilter() != null
                && (categoryRequestDTO.getFilter().equals("new") || categoryRequestDTO.getFilter().equals("highprice") || categoryRequestDTO.getFilter().equals("lowprice"));

        List<ProductDTO> categoryProductDTOS;
        if (filterCheck){
            categoryProductDTOS = productService.getFilterProductDTOS(categoryRequestDTO);
        }else {
            categoryProductDTOS = productService.getCategoryProductDTOS(categoryRequestDTO.getCode(), true);
        }

        return ResponseEntity.status(200).body(categoryProductDTOS);
    }
}
