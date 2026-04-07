# Arquitectura de Red

## 🧩 Conceptos Base

### Modelo OSI, Subred (Subnet) y DNS

| Concepto | Definición simple | Nivel técnico breve | Ejemplo real |
|---|---|---|---|
| **Modelo OSI** | Es un mapa de 7 capas que explica cómo viaja la información entre dos computadoras en una red, dividiendo el proceso en pasos ordenados | Modelo de referencia (Open Systems Interconnection) que estandariza las funciones de comunicación en 7 capas: Física, Enlace de datos, Red, Transporte, Sesión, Presentación y Aplicación | Cuando abres Gmail, tu navegador opera en la capa 7 (Aplicación), TCP gestiona la entrega en la capa 4 (Transporte) y tu tarjeta de red trabaja en las capas 1-2 |
| **Subred (Subnet)** | Es dividir una red grande en redes más pequeñas, como separar un edificio en departamentos con su propio número de puerta | Segmento lógico de una red IP creado mediante una máscara de subred (subnet mask). Permite organizar dispositivos, controlar el tráfico y mejorar la seguridad. Notación CIDR: `192.168.1.0/24` | Una empresa separa su red en subredes: una para el área de RRHH (`192.168.1.0/24`) y otra para TI (`192.168.2.0/24`), impidiendo que se vean entre sí |
| **DNS** | Es la "agenda telefónica" de internet: traduce nombres legibles como `google.com` a números IP que las máquinas entienden | Sistema distribuido de resolución de nombres (Domain Name System). Funciona con una jerarquía de servidores: root → TLD (`.com`, `.cl`) → autoritativo → resolver local. Usa principalmente UDP puerto 53 | Al escribir `github.com` en tu navegador, el DNS resuelve esa dirección a algo como `140.82.113.4` para que tu equipo sepa a dónde conectarse |

## 🧱 Modelo OSI

### ¿Qué es?

El modelo OSI (*Open Systems Interconnection*) es un estándar conceptual creado en 1984 por la ISO que describe **cómo debe comunicarse información entre dos dispositivos en una red**. Divide ese proceso en **7 capas**, cada una con una responsabilidad específica y bien definida.

Piénsalo como una línea de ensamblaje: cada estación hace su parte del trabajo y le pasa el producto a la siguiente.

### ¿Para qué sirve?

Hoy ningún sistema real implementa OSI de forma estricta (el mundo usa TCP/IP), pero el modelo sigue siendo útil porque:

- **Estandariza el lenguaje** — permite que ingenieros de todo el mundo hablen de redes usando los mismos términos
- **Facilita el diagnóstico** — cuando algo falla, ayuda a identificar *en qué capa* está el problema ("¿es físico? ¿es de red? ¿es la aplicación?")
- **Separa responsabilidades** — cada capa es independiente, lo que permite que fabricantes y desarrolladores trabajen sin depender unos de otros

---

### Las 7 Capas

| Capa | Nombre | ¿Qué hace? (simple) |
|---|---|---|
| 1 | Física | Transmite bits crudos (0s y 1s) a través del medio físico: cables, señales eléctricas, luz o radio |
| 2 | Enlace de datos | Empaqueta los bits en *tramas* y controla la comunicación entre dispositivos en la **misma red local**. Aquí operan las direcciones MAC |
| 3 | Red | Determina la **ruta** que deben seguir los datos entre redes distintas. Aquí operan las direcciones IP y los routers |
| 4 | Transporte | Garantiza que los datos lleguen **completos y en orden**. Divide la información en segmentos. Aquí operan TCP y UDP |
| 5 | Sesión | Abre, mantiene y cierra la **conversación** entre dos aplicaciones. Controla quién habla y cuándo |
| 6 | Presentación | Traduce, **cifra o comprime** los datos para que la aplicación los entienda. Aquí opera SSL/TLS y formatos como JPEG o JSON |
| 7 | Aplicación | Es lo que el usuario **ve y usa directamente**. Aquí operan protocolos como HTTP, FTP, SMTP y DNS |

