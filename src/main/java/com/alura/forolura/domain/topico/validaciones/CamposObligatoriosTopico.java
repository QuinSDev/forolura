package com.alura.forolura.domain.topico.validaciones;

import com.alura.forolura.domain.topico.DatosRegistroTopico;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class CamposObligatoriosTopico implements ValidorTopico{
    @Override
    public void validar(DatosRegistroTopico datosTopico) {
        if (datosTopico.idUsuario() == null) {
            throw new ValidationException("El usuario es obligatorio");
        }
        if (datosTopico.titulo() == null || datosTopico.titulo().isEmpty()) {
            throw new ValidationException("El título es obligatorio");
        }
        if (datosTopico.mensaje() == null || datosTopico.mensaje().isEmpty()) {
            throw new ValidationException("El mensaje no puede esatr vacío");
        }
        if (datosTopico.curso() == null || datosTopico.curso().isEmpty()) {
            throw new ValidationException("El curso es obligatorio");
        }
    }
}
