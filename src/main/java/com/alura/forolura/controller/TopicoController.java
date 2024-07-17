package com.alura.forolura.controller;

import com.alura.forolura.domain.topico.*;
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
    public ResponseEntity<DatosListarTopico> listarTopicoPorId(@PathVariable Long id) {
        Optional<Topico> topico = topicoService.buscarTopicoPorId(id);

        if (!topico.isPresent()) {
            return ResponseEntity.notFound().build();
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
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<DatosListarTopico> actualizarTopico(@PathVariable Long id, @RequestBody DatosRegistroTopico datos) {
        Topico topicoActualizado = topicoService.actualizarTopico(id, datos);
        DatosListarTopico datosActualizado = new DatosListarTopico(topicoActualizado.getId(), topicoActualizado.getTitulo(),
                topicoActualizado.getMensaje(), topicoActualizado.getUsuario().getEmail(), topicoActualizado.getCurso(),
                topicoActualizado.getFechaCreacion());

        return ResponseEntity.ok(datosActualizado);
    }

    @DeleteMapping("/desactivar/{id}")
    @Transactional
    public ResponseEntity desactivarTopico(@PathVariable Long id) {
        var topico = topicoRepository.getReferenceById(id);
        topico.desactivar();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/eliminar/{id}")
    @Transactional
    public ResponseEntity eliminarTopicoDeBD(@PathVariable Long id) {
        topicoService.eliminarTopico(id);
        return ResponseEntity.noContent().build();
    }
}
