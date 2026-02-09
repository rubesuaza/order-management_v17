package com.example.management.infrastructure.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repositorio Spring Data JPA para OrderEntity.
 * Solo usado por el adaptador de infraestructura.
 */
interface OrderSpringRepository extends JpaRepository<OrderEntity, String> {

    /**
     * Recupera el pedido con sus líneas en una sola consulta (evita N+1).
     */
    @Query("SELECT o FROM OrderEntity o LEFT JOIN FETCH o.lines WHERE o.id = :id")
    Optional<OrderEntity> findByIdWithLines(@Param("id") String id);
}
