package com.alura.forolura.domain.topico;

import com.alura.forolura.domain.topico.validaciones.ValidorTopico;
import com.alura.forolura.domain.usuarios.Usuario;
import com.alura.forolura.domain.usuarios.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    List<ValidorTopico> validorTopicos;

    public Topico registrarTopico(DatosRegistroTopico datosRegistroTopico) {
        Long userId = Long.parseLong(datosRegistroTopico.idUsuario());

        validorTopicos.forEach(v -> v.validar(datosRegistroTopico));

        Optional<Usuario> usuario = usuarioRepository.findById(userId);

        if (!usuario.isPresent()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        Topico topico = topicoRepository.findByTituloIgnoreCase(datosRegistroTopico.titulo());

        if (topico != null) {
            throw new ValidationException("Titulo repetido");
        }

        topico = topicoRepository.findByMensajeIgnoreCase(datosRegistroTopico.mensaje());

        if (topico != null) {
            throw new ValidationException("Mensaje repetido");
        }

        DatosTopico datosTopico = new DatosTopico(usuario.get(), datosRegistroTopico.titulo(), datosRegistroTopico.mensaje(),
                datosRegistroTopico.curso());

        topico = topicoRepository.save(new Topico(datosTopico));

        return topico;
    }

    public Optional<Topico> buscarTopicoPorId(Long id) {
        return topicoRepository.findById(id);
    }

    public Topico actualizarTopico(Long id, DatosRegistroTopico datos) {
        Optional<Topico> optionalTopico = topicoRepository.findById(id);

        if (!optionalTopico.isPresent()) {
            throw new EntityNotFoundException("Tópico no encontrado");
        }

        validorTopicos.forEach(v -> v.validar(datos));

        Topico topico = optionalTopico.get();

        Long userId = Long.valueOf(datos.idUsuario());
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("Usuario no encontrado"));

        topico.actualizarDatos(datos, usuario);

        return topicoRepository.save(topico);
    }

    public void eliminarTopico(Long id) {
        Optional<Topico> optionalTopico = topicoRepository.findById(id);
        if (optionalTopico.isPresent()) {
            topicoRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("No se encontró el tópico con ID: " + id);
        }
    }
}
