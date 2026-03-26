# Comunicación en Redes

## 🧩 Conceptos Base

### TCP, UDP, Puerto y HTTP

| Concepto | Definición simple | Nivel técnico breve | Ejemplo real |
|---|---|---|---|
| **TCP** | Protocolo de comunicación que garantiza que los datos lleguen completos y en orden, como un mensajero que espera confirmación de entrega. | Orientado a conexión; usa handshake (SYN/SYN-ACK/ACK), control de flujo y retransmisión de paquetes perdidos. Opera en capa de transporte (OSI 4). | Cargar una página web, enviar un correo, descargar un archivo. |
| **UDP** | Protocolo de comunicación que envía datos rápido sin esperar confirmación, como gritar un mensaje al otro lado de la sala. | Sin conexión (connectionless), sin retransmisión ni orden garantizado. Menor latencia, mayor velocidad. Opera en capa de transporte (OSI 4). | Videollamadas, streaming de video/audio, juegos online en tiempo real. |
| **Puerto** | Número que identifica a qué "puerta" dentro de un servidor va dirigido un mensaje, como el número de departamento en un edificio. | Entero de 16 bits (0–65535). Los puertos conocidos (well-known) van del 0 al 1023 y están reservados para servicios estándar. | Puerto 80 → HTTP, 443 → HTTPS, 22 → SSH, 3306 → MySQL. |
| **HTTP** | Protocolo que define cómo un navegador pide páginas web y cómo el servidor las entrega, como el lenguaje común entre cliente y vendedor. | Basado en texto, sin estado (stateless), modelo petición-respuesta (request/response). Corre sobre TCP. HTTPS es HTTP + cifrado TLS. | Escribir una URL en el navegador y recibir la página web como respuesta. |

> **TLS (Transport Layer Security)**: protocolo de cifrado que crea un canal seguro entre cliente y servidor antes de enviar cualquier dato. Garantiza que la información viajé encriptada — ilegible para cualquiera que la intercepte en el camino. HTTPS es simplemente HTTP con TLS encima.

## ⚙️ TCP vs UDP

### ¿Cuál es la principal diferencia?

La diferencia fundamental está en la **prioridad**: TCP prioriza que los datos lleguen correctamente; UDP prioriza que lleguen rápido.

TCP establece una conversación formal antes de enviar cualquier dato. Primero hace un "apretón de manos" (handshake) de tres pasos: el cliente dice *"¿me escuchas?"*, el servidor responde *"sí, ¿me escuchas tú?"*, y el cliente confirma *"sí"*. Solo entonces comienza la transferencia. Además, cada paquete enviado debe ser confirmado por el receptor — si no llega confirmación, el paquete se reenvía.

UDP simplemente dispara los datos hacia el destino y no mira atrás. No hay handshake, no hay confirmación, no hay reenvío si algo se pierde.

### ¿Cuál es más confiable?

**TCP**, por tres razones concretas:

- **Confirmación de entrega (ACK):** el receptor avisa por cada paquete recibido. Si el emisor no recibe ese aviso, reenvía el paquete.
- **Orden garantizado:** los paquetes se numeran en secuencia. Si llegan desordenados, TCP los reordena antes de entregarlos a la aplicación.
- **Control de errores:** detecta paquetes corruptos o perdidos y los solicita de nuevo automáticamente.

UDP no hace ninguna de estas tres cosas. Si un paquete se pierde en tránsito, simplemente desaparece — la aplicación nunca sabrá que faltó, a menos que ella misma lo gestione.

### ¿Cuál es más rápido?

**UDP**, por lo mismo que lo hace menos confiable:

- No hay handshake inicial → se empieza a enviar inmediatamente.
- No hay espera de confirmaciones → no hay pausas entre paquetes.
- No hay reordenamiento → menos procesamiento en el receptor.
- Los encabezados UDP ocupan 8 bytes vs los 20–60 bytes de TCP → menos peso por paquete.

En aplicaciones donde la velocidad importa más que la perfección — como una videollamada — es preferible perder un frame de video ocasionalmente que pausar la transmisión esperando que ese frame perdido sea reenviado. Ese reenvío llegaría demasiado tarde para ser útil.

### Tabla comparativa

