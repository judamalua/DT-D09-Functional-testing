
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Slider;

@Repository
public interface SliderRepository extends JpaRepository<Slider, Integer> {

}
