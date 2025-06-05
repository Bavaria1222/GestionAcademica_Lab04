# GestionAcademica_Lab04

Este repositorio contiene un ejemplo de aplicación de gestión académica
compuesto por un **backend** Java (carpeta `Laboratorio_Gym_Backend`) y una
aplicación Android (carpeta `Lab4_Moviles`).

## Cómo probar el sistema de historial y registro de notas

1. Importe el script `Laboratorio_Gym_Backend/Script.sql` en su base de datos
   Oracle.  Este script crea las tablas y registra datos de ejemplo para los
   alumnos `S001` y `S002`, así como varios grupos impartidos por los profesores
   `P001` y `P002`.  Ambos alumnos cuentan con matrículas que incluyen una nota
   inicial para que pueda probarse el registro y la modificación desde la
   aplicación móvil.
2. Desde la carpeta `Laboratorio_Gym_Backend/Gym_Backend` ejecute:

   ```bash
   ./gradlew run
   ```

   Esto iniciará el servidor en `http://localhost:8080`.
3. Compile la aplicación Android ubicada en `Lab4_Moviles` utilizando Android
   Studio.  Al ingresar como `S001` o `S002` podrá visualizar el historial de
   matrículas.  Si ingresa como profesor (`P001` o `P002`) se mostrará la lista
   de cursos que imparte para registrar notas.

### Credenciales de prueba

| Usuario | Clave  | Rol      |
|---------|-------|----------|
| S001    | passS1| ALUMNO   |
| S002    | passS2| ALUMNO   |
| P001    | passP1| PROFESOR |
| P002    | passP2| PROFESOR |

Los profesores pueden consultar sus grupos actuales mediante el endpoint
`/api/grupos/profesor/{cedula}/ciclo/{idCiclo}` y registrar notas utilizando el
endpoint `/api/matriculas/grupo/{idGrupo}` para obtener la lista de estudiantes.

Para registrar y modificar notas desde la app, diríjase al menú "Registro de notas".
Allí se muestran los grupos del profesor actual.
Al seleccionar un grupo se cargan automáticamente los estudiantes matriculados y su nota.
Tocando un estudiante se abre la actividad RegistrarNota donde puede editarse la calificación.