## 🌐 DNS — El Sistema de Nombres de Dominio

### ¿Qué es DNS?

DNS (*Domain Name System*) es el sistema que **traduce nombres de dominio legibles por humanos en direcciones IP** que las máquinas pueden usar para encontrarse en la red.

Es exactamente como una agenda de contactos: en vez de memorizar el número de teléfono de cada persona, guardas su nombre y el teléfono se resuelve solo al llamar.

### ¿Por qué no usamos IP directamente?

Técnicamente, podrías escribir `142.250.80.46` en tu navegador y llegarías a Google. El problema es que:

- Las IPs son **difíciles de memorizar** y no dicen nada sobre el sitio
- Las IPs **cambian** — una empresa puede mover sus servidores y si usaras la IP fija, dejarías de llegar
- Un mismo dominio puede apuntar a **decenas de servidores** distintos según tu ubicación geográfica (balanceo de carga), algo imposible de manejar manualmente
- Los nombres transmiten **identidad y confianza** — `banco.cl` comunica algo, `187.43.12.9` no

### ¿Qué pasa cuando escribes `google.com` en el navegador?

El proceso ocurre en milisegundos y pasa por varias etapas:

**Paso 1 — Caché local**
Tu computador revisa si ya resolvió `google.com` antes. Si tiene la respuesta guardada en memoria, la usa directamente y el proceso termina aquí.

**Paso 2 — Resolver del ISP (DNS Resolver)**
Si no hay caché, tu equipo consulta al servidor DNS configurado en tu red (generalmente el de tu proveedor de internet, o uno como `8.8.8.8` de Google). Este servidor es el que hace el trabajo pesado.

**Paso 3 — Servidor Raíz (Root Server)**
El resolver pregunta a uno de los 13 grupos de servidores raíz del mundo: *"¿quién sabe sobre `.com`?"*. El servidor raíz responde con la dirección del servidor TLD correspondiente.

**Paso 4 — Servidor TLD**
El servidor TLD (Top-Level Domain) para `.com` responde: *"para `google.com` específicamente, pregunta a este servidor autoritativo"*.

**Paso 5 — Servidor Autoritativo**
Este servidor tiene la respuesta final y definitiva: la dirección IP real de `google.com`.

**Paso 6 — Respuesta al navegador**
El resolver devuelve la IP a tu equipo, la guarda en caché para consultas futuras, y tu navegador se conecta al servidor de Google.

### Flujo visual

```
[Tú] → escribes google.com
   ↓
[Caché local] → ¿ya lo sé? → SÍ → usa la IP guardada ✓
   ↓ NO
[DNS Resolver / ISP]
   ↓
[Servidor Raíz] → "pregunta al TLD de .com"
   ↓
[Servidor TLD .com] → "pregunta al autoritativo de google"
   ↓
[Servidor Autoritativo] → "google.com = 142.250.80.46"
   ↓
[Tu navegador] → se conecta a 142.250.80.46 → 🌐 Google carga
```

## 🧩 Subnets — Subredes

### ¿Qué es una subred?

Una subred es una **división lógica de una red IP más grande**. En vez de tener todos los dispositivos en una misma red plana, puedes segmentarla en grupos más pequeños y controlados.

La analogía más clara: imagina un edificio de oficinas. El edificio completo es tu red. Cada piso es una subred — tienen su propia numeración de salas, sus propias reglas de acceso, y no puedes entrar a otro piso sin pasar por las escaleras (el router).

---

### ¿Para qué sirve?

- **Organización** — agrupa dispositivos por función, equipo o propósito
- **Seguridad** — limita quién puede hablar con quién; un problema en una subred no se propaga a las demás
- **Rendimiento** — reduce el tráfico innecesario al contener el *broadcast* (mensajes que se envían a todos) dentro de cada segmento
- **Control administrativo** — permite aplicar reglas distintas a cada grupo de dispositivos

