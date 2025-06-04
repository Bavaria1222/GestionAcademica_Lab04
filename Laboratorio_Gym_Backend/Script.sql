CREATE OR REPLACE PACKAGE types
AS
     TYPE ref_cursor IS REF CURSOR;
END;
/

--TABLA PROCEDIMIENTOS Y FUNCIONES DE CARRERA
CREATE TABLE carrera (
    idcarrera INT PRIMARY KEY,
    codigo    VARCHAR(20),
    nombre    VARCHAR(50),
    titulo    VARCHAR(50)
);

CREATE OR REPLACE PROCEDURE insertarcarrera (
    p_idcarrera IN carrera.idcarrera%TYPE,
    p_codigo    IN carrera.codigo%TYPE,
    p_nombre    IN carrera.nombre%TYPE,
    p_titulo    IN carrera.titulo%TYPE
) AS
BEGIN
    INSERT INTO carrera (
        idcarrera,
        codigo,
        nombre,
        titulo
    ) VALUES (
        p_idcarrera,
        p_codigo,
        p_nombre,
        p_titulo
    );

END;
/

CREATE OR REPLACE FUNCTION listarcarrera
RETURN Types.ref_cursor 
AS 
    carrera_cursor Types.ref_cursor; 
BEGIN 
    OPEN carrera_cursor FOR 
       SELECT idcarrera, codigo, nombre, titulo FROM carrera; 
    RETURN carrera_cursor; 
END;
/

CREATE OR REPLACE FUNCTION buscarCarrera(idbuscar IN carrera.idcarrera%TYPE)
RETURN Types.ref_cursor 
AS 
    carrera_cursor Types.ref_cursor; 
BEGIN 
    OPEN carrera_cursor FOR 
       SELECT idcarrera, codigo, nombre, titulo 
       FROM carrera 
       WHERE idcarrera = idbuscar; 
    RETURN carrera_cursor; 
END;
/

CREATE OR REPLACE PROCEDURE modificarCarrera(
    p_idcarrera IN carrera.idcarrera%TYPE,
    p_codigo    IN carrera.codigo%TYPE,
    p_nombre    IN carrera.nombre%TYPE,
    p_titulo    IN carrera.titulo%TYPE,
    p_rows OUT NUMBER
) AS
BEGIN
    UPDATE carrera 
    SET codigo = p_codigo,
        nombre = p_nombre,
        titulo = p_titulo
    WHERE idcarrera = p_idcarrera;
    
    p_rows := SQL%ROWCOUNT;
END;
/


CREATE OR REPLACE PROCEDURE eliminarCarrera(
    idin IN carrera.idcarrera%TYPE
) AS
BEGIN
    DELETE FROM carrera WHERE idcarrera = idin;
END;
/

--TABLA PROCEDIMIENTOS Y FUNCIONES DE USUARIO
CREATE TABLE usuario (
    cedula VARCHAR2(20) PRIMARY KEY,
    clave  VARCHAR2(100) NOT NULL,
    rol    VARCHAR2(30) NOT NULL
);

CREATE OR REPLACE PROCEDURE insertarUsuario(
    p_cedula IN usuario.cedula%TYPE,
    p_clave  IN usuario.clave%TYPE,
    p_rol    IN usuario.rol%TYPE
) AS
BEGIN
    INSERT INTO usuario (cedula, clave, rol)
    VALUES (p_cedula, p_clave, p_rol);
END;
/

CREATE OR REPLACE FUNCTION listarUsuario
RETURN types.ref_cursor 
AS 
    usuario_cursor types.ref_cursor; 
BEGIN 
    OPEN usuario_cursor FOR 
       SELECT cedula, clave, rol FROM usuario; 
    RETURN usuario_cursor; 
END;
/
CREATE OR REPLACE FUNCTION buscarUsuario(
    p_cedula IN usuario.cedula%TYPE
) RETURN types.ref_cursor 
AS 
    usuario_cursor types.ref_cursor; 
BEGIN 
    OPEN usuario_cursor FOR 
       SELECT cedula, clave, rol FROM usuario 
       WHERE cedula = p_cedula; 
    RETURN usuario_cursor; 
END;
/

CREATE OR REPLACE PROCEDURE modificarUsuario(
    p_cedula IN usuario.cedula%TYPE,
    p_clave  IN usuario.clave%TYPE,
    p_rol    IN usuario.rol%TYPE,
    p_rows   OUT NUMBER
) AS
BEGIN
    UPDATE usuario
    SET clave = p_clave,
        rol   = p_rol
    WHERE cedula = p_cedula;
    p_rows := SQL%ROWCOUNT;
END;
/

CREATE OR REPLACE PROCEDURE eliminarUsuario(
    p_cedula IN usuario.cedula%TYPE
) AS
BEGIN
    DELETE FROM usuario WHERE cedula = p_cedula;
END;
/


