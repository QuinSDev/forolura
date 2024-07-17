package com.alura.forolura.controller;

import com.alura.forolura.domain.topico.*;
import com.alura.forolura.domain.topico.validaciones.ErrorResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Topico", description = "Operaciones de gestión de tópicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private TopicoService topicoService;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosListarTopico> registrarTopico(@RequestBody @Valid DatosRegistroTopico datos,
                                                             UriComponentsBuilder uriComponentsBuilder) {

        Topico topico = topicoService.registrarTopico(datos);
        DatosListarTopico datosListarTopico = new DatosListarTopico(topico.getId(), topico.getTitulo(),
                topico.getMensaje(), topico.getUsuario().getEmail(), topico.getCurso(), topico.getFechaCreacion());
        URI uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(datosListarTopico);
    }

    @GetMapping
    public ResponseEntity<List<DatosListarTopico>> listarTopicos(
            @PageableDefault(size = 10, sort = {"fechaCreacion"}, direction = Sort.Direction.ASC) Pageable paginacion) {
        Page<DatosListarTopico> topicosPage = topicoRepository.findByStatusTrue(paginacion).map(DatosListarTopico::new);
        List<DatosListarTopico> contentList = topicosPage.getContent();
        return ResponseEntity.ok(contentList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> listarTopicoPorId(@PathVariable String id) {
        try {
            Long idConver = Long.parseLong(id);

            Optional<Topico> topico = topicoService.buscarTopicoPorId(idConver);

            if (!topico.isPresent()) {
                String errorMensaje = "El id no se encuentra en la base de datos";
                return ResponseEntity.badRequest().body(new ErrorResponse(errorMensaje));
            }
            DatosListarTopico datosListarTopico = new DatosListarTopico(
                    topico.get().getId(),
                    topico.get().getTitulo(),
                    topico.get().getMensaje(),
                    topico.get().getUsuario().getEmail(),
                    topico.get().getCurso(),
                    topico.get().getFechaCreacion()
            );
            return ResponseEntity.ok(datosListarTopico);
        } catch (NumberFormatException e) {
            String errorMessage = "El ID debe ser un número válido.";
            return ResponseEntity.badRequest().body(new ErrorResponse(errorMessage));
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarTopico(@PathVariable String id, @RequestBody DatosRegistroTopico datos) {
        try {
            Long idConver = Long.parseLong(id);

            Topico topicoActualizado = topicoService.actualizarTopico(idConver, datos);
            DatosListarTopico datosActualizado = new DatosListarTopico(topicoActualizado.getId(), topicoActualizado.getTitulo(),
                    topicoActualizado.getMensaje(), topicoActualizado.getUsuario().getEmail(), topicoActualizado.getCurso(),
                    topicoActualizado.getFechaCreacion());

            return ResponseEntity.ok(datosActualizado);
        } catch (NumberFormatException e) {
            String errorMessage = "El ID debe ser un número válido.";
            return ResponseEntity.badRequest().body(new ErrorResponse(errorMessage));
        }

    }

    @DeleteMapping("/desactivar/{id}")
    @Transactional
    public ResponseEntity desactivarTopico(@PathVariable String id) {
        try {
            Long idConver = Long.parseLong(id);
            var topico = topicoRepository.getReferenceById(idConver);

            if (topico == null) {
                String errorMensaje = "El id no se encuentra en la base de datos";
                return ResponseEntity.badRequest().body(new ErrorResponse(errorMensaje));
            }

            topico.desactivar();
            return ResponseEntity.noContent().build();
        } catch (NumberFormatException e) {
            String errorMessage = "El ID debe ser un número válido.";
            return ResponseEntity.badRequest().body(new ErrorResponse(errorMessage));
        }


    }

    @DeleteMapping("/eliminar/{id}")
    @Transactional
    public ResponseEntity eliminarTopicoDeBD(@PathVariable String id) {
        try {
            Long idConver = Long.parseLong(id);
            topicoService.eliminarTopico(idConver);
            return ResponseEntity.noContent().build();
        } catch (NumberFormatException e) {
            String errorMessage = "El ID debe ser un número válido.";
            return ResponseEntity.badRequest().body(new ErrorResponse(errorMessage));
        }
    }
}
