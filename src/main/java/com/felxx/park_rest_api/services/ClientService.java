package com.felxx.park_rest_api.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.felxx.park_rest_api.entities.Client;
import com.felxx.park_rest_api.exceptions.CpfUniqueViolationException;
import com.felxx.park_rest_api.exceptions.EntityNotFoundException;
import com.felxx.park_rest_api.repositories.ClientRepository;
import com.felxx.park_rest_api.repositories.projection.ClientProjection;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService {
    
    private final ClientRepository clientRepository;

    @Transactional
    public Client save(Client client) {
        try {
            return clientRepository.save(client);
        } catch (DataIntegrityViolationException e) {
            throw new CpfUniqueViolationException(String.format("CPF %s already exists", client.getCpf()));
        }
    }

    @Transactional(readOnly = true)
    public Client findById(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Client with id %d not found", id)));
    }

    @Transactional(readOnly = true)
    public Page<ClientProjection> findAll(Pageable pageable) {
        return clientRepository.findAllPageable(pageable);
    }

    @Transactional(readOnly = true)
    public Client findByUserId(Long userId) {
        return clientRepository.findByUserId(userId);
    }

    public Client findByCpf(String cpf) {
        return clientRepository.findByCpf(cpf).orElseThrow(() -> new EntityNotFoundException(String.format("Client with CPF %s not found", cpf)));
    }
}
