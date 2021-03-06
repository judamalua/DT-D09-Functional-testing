
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	@Query("select c from Category c where c.fatherCategory.id=?1")
	Collection<Category> findSubCategories(int categoriesId);

	@Query("select c from Category c where c.fatherCategory.id=?1")
	Page<Category> findSubCategories(int categoriesId, Pageable pageable);

	@Query("select c from Category c where c.fatherCategory=null")
	Page<Category> findFirstLevelCategories(Pageable pageable);

	@Query("select c from Category c where c.fatherCategory=null")
	Collection<Category> findFirstLevelCategories();

	//Dashboard queries

	@Query("select avg(c.services.size/(select count(sv) from DomainService sv)) from Category c")
	String getAverageRatioServicesInCategory();
}
