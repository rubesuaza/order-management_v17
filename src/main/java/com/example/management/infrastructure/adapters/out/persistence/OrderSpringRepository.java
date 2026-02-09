package com.example.management.infrastructure.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio Spring Data JPA para OrderEntity.
 * Solo usado por el adaptador de infraestructura.
 */
interface OrderSpringRepository extends JpaRepository<OrderEntity, String> {
}