--TABLA PROCEDIMIENTOS Y FUNCIONES DE CURSO
CREATE TABLE curso (
    idcurso NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    codigo VARCHAR2(20) NOT NULL,
    nombre VARCHAR2(50) NOT NULL,
    creditos INT,
    horasSemanales INT
);

CREATE OR REPLACE PROCEDURE insertarCurso(
    p_codigo         IN curso.codigo%TYPE,
    p_nombre         IN curso.nombre%TYPE,
    p_creditos       IN curso.creditos%TYPE,
    p_horasSemanales IN curso.horasSemanales%TYPE
) AS
BEGIN
    INSERT INTO curso (codigo, nombre, creditos, horasSemanales)
    VALUES (p_codigo, p_nombre, p_creditos, p_horasSemanales);
END;
/

CREATE OR REPLACE FUNCTION listarCurso
RETURN types.ref_cursor 
AS 
    curso_cursor types.ref_cursor; 
BEGIN 
    OPEN curso_cursor FOR 
       SELECT idcurso, codigo, nombre, creditos, horasSemanales FROM curso; 
    RETURN curso_cursor; 
END;
/
CREATE OR REPLACE FUNCTION buscarCurso(
    p_id IN curso.idcurso%TYPE
) RETURN types.ref_cursor 
AS 
    curso_cursor types.ref_cursor; 
BEGIN 
    OPEN curso_cursor FOR 
       SELECT idcurso, codigo, nombre, creditos, horasSemanales 
       FROM curso 
       WHERE idcurso = p_id; 
    RETURN curso_cursor; 
END;
/
CREATE OR REPLACE PROCEDURE modificarCurso(
    p_id             IN curso.idcurso%TYPE,
    p_codigo         IN curso.codigo%TYPE,
    p_nombre         IN curso.nombre%TYPE,
    p_creditos       IN curso.creditos%TYPE,
    p_horasSemanales IN curso.horasSemanales%TYPE,
    p_rows           OUT NUMBER
) AS
BEGIN
    UPDATE curso
    SET codigo = p_codigo,
        nombre = p_nombre,
        creditos = p_creditos,
        horasSemanales = p_horasSemanales
    WHERE idcurso = p_id;
    
    p_rows := SQL%ROWCOUNT;
END;
/
CREATE OR REPLACE PROCEDURE eliminarCurso(
    p_id IN curso.idcurso%TYPE
) AS
BEGIN
    DELETE FROM curso WHERE idcurso = p_id;
END;
/

--TABLA, PROCEDIMIENTOS Y FUNCIONES DE PLAN_ESTUDIO

CREATE TABLE plan_estudio (
    idPlanEstudio NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    idCarrera     INT NOT NULL,
    idCurso       INT NOT NULL,
    anio          INT,
    ciclo         INT,
    CONSTRAINT fk_plan_carrera FOREIGN KEY (idCarrera) REFERENCES carrera(idcarrera),
    CONSTRAINT fk_plan_curso   FOREIGN KEY (idCurso)   REFERENCES curso(idcurso)
);

CREATE OR REPLACE PROCEDURE insertarPlanEstudio(
    p_idCarrera IN plan_estudio.idCarrera%TYPE,
    p_idCurso   IN plan_estudio.idCurso%TYPE,
    p_anio      IN plan_estudio.anio%TYPE,
    p_ciclo     IN plan_estudio.ciclo%TYPE
) AS
BEGIN
    INSERT INTO plan_estudio (idCarrera, idCurso, anio, ciclo)
    VALUES (p_idCarrera, p_idCurso, p_anio, p_ciclo);
END;
/

CREATE OR REPLACE FUNCTION listarPlanEstudio
RETURN types.ref_cursor 
AS 
    plan_cursor types.ref_cursor; 
BEGIN 
    OPEN plan_cursor FOR 
       SELECT idPlanEstudio, idCarrera, idCurso, anio, ciclo 
       FROM plan_estudio; 
    RETURN plan_cursor; 
END;
/

CREATE OR REPLACE FUNCTION buscarPlanEstudio(
    p_id IN plan_estudio.idPlanEstudio%TYPE
) RETURN types.ref_cursor 
AS 
    plan_cursor types.ref_cursor; 
BEGIN 
    OPEN plan_cursor FOR 
       SELECT idPlanEstudio, idCarrera, idCurso, anio, ciclo 
       FROM plan_estudio 
       WHERE idPlanEstudio = p_id; 
    RETURN plan_cursor; 
END;
/

CREATE OR REPLACE PROCEDURE modificarPlanEstudio(
    p_id             IN plan_estudio.idPlanEstudio%TYPE,
    p_idCarrera      IN plan_estudio.idCarrera%TYPE,
    p_idCurso        IN plan_estudio.idCurso%TYPE,
    p_anio           IN plan_estudio.anio%TYPE,
    p_ciclo          IN plan_estudio.ciclo%TYPE,
    p_rows           OUT NUMBER
) AS
BEGIN
    UPDATE plan_estudio
    SET idCarrera = p_idCarrera,
        idCurso   = p_idCurso,
        anio      = p_anio,
        ciclo     = p_ciclo
    WHERE idPlanEstudio = p_id;
    
    p_rows := SQL%ROWCOUNT;
