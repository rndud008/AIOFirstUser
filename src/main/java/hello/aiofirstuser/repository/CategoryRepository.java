package hello.aiofirstuser.repository;

import hello.aiofirstuser.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryName(String categoryName);

    @Query("select c from Category c where c.depNo = 0 and c.id <> :id")
    List<Category> getMainCategoryExcludeInquery(@Param("id") Long id);

    List<Category> findByDepNo(Long id);

}