---

### ¿Por qué dividir redes?

Sin subredes, todos los dispositivos de una organización estarían en el mismo espacio de red. Esto significa:

- Un computador comprometido puede **ver el tráfico de todos**
- Los mensajes de broadcast **saturan toda la red**
- Es imposible aplicar **políticas de acceso diferenciadas**
- Escalar se vuelve **caótico y difícil de mantener**

Dividir es la diferencia entre una ciudad sin calles nombradas y una con barrios, avenidas y códigos postales.

---

### Ejemplo developer: Red local vs red en cloud

### 🏠 Red local (tu casa u oficina)

```
Red: 192.168.0.0/24
├── Subred dispositivos personales → 192.168.0.1 - 192.168.0.50
├── Subred impresoras/IoT         → 192.168.0.51 - 192.168.0.100
└── Subred servidores locales     → 192.168.0.101 - 192.168.0.150
```

Cada grupo está separado. Si tu smartTV tiene un problema de seguridad, no tiene acceso directo a tu servidor de archivos.

---

### ☁ Red en cloud (AWS / Google Cloud / Azure)

En cloud, las subredes son un concepto fundamental de arquitectura. Un proyecto típico se ve así:

```
VPC (Virtual Private Cloud): 10.0.0.0/16
├── Subred pública  → 10.0.1.0/24   (servidores web, accesibles desde internet)
├── Subred privada  → 10.0.2.0/24   (bases de datos, solo accesibles internamente)
└── Subred admin    → 10.0.3.0/24   (herramientas internas, acceso restringido)
```

La base de datos **nunca está expuesta a internet directamente** — solo la subred pública puede hablar con ella, y solo por los puertos que tú definas. Esto es seguridad por diseño.

> 💡 En el mundo real, cuando configuras un proyecto en AWS o Railway, estás implícitamente operando dentro de esta lógica aunque no lo veas directamente.

## 💻 Conexión directa con desarrollo

### Cuando tu app falla al conectarse

Estos errores son de los más frecuentes en el día a día de un desarrollador. Entender qué capa está fallando ahorra horas de debugging.

### ❌ La app no encuentra el servidor

**Qué ves:**
```
Error: connect ECONNREFUSED 127.0.0.1:3000
Connection refused
502 Bad Gateway
```

**Qué significa:**
La IP se resolvió correctamente, el equipo llegó hasta el servidor — pero **nadie está escuchando en ese puerto**. El servidor simplemente no está corriendo, cayó, o está escuchando en un puerto distinto.

**Relación con red/IP:**
El problema está en la capa de Transporte (OSI capa 4). La dirección IP era correcta, pero el proceso destino no existe o no responde.

**Causas comunes:**
- El servidor de desarrollo no está iniciado
- El proceso murió silenciosamente
- Hay un mismatch de puertos (`3000` vs `3001`)
- En cloud: el servicio no desplegó correctamente

### ❌ La app no resuelve el dominio

**Qué ves:**
```
Error: getaddrinfo ENOTFOUND mi-base-de-datos.internal
Could not resolve host: api.miservicio.com
DNS_PROBE_FINISHED_NXDOMAIN
```

**Qué significa:**
Tu app intentó convertir un nombre de dominio en una IP y **el DNS no supo responder**. El dominio no existe, está mal escrito, o tu entorno no tiene acceso al servidor DNS que lo conoce.

**Relación con DNS:**
Fallo puro de resolución DNS. El viaje ni siquiera comenzó — tu app no sabe a qué IP conectarse.

**Causas comunes:**
- Typo en la variable de entorno (ejemplo: `DATABSE_HOST` en vez de `DATABASE_HOST`)
- El dominio interno del cloud solo existe dentro de la VPC y estás corriendo la app fuera
- DNS del sistema mal configurado
- El servicio al que apunta fue eliminado o renombrado

### ❌ La app no puede conectarse

