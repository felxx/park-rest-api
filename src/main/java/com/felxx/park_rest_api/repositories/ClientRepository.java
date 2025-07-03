package com.felxx.park_rest_api.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.felxx.park_rest_api.entities.Client;
import com.felxx.park_rest_api.repositories.projection.ClientProjection;

public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("SELECT c FROM Client c")
    Page<ClientProjection> findAllPageable(Pageable pageable);

    Client findByUserId(Long userId);

    Optional<Client> findByCpf(String cpf);
}