END;
/
CREATE OR REPLACE PROCEDURE eliminarPlanEstudio(
    p_id IN plan_estudio.idPlanEstudio%TYPE
) AS
BEGIN
    DELETE FROM plan_estudio 
    WHERE idPlanEstudio = p_id;
END;
/

--TABLA, PROCEDIMIENTO Y FUNCIONES DE PROFESOR


CREATE TABLE profesor (
    cedula VARCHAR2(20) PRIMARY KEY,
    nombre VARCHAR2(100) NOT NULL,
    telefono VARCHAR2(20),
    email VARCHAR2(100),
    CONSTRAINT fk_profesor_usuario FOREIGN KEY (cedula) REFERENCES usuario(cedula)
);

CREATE OR REPLACE PROCEDURE insertarProfesorCompleto(
    p_cedula   IN usuario.cedula%TYPE,
    p_clave    IN usuario.clave%TYPE,
    p_rol      IN usuario.rol%TYPE,
    p_nombre   IN profesor.nombre%TYPE,
    p_telefono IN profesor.telefono%TYPE,
    p_email    IN profesor.email%TYPE
) AS
BEGIN
    -- Inserción en la tabla de usuarios (datos comunes)
    INSERT INTO usuario (cedula, clave, rol)
    VALUES (p_cedula, p_clave, p_rol);
    
    -- Inserción en la tabla de profesores (datos específicos)
    INSERT INTO profesor (cedula, nombre, telefono, email)
    VALUES (p_cedula, p_nombre, p_telefono, p_email);
    
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/

CREATE OR REPLACE FUNCTION listarProfesor
RETURN types.ref_cursor 
AS 
    prof_cursor types.ref_cursor; 
BEGIN 
    OPEN prof_cursor FOR 
       SELECT u.cedula, u.clave, u.rol, p.nombre, p.telefono, p.email
       FROM usuario u
       JOIN profesor p ON u.cedula = p.cedula;
    RETURN prof_cursor; 
END;
/

CREATE OR REPLACE FUNCTION buscarProfesor(
    p_cedula IN usuario.cedula%TYPE
) RETURN types.ref_cursor 
AS 
    prof_cursor types.ref_cursor; 
BEGIN 
    OPEN prof_cursor FOR 
       SELECT u.cedula, u.clave, u.rol, p.nombre, p.telefono, p.email
       FROM usuario u
       JOIN profesor p ON u.cedula = p.cedula
       WHERE u.cedula = p_cedula;
    RETURN prof_cursor; 
END;
/

CREATE OR REPLACE PROCEDURE modificarProfesor(
    p_cedula   IN usuario.cedula%TYPE,
    p_clave    IN usuario.clave%TYPE,
    p_nombre   IN profesor.nombre%TYPE,
    p_telefono IN profesor.telefono%TYPE,
    p_email    IN profesor.email%TYPE,
    p_rows     OUT NUMBER
) AS
    rows1 NUMBER;
    rows2 NUMBER;
BEGIN
    -- Actualizar datos en la tabla USUARIO
    UPDATE usuario
    SET clave = p_clave,
        rol = 'PROFESOR'
    WHERE cedula = p_cedula;
    rows1 := SQL%ROWCOUNT;
    
    -- Actualizar datos específicos en la tabla PROFESOR
    UPDATE profesor
    SET nombre = p_nombre,
        telefono = p_telefono,
        email = p_email
    WHERE cedula = p_cedula;
    rows2 := SQL%ROWCOUNT;
    
    p_rows := rows1 + rows2;
END;
/

CREATE OR REPLACE PROCEDURE eliminarProfesor(
    p_cedula IN usuario.cedula%TYPE
) AS
BEGIN
    -- Primero eliminar de la tabla PROFESOR
    DELETE FROM profesor WHERE cedula = p_cedula;
    
    -- Luego eliminar de la tabla USUARIO
    DELETE FROM usuario WHERE cedula = p_cedula;
    
    COMMIT;
END;
/

--TABLA,  PROCEDIMIENTOS FUNCIONES ALUMNO

CREATE TABLE alumno (
    cedula VARCHAR2(20) PRIMARY KEY,
    nombre VARCHAR2(100) NOT NULL,
    telefono VARCHAR2(20),
    email VARCHAR2(100),
    fechaNacimiento DATE,
    idCarrera INT NOT NULL,
    CONSTRAINT fk_alumno_carrera FOREIGN KEY (idCarrera) REFERENCES carrera(idcarrera),
    CONSTRAINT fk_alumno_usuario FOREIGN KEY (cedula) REFERENCES usuario(cedula)
);

