package com.example.products_rest_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import com.example.products_rest_api.Entities.Product;
import com.example.products_rest_api.repo.ProductRepository;
import com.example.products_rest_api.dto.Voucher;;

@RestController()
@RequestMapping("/productapi")
public class productsController {
    @Autowired
    ProductRepository repository;
    @Autowired
    private RestTemplate restTemplate;

    @Value("${voucherService.url}")
    private String voucherServiceUrl;

    @PostMapping("/products/")
    public Product createProduct(@RequestBody Product product){
        Voucher voucher = restTemplate.getForObject(voucherServiceUrl + product.getVoucherCode(), Voucher.class);
        product.setPrice(product.getPrice().subtract(voucher.getDiscount()));
        return repository.save(product);
    }
    
    @GetMapping("/products/")
    public List<Product> getProducts(){
        return repository.findAll();
    }
    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable("id") int id){
        return repository.findById(id).get();
    }
    @PutMapping("/products/{id}")
    public Product updateProduct(@PathVariable("id") int id,@RequestBody Product product) {
        product.setId(id);
        return repository.save(product);
    }
    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable("id") int id){
         repository.deleteById(id);
    }
    @PatchMapping("/products/{id}") 
    public Product updateRowProduct(@PathVariable("id") int id, @RequestBody Product product){
        // product.setId(id);
        Product dummy = repository.findById(id).get();
        if(product.getName()!=null){
            dummy.setName(product.getName());
        }
        if(product.getDescription()!=null){
            dummy.setDescription(product.getDescription());
        }
        if(product.getPrice() != null){
            dummy.setPrice(product.getPrice());
        }
        return repository.save(dummy);
    }
}
