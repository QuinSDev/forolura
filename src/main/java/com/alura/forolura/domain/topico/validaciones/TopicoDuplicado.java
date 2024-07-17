package com.alura.forolura.domain.topico.validaciones;

import com.alura.forolura.domain.topico.DatosRegistroTopico;
import com.alura.forolura.domain.topico.TopicoRepository;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class TopicoDuplicado implements ValidorTopico{

    private final TopicoRepository topicoRepository;

    public TopicoDuplicado(TopicoRepository topicoRepository) {
        this.topicoRepository = topicoRepository;
    }

    @Override
    public void validar(DatosRegistroTopico datosTopico) {
        if (topicoRepository.findByTituloIgnoreCase(datosTopico.titulo()) != null
            && topicoRepository.findByMensajeIgnoreCase(datosTopico.mensaje()) != null) {
                throw new ValidationException("Ya existe un tópico con el mismo título y mensaje.");
        }
    }
}