CREATE OR REPLACE PROCEDURE insertarAlumnoCompleto(
    p_cedula         IN usuario.cedula%TYPE,
    p_clave          IN usuario.clave%TYPE,
    p_nombre         IN alumno.nombre%TYPE,
    p_telefono       IN alumno.telefono%TYPE,
    p_email          IN alumno.email%TYPE,
    p_fechaNacimiento IN alumno.fechaNacimiento%TYPE,
    p_idCarrera      IN alumno.idCarrera%TYPE
) AS
BEGIN
    -- Inserta en la tabla usuario
    INSERT INTO usuario (cedula, clave, rol)
    VALUES (p_cedula, p_clave, 'ALUMNO');
    
    -- Inserta en la tabla alumno
    INSERT INTO alumno (cedula, nombre, telefono, email, fechaNacimiento, idCarrera)
    VALUES (p_cedula, p_nombre, p_telefono, p_email, p_fechaNacimiento, p_idCarrera);
    
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
/
CREATE OR REPLACE FUNCTION listarAlumno
RETURN types.ref_cursor 
AS 
    alumno_cursor types.ref_cursor; 
BEGIN 
    OPEN alumno_cursor FOR 
       SELECT u.cedula, u.clave, u.rol, a.nombre, a.telefono, a.email, a.fechaNacimiento, a.idCarrera
       FROM usuario u
       JOIN alumno a ON u.cedula = a.cedula
       WHERE u.rol = 'ALUMNO';
    RETURN alumno_cursor; 
END;
/

CREATE OR REPLACE FUNCTION buscarAlumno(
    p_cedula IN usuario.cedula%TYPE
) RETURN types.ref_cursor 
AS 
    alumno_cursor types.ref_cursor; 
BEGIN 
    OPEN alumno_cursor FOR 
       SELECT u.cedula, u.clave, u.rol, a.nombre, a.telefono, a.email, a.fechaNacimiento, a.idCarrera
       FROM usuario u
       JOIN alumno a ON u.cedula = a.cedula
       WHERE u.cedula = p_cedula AND u.rol = 'ALUMNO';
    RETURN alumno_cursor; 
END;
/

CREATE OR REPLACE PROCEDURE modificarAlumno(
    p_cedula         IN usuario.cedula%TYPE,
    p_clave          IN usuario.clave%TYPE,
    p_nombre         IN alumno.nombre%TYPE,
    p_telefono       IN alumno.telefono%TYPE,
    p_email          IN alumno.email%TYPE,
    p_fechaNacimiento IN alumno.fechaNacimiento%TYPE,
    p_idCarrera      IN alumno.idCarrera%TYPE,
    p_rows           OUT NUMBER
) AS
    rows1 NUMBER;
    rows2 NUMBER;
BEGIN
    -- Actualiza la tabla usuario
    UPDATE usuario
    SET clave = p_clave,
        rol = 'ALUMNO'
    WHERE cedula = p_cedula;
    rows1 := SQL%ROWCOUNT;
    
    -- Actualiza la tabla alumno
    UPDATE alumno
    SET nombre = p_nombre,
        telefono = p_telefono,
        email = p_email,
        fechaNacimiento = p_fechaNacimiento,
        idCarrera = p_idCarrera
    WHERE cedula = p_cedula;
    rows2 := SQL%ROWCOUNT;
    
    p_rows := rows1 + rows2;
END;
/

CREATE OR REPLACE PROCEDURE eliminarAlumno(
    p_cedula IN usuario.cedula%TYPE
) AS
BEGIN
    -- Eliminar de la tabla alumno
    DELETE FROM alumno WHERE cedula = p_cedula;
    
    -- Eliminar de la tabla usuario
    DELETE FROM usuario WHERE cedula = p_cedula;
    
    COMMIT;
END;
/

--TABLA,  PROCEDIMIENTOS FUNCIONES CICLO

CREATE TABLE ciclo (
    idCiclo NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    anio      INT NOT NULL,
    numero    INT NOT NULL,  -- 1 para el primer ciclo, 2 para el segundo
    fechaInicio DATE NOT NULL,
    fechaFin    DATE NOT NULL
);

CREATE OR REPLACE PROCEDURE insertarCiclo(
    p_anio       IN ciclo.anio%TYPE,
    p_numero     IN ciclo.numero%TYPE,
    p_fechaInicio IN ciclo.fechaInicio%TYPE,
    p_fechaFin    IN ciclo.fechaFin%TYPE
) AS
BEGIN
    INSERT INTO ciclo (anio, numero, fechaInicio, fechaFin)
    VALUES (p_anio, p_numero, p_fechaInicio, p_fechaFin);
END;
/

CREATE OR REPLACE FUNCTION listarCiclo
RETURN types.ref_cursor 
AS 
    ciclo_cursor types.ref_cursor; 
BEGIN 
    OPEN ciclo_cursor FOR 
       SELECT idCiclo, anio, numero, fechaInicio, fechaFin FROM ciclo; 
    RETURN ciclo_cursor; 
END;
/

