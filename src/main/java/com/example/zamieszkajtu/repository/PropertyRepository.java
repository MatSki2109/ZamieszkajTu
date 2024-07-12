package com.example.zamieszkajtu.repository;

import com.example.zamieszkajtu.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, Long> {
}