| Protocolo | Característica clave | Uso típico |
|---|---|---|
| **TCP** | Entrega garantizada, ordenada y con confirmación. Más lento por el overhead de control. | Navegación web, correo electrónico, transferencia de archivos, APIs REST. |
| **UDP** | Envío rápido sin confirmación ni orden garantizado. Tolera pérdida de datos. | Streaming de video/audio, videollamadas, videojuegos online, DNS. |

## 🚪 Puertos — Clave en desarrollo

### ¿Qué es un puerto en una red?

Un puerto es un número que identifica un proceso o servicio específico dentro de un dispositivo. Si la dirección IP es como la dirección de un edificio, el puerto es el número del departamento. Dos computadoras pueden comunicarse a través de la misma IP, pero el puerto le dice exactamente a qué "inquilino" (proceso) va dirigido el mensaje.

Los puertos son números enteros del 0 al 65535 y se dividen en tres rangos:

| Rango | Nombre | Descripción |
|---|---|---|
| 0 – 1023 | Well-known ports | Reservados para servicios estándar del sistema (HTTP, HTTPS, SSH…) |
| 1024 – 49151 | Registered ports | Usados por aplicaciones conocidas (bases de datos, servidores de apps…) |
| 49152 – 65535 | Dynamic/private ports | Asignados temporalmente por el sistema operativo |

### ¿Por qué una aplicación necesita un puerto?

Porque en un mismo servidor pueden estar corriendo decenas de servicios al mismo tiempo — un servidor web, una base de datos, una API, un servidor de correo — todos compartiendo la misma IP. Sin puertos, el sistema operativo no sabría a qué proceso entregar cada mensaje que llega.

Cuando una aplicación quiere "escuchar" conexiones entrantes, le dice al sistema operativo: *"reservame el puerto 3000"*. A partir de ese momento, todo tráfico que llegue a esa IP en el puerto 3000 se redirige a esa aplicación. Si otro proceso intenta usar el mismo puerto, el sistema lanza un error — por eso en desarrollo a veces ves mensajes como `port already in use`.

### ¿Qué significan estos puertos?

**Puerto 80 — HTTP**
El puerto estándar para tráfico web sin cifrado. Cuando escribes `http://ejemplo.com` sin especificar puerto, el navegador asume automáticamente el 80. Hoy en día casi no se usa solo, sino como redirección hacia el 443.

**Puerto 443 — HTTPS**
El puerto estándar para tráfico web cifrado con TLS. Es el que usa prácticamente toda la web moderna. Cuando el navegador muestra el candado, la comunicación va por aquí.

**Puerto 3000 — Convención de desarrollo**
No es un estándar oficial, pero por convención es el puerto que usan por defecto muchos frameworks de JavaScript: React, Next.js, Express, entre otros. Es el "puerto del desarrollador front-end".

**Puerto 5432 — PostgreSQL**
Puerto oficial y por defecto del motor de base de datos PostgreSQL. Cuando tu app backend se conecta a Postgres, lo hace a este puerto (en localhost durante desarrollo, o a la IP del servidor en producción).

### ¿Qué pasa cuando ejecutas `npm run dev`?

```
npm run dev
```

Este comando típicamente hace lo siguiente en secuencia:

**1. Lee `package.json`** y encuentra el script `dev`, que usualmente apunta a algo como `next dev`, `vite`, o `node server.js`.

**2. El framework arranca un servidor local** — un proceso Node.js que empieza a escuchar conexiones.

**3. El proceso reserva un puerto** — por defecto el 3000 (o el 5173 en Vite, el 4200 en Angular, etc.). Le dice al sistema operativo: *"todo lo que llegue a localhost:3000 es mío"*.

**4. Levanta un servidor HTTP** que sirve tu aplicación. En la mayoría de los frameworks modernos esto incluye Hot Module Replacement (HMR): cuando guardas un archivo, el servidor detecta el cambio y actualiza el navegador automáticamente sin recargar la página completa.

**5. Ves en la terminal algo como:**
```
Local: http://localhost:3000
```
Eso significa: en *esta máquina* (`localhost` = 127.0.0.1), el proceso está escuchando en el puerto `3000`.

**6. Abres el navegador**, escribes `localhost:3000`, y el navegador hace una petición HTTP a tu propio computador en ese puerto — tu servidor de desarrollo la recibe y devuelve la app.

