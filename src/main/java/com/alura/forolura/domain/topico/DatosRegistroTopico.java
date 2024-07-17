package com.alura.forolura.domain.topico;

import com.alura.forolura.domain.usuarios.Usuario;
import jakarta.validation.constraints.NotBlank;

public record DatosRegistroTopico(

        @NotBlank
        String idUsuario,
        @NotBlank
        String titulo,
        @NotBlank
        String mensaje,
        @NotBlank
        String curso
) {
}
