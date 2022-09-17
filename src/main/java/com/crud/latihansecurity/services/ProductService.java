package com.crud.latihansecurity.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crud.latihansecurity.models.ProductEntity;
import com.crud.latihansecurity.repositories.ProductRepo;

@Service
@Transactional
public class ProductService {
    @Autowired
    private ProductRepo productRepo;

    public ProductEntity save(ProductEntity product){
        return (ProductEntity) productRepo.save(product);
    }

    public Iterable<ProductEntity> findAll(){
        return productRepo.findAll();
    }

    public ProductEntity findById(long id){
        Optional<ProductEntity> product  = productRepo.findById(id);
        if(product.isPresent()){

            return product.get();
        }

        return null;
    }

   
    public void deleteById(long id){
        productRepo.deleteById(id);
    }
}