Si el puerto 3000 ya está ocupado por otro proceso, el framework intentará el 3001, luego 3002, y así hasta encontrar uno libre — o te pedirá que lo liberes manualmente.

## 🌐 HTTP - HyperText Transfer Protocol

### ¿Qué es HTTP?

HTTP es el protocolo que define cómo dos programas se comunican a través de la web: uno pide algo (cliente) y el otro responde (servidor). Es el idioma común entre un navegador y un servidor web.

Tiene tres características fundamentales que vale la pena entender desde el principio:

- **Basado en texto:** las peticiones y respuestas son mensajes de texto legibles por humanos, no código binario.
- **Sin estado (stateless):** cada petición es completamente independiente. El servidor no recuerda peticiones anteriores. Si mandas dos peticiones seguidas, para el servidor son de un desconocido cada vez — por eso existen las cookies y los tokens de autenticación, para simular memoria.
- **Modelo petición-respuesta:** siempre es el cliente quien inicia. El servidor nunca habla primero.

### ¿Cómo funciona una petición HTTP?

Cada petición HTTP tiene una estructura fija con tres partes:

**La petición (Request) contiene:**
- **Método:** qué quiere hacer el cliente (GET, POST, etc.)
- **URL:** a qué recurso apunta
- **Headers:** metadatos como qué tipo de contenido acepta, qué idioma prefiere, si está autenticado
- **Body** *(opcional)*: datos que envía el cliente, típicamente en POST y PUT

**La respuesta (Response) contiene:**
- **Status code:** un número que indica si todo salió bien o qué falló
- **Headers:** metadatos de la respuesta (tipo de contenido, caché, etc.)
- **Body:** los datos que devuelve el servidor (HTML, JSON, imagen, etc.)

Los códigos de estado más importantes son:

| Código | Significado |
|---|---|
| 200 | OK — todo bien |
| 201 | Created — recurso creado exitosamente |
| 400 | Bad Request — la petición tiene errores |
| 401 | Unauthorized — falta autenticación |
| 403 | Forbidden — autenticado pero sin permiso |
| 404 | Not Found — el recurso no existe |
| 500 | Internal Server Error — algo falló en el servidor |

### ¿Qué significan GET, POST, PUT y DELETE?

Estos se llaman **métodos HTTP** (o verbos) y expresan la *intención* de la petición. Son la base de lo que más adelante se conoce como arquitectura REST.

**GET — Pedir datos**
Solicita un recurso sin modificar nada. Es de solo lectura. No tiene body. Ejemplo: cargar la lista de usuarios, obtener el detalle de un producto.
```
GET /api/usuarios
GET /api/productos/42
```

**POST — Crear algo nuevo**
Envía datos al servidor para crear un nuevo recurso. Lleva body con la información del nuevo elemento. Ejemplo: registrar un usuario, publicar un comentario.
```
POST /api/usuarios
Body: { "nombre": "Ana", "email": "ana@mail.com" }
```

**PUT — Reemplazar/actualizar**
Envía datos para actualizar un recurso existente, reemplazándolo completamente. Si el recurso no existe, algunos servidores lo crean. Ejemplo: editar el perfil completo de un usuario.
```
PUT /api/usuarios/42
Body: { "nombre": "Ana López", "email": "ana@mail.com" }
```

**DELETE — Eliminar**
Solicita que el servidor elimine un recurso específico. Generalmente no lleva body. Ejemplo: borrar una publicación, dar de baja una cuenta.
```
DELETE /api/usuarios/42
```

### Flujo completo: Frontend → Request → Backend → Response → Frontend

Tomemos un ejemplo concreto: el usuario hace clic en "Ver mi perfil" en una app web.

```
1. FRONTEND genera la petición
   ┌───────────────────────────────────┐
   │  GET /api/usuarios/42             │
   │  Headers:                         │
   │    Authorization: Bearer <token>  │
   │    Accept: application/json       │
   └───────────────────────────────────┘
            │
            │ viaja por internet sobre TCP/IP
            ▼

2. BACKEND recibe y procesa
   ┌────────────────────────────────┐
   │  Router identifica la ruta     │
   │  Middleware verifica el token  │
   │  Controlador consulta la BDD   │
   │  Arma la respuesta             │
   └────────────────────────────────┘
            │
            │ responde por la misma conexión TCP
            ▼

3. BACKEND envía la respuesta
   ┌────────────────────────────────────┐
   │  Status: 200 OK                    │
   │  Headers:                          │
   │    Content-Type: application/json  │
   │  Body:                             │
   │    { "id": 42,                     │
   │      "nombre": "Ana López",        │
   │      "email": "ana@mail.com" }     │
   └────────────────────────────────────┘
            │
            ▼

4. FRONTEND recibe y renderiza
   Actualiza la interfaz con los datos
   El usuario ve su perfil en pantalla
```

