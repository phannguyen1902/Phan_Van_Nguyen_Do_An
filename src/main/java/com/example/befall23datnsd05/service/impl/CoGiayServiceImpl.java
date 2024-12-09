package com.example.befall23datnsd05.service.impl;

import com.example.befall23datnsd05.entity.CoGiay;
import com.example.befall23datnsd05.repository.CoGiayRepository;
import com.example.befall23datnsd05.service.CoGiayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoGiayServiceImpl implements CoGiayService {

    @Autowired
    private CoGiayRepository repository;

    @Override
    public List<CoGiay> getAll() {
        return repository.findAll();
    }

    @Override
    public CoGiay getById(Long id) {
        Optional<CoGiay> optional = repository.findById(id);
        if (optional.isPresent()){
            return optional.get();
        }else {
            return null;
        }
    }

    @Override
    public CoGiay add(CoGiay coGiay) {
        CoGiay coGiay1 = new CoGiay();
        coGiay1.setMa(coGiay.getMa());
        coGiay1.setTen(coGiay.getTen());
        repository.save(coGiay1);
        return coGiay1;
    }

    @Override
    public CoGiay update(CoGiay coGiay) {
        CoGiay coGiay1 = repository.getReferenceById(coGiay.getId());
        if(coGiay1==null){
            return null;
        }
        coGiay1.setTen(coGiay.getTen());
        repository.save(coGiay1);
        return coGiay1;
    }

    @Override
    public void remove(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existByMa(String ma) {
        return repository.existsByMa(ma);
    }

    @Override
    public boolean existsByTen(String ten) {
        return repository.existsByTen(ten);
    }

    @Override
    public boolean existsByTenAndIdNot(String ten, Long id) {
        return repository.existsByTenAndIdNot(ten, id);
    }

}
