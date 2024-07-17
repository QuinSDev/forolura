package com.alura.forolura.infra.errores;

public class ValidacionDeIntegridad extends RuntimeException {

    public ValidacionDeIntegridad(String s) {
        super(s);
    }

}