Lo importante del flujo es que HTTP no es una conexión permanente. Una vez que el servidor entrega la respuesta, esa conversación termina. Si el frontend necesita más datos, debe iniciar una nueva petición desde cero — de ahí que se diga que HTTP es sin estado.

## 💻 Conexión directa con desarrollo

### ¿Qué pasa cuando ejecutas `fetch("http://localhost:3000/api")`?

Esta única línea de código activa una cadena completa de conceptos — todos los que hemos visto hasta ahora ocurren en secuencia, en cuestión de milisegundos.

### Disección de la URL

Antes de ver el flujo, conviene leer la URL como la lee el navegador:

```
http://localhost:3000/api
 │        │        │    │
 │        │        │    └── Ruta del recurso que se pide
 │        │        └─────── Puerto donde escucha el servidor
 │        └──────────────── Host (localhost = esta misma máquina)
 └───────────────────────── Protocolo a usar
```

`localhost` es un alias que siempre apunta a `127.0.0.1` — la dirección IP de tu propia computadora. Nunca sale a internet. La petición va de tu navegador a tu propio sistema operativo y vuelve.

### ¿Qué protocolo se usa?

**HTTP** — porque la URL empieza con `http://`.

El navegador sabe que debe hablar en HTTP: construirá un mensaje de texto con un método (por defecto GET), headers, y opcionalmente un body, siguiendo exactamente la estructura que define ese protocolo.

Si la URL fuera `https://`, usaría HTTP pero con una capa de cifrado TLS encima. En desarrollo local se usa `http://` sin cifrado porque la comunicación nunca sale de tu máquina — no hay nada que interceptar.

### ¿Qué puerto?

**El 3000** — está explícito en la URL.

Si no hubiera número de puerto, el navegador asumiría el 80 para `http://` y el 443 para `https://`. Al escribir `:3000` se lo indicamos explícitamente: *"habla con el proceso que está escuchando en el puerto 3000 de esta máquina"*.

Ese proceso es tu servidor de desarrollo — el que se levanta con `npm run dev`.

### ¿Qué tipo de comunicación?

Una **HTTP Request** — específicamente una API Request. El frontend envía una petición HTTP al backend usando el método GET (por defecto en `fetch`), y espera una HTTP Response con los datos. Es exactamente el modelo petición-respuesta que vimos en el punto anterior: el cliente pide, el servidor responde, la conversación termina.

### El flujo completo, paso a paso

```
Tu código JS
│
│  fetch("http://localhost:3000/api")
│
▼
1. RESOLUCIÓN
   "localhost" → 127.0.0.1 (sin salir a internet)
   Puerto destino: 3000
   Protocolo: HTTP con método GET
│
▼
2. CONEXIÓN TCP
   Tu navegador → pide conexión al puerto 3000
   Tu servidor  → acepta (SYN / SYN-ACK / ACK)
   Conexión establecida
│
▼
3. PETICIÓN HTTP (viaja por la conexión TCP)
   ┌──────────────────────────────────┐
   │ GET /api HTTP/1.1                │
   │ Host: localhost:3000             │
   │ Accept: application/json        │
   └──────────────────────────────────┘
│
▼
4. TU SERVIDOR RECIBE LA PETICIÓN
   El proceso en el puerto 3000 la lee,
   ejecuta la lógica (consulta BD, etc.)
   y prepara la respuesta
│
▼
5. RESPUESTA HTTP
   ┌──────────────────────────────────┐
   │ HTTP/1.1 200 OK                  │
   │ Content-Type: application/json  │
   │                                  │
   │ { "mensaje": "hola mundo" }      │
   └──────────────────────────────────┘
│
▼
6. fetch() RECIBE LA RESPUESTA
   La promesa se resuelve
   Puedes leer los datos con .json()
   y actualizar la interfaz
```

