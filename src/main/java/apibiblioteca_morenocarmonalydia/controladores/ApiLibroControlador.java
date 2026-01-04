package apibiblioteca_morenocarmonalydia.controladores;

import apibiblioteca_morenocarmonalydia.entidades.Autor;
import apibiblioteca_morenocarmonalydia.entidades.Ejemplar;
import apibiblioteca_morenocarmonalydia.entidades.Libro;
import apibiblioteca_morenocarmonalydia.repositorios.AutorRepositorio;
import apibiblioteca_morenocarmonalydia.repositorios.EjemplarRepositorio;
import apibiblioteca_morenocarmonalydia.repositorios.LibroRepositorio;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class ApiLibroControlador {

    @Autowired
    private LibroRepositorio libroRepositorio;

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Autowired
    private EjemplarRepositorio ejemplarRepositorio;

    @GetMapping("/api/libros")
    public ResponseEntity<Page<Libro>> obtenerLibrosPaginados(
            @PageableDefault(page = 0, size = 5, sort = "titulo", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok(libroRepositorio.findAll(pageable));
    }
    @GetMapping("/api/libros/resumen")
    public ResponseEntity<?> obtenerLibrosResumen() {
        return ResponseEntity.ok(libroRepositorio.obtenerLibrosConNumEjemplares());
    }
    @PostMapping("/api/libros")
    public ResponseEntity<?> crearLibro(@Valid @RequestBody Libro libro) {
        try {
            return ResponseEntity.status(201).body(libroRepositorio.save(libro));
        } catch (DataIntegrityViolationException e) {
            Map<String, String> error = Collections.singletonMap("error", "isbn repetido");
            return ResponseEntity.status(409).body(error);
        }
    }
    @GetMapping("/api/libros/{id}")
    public ResponseEntity<Libro> obtenerLibro(@PathVariable Long id) {
        return libroRepositorio.findById(id)
                .map(libro -> ResponseEntity.ok(libro))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/api/libros/{id}")
    public ResponseEntity<Object> borrarLibro(@PathVariable Long id) {
        return libroRepositorio.findById(id)
                .map(libro -> {
                    libroRepositorio.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping(value = "/api/libros", params = "titulo")
    public ResponseEntity<List<Libro>> buscarPorTitulo(@RequestParam String titulo) {
        return ResponseEntity.ok(libroRepositorio.findByTituloContainingIgnoreCase(titulo));
    }
    @GetMapping(value = "/api/libros", params = "fecha")
    public ResponseEntity<List<Libro>> buscarPorFecha(@RequestParam LocalDate fecha) {
        return ResponseEntity.ok(libroRepositorio.findByFechaPublicacion(fecha));
    }
    @PostMapping("/api/libros/{libroId}/autor/{autorId}")
    public ResponseEntity<Libro> anadirAutor(@PathVariable Long libroId, @PathVariable Long autorId) {
        Optional<Libro> libroOpt = libroRepositorio.findById(libroId);
        Optional<Autor> autorOpt = autorRepositorio.findById(autorId);

        if (libroOpt.isPresent() && autorOpt.isPresent()) {
            Libro libro = libroOpt.get();
            Autor autor = autorOpt.get();

            if (!libro.getAutores().contains(autor)) {
                libro.getAutores().add(autor);
                libroRepositorio.save(libro);
            }
            return ResponseEntity.ok(libro);
        }
        return ResponseEntity.notFound().build();
    }
    @DeleteMapping("/api/libros/{libroId}/autor/{autorId}")
    public ResponseEntity<Libro> quitarAutor(@PathVariable Long libroId, @PathVariable Long autorId) {
        Optional<Libro> libroOpt = libroRepositorio.findById(libroId);
        Optional<Autor> autorOpt = autorRepositorio.findById(autorId);

        if (libroOpt.isPresent() && autorOpt.isPresent()) {
            Libro libro = libroOpt.get();
            Autor autor = autorOpt.get();

            if (libro.getAutores().contains(autor)) {
                libro.getAutores().remove(autor);
                libroRepositorio.save(libro);
            }
            return ResponseEntity.ok(libro);
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/api/libros/{libroId}/ejemplares")
    public ResponseEntity<Libro> anadirEjemplar(@PathVariable Long libroId, @RequestBody Ejemplar ejemplar) {
        Optional<Libro> libroOpt = libroRepositorio.findById(libroId);

        if (libroOpt.isPresent()) {
            Libro libro = libroOpt.get();

            ejemplar.setLibro(libro);
            libro.getEjemplares().add(ejemplar);

            libroRepositorio.save(libro);
            return ResponseEntity.status(201).body(libro);
        }
        return ResponseEntity.notFound().build();
    }
    @DeleteMapping("/api/libros/{libroId}/ejemplares/{ejemplarId}")
    public ResponseEntity<Void> borrarEjemplar(@PathVariable Long libroId, @PathVariable Long ejemplarId) {
        Optional<Ejemplar> ejemplarOpt = ejemplarRepositorio.findById(ejemplarId);

        if (ejemplarOpt.isPresent()) {
            Ejemplar ejemplar = ejemplarOpt.get();

            if (ejemplar.getLibro().getId().equals(libroId)) {
                ejemplarRepositorio.deleteById(ejemplarId);
                return ResponseEntity.noContent().build();
            }
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/api/libros/cuenta")
    public ResponseEntity<Long> contarLibrosAnterioresA(@RequestParam int anio) {
        return ResponseEntity.ok(libroRepositorio.contarLibrosAntesDeAnio(anio));
    }
}