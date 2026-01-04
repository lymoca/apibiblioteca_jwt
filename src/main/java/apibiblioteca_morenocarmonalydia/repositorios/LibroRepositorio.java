package apibiblioteca_morenocarmonalydia.repositorios;

import apibiblioteca_morenocarmonalydia.dtos.LibroYNumEjemplaresDTO;
import apibiblioteca_morenocarmonalydia.entidades.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LibroRepositorio extends JpaRepository <Libro, Long> {

    List<Libro>findByTituloContainingIgnoreCase(String titulo);
    List<Libro>findByFechaPublicacion(LocalDate fechaPublicacion);

    @Query("SELECT new apibiblioteca_morenocarmonalydia.dtos.LibroYNumEjemplaresDTO(" +
            "l.id, l.titulo, l.isbn, l.fechaPublicacion, COUNT(e)) " +
            "FROM Libro l LEFT JOIN l.ejemplares e " +
            "GROUP BY l.id, l.titulo, l.isbn, l.fechaPublicacion")
    List<LibroYNumEjemplaresDTO> obtenerLibrosConNumEjemplares();

    @Query("SELECT COUNT(l) FROM Libro l WHERE YEAR(l.fechaPublicacion) < :anio")
    Long contarLibrosAntesDeAnio(@Param("anio") int anio);
}