### ¿Qué pasa si algo falla?

Cada capa puede fallar de forma distinta, y entender cuál falló ahorra mucho tiempo en debugging:

| Error | Causa probable | Capa que falló |
|---|---|---|
| `net::ERR_CONNECTION_REFUSED` | Nada escucha en ese puerto | TCP — el servidor no está corriendo |
| `404 Not Found` | El servidor corre pero `/api` no existe | HTTP — ruta no definida |
| `500 Internal Server Error` | La ruta existe pero el código tiene un error | HTTP — lógica del servidor |
| `CORS error` | El servidor no permite peticiones de ese origen | HTTP — configuración de headers |

## 🚨 Problemas reales - Debugging

### "Port already in use"

El puerto que tu aplicación quiere reservar ya está siendo usado por otro proceso. Recuerda que dos procesos no pueden escuchar en el mismo puerto al mismo tiempo — el sistema operativo no lo permite.

**Causas típicas:**
- Dejaste corriendo una sesión anterior de `npm run dev` en otra terminal.
- Otra aplicación usa ese puerto por defecto (otro servidor, una base de datos, etc.).
- El proceso anterior no cerró limpiamente y sigue "ocupando" el puerto aunque ya no responda.

**Qué revisarías:**
- Buscar y cerrar otras terminales con el servidor corriendo.
- Identificar qué proceso usa ese puerto y matarlo:
```bash
# Mac/Linux
lsof -i :3000

# Windows
netstat -ano | findstr :3000
```
- O simplemente cambiar el puerto de tu app en la configuración.

### "Connection refused"

Tu petición llegó a la máquina correcta, pero nada estaba escuchando en ese puerto. El sistema operativo recibió la solicitud de conexión TCP y la rechazó activamente porque no hay ningún proceso registrado ahí.

La diferencia con un timeout es importante: *refused* es una respuesta inmediata — el servidor dice "no" al instante. No hay silencio, hay un rechazo explícito.

**Causas típicas:**
- El servidor simplemente no está corriendo — olvidaste hacer `npm run dev`.
- El servidor está corriendo en un puerto diferente al que estás llamando.
- El servidor arrancó con errores y se cayó antes de empezar a escuchar.

**Qué revisarías:**
- Confirmar que el servidor está corriendo y en qué puerto.
- Comparar el puerto del servidor con el puerto en tu `fetch` o configuración.
- Revisar la terminal del servidor en busca de errores de arranque.

### "Timeout"

La petición salió, llegó al destino, pero nunca llegó una respuesta dentro del tiempo límite. A diferencia del *connection refused*, aquí no hay un rechazo inmediato — simplemente el tiempo se acaba esperando.

**Causas típicas:**
- El servidor recibió la petición pero se quedó procesando indefinidamente (un loop infinito, una consulta a base de datos que nunca termina).
- Un firewall o proxy está bloqueando silenciosamente la conexión sin avisar.
- La red entre cliente y servidor tiene problemas.
- El servidor está tan sobrecargado que no alcanza a responder a tiempo.

**Qué revisarías:**
- Los logs del servidor — ¿recibió la petición? ¿dónde se detuvo?
- Si hay una consulta a base de datos involucrada, probarla directamente.
- Si es un servidor externo, verificar que la URL y credenciales sean correctas.

### Tabla resumen — diagnóstico rápido

| Error | Qué significa | Primera cosa a revisar |
|---|---|---|
| Port already in use | Puerto ocupado por otro proceso | Cerrar otras terminales o cambiar de puerto |
| Connection refused | Nada escucha en ese puerto | ¿Está corriendo el servidor? |
| Timeout | El servidor no respondió a tiempo | Logs del servidor — ¿dónde se colgó? |

## 🌍 Caso práctico real

Escenario:

> “Tu frontend no se conecta al backend”
> 

Este es el error más común en desarrollo y casi siempre tiene una causa simple. La clave es descartar causas en orden, de la más obvia a la más compleja.

### ¿Puede ser problema de puerto?

**Sí, y es la causa más frecuente.**

El frontend está llamando a un puerto y el backend está escuchando en otro. Puede pasar de varias formas:

- El backend corre en el 3001 pero el fetch apunta al 3000.
- Alguien cambió el puerto en la configuración del servidor y no actualizó la URL del frontend.
- El backend no está corriendo — entonces ningún puerto responde.

