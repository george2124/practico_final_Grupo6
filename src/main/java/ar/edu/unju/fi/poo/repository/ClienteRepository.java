package ar.edu.unju.fi.poo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.unju.fi.poo.model.Cliente; // <-- Corregido el import

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    Optional<Cliente> findByIdentificacion(String identificacion);

    List<Cliente> findByNombreContainingIgnoreCaseOrApellidoRazonSocialContainingIgnoreCase(String nombre, String apellido);
}