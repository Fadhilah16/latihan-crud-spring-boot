package com.crud.latihansecurity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crud.latihansecurity.models.ProductEntity;

@Repository
public interface ProductRepo extends JpaRepository<ProductEntity, Long> {
    
}