```javascript
// Frontend llama aquí:
fetch("http://localhost:3000/api/usuarios")

// Backend está escuchando aquí:
app.listen(3001) // ← puerto diferente → Connection refused
```

**Qué revisarías:** comparar el puerto en cada `fetch` o archivo de configuración del frontend contra el puerto en el `app.listen()` o `.env` del backend.

### ¿Puede ser problema de protocolo?

**Sí, de dos maneras distintas.**

**1. HTTP vs HTTPS:** si el frontend hace una petición `https://` pero el servidor local solo habla `http://`, la conexión falla. En desarrollo esto pasa cuando se configura un certificado SSL local mal o innecesariamente.

**2. CORS — el problema de protocolo más común en desarrollo:**
CORS (Cross-Origin Resource Sharing) es una política de seguridad del navegador. Cuando el frontend y el backend corren en orígenes distintos — diferente puerto, diferente dominio, o diferente protocolo — el navegador bloquea la respuesta a menos que el servidor la autorice explícitamente.

```
Frontend: http://localhost:3000   ← origen A
Backend:  http://localhost:3001   ← origen B (puerto distinto = origen distinto)

Resultado: el navegador bloquea la respuesta aunque el backend respondió bien
Error en consola: "CORS policy: No 'Access-Control-Allow-Origin' header"
```

Lo importante de entender: **el backend recibió la petición y respondió** — es el navegador quien bloquea que el frontend lea esa respuesta. El problema no es de red ni de puerto, es de permisos declarados en los headers HTTP.

**Qué revisarías:** que el backend tenga configurado el middleware de CORS permitiendo el origen del frontend.

### ¿Puede ser problema de red?

**Sí, aunque en desarrollo local es la causa menos probable.**

Si frontend y backend están en la misma máquina usando `localhost`, los problemas de red son raros. Pero aparecen en estos escenarios:

- **Backend en otro equipo o servidor:** si el backend corre en una IP distinta a `localhost`, puede haber un firewall bloqueando el puerto, la IP puede ser incorrecta, o el servidor puede no estar accesible desde tu red.
- **Variables de entorno mal configuradas:** el frontend tiene una variable `REACT_APP_API_URL` o `VITE_API_URL` apuntando a una URL incorrecta o de producción en lugar de localhost.
- **Docker o máquinas virtuales:** cuando el backend corre en un contenedor, `localhost` dentro del contenedor no es el mismo `localhost` de tu máquina — hay que usar la IP del contenedor o configurar la red correctamente.

### Checklist de diagnóstico — en este orden

```
1. ¿Está corriendo el backend?
   → Revisar la terminal. ¿Hay errores? ¿En qué puerto dice que escucha?

2. ¿Coinciden los puertos?
   → Comparar el puerto del fetch con el puerto del app.listen()

3. ¿Hay error de CORS en la consola del navegador?
   → Agregar middleware de CORS en el backend

4. ¿La URL base está bien configurada?
   → Revisar variables de entorno (.env)

5. ¿El backend está en otra máquina o contenedor?
   → Verificar IP, firewall y configuración de red
```

## 🧪 Analogías

| Concepto | Analogía |
|---|---|
| **TCP** | Pedir comida con seguimiento en tiempo real: el restaurante confirma que recibió el pedido, avisa cuando sale a despacho, y tú confirmas cuando lo recibes. Si algo falla en el camino, se reintenta hasta que llegue completo. |
| **UDP** | El canillita que lanza el diario por la ventana del auto cada mañana. Sale disparado hacia tu casa sin esperar confirmación. La mayoría llegan, algunos caen en el jardín del vecino — no importa, ya viene el de mañana. |
| **Puerto** | El número de local dentro de un mall. La dirección del mall te lleva al edificio (IP), pero el número de local te dice exactamente a qué negocio entrar: local 80 es la tienda de ropa, local 443 es la joyería, local 3000 es el nuevo ciber-café. |
| **HTTP** | El protocolo de atención del restaurante: el cliente siempre habla primero, hace un pedido usando palabras específicas ("quiero", "cancela", "modifica"), el mesero responde con un código ("listo", "no existe ese plato", "error en cocina") y entrega lo solicitado. Nadie improvisa — hay un guión definido que ambos conocen. |