**Qué ves:**
```
Error: connect ETIMEDOUT
Request timeout after 5000ms
Network unreachable
```

**Qué significa:**
Tu app sabe a dónde ir (resolvió el DNS, tiene la IP) pero **los paquetes no llegan**. Algo en el camino los está bloqueando o descartando silenciosamente.

**Relación con red/subredes:**
El problema está en la capa de Red (OSI capa 3) o en las reglas de firewall. Clásico en cloud cuando las subredes o los *security groups* no están bien configurados.

**Causas comunes:**
- Firewall bloqueando el puerto
- En AWS/GCP: el Security Group no permite tráfico entrante en ese puerto
- La app está en una subred privada intentando salir a internet sin NAT Gateway
- VPN no activa cuando se requiere

### Mapa de diagnóstico rápido

```
App falla al conectar
        ↓
¿Resuelve el dominio?
   ↓ NO → Problema de DNS
          → revisar nombre, variables de entorno, acceso al DNS
   ↓ SÍ
¿Llega al servidor?
   ↓ NO → Problema de red / firewall / subred
          → revisar security groups, rutas, VPN
   ↓ SÍ
¿Responde el proceso?
   ↓ NO → Problema de aplicación / puerto
          → revisar que el servidor esté corriendo y en el puerto correcto
   ↓ SÍ → El problema es otro (auth, payload, lógica)
```

---

### Comandos de diagnóstico básicos

| Problema sospechado | Comando | Qué confirma |
|---|---|---|
| DNS no resuelve | `nslookup google.com` | Si el DNS responde y qué IP devuelve |
| DNS alternativo | `dig google.com` | Resolución detallada con tiempos |
| Puerto accesible | `telnet host 3000` | Si hay algo escuchando en ese puerto |
| Conectividad básica | `ping host` | Si los paquetes llegan al destino |
| Ruta de red | `traceroute host` | En qué punto de la ruta se corta la conexión |

## 🚨 Problemas Reales — Debugging de Red

### "DNS not found"

**Qué está pasando:**
Tu aplicación o navegador intentó resolver un nombre de dominio y **ningún servidor DNS pudo dar una respuesta**. El nombre no existe, no es visible desde tu red, o hay un problema con tu configuración DNS.

**En la práctica significa:**
El viaje ni siquiera comenzó. Tu app no tiene IP a la que conectarse.

**Qué revisarías primero:**
1. ¿Escribiste bien el dominio o la variable de entorno?
2. ¿El dominio existe? → `nslookup dominio.com`
3. ¿Estás intentando resolver un dominio interno de cloud desde fuera de la VPC?
4. ¿Tu máquina tiene DNS configurado? → revisar `/etc/resolv.conf` (Linux/Mac)

### "Host unreachable"

**Qué está pasando:**
El DNS funcionó — tu app tiene la IP correcta — pero **los paquetes no llegan al destino**. Hay una barrera en el camino: un firewall, una regla de red, una subred mal configurada, o simplemente el host está apagado.

**En la práctica significa:**
Sabes a dónde quieres ir pero el camino está cortado.

**Qué revisarías primero:**
1. ¿El servidor está encendido y activo?
2. ¿Hay un firewall bloqueando el tráfico? → revisar reglas de `iptables` o Security Groups en cloud
3. ¿Estás en la subred correcta? ¿La app puede alcanzar esa red?
4. ¿Necesitas VPN activa para llegar a ese host?
5. → `ping host` y `traceroute host` para ver dónde se corta

### "Network error"

**Qué está pasando:**
Es el mensaje más genérico de los tres. Puede significar cualquier cosa: DNS falló, la conexión fue rechazada, el timeout expiró, o la red simplemente no está disponible. Es el equivalente a "algo salió mal".

**En la práctica significa:**
Necesitas más información — este error solo te dice que hubo un fallo de comunicación, no dónde ni por qué.

