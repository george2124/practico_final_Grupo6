<<<<<<< HEAD
package ar.edu.unju.fi.poo.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.poo.model.Producto;
import ar.edu.unju.fi.poo.repository.ProductoRepository;
import ar.edu.unju.fi.poo.service.ProductoService;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Override
    public List<Producto> filtrarPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }

    @Override
    public List<Producto> filtrarPorMarca(String marca) {
        return productoRepository.findByMarcaIgnoreCase(marca);
    }

    @Override
    public List<Producto> filtrarPorRangoPrecio(BigDecimal min, BigDecimal max) {
        return productoRepository.findByPrecioBaseBetween(min, max);
    }

    @Override
    public List<Producto> consultarStockCritico(Integer umbral) {
        return productoRepository.findByStockLessThanEqual(umbral);
    }
    
    @Override
    public List<Producto> listarStockCritico(Integer umbral) {
        return productoRepository.findByStockLessThanEqual(umbral);
    }

    @Override
    public List<Producto> filtrarProductos(Long categoriaId, String marca, Long precioMin, Long precioMax) {
        // Podés usar una consulta personalizada en el repositorio o filtrar mediante queries de Spring Data JPA
        return productoRepository.filtrarCustom(categoriaId, marca, precioMin, precioMax);
    }
=======
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
>>>>>>> refs/remotes/origin/fix/desacoplamiento-dto
}