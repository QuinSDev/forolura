package com.alura.forolura.domain.topico;

import java.time.LocalDateTime;

public record DatosListarTopico(
        Long id,
        String titulo,
        String mensaje,
        String usuario,
        String curso,
        LocalDateTime fechaCreacion
) {

    public DatosListarTopico(Topico topico) {
        this(topico.getId(),topico.getTitulo(), topico.getMensaje(), topico.getUsuario().getEmail(), topico.getCurso(),topico.getFechaCreacion());
    }
}
