package com.example.toprak_rehberi.repos;

import com.example.toprak_rehberi.entities.Lands;
import com.example.toprak_rehberi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LandsRepository extends JpaRepository<Lands, Long> {
    List<Lands> findByUser(User user); // Kullanıcıya ait tüm arazileri bulma
    List<Lands> findByUserId(Long userId);
    List<Lands> findByUser_Username(String username);

}