CREATE OR REPLACE FUNCTION buscarCiclo(
    p_id IN ciclo.idCiclo%TYPE
) RETURN types.ref_cursor 
AS 
    ciclo_cursor types.ref_cursor; 
BEGIN 
    OPEN ciclo_cursor FOR 
       SELECT idCiclo, anio, numero, fechaInicio, fechaFin 
       FROM ciclo 
       WHERE idCiclo = p_id; 
    RETURN ciclo_cursor; 
END;
/

CREATE OR REPLACE PROCEDURE modificarCiclo(
    p_id         IN ciclo.idCiclo%TYPE,
    p_anio       IN ciclo.anio%TYPE,
    p_numero     IN ciclo.numero%TYPE,
    p_fechaInicio IN ciclo.fechaInicio%TYPE,
    p_fechaFin    IN ciclo.fechaFin%TYPE,
    p_rows       OUT NUMBER
) AS
BEGIN
    UPDATE ciclo
    SET anio = p_anio,
        numero = p_numero,
        fechaInicio = p_fechaInicio,
        fechaFin = p_fechaFin
    WHERE idCiclo = p_id;
    
    p_rows := SQL%ROWCOUNT;
END;
/

CREATE OR REPLACE PROCEDURE eliminarCiclo(
    p_id IN ciclo.idCiclo%TYPE
) AS
BEGIN
    DELETE FROM ciclo WHERE idCiclo = p_id;
END;
/

--TABLA,  PROCEDIMIENTOS FUNCIONE GRUPO

CREATE TABLE grupo (
    idGrupo NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    idCiclo NUMBER NOT NULL,
    idCurso NUMBER NOT NULL,
    numGrupo NUMBER NOT NULL,
    horario VARCHAR2(100),
    idProfesor VARCHAR2(20) NOT NULL,
    CONSTRAINT fk_grupo_ciclo FOREIGN KEY (idCiclo) REFERENCES ciclo(idCiclo),
    CONSTRAINT fk_grupo_curso FOREIGN KEY (idCurso) REFERENCES curso(idcurso),
    CONSTRAINT fk_grupo_profesor FOREIGN KEY (idProfesor) REFERENCES profesor(cedula)
);

CREATE OR REPLACE PROCEDURE insertarGrupo(
    p_idCiclo    IN grupo.idCiclo%TYPE,
    p_idCurso    IN grupo.idCurso%TYPE,
    p_numGrupo   IN grupo.numGrupo%TYPE,
    p_horario    IN grupo.horario%TYPE,
    p_idProfesor IN grupo.idProfesor%TYPE
) AS
BEGIN
    INSERT INTO grupo (idCiclo, idCurso, numGrupo, horario, idProfesor)
    VALUES (p_idCiclo, p_idCurso, p_numGrupo, p_horario, p_idProfesor);
END;
/
CREATE OR REPLACE FUNCTION listarGrupo
RETURN types.ref_cursor 
AS 
    grupo_cursor types.ref_cursor; 
BEGIN 
    OPEN grupo_cursor FOR 
       SELECT idGrupo, idCiclo, idCurso, numGrupo, horario, idProfesor 
       FROM grupo; 
    RETURN grupo_cursor; 
END;
/
CREATE OR REPLACE FUNCTION buscarGrupo(
    p_id IN grupo.idGrupo%TYPE
) RETURN types.ref_cursor 
AS 
    grupo_cursor types.ref_cursor; 
BEGIN 
    OPEN grupo_cursor FOR 
       SELECT idGrupo, idCiclo, idCurso, numGrupo, horario, idProfesor 
       FROM grupo 
       WHERE idGrupo = p_id; 
    RETURN grupo_cursor; 
END;
/
CREATE OR REPLACE PROCEDURE modificarGrupo(
    p_id         IN grupo.idGrupo%TYPE,
    p_idCiclo    IN grupo.idCiclo%TYPE,
    p_idCurso    IN grupo.idCurso%TYPE,
    p_numGrupo   IN grupo.numGrupo%TYPE,
    p_horario    IN grupo.horario%TYPE,
    p_idProfesor IN grupo.idProfesor%TYPE,
    p_rows       OUT NUMBER
) AS
BEGIN
    UPDATE grupo
    SET idCiclo = p_idCiclo,
        idCurso = p_idCurso,
        numGrupo = p_numGrupo,
        horario = p_horario,
        idProfesor = p_idProfesor
    WHERE idGrupo = p_id;
    
    p_rows := SQL%ROWCOUNT;
END;
/

CREATE OR REPLACE PROCEDURE eliminarGrupo(
    p_id IN grupo.idGrupo%TYPE
) AS
BEGIN
    DELETE FROM grupo WHERE idGrupo = p_id;
END;
/

CREATE OR REPLACE FUNCTION listarGrupoPorProfesor(
    p_idCiclo IN grupo.idCiclo%TYPE,
    p_cedula  IN grupo.idProfesor%TYPE
) RETURN types.ref_cursor
AS
    grupo_cursor types.ref_cursor;
