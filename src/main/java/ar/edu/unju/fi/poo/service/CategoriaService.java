package ar.edu.unju.fi.poo.service;

import ar.edu.unju.fi.poo.dto.CategoriaRequestDTO;
import ar.edu.unju.fi.poo.dto.CategoriaResponseDTO;
import java.util.List;

public interface CategoriaService {
    CategoriaResponseDTO crearCategoria(CategoriaRequestDTO requestDTO);
    List<CategoriaResponseDTO> listarCategorias();
    CategoriaResponseDTO obtenerPorId(Long id);
    CategoriaResponseDTO actualizarCategoria(Long id, CategoriaRequestDTO requestDTO);
    void eliminarCategoria(Long id);
}