**Qué revisarías primero:**
1. ¿Hay conexión a internet en general? → `ping 8.8.8.8`
2. ¿El error trae más detalle en los logs? → buscar `ECONNREFUSED`, `ETIMEDOUT`, `ENOTFOUND`
3. ¿Funciona desde otro entorno? (navegador, Postman, otro equipo)
4. ¿Es intermitente o constante? → intermitente sugiere inestabilidad de red o timeout

### Flujo de debugging recomendado

```
Recibo un error de red
        ↓
Paso 1 — ¿Tengo internet?
   ping 8.8.8.8
   ↓ NO → problema local de red
   ↓ SÍ
Paso 2 — ¿Resuelve el DNS?
   nslookup mi-dominio.com
   ↓ NO → problema de DNS (nombre, config, VPC)
   ↓ SÍ
Paso 3 — ¿Llegan los paquetes?
   ping mi-dominio.com
   ↓ NO → problema de red / firewall / subred
   ↓ SÍ
Paso 4 — ¿Responde el puerto?
   telnet mi-dominio.com 443
   ↓ NO → servidor caído o puerto bloqueado
   ↓ SÍ → el problema está en la app (auth, headers, payload)
```

---

### Tabla resumen

| Error | Capa OSI | Causa más común | Primer comando |
|---|---|---|---|
| DNS not found | Aplicación (7) | Nombre mal escrito, dominio no existe, fuera de VPC | `nslookup dominio.com` |
| Host unreachable | Red (3) | Firewall, security group, subred incorrecta, host apagado | `ping host` / `traceroute host` |
| Network error | Cualquiera | Error genérico, revisar logs para detallar | `ping 8.8.8.8` → luego escalar |

## 🌍 Caso práctico real

Escenario:

> “Tu aplicación `miapp.com` está deployada, pero nadie puede acceder”
>

### ¿Es problema de DNS?

**Señales que apuntan a DNS:**
- El dominio muestra `DNS_PROBE_FINISHED_NXDOMAIN`
- `nslookup miapp.com` no devuelve ninguna IP
- El error dice `ENOTFOUND` en los logs
- Funciona si accedes directamente por IP pero no por dominio

**Qué revisar:**
1. ¿El dominio está registrado y vigente? → puede haber expirado
2. ¿El registro DNS apunta a la IP correcta? → revisar en el panel de tu registrador (GoDaddy, Namecheap, Cloudflare)
3. ¿Propagó el cambio DNS? → los cambios DNS pueden tardar hasta 48 horas en propagarse globalmente
4. ¿Hay un registro `A` o `CNAME` configurado correctamente?

```bash
# Verificar resolución DNS
nslookup miapp.com
dig miapp.com

# Verificar desde un DNS externo (descarta caché local)
nslookup miapp.com 8.8.8.8
```

### ¿Es problema de red?

**Señales que apuntan a red:**
- `nslookup` resuelve la IP correctamente
- `ping miapp.com` no responde o da timeout
- `traceroute` se corta antes de llegar al servidor
- Funciona desde tu máquina pero no desde otras redes

**Qué revisar:**
1. ¿El servidor está encendido? → revisar en el panel del cloud provider
2. ¿Los Security Groups / Firewall permiten tráfico en los puertos 80 y 443?
3. ¿La subred donde está el servidor es pública? ¿Tiene Internet Gateway asignado?
4. ¿Hay un Load Balancer en el medio que no está configurado?

```bash
# Verificar conectividad básica
ping miapp.com

# Ver dónde se corta el camino
traceroute miapp.com

# Verificar si el puerto responde
telnet miapp.com 443
```

### ¿Es problema de configuración?

**Señales que apuntan a configuración:**
- El DNS resuelve, la red responde, pero el navegador muestra `502`, `503` o página en blanco
- `telnet miapp.com 443` conecta pero la respuesta es vacía o errónea
- Los logs del servidor muestran que el proceso crasheó o no arrancó
- Funciona en local pero no en producción

