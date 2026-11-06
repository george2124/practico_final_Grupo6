package ar.edu.unju.fi.poo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.unju.fi.poo.jpa.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente,Long> {
	
	Optional<Cliente> findByIdentificacion(String identificacion);

    // Buscar clientes por nombre o razón social (usamos Containing para búsquedas parciales)
    List<Cliente> findByNombreContainingIgnoreCaseOrApellidoRazonSocialContainingIgnoreCase(String nombre, String apellido);

}
