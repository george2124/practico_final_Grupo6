package ar.edu.unju.fi.poo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.poo.dto.ProductoRequestDTO;
import ar.edu.unju.fi.poo.dto.ProductoResponseDTO;
import ar.edu.unju.fi.poo.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.poo.model.Categoria;
import ar.edu.unju.fi.poo.model.Producto;
import ar.edu.unju.fi.poo.repository.ProductoRepository;
import ar.edu.unju.fi.poo.service.ProductoService;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public ProductoResponseDTO guardarProducto(ProductoRequestDTO request) {
        Categoria categoriaAsignada = null;
        if (request.getCategoriaId() != null) {
            categoriaAsignada = new Categoria();
            categoriaAsignada.setId(request.getCategoriaId());
        }

        Producto nuevoProducto = new Producto(
            null,
            request.getNombre(),
            request.getDescripcion(),
            request.getMarca(),
            request.getStock(),
            request.getPrecioBase(),
            categoriaAsignada
        );

        Producto productoGuardado = productoRepository.save(nuevoProducto);
        return convertirAResponseDTO(productoGuardado);
    }

    @Override
    public ProductoResponseDTO buscarPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() ->
                    new RecursoNoEncontradoException("Producto no encontrado con ID: " + id)
                );
        return convertirAResponseDTO(producto);
    }

    @Override
    public List<ProductoResponseDTO> listarTodos() {
        return productoRepository.findAll()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductoResponseDTO> listarStockCritico(Integer umbral) {
        return productoRepository.findByStockLessThanEqual(umbral)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductoResponseDTO> filtrarProductos(
            Long categoriaId,
            String marca,
            Long precioMin,
            Long precioMax) {
        return productoRepository
                .findByCategoriaIdAndMarcaAndPrecioBaseBetween(categoriaId, marca, precioMin, precioMax)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    private ProductoResponseDTO convertirAResponseDTO(Producto producto) {
        Long catId = (producto.getCategoria() != null) ? producto.getCategoria().getId() : null;
        String catNombre = (producto.getCategoria() != null) ? producto.getCategoria().getNombre() : "Sin Categoría";

        return new ProductoResponseDTO(
            producto.getId(),
            producto.getNombre(),
            producto.getDescripcion(),
            producto.getMarca(),
            producto.getStock(),
            producto.getPrecioBase(),
            catId,
            catNombre
        );
    }
}