BEGIN
    OPEN grupo_cursor FOR
       SELECT idGrupo, idCiclo, idCurso, numGrupo, horario, idProfesor
       FROM grupo
       WHERE idProfesor = p_cedula
         AND idCiclo = p_idCiclo;
    RETURN grupo_cursor;
END;
/

--TABLA,  PROCEDIMIENTOS FUNCIONE MATRICULA
CREATE TABLE matricula (
    idMatricula NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cedulaAlumno VARCHAR2(20) NOT NULL,
    idGrupo      NUMBER NOT NULL,
    nota         NUMBER,  -- Puedes ajustar precisión según requerimientos, por ejemplo NUMBER(3,2)
    CONSTRAINT fk_matricula_alumno FOREIGN KEY (cedulaAlumno) REFERENCES alumno(cedula),
    CONSTRAINT fk_matricula_grupo  FOREIGN KEY (idGrupo) REFERENCES grupo(idGrupo)
);

CREATE OR REPLACE PROCEDURE insertarMatricula(
    p_cedulaAlumno IN matricula.cedulaAlumno%TYPE,
    p_idGrupo      IN matricula.idGrupo%TYPE,
    p_nota         IN matricula.nota%TYPE
) AS
BEGIN
    INSERT INTO matricula (cedulaAlumno, idGrupo, nota)
    VALUES (p_cedulaAlumno, p_idGrupo, p_nota);
END;
/
CREATE OR REPLACE FUNCTION listarMatricula
RETURN types.ref_cursor 
AS 
    matricula_cursor types.ref_cursor; 
BEGIN 
    OPEN matricula_cursor FOR 
       SELECT idMatricula, cedulaAlumno, idGrupo, nota 
       FROM matricula; 
    RETURN matricula_cursor; 
END;
/
CREATE OR REPLACE FUNCTION buscarMatricula(
    p_id IN matricula.idMatricula%TYPE
) RETURN types.ref_cursor
AS
    matricula_cursor types.ref_cursor;
BEGIN
    OPEN matricula_cursor FOR
       SELECT idMatricula, cedulaAlumno, idGrupo, nota
       FROM matricula
       WHERE idMatricula = p_id;
    RETURN matricula_cursor;
END;
/

CREATE OR REPLACE FUNCTION listarMatriculaAlumno(
    p_cedula IN matricula.cedulaAlumno%TYPE
) RETURN types.ref_cursor
AS
    matricula_cursor types.ref_cursor;
BEGIN
    OPEN matricula_cursor FOR
       SELECT idMatricula, cedulaAlumno, idGrupo, nota
       FROM matricula
       WHERE cedulaAlumno = p_cedula;
    RETURN matricula_cursor;
END;
/

CREATE OR REPLACE PROCEDURE modificarMatricula(
    p_id            IN matricula.idMatricula%TYPE,
    p_cedulaAlumno  IN matricula.cedulaAlumno%TYPE,
    p_idGrupo       IN matricula.idGrupo%TYPE,
    p_nota          IN matricula.nota%TYPE,
    p_rows          OUT NUMBER
) AS
BEGIN
    UPDATE matricula
    SET cedulaAlumno = p_cedulaAlumno,
        idGrupo = p_idGrupo,
        nota = p_nota
    WHERE idMatricula = p_id;
    
    p_rows := SQL%ROWCOUNT;
END;
/
CREATE OR REPLACE PROCEDURE eliminarMatricula(
    p_id IN matricula.idMatricula%TYPE
) AS
BEGIN
    DELETE FROM matricula
    WHERE idMatricula = p_id;
END;
/




-----Datos quemados

---Insertar Carrera
INSERT INTO carrera (idcarrera, codigo, nombre, titulo) 
VALUES (1, 'ING-SOFT', 'Ingeniería en Software', 'Licenciado en Ingeniería en Software');

INSERT INTO carrera (idcarrera, codigo, nombre, titulo) 
VALUES (2, 'ADM-EMP', 'Administración de Empresas', 'Licenciado en Administración de Empresas');

-- Para los estudiantes:
-- Estudiante 1 (asociado a la carrera con id 1: ING-SOFT)
INSERT INTO usuario (cedula, clave, rol)
VALUES ('S001', 'passS1', 'ALUMNO');

INSERT INTO alumno (cedula, nombre, telefono, email, fechaNacimiento, idCarrera)
VALUES (
  'S001',
  'Estudiante Uno',
  '111111',
  'est1@example.com',
  TO_DATE('1999-01-01','YYYY-MM-DD'),
  1
);

-- Estudiante 2 (asociado a la carrera con id 2: ADM-EMP)
INSERT INTO usuario (cedula, clave, rol)
VALUES ('S002', 'passS2', 'ALUMNO');

INSERT INTO alumno (cedula, nombre, telefono, email, fechaNacimiento, idCarrera)
VALUES (
  'S002',
  'Estudiante Dos',
  '222222',
  'est2@example.com',
  TO_DATE('2000-02-02','YYYY-MM-DD'),
  2
);

