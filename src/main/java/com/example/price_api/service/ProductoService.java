package com.example.price_api.service;

import com.example.price_api.entity.Producto;
import com.example.price_api.repository.ProductoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> obtenerTodos() {
        // Mantiene el orden estricto ascendente por el ID del producto
        return productoRepository.findAll(Sort.by(Sort.Direction.ASC, "productoId"));
    }

    // Método para Guardar o Actualizar un producto
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    // Método para Eliminar un producto por su ID
    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }
}