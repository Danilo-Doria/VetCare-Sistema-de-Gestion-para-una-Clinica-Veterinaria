package com.vetcare.vetcare;

import com.vetcare.vetcare.repository.*;
import com.vetcare.vetcare.repository.impl.*;
import com.vetcare.vetcare.service.*;
import com.vetcare.vetcare.service.impl.*;
import com.vetcare.vetcare.controller.*;
import com.vetcare.vetcare.view.MenuPrincipal;

public class VetCare {

    public static void main(String[] args) {
        // Repositorios
        PropietarioRepository propietarioRepository = new PropietarioRepositoryImpl();
        VeterinarioRepository veterinarioRepository = new VeterinarioRepositoryImpl();
        MascotaRepository mascotaRepository = new MascotaRepositoryImpl();
        CitaRepository citaRepository = new CitaRepositoryImpl();
        MedicamentoRepository medicamentoRepository = new MedicamentoRepositoryImpl();
        UsuarioRepository usuarioRepository = new UsuarioRepositoryImpl();
        AtencionMedicaRepository atencionMedicaRepository = new AtencionMedicaRepositoryImpl();
        DetalleMedicamentoAtencionRepository detalleRepository = new DetalleMedicamentoAtencionRepositoryImpl();

        // Servicios (reciben los repositorios por constructor)
        PropietarioService propietarioService = new PropietarioServiceImpl(propietarioRepository);
        VeterinarioService veterinarioService = new VeterinarioServiceImpl(veterinarioRepository);
        MascotaService mascotaService = new MascotaServiceImpl(mascotaRepository);
        CitaService citaService = new CitaServiceImpl(citaRepository);
        MedicamentoService medicamentoService = new MedicamentoServiceImpl(medicamentoRepository);
        UsuarioService usuarioService = new UsuarioServiceImpl(usuarioRepository);
        AtencionMedicaService atencionMedicaService = new AtencionMedicaServiceImpl(atencionMedicaRepository, citaRepository);
        DetalleMedicamentoAtencionService detalleService = new DetalleMedicamentoAtencionServiceImpl(detalleRepository);

        // Controllers (reciben los servicios por constructor)
        PropietarioController propietarioController = new PropietarioController(propietarioService);
        VeterinarioController veterinarioController = new VeterinarioController(veterinarioService);
        MascotaController mascotaController = new MascotaController(mascotaService);
        CitaController citaController = new CitaController(citaService);
        MedicamentoController medicamentoController = new MedicamentoController(medicamentoService);
        UsuarioController usuarioController = new UsuarioController(usuarioService);
        AtencionMedicaController atencionMedicaController = new AtencionMedicaController(atencionMedicaService);

        // Arrancamos el menú principal, pasándole todos los controllers que necesita
        MenuPrincipal menu = new MenuPrincipal(
                propietarioController, veterinarioController, mascotaController,
                citaController, medicamentoController, usuarioController, atencionMedicaController
        );
        menu.mostrar();
    }
}
