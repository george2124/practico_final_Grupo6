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
}