package apibiblioteca_morenocarmonalydia.controladores;

import apibiblioteca_morenocarmonalydia.entidades.Autor;
import apibiblioteca_morenocarmonalydia.repositorios.AutorRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/autores")
public class ApiAutorControlador {

    @Autowired
    private AutorRepositorio autorRepositorio;

    @PostMapping
    public ResponseEntity<Autor> crearAutor(@RequestBody Autor autor) {
        return new ResponseEntity<>(autorRepositorio.save(autor), HttpStatus.CREATED);
    }
    @GetMapping
    public List<Autor> listarAutores() {
        return autorRepositorio.findAll();
    }
}