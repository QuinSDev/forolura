package com.alura.forolura.domain.topico;

import com.alura.forolura.domain.usuarios.Usuario;
import com.fasterxml.jackson.annotation.JsonManagedReference;

public record DatosTopico(

        @JsonManagedReference
        Usuario idUsuario,
        String titulo,
        String mensaje,
        String curso

) {
}
