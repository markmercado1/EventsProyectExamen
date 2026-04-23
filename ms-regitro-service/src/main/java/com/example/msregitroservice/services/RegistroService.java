package com.example.msregitroservice.services;

import com.example.msregitroservice.dtos.RegistroDTO;

import java.util.List;

public interface RegistroService {

    RegistroDTO guardar(Long participantId, RegistroDTO registroDTO);

    RegistroDTO buscarPorId(Long id);

    List<RegistroDTO> listar();

    void eliminar(Long id);
}