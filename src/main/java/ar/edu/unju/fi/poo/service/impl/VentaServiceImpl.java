package ar.edu.unju.fi.poo.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.unju.fi.poo.model.Venta;
import ar.edu.unju.fi.poo.model.VentaContado;
import ar.edu.unju.fi.poo.model.VentaFinanciada;
import ar.edu.unju.fi.poo.repository.VentaRepository;
import ar.edu.unju.fi.poo.service.VentaService;

@Service
public class VentaServiceImpl implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Override
    public VentaContado registrarVentaContado(VentaContado ventaContado) {
        return ventaRepository.save(ventaContado);
    }

    @Override
    public VentaFinanciada registrarVentaFinanciada(VentaFinanciada ventaFinanciada) {
        return ventaRepository.save(ventaFinanciada);
    }

    @Override
    public List<Venta> listarTodas() {
        return ventaRepository.findAll();
    }

    @Override
    public List<Venta> consultarVentasPorCliente(Long clienteId) {
        return ventaRepository.findByClienteId(clienteId);
    }

    @Override
    public List<Venta> consultarVentasPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return ventaRepository.findByFechaBetween(fechaInicio, fechaFin);
    }

    @Override
    public List<Venta> filtrarPorModalidad(String modalidad) {
        if ("CONTADO".equalsIgnoreCase(modalidad)) {
            return ventaRepository.findByModalidadPago(VentaContado.class);
        } else if ("FINANCIADA".equalsIgnoreCase(modalidad)) {
            return ventaRepository.findByModalidadPago(VentaFinanciada.class);
        }
        return ventaRepository.findAll(); 
    }
}