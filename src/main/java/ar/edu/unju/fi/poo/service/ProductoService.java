
package ar.edu.unju.fi.poo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.poo.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.poo.model.Producto;
import ar.edu.unju.fi.poo.repository.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    // Guardar producto
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    // Buscar producto por ID
    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() ->
                    new RecursoNoEncontradoException(
                        "Producto no encontrado con ID: " + id
                    )
                );
    }

    // Listar productos con stock crítico
    public List<Producto> listarStockCritico(Integer umbral) {
        return productoRepository.findByStockLessThanEqual(umbral);
    }

    // Listar todos los productos
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    // Filtrar productos
    public List<Producto> filtrarProductos(
            Long categoriaId,
            String marca,
            Long precioMin,
            Long precioMax) {

        return productoRepository
                .findByCategoriaIdAndMarcaAndPrecioBaseBetween(
                    categoriaId,
                    marca,
                    precioMin,
                    precioMax
                );
    }
}