**Qué revisar:**
1. ¿El proceso de la app está corriendo? → `ps aux | grep node` o revisar en el panel
2. ¿Las variables de entorno están configuradas en producción? → falta de `DATABASE_URL`, `PORT`, etc.
3. ¿El servidor escucha en `0.0.0.0` y no solo en `localhost`? → error muy común
4. ¿El puerto expuesto coincide con el que espera el proxy o load balancer?

```bash
# Ver si el proceso está vivo
ps aux | grep node

# Ver en qué puerto está escuchando
netstat -tulpn | grep LISTEN

# Ver logs recientes
journalctl -u miapp --since "10 min ago"
```

> ⚠️ El error más clásico en deploy: la app escucha en `127.0.0.1:3000` (solo localhost) en vez de `0.0.0.0:3000` (accesible desde afuera). Desde dentro del servidor funciona, desde fuera no llega nunca.

### Flujo completo de diagnóstico

```
"Nadie puede acceder a miapp.com"
            ↓
¿nslookup resuelve la IP?
   ↓ NO  → Problema de DNS
           → revisar registros DNS, expiración, propagación
   ↓ SÍ
¿ping / traceroute llega al servidor?
   ↓ NO  → Problema de red
           → revisar firewall, security groups, subred pública, gateway
   ↓ SÍ
¿telnet al puerto 80/443 responde?
   ↓ NO  → Problema de configuración de red en el servidor
           → revisar que el proceso escuche en 0.0.0.0
   ↓ SÍ
¿La app responde correctamente?
   ↓ NO  → Problema de aplicación
           → revisar variables de entorno, logs, proceso caído
   ↓ SÍ  → Problema intermitente o de caché del cliente
```

### Tabla de síntomas y diagnóstico rápido

| Síntoma | Probable causa | Acción inmediata |
|---|---|---|
| `DNS_PROBE_FINISHED_NXDOMAIN` | DNS no resuelve | `nslookup` + revisar registros DNS |
| `ERR_CONNECTION_TIMED_OUT` | Firewall o red bloqueando | Revisar security groups y puertos |
| `502 Bad Gateway` | App caída o puerto incorrecto | Revisar proceso y configuración del proxy |
| `503 Service Unavailable` | App sobrecargada o no iniciada | Revisar logs y estado del proceso |
| Carga en local, no en prod | Variable de entorno faltante o `localhost` hardcodeado | Revisar `.env` de producción y binding de red |

## 🧪 Analogías — Arquitectura de Red

### OSI
* El proceso de publicar un libro: el autor escribe el contenido (capa 7 — Aplicación).
* Un editor lo revisa y formatea (capas 6 y 5).
* La imprenta lo divide en pliegos y los empaqueta (capa 4).
* El sistema de distribución decide qué camión va a qué ciudad (capa 3).
* El camión específico que lleva el paquete de una bodega a otra (capa 2).
* La ruta física de asfalto que conecta los puntos (capa 1).
* Cada etapa hace su trabajo sin saber cómo funcionan las demás.

### DNS
* El sistema de búsqueda de un hospital: tú llegas y preguntas por "el Dr. Ramírez de cardiología".
* No sabes en qué box está ni su número de interno.
* La recepcionista (DNS resolver) consulta el directorio interno.
* Encuentra que está en el box 412 y te da la dirección exacta.
* Tú solo necesitabas el nombre; el sistema tradujo eso a una ubicación concreta.

### Subnet
* Un aeropuerto internacional dividido en terminales: todos comparten el mismo recinto y la misma pista.
* El terminal nacional, el internacional y el de carga están separados.
* Cada uno tiene sus propios accesos y controles.
* El personal de una terminal no puede cruzar a otra sin pasar por un punto de control.
* La separación existe por seguridad, organización y flujo — no porque sean aeropuertos distintos.

## 🧠 Bonus — Nivel Bootcamp Pro

### ¿Qué es un VPC en cloud?

