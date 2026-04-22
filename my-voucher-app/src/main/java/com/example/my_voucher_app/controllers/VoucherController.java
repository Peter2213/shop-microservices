package com.example.my_voucher_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.my_voucher_app.model.Voucher;
import com.example.my_voucher_app.repo.VoucherRepo;

@RestController
@RequestMapping("/voucherapi")
public class VoucherController {
    @Autowired
    VoucherRepo repo;
    @PostMapping("/vouchers")
    public Voucher createVoucher(@RequestBody Voucher vouch){
        return repo.save(vouch); 
    }
    @GetMapping("/vouchers/{code}")
    public Voucher getVoucher(@PathVariable("code") String code){
        return repo.findByCode(code);
    }
}
