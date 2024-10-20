package hello.aiofirstuser.service;

import hello.aiofirstuser.domain.Category;
import hello.aiofirstuser.dto.CategoryDTO;
import hello.aiofirstuser.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDTO> mainCategoryInqueryExcludeList() {

        Optional<Category> result = categoryRepository.findByCategoryName("커뮤니티");

        Category inqueryCategory = result.orElseThrow();

        List<Category> categories = categoryRepository.getMainCategoryExcludeInquery(inqueryCategory.getId());

        List<CategoryDTO> categoryDTOS = new ArrayList<>();

        for(Category category : categories){
            List<Category> subCategory = categoryRepository.findByDepNo(category.getId());

            CategoryDTO categoryDTO = entityToDto(category);
            categoryDTO.setSubCategories(entityListToDtoList(subCategory));

            categoryDTOS.add(categoryDTO);
        }

        return categoryDTOS;
    }

    @Override
    public CategoryDTO InqueryCategory() {
        Optional<Category> result = categoryRepository.findByCategoryName("커뮤니티");

        Category inqueryCategory = result.orElseThrow();

        List<Category> subCategories = categoryRepository.findByDepNo(inqueryCategory.getId());

        CategoryDTO categoryDTO = entityToDto(inqueryCategory);
        categoryDTO.setSubCategories(entityListToDtoList(subCategories));

        return categoryDTO;
    }

    @Override
    public List<CategoryDTO> findAll() {
        List<Category> categories = categoryRepository.findByDepNo(0L);

        List<CategoryDTO> categoryDTOS = new ArrayList<>();

        for(Category category : categories){
            List<Category> subCategories = categoryRepository.findByDepNo(category.getId());

            CategoryDTO categoryDTO = entityToDto(category);
            categoryDTO.setSubCategories(entityListToDtoList(subCategories));

            categoryDTOS.add(categoryDTO);
        }


        return categoryDTOS;
    }

    @Override
    public CategoryDTO getCategory(Long id) {
        Optional<Category> result = categoryRepository.findById(id);

        Category category = result.orElseThrow();

        return entityToDto(category);
    }

}
