CREATE TYPE estado_enum AS ENUM ('ACTIVO', 'INACTIVO');

CREATE TYPE estado_atencion_enum AS ENUM ('EN_PROCESO',
    'FINALIZADO');
	
CREATE TYPE especialidad_enum AS ENUM ('MEDICINA_GENERAL', 
    'CIRUGIA', 
    'DERMATOLOGIA', 
    'NUTRICION', 
    'VACUNACION', 
    'ANIMALES_EXOTICOS');

CREATE TYPE rol_enum AS ENUM ('ADMIN',
    'RECEPCIONISTA',
    'VETERINARIO');

CREATE TYPE sexo_enum AS ENUM ('MACHO',
   'HEMBRA');

CREATE TYPE estado_cita_enum AS ENUM ('PROGRAMADA', 
    'CONFIRMADA', 
    'EN_ATENCION', 
    'FINALIZADA', 
    'CANCELADA');
	
CREATE TABLE propietarios (
    id SERIAL PRIMARY KEY,
    tipo_identificacion VARCHAR(20) NOT NULL,
    numero_identificacion VARCHAR(20) NOT NULL UNIQUE,
    nombre_completo VARCHAR(150) NOT NULL,
	estado estado_enum NOT NULL DEFAULT 'ACTIVO',
    telefono VARCHAR(20) NOT NULL,
    correo_electronico VARCHAR(150) UNIQUE,
    direccion VARCHAR(200),
    fecha_registro DATE NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE veterinarios (
	id SERIAL PRIMARY KEY,
    tipo_identificacion VARCHAR(20) NOT NULL,
    numero_identificacion VARCHAR(20) NOT NULL UNIQUE,
    nombre_completo VARCHAR(150) NOT NULL,
	estado estado_enum NOT NULL DEFAULT 'ACTIVO',
	tarjeta_profesional VARCHAR(20) NOT NULL UNIQUE,
	especialidad especialidad_enum NOT NULL,
	telefono VARCHAR(20) NOT NULL,
    correo_electronico VARCHAR(150) UNIQUE
);

CREATE TABLE usuarios (
	id SERIAL PRIMARY KEY,
    tipo_identificacion VARCHAR(20) NOT NULL,
    numero_identificacion VARCHAR(20) NOT NULL UNIQUE,
    nombre_completo VARCHAR(150) NOT NULL,
	estado estado_enum NOT NULL DEFAULT 'ACTIVO',
	nombre_usuario VARCHAR(20) NOT NULL UNIQUE,
	contrasena VARCHAR(255) NOT NULL,
	rol rol_enum NOT NULL,
    veterinario_id INTEGER UNIQUE,
	FOREIGN KEY (veterinario_id) REFERENCES veterinarios(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE mascotas (
	id SERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
	especie VARCHAR(150) NOT NULL,
	raza VARCHAR(150) NOT NULL,
	sexo sexo_enum NOT NULL,
	fecha_nacimiento DATE NOT NULL,
	peso NUMERIC(5,2) NOT NULL,
	propietario_id INTEGER NOT NULL,
	estado estado_enum NOT NULL DEFAULT 'ACTIVO',
	fecha_registro DATE NOT NULL DEFAULT CURRENT_DATE,
	FOREIGN KEY (propietario_id) REFERENCES propietarios(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE citas (
	id SERIAL PRIMARY KEY,
    mascota_id INTEGER NOT NULL,
	veterinario_id INTEGER NOT NULL,
	fecha_hora TIMESTAMP NOT NULL,
	motivo VARCHAR(255) NOT NULL,
	estado_cita estado_cita_enum NOT NULL DEFAULT 'PROGRAMADA',
	fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (mascota_id) REFERENCES mascotas(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
	FOREIGN KEY (veterinario_id) REFERENCES veterinarios(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE medicamentos (
	id SERIAL PRIMARY KEY,
    codigo VARCHAR(150) UNIQUE NOT NULL,
	nombre VARCHAR(150) NOT NULL,
	presentacion VARCHAR(150) NOT NULL,
	laboratorio VARCHAR(150) NOT NULL,
	cantidad_disponible INTEGER NOT NULL CHECK (cantidad_disponible >= 0),
	cantidad_minima INTEGER NOT NULL CHECK (cantidad_minima >= 0),
	precio_unitario DECIMAL(10,2) NOT NULL CHECK (precio_unitario >= 0),
	estado estado_enum NOT NULL,
	fecha_registro DATE NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE atenciones_medicas (
	id SERIAL PRIMARY KEY,
	cita_id INTEGER NOT NULL UNIQUE,
	mascota_id INTEGER NOT NULL,
	veterinario_id INTEGER NOT NULL,
    sintomas TEXT NOT NULL,
	diagnostico TEXT,
	tratamiento TEXT,
	observaciones TEXT,
	fecha_atencion DATE NOT NULL,
	estado_atencion estado_atencion_enum NOT NULL DEFAULT 'EN_PROCESO',
	FOREIGN KEY (cita_id) REFERENCES citas(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
	FOREIGN KEY (mascota_id) REFERENCES mascotas(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
	FOREIGN KEY (veterinario_id) REFERENCES veterinarios(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE detalles_medicamento_atencion (
	id SERIAL PRIMARY KEY,
	atencion_medica_id INTEGER NOT NULL,
	medicamento_id INTEGER NOT NULL,
	cantidad_utilizada INTEGER NOT NULL CHECK (cantidad_utilizada > 0),
	UNIQUE (atencion_medica_id, medicamento_id),
	FOREIGN KEY (atencion_medica_id) REFERENCES atenciones_medicas(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
	FOREIGN KEY (medicamento_id) REFERENCES medicamentos(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

-- 1. Un veterinario base
INSERT INTO veterinarios (tipo_identificacion, numero_identificacion, nombre_completo, estado,
                           tarjeta_profesional, especialidad, telefono, correo_electronico)
VALUES ('CC', '1000000001', 'Dr. Carlos Ramírez', 'ACTIVO',
        'TP-12345', 'MEDICINA_GENERAL', '3001112233', 'carlos.ramirez@vetcare.com');

-- 2. Usuario ADMIN (sin veterinario asociado)
INSERT INTO usuarios (tipo_identificacion, numero_identificacion, nombre_completo, estado,
                       nombre_usuario, contrasena, rol, veterinario_id)
VALUES ('CC', '2000000001', 'Admin Sistema', 'ACTIVO', 'admin', 'admin123', 'ADMIN', NULL);

-- 3. Usuario RECEPCIONISTA (sin veterinario asociado)
INSERT INTO usuarios (tipo_identificacion, numero_identificacion, nombre_completo, estado,
                       nombre_usuario, contrasena, rol, veterinario_id)
VALUES ('CC', '2000000002', 'Recepción Uno', 'ACTIVO', 'recepcion', 'recepcion123', 'RECEPCIONISTA', NULL);

-- 4. Usuario VETERINARIO (SÍ asociado al veterinario del paso 1)
INSERT INTO usuarios (tipo_identificacion, numero_identificacion, nombre_completo, estado,
                       nombre_usuario, contrasena, rol, veterinario_id)
VALUES ('CC', '1000000001', 'Dr. Carlos Ramírez', 'ACTIVO', 'cramirez', 'vet123',
        'VETERINARIO', (SELECT id FROM veterinarios WHERE tarjeta_profesional = 'TP-12345'));