package apibiblioteca_morenocarmonalydia.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroYNumEjemplaresDTO {


    private Long id;
    private String titulo;
    private String isbn;
    private LocalDate fechaPublicacion;
    private long numEjemplares;

}
