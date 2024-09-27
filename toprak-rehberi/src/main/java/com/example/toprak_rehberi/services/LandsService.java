package com.example.toprak_rehberi.services;

import com.example.toprak_rehberi.entities.Lands;
import com.example.toprak_rehberi.repos.LandsRepository;
import com.example.toprak_rehberi.entities.User;
import com.example.toprak_rehberi.repos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LandsService {

    @Autowired
    private LandsRepository landsRepository;

    private ProductRepository productRepository;

    public Lands saveLand(Lands land) { //arazi ekleme
        return landsRepository.save(land);
    }
    public List<Lands> getAllLands() { //arazileri gösterme
        return landsRepository.findAll();
    }
    public List<Lands> findLandsByUser(User user) {
        return landsRepository.findByUser(user);
    }

    public List<Lands> getLandsByUserId(Long userId) {//user id e göre arazilerini getirme
        return landsRepository.findByUserId(userId);
    }
    public Lands findLandById(Long id) {
        Optional<Lands> land = landsRepository.findById(id);
        return land.orElse(null);  // Eğer land bulunamazsa null döner
    }
    public List<Lands> getLandsByUsername(String username) {
        return landsRepository.findByUser_Username(username);
    }
    public void deleteLand(Long id) {
        landsRepository.deleteById(id);
    }
}
