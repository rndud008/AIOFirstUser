package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.Category;
import hello.aiofirstuser.dto.category.CategoryDTO;

import java.util.ArrayList;
import java.util.List;

public interface CategoryService {


    List<CategoryDTO> mainCategoryInqueryExcludeList();

    CategoryDTO InqueryCategory();

    List<CategoryDTO> findAll();

    CategoryDTO getCategory(Long id);

    CategoryDTO getCategory(String str);

    List<CategoryDTO> getSubCategoryList(Long depno);

    default List<CategoryDTO> entityListToDtoList(List<Category> categories){

        List<CategoryDTO> categoryDTOS = new ArrayList<>();

        for (Category category : categories){
            categoryDTOS.add(entityToDto(category));
        }

        return categoryDTOS;
    }

    default CategoryDTO entityToDto(Category category){

        return CategoryDTO.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .depNo(category.getDepNo())
                .build();
    }

}