Un VPC (*Virtual Private Cloud*) es tu **red privada dentro de un proveedor cloud**. Cuando contratas infraestructura en AWS, Google Cloud o Azure, no estás conectando tus servidores directamente a internet — los estás poniendo dentro de una red virtual aislada que tú controlas.

Piénsalo como arrendar un piso completo en un edificio compartido: el edificio es el cloud provider, y tu piso es el VPC. Puedes decorarlo como quieras, decidir quién entra, qué puertas existen y qué ventanas dan hacia afuera.

Dentro de un VPC puedes:
- Crear **subredes** públicas y privadas
- Definir **reglas de firewall** (Security Groups)
- Controlar qué tiene acceso a internet y qué no
- Conectar múltiples VPCs entre sí si es necesario

```
VPC: 10.0.0.0/16
├── Subred pública  10.0.1.0/24  → servidor web (accesible desde internet)
├── Subred privada  10.0.2.0/24  → base de datos (solo accesible internamente)
└── Subred admin    10.0.3.0/24  → herramientas internas (acceso restringido)
```

> 💡 Todo proyecto serio en cloud vive dentro de un VPC. Si alguna vez usas AWS o GCP en el bootcamp, ya estás operando dentro de uno aunque no lo hayas configurado tú.

---

### ¿Qué es una IP privada vs pública?

Son dos tipos de dirección IP con propósitos distintos:

**IP Pública**
Es la dirección que **identifica a tu dispositivo o servidor en internet**. Es única en el mundo en un momento dado. Cuando accedes a un sitio web desde tu casa, el servidor ve tu IP pública. Cuando haces deploy de una app, le asignas una IP pública para que el mundo pueda encontrarla.

**IP Privada**
Es la dirección que **identifica a un dispositivo dentro de una red local**. No es visible desde internet — solo existe dentro de tu red doméstica, corporativa o VPC. Varios dispositivos en el mundo pueden tener la misma IP privada sin conflicto porque están en redes distintas.

| | IP Pública | IP Privada |
|---|---|---|
| **Visible desde internet** | Sí | No |
| **Única globalmente** | Sí | No |
| **Asignada por** | ISP o cloud provider | Router o configuración local |
| **Rangos comunes** | Variables | `192.168.x.x`, `10.x.x.x`, `172.16.x.x` |
| **Ejemplo de uso** | Servidor web, app deployada | Base de datos, servidor interno |

> 💡 En cloud, tu servidor web tiene una IP pública (para recibir tráfico de usuarios) y una IP privada (para comunicarse internamente con la base de datos). Las dos coexisten en el mismo servidor.

---

### ¿Qué es un Load Balancer?

Un load balancer (*balanceador de carga*) es un componente que **distribuye el tráfico entrante entre varios servidores** para que ninguno se sobrecargue.

Imagina una fila de cajas en un supermercado: si todos van a la misma caja, se forma una cola enorme. El load balancer es el empleado en la entrada que mira cuál caja está más libre y te manda para allá.

**¿Por qué se usa?**
- **Escalabilidad** — si un servidor no da abasto, agregas otro y el load balancer reparte el trabajo
- **Alta disponibilidad** — si un servidor cae, el load balancer deja de mandarle tráfico automáticamente
- **Rendimiento** — los usuarios siempre llegan al servidor más disponible en ese momento

**Cómo encaja en la arquitectura:**

```
[Usuarios en internet]
        ↓
  [Load Balancer]        ← única IP pública visible
      ↙   ↘
[Servidor 1] [Servidor 2]   ← IPs privadas, no visibles
        ↓
  [Base de datos]
```

Los usuarios siempre hablan con el load balancer — nunca saben cuántos servidores hay detrás ni cuál les respondió.

> 💡 En plataformas como Railway, Render o Heroku, el load balancer ya está incluido y configurado por defecto. En AWS se llama ELB (*Elastic Load Balancer*) y es uno de los servicios más usados.
