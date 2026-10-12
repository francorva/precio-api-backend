package com.example.price_api.controller;

import com.example.price_api.entity.Producto;
import com.example.price_api.service.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@RequestMapping("/productos")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // 1. Obtener todos
    @GetMapping
    public List<Producto> obtenerTodos() {
        return productoService.obtenerTodos();
    }

    // 2. Crear producto con imagen (Protegido para que la BD siempre guarde)
    @PostMapping
    public ResponseEntity<?> crearProducto(
            @RequestPart("producto") Producto producto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        String nombreArchivoUnico = null;

        if (imagen != null && !imagen.isEmpty()) {
            try {
                // Ruta de destino (intentamos guardarla)
                String directorioDestino = "C:/Users/User/OneDrive/Documents cloud/Proyecto Developer/proyecto-de-precios-angular/public/foto_producto/";

                // Asegurar que el directorio físico exista
                Path dirPath = Paths.get(directorioDestino);
                if (!Files.exists(dirPath)) {
                    Files.createDirectories(dirPath);
                }

                String nombreOriginal = imagen.getOriginalFilename();
                nombreArchivoUnico = System.currentTimeMillis() + "_" + nombreOriginal;

                Path rutaCompleta = Paths.get(directorioDestino + nombreArchivoUnico);
                Files.copy(imagen.getInputStream(), rutaCompleta, StandardCopyOption.REPLACE_EXISTING);

                producto.setImagen(nombreArchivoUnico);
            } catch (IOException e) {
                // Si falla la imagen, imprimimos el error en consola pero NO detenemos el guardado del producto
                System.err.println("⚠️ Advertencia: No se pudo guardar la imagen, pero se guardará el producto: " + e.getMessage());
            }
        }

        // Esto ahora se ejecutará siempre, guardando el producto en la BD sí o sí
        Producto productoGuardado = productoService.guardar(producto);
        return ResponseEntity.ok(productoGuardado);
    }

    // 3. Actualizar producto (PUT) con soporte para imagen y datos multipart
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(
            @PathVariable Long id,
            @RequestPart("producto") Producto productoDetalles,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        return productoService.obtenerTodos().stream()
                .filter(p -> p.getProductoId().equals(id))
                .findFirst()
                .map(productoExistente -> {
                    productoExistente.setCodigo(productoDetalles.getCodigo());
                    productoExistente.setNombre(productoDetalles.getNombre());
                    productoExistente.setPrecioUnitario(productoDetalles.getPrecioUnitario());
                    productoExistente.setStock(productoDetalles.getStock());
                    productoExistente.setCategoriaId(productoDetalles.getCategoriaId());

                    // Procesar la nueva imagen si el usuario la seleccionó
                    if (imagen != null && !imagen.isEmpty()) {
                        try {
                            String directorioDestino = "C:/Users/User/OneDrive/Documents cloud/Proyecto Developer/proyecto-de-precios-angular/public/foto_producto/";

                            Path dirPath = Paths.get(directorioDestino);
                            if (!Files.exists(dirPath)) {
                                Files.createDirectories(dirPath);
                            }

                            String nombreOriginal = imagen.getOriginalFilename();
                            String nombreArchivoUnico = System.currentTimeMillis() + "_" + nombreOriginal;

                            Path rutaCompleta = Paths.get(directorioDestino + nombreArchivoUnico);
                            Files.copy(imagen.getInputStream(), rutaCompleta, StandardCopyOption.REPLACE_EXISTING);

                            productoExistente.setImagen(nombreArchivoUnico);
                        } catch (IOException e) {
                            System.err.println("⚠️ Advertencia: No se pudo guardar la nueva imagen al actualizar: " + e.getMessage());
                        }
                    }

                    Producto actualizado = productoService.guardar(productoExistente);
                    return ResponseEntity.ok(actualizado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}