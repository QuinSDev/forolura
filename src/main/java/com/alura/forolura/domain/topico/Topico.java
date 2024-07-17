package com.alura.forolura.domain.topico;

import com.alura.forolura.domain.usuarios.Usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "topicos")
@Entity(name = "Topico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String mensaje;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime fechaCreacion;
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;
    private String curso;

    public Topico(DatosTopico datos) {
        this.status = true;
        this.titulo = datos.titulo();
        this.mensaje = datos.mensaje();
        this.usuario = datos.idUsuario();
        this.curso = datos.curso();
        this.fechaCreacion = LocalDateTime.now();
    }

    public void actualizarDatos(DatosRegistroTopico datos, Usuario usuario) {
        this.titulo = datos.titulo();
        this.mensaje = datos.mensaje();
        this.usuario = usuario;
        this.curso = datos.curso();
    }

    public void desactivar() {
        this.status = false;
    }
}
