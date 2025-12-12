# LeadsApp MVVM

Sistema de **Registro de Clientes Potenciales (Leads)** para Android, desarrollado en **Kotlin** con:

- Arquitectura **MVVM**
- **Room** para almacenamiento local
- **Retrofit** para envío de datos al servidor
- Captura de foto con **cámara** y asociación al lead
- Tabla de **logs** con eventos clave: creación, modificación, sincronización y errores
- UI con **RecyclerView**, **ConstraintLayout** y validaciones básicas

## Requisitos de Gradle

El proyecto está preparado para usarse con **Gradle 8.7** mediante el *wrapper* de Android Studio.
Si fuera necesario, después de importar puedes ejecutar:

```bash
./gradlew wrapper --gradle-version 8.7
```

(o dejar que Android Studio actualice automáticamente).

## Cómo importar el proyecto en Android Studio

1. Descarga y descomprime el ZIP.
2. Abre **Android Studio** → `Open` → selecciona la carpeta `LeadsApp_MVVM`.
3. Espera a que sincronice Gradle.
4. Conecta un dispositivo o emulador Android.
5. Ejecuta la app (`Run > Run app`).

## Flujo principal

- Pantalla de **lista de leads** (RecyclerView).
- Botón **+** para agregar un nuevo lead.
- Formulario con:
  - Nombre (obligatorio)
  - Teléfono (obligatorio)
  - Email (opcional)
  - Notas (opcional)
  - Foto (opcional) capturada con la cámara
- Al guardar:
  - Si es un lead nuevo → se inserta en Room y se registra un log `CREATED`.
  - Si es un lead existente → se actualiza y se registra un log `UPDATED`.
- Desde el formulario se puede **sincronizar** el lead con el servidor mediante Retrofit → log `SYNC` o `ERROR` según corresponda.
- Menú en la lista para ir a la pantalla de **logs**.

## MVVM

- **Model**: entidades `Lead` y `LogEntry`, DAOs y `LeadsDatabase` (Room), `LeadRepository`.
- **View**: `MainActivity`, `LeadListFragment`, `LeadFormFragment`, `LogsFragment` + layouts.
- **ViewModel**: `LeadViewModel` expone `LiveData` de leads, logs, lead seleccionado y mensajes.

## Room

- Entidades: `Lead`, `LogEntry`.
- DAOs: `LeadDao`, `LogDao`.
- Base de datos: `LeadsDatabase`.
- Acceso a la BD se hace exclusivamente desde `LeadRepository` y `LeadViewModel`.

## Retrofit

- Cliente centralizado en `RetrofitClient`.
- Interfaz `LeadsApi` con un endpoint `sendLead()`.
- DTOs `LeadDto` y `ApiResponse`.
- URL base por defecto: `https://webhook.site/5abd8131-eced-41c0-8244-ec85f1c2a3b2` (cámbiala por la URL de prueba que te dé tu docente).

## Git y GitHub

Pasos sugeridos para subir este proyecto a GitHub:

```bash
git init
git add .
git commit -m "LeadsApp MVVM inicial"
git branch -M main
git remote add origin https://github.com/TU_USUARIO/LeadsApp_MVVM.git
git push -u origin main
```

No olvides completar la descripción del proyecto en GitHub y adjuntar en el repositorio el documento de pruebas (`docs/pruebas.md`) y las capturas de pantalla.
