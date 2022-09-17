package com.crud.latihansecurity.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crud.latihansecurity.DTO.ResponseDTO;
import com.crud.latihansecurity.models.ProductEntity;
import com.crud.latihansecurity.services.ProductService;




@RestController
@RequestMapping("api/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<ProductEntity>> create(@Valid @RequestBody ProductEntity product, Errors errors) {

        ResponseDTO<ProductEntity> response = new ResponseDTO<>();
        
        if(errors.hasErrors()){
            for (ObjectError error : errors.getAllErrors()) {
                System.out.println(error.getDefaultMessage());
                response.getMessages().add(error.getDefaultMessage());
            }
            response.setStatus("400");
            response.setEntity(null);
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        response.setStatus("201");
        response.setEntity(productService.save(product));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }
    @GetMapping
    public Iterable<ProductEntity> readAll(){
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ProductEntity readOne(@PathVariable("id") long id){
        return productService.findById(id);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<ProductEntity>> update(@Valid @RequestBody ProductEntity product, Errors errors){
        ResponseDTO<ProductEntity> response = new ResponseDTO<>();
        
        if(errors.hasErrors()){
            for (ObjectError error : errors.getAllErrors()) {
                System.out.println(error.getDefaultMessage());
                response.getMessages().add(error.getDefaultMessage());
            }
            response.setStatus("400");
            response.setEntity(null);
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        response.setStatus("204");
        response.setEntity(productService.save(product));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable("id") long id){
        productService.deleteById(id);
    }


}