-- Para los profesores:
-- Profesor 1
INSERT INTO usuario (cedula, clave, rol)
VALUES ('P001', 'passP1', 'PROFESOR');

INSERT INTO profesor (cedula, nombre, telefono, email)
VALUES ('P001', 'Profesor Uno', '333333', 'prof1@example.com');

-- Profesor 2
INSERT INTO usuario (cedula, clave, rol)
VALUES ('P002', 'passP2', 'PROFESOR');

INSERT INTO profesor (cedula, nombre, telefono, email)
VALUES ('P002', 'Profesor Dos', '444444', 'prof2@example.com');



-- Insertar curso para Ingeniería en Software
INSERT INTO curso (codigo, nombre, creditos, horasSemanales)
VALUES ('CS101', 'Fundamentos de Programación', 4, 3);

-- Insertar curso para Administración de Empresas
INSERT INTO curso (codigo, nombre, creditos, horasSemanales)
VALUES ('ADM101', 'Fundamentos de Administración', 3, 2);

COMMIT;


--Insertar plan de estudio para la carrera Administraci[on
INSERT INTO plan_estudio (idCarrera, idCurso, anio, ciclo)
VALUES (
    2,
    (SELECT idcurso FROM curso WHERE codigo = 'ADM101'),
    2023,
    1
);


--Insert 2 cnuevos ciclos

-- Inserta el primer ciclo lectivo para el año 2023 (primer ciclo)
INSERT INTO ciclo (anio, numero, fechaInicio, fechaFin)
VALUES (
    2023,
    1,
    TO_DATE('2023-01-15','YYYY-MM-DD'),
    TO_DATE('2023-06-15','YYYY-MM-DD')
);

-- Inserta el segundo ciclo lectivo para el año 2023 (segundo ciclo)
INSERT INTO ciclo (anio, numero, fechaInicio, fechaFin)
VALUES (
    2023,
    2,
    TO_DATE('2023-07-01','YYYY-MM-DD'),
    TO_DATE('2023-12-15','YYYY-MM-DD')
);

---Insertar datos de un  Grupo

INSERT INTO grupo (idCiclo, idCurso, numGrupo, horario, idProfesor)
VALUES (
    1,                           -- idCiclo: asume que ya existe un ciclo con id 1
    2,                           -- idCurso: asume que ya existe un curso con id 2 (CS101, Fundamentos de Programación)
    1,                           -- numGrupo: número del grupo (por ejemplo, grupo 1)
    'Lunes y Miércoles 10:00-12:00', -- horario
    'P002'                     -- idProfesor: asume que existe un profesor con cédula '987654'
);


-- Matrícula del estudiante S002 con nota asignada
INSERT INTO matricula (cedulaAlumno, idGrupo, nota)
VALUES ('S002', 1, 85);

-- Grupo adicional impartido por el profesor P001
INSERT INTO grupo (idCiclo, idCurso, numGrupo, horario, idProfesor)
VALUES (
    1,
    (SELECT idcurso FROM curso WHERE codigo = 'CS101'),
    2,
    'Martes y Jueves 08:00-10:00',
    'P001'
);

-- Matrícula del estudiante S001 en el grupo anterior con nota
INSERT INTO matricula (cedulaAlumno, idGrupo, nota)
VALUES (
    'S001',
    (SELECT idGrupo FROM grupo WHERE idCurso = (SELECT idcurso FROM curso WHERE codigo = 'CS101') AND numGrupo = 2 AND idProfesor = 'P001'),
    90
);

-- Grupo extra para pruebas en ciclo 1
INSERT INTO grupo (idCiclo, idCurso, numGrupo, horario, idProfesor)
VALUES (
    1,
    (SELECT idcurso FROM curso WHERE codigo = 'ADM101'),
    3,
    'Viernes 09:00-11:00',
    'P002'
);

-- Matrícula sin nota inicial
INSERT INTO matricula (cedulaAlumno, idGrupo, nota)
VALUES (
    'S002',
    (SELECT idGrupo FROM grupo WHERE idCurso = (SELECT idcurso FROM curso WHERE codigo = 'ADM101') AND numGrupo = 3 AND idProfesor = 'P002'),
    NULL
);

COMMIT;


--select * from carrera;
--select * from usuario;
--select * from curso;
--select * from plan_estudio;
--select * from profesor;
--select  * from alumno;
--select * from ciclo;
--select * from grupo;
--select * from matricula;


--====================================

--01/06/2025--
--====================================






-- ================================
-- ELIMINAR ALUMNO
-- ================================
CREATE OR REPLACE PROCEDURE eliminarAlumno (
    p_cedula IN usuario.cedula%TYPE,
    p_result OUT NUMBER
) AS
    v_count NUMBER := 0;
BEGIN
    DELETE FROM alumno WHERE cedula = p_cedula;
    v_count := SQL%ROWCOUNT;

    DELETE FROM usuario WHERE cedula = p_cedula;
    v_count := v_count + SQL%ROWCOUNT;

    COMMIT;

    p_result := v_count;
END;
/

-- ================================
-- ELIMINAR PROFESOR
-- ================================
CREATE OR REPLACE PROCEDURE eliminarProfesor (
    p_cedula IN usuario.cedula%TYPE,
    p_result OUT NUMBER
) AS
    v_count NUMBER := 0;
BEGIN
    DELETE FROM profesor WHERE cedula = p_cedula;
    v_count := SQL%ROWCOUNT;

    DELETE FROM usuario WHERE cedula = p_cedula;
    v_count := v_count + SQL%ROWCOUNT;

    COMMIT;

    p_result := v_count;
END;
/

-- ================================
-- ELIMINAR CARRERA
-- ================================
CREATE OR REPLACE PROCEDURE eliminarCarrera (
    p_idCarrera IN carrera.idCarrera%TYPE,
    p_result OUT NUMBER
) AS
BEGIN
    DELETE FROM carrera WHERE idCarrera = p_idCarrera;
    p_result := SQL%ROWCOUNT;
    COMMIT;
END;
/

-- ================================
-- ELIMINAR CURSO
-- ================================
CREATE OR REPLACE PROCEDURE eliminarCurso (
    p_idCurso IN curso.idCurso%TYPE,
    p_result OUT NUMBER
) AS
BEGIN
    DELETE FROM curso WHERE idCurso = p_idCurso;
    p_result := SQL%ROWCOUNT;
    COMMIT;
END;
/

-- ================================
-- ELIMINAR GRUPO
-- ================================
CREATE OR REPLACE PROCEDURE eliminarGrupo (
    p_idGrupo IN grupo.idGrupo%TYPE,
    p_result OUT NUMBER
) AS
BEGIN
    DELETE FROM grupo WHERE idGrupo = p_idGrupo;
    p_result := SQL%ROWCOUNT;
    COMMIT;
END;
/

-- ================================
-- ELIMINAR MATRICULA
-- ================================
CREATE OR REPLACE PROCEDURE eliminarMatricula (
    p_idMatricula IN matricula.idMatricula%TYPE,
    p_result OUT NUMBER
) AS
BEGIN
    DELETE FROM matricula WHERE idMatricula = p_idMatricula;
    p_result := SQL%ROWCOUNT;
    COMMIT;
END;
/

-- ================================
-- ELIMINAR PLAN DE ESTUDIO
-- ================================
CREATE OR REPLACE PROCEDURE eliminarPlanEstudio (
    p_idPlan IN plan_estudio.idPlan%TYPE,
    p_result OUT NUMBER
) AS
BEGIN
    DELETE FROM plan_estudio WHERE idPlan = p_idPlan;
    p_result := SQL%ROWCOUNT;
    COMMIT;
END;
/

-- ================================
-- ELIMINAR USUARIO (GENÉRICO)
-- ================================
CREATE OR REPLACE PROCEDURE eliminarUsuario (
    p_cedula IN usuario.cedula%TYPE,
    p_result OUT NUMBER
) AS
BEGIN
    DELETE FROM usuario WHERE cedula = p_cedula;
    p_result := SQL%ROWCOUNT;
    COMMIT;
END;
/
-- ================================
-- ELIMINAR CICLO ()
-- ================================
CREATE OR REPLACE PROCEDURE eliminarCiclo(
    p_id IN ciclo.idCiclo%TYPE,
    p_result OUT NUMBER
) AS
BEGIN
    DELETE FROM ciclo WHERE idCiclo = p_id;
    p_result := SQL%ROWCOUNT;
    COMMIT;
END;
/


CREATE OR REPLACE PROCEDURE eliminarPlanEstudio (
    p_idPlanEstudio IN NUMBER,
    p_result OUT NUMBER
) AS
BEGIN
    DELETE FROM plan_estudio
    WHERE idPlanEstudio = p_idPlanEstudio;

    p_result := SQL%ROWCOUNT;
    COMMIT;
END;
/
CREATE OR REPLACE PROCEDURE eliminarProfesor(
    p_cedula IN usuario.cedula%TYPE,
    p_result OUT NUMBER
) AS
BEGIN
    -- Elimina de la tabla PROFESOR
    DELETE FROM profesor WHERE cedula = p_cedula;

    -- Elimina de la tabla USUARIO
    DELETE FROM usuario WHERE cedula = p_cedula;

    p_result := SQL%ROWCOUNT;  -- número de filas afectadas (de profesor)
    COMMIT;
END;

CREATE OR REPLACE PROCEDURE eliminarUsuario(
    p_cedula IN usuario.cedula%TYPE,
    p_result OUT NUMBER
) AS
BEGIN
    DELETE FROM usuario
     WHERE cedula = p_cedula;

    p_result := SQL%ROWCOUNT;
    COMMIT;
END;
/
