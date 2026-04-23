package com.example.msregitroservice.repositorys;

import com.example.msregitroservice.models.Registro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistroRepository extends JpaRepository<Registro, Long> {
}