# Documento de Pruebas - LeadsApp MVVM

## 1. Objetivo general del sistema

Desarrollar un **sistema de Registro de Clientes Potenciales (Leads)** que permita:

- CRUD completo de leads (crear, listar, editar, eliminar).
- Asociar una **foto** al lead (cliente/local) tomando la imagen con la cámara.
- Guardar la información localmente en **Room**.
- **Sincronizar** los leads con un servidor vía **Retrofit**.
- Registrar en una tabla de **logs** los principales eventos del sistema.

---

## 2. Entorno de pruebas

- Dispositivo / Emulador: _[Completar por el alumno]_
- Versión de Android: _[Completar]_  
- Android Studio: _[Completar]_  
- Conexión a Internet: Wi-Fi / Datos

---

## 3. Casos de prueba

### 3.1. Crear lead (CRUD - Create)

**Pasos:**

1. Abrir la aplicación.
2. En la pantalla de lista de leads, tocar el botón `+`.
3. Completar **Nombre** y **Teléfono** (campos obligatorios).
4. (Opcional) Completar Email y Notas.
5. Tocar en **Tomar foto** y capturar una imagen.
6. Presionar **Guardar**.

**Resultado esperado:**

- El formulario se cierra.
- El nuevo lead aparece en la lista.
- Se registra un log con tipo `CREATED`.

---

### 3.2. Editar lead (CRUD - Update)

**Pasos:**

1. En la lista de leads, tocar un lead existente.
2. Modificar algún dato (por ejemplo, el teléfono).
3. Presionar **Guardar**.

**Resultado esperado:**

- Los datos del lead se actualizan en la lista.
- Se registra un log con tipo `UPDATED`.

---

### 3.3. Listar leads (CRUD - Read)

**Pasos:**

1. Abrir la aplicación.

**Resultado esperado:**

- Se muestra la lista de leads almacenados en Room en orden descendente por fecha de creación.

---

### 3.4. Eliminar lead (CRUD - Delete) *(opcional)*

*(Si implementas eliminación, documenta aquí los pasos y resultados esperados.)*

---

### 3.5. Captura de imagen con cámara

**Pasos:**

1. En el formulario de lead, tocar **Tomar foto**.
2. Aceptar permiso de cámara si el sistema lo solicita.
3. Tomar la foto y confirmar.

**Resultado esperado:**

- La imagen capturada se muestra en el formulario.
- La ruta de la foto queda asociada al lead.

---

### 3.6. Sincronización con el servidor (Retrofit)

**Pasos:**

1. Editar el archivo `RetrofitClient.kt` y configurar la URL base dada por el docente.
2. Crear o editar un lead y guardar.
3. Dentro del formulario del lead, presionar el botón **Sincronizar**.

**Resultado esperado:**

- Si la URL está correcta y el servidor responde OK:
  - Aparece un mensaje de **"Lead sincronizado con el servidor"**.
  - En la tabla de logs se registra un log tipo `SYNC`.
- Si hay error de conexión o respuesta no exitosa:
  - Aparece un mensaje de error.
  - Se registra un log tipo `ERROR` con el detalle.

---

### 3.7. Visualización de logs

**Pasos:**

1. Desde la pantalla de lista de leads, abrir el menú (tres puntos).
2. Seleccionar la opción **Logs**.

**Resultado esperado:**

- Se muestra la lista de logs con:
  - Tipo (`CREATED`, `UPDATED`, `SYNC`, `ERROR`, etc.).
  - Mensaje descriptivo.
  - Fecha y hora.

---

## 4. Capturas de pantalla

Incluye aquí (o en la carpeta `docs/screenshots`) las imágenes de:

- Lista de leads.
- Formulario de creación/edición.
- Pantalla de logs.
- Ejemplo de sincronización exitosa.
- Ejemplo de error de sincronización (si aplica).

---

## 5. Conclusiones

- El sistema cumple con los requisitos de:
  - Arquitectura MVVM.
  - Uso de Room.
  - Envío de datos con Retrofit.
  - Captura de imagen con cámara.
  - Registro de logs de eventos clave.
  - CRUD completo de leads.
- Observaciones adicionales y posibles mejoras: _[Completar por el alumno]_.
