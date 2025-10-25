package ar.edu.utn.dds.k3003.model.consenso;

import ar.edu.utn.dds.k3003.facades.dtos.ConsensosEnum;

public enum ConsensoTipo { 
    TODOS, AL_MENOS_2, ESTRICTO;

    public static ConsensoTipo parse(String s) {
        if (s == null) throw new IllegalArgumentException("tipo nulo");
        return ConsensoTipo.valueOf(s.trim().toUpperCase());
    }

    public static ConsensoTipo from(ConsensosEnum ext) {
        if (ext == null) throw new IllegalArgumentException("enum externo nulo");
        return switch (ext) {
            case TODOS -> TODOS;
            case AL_MENOS_2 -> AL_MENOS_2;
        };
    }
}

