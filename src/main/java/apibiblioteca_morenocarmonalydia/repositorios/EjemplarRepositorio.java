package apibiblioteca_morenocarmonalydia.repositorios;

import apibiblioteca_morenocarmonalydia.entidades.Ejemplar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EjemplarRepositorio extends JpaRepository <Ejemplar, Long> {
}
