package ar.edu.unju.fi.poo.service.impl;

import ar.edu.unju.fi.poo.dto.CategoriaRequestDTO;
import ar.edu.unju.fi.poo.dto.CategoriaResponseDTO;
import ar.edu.unju.fi.poo.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.poo.exception.SolicitudInvalidaException;
import ar.edu.unju.fi.poo.model.Categoria;
import ar.edu.unju.fi.poo.repository.CategoriaRepository;
import ar.edu.unju.fi.poo.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public CategoriaResponseDTO crearCategoria(CategoriaRequestDTO requestDTO) {
        if (categoriaRepository.existsByNombreIgnoreCase(requestDTO.getNombre())) {
            throw new SolicitudInvalidaException("Ya existe una categoría con el nombre: " + requestDTO.getNombre());
        }
        Categoria categoria = new Categoria();
        categoria.setNombre(requestDTO.getNombre());

        Categoria guardada = categoriaRepository.save(categoria);
        return mapToResponseDTO(guardada);
    }

    @Override
    public List<CategoriaResponseDTO> listarCategorias() {
        return categoriaRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoriaResponseDTO obtenerPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada con ID: " + id));
        return mapToResponseDTO(categoria);
    }

    @Override
    public CategoriaResponseDTO actualizarCategoria(Long id, CategoriaRequestDTO requestDTO) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada con ID: " + id));

        categoria.setNombre(requestDTO.getNombre());

        Categoria actualizada = categoriaRepository.save(categoria);
        return mapToResponseDTO(actualizada);
    }

    @Override
    public void eliminarCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Categoría no encontrada con ID: " + id);
        }
        categoriaRepository.deleteById(id);
    }

    private CategoriaResponseDTO mapToResponseDTO(Categoria categoria) {
        return new CategoriaResponseDTO(categoria.getId(), categoria.getNombre());
    }
}
