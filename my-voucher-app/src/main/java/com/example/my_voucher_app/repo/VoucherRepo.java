package com.example.my_voucher_app.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.my_voucher_app.model.Voucher;

public interface VoucherRepo extends JpaRepository<Voucher, Long> {
    public Voucher findByCode(String code);
}