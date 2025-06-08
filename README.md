Estructura completa del AntiCheat avanzado (Spigot/Paper)
1. Paquete principal

AntiCheatPlugin.java

- Clase principal que extiende JavaPlugin.
- Inicializa todos los módulos de chequeo (movement, combat, xray, etc.).
- Registra los eventos.
- Maneja el ciclo de vida (enable, disable).

2. Módulos de detección (Listeners y Checks)

Cada módulo escucha eventos relevantes y aplica su lógica para detectar trampas:

  MovementCheck.java
  Detecta hacks relacionados con movimiento:

  - Speed hacks
	- Fly hacks
	- No fall damage
	- Teleports no autorizados

    CombatCheck.java
    Detecta hacks relacionados con combate:

  - Aimbot
	- Kill aura
	- No recoil
	- Hitbox alterations

    XrayCheck.java
    Detecta uso de rayos X:

  - Análisis de patrones de minería inusuales
	- Bloques rotos con patrón sospechoso
	- Minado a altas velocidades sin exposición

3. Sistema de sospecha y gestión de alertas

    SuspicionManager.java

	- Recibe señales de cada check.
	- Acumula puntos de sospecha por jugador.
	- Decide cuándo bloquear, alertar o sancionar.
	- Puede guardar logs o enviar alertas a administradores.

4. Logger personalizado

    Logger.java

	- Maneja los logs del plugin.
	- Permite registrar eventos importantes, sospechas y bloqueos.
	- Puede guardar en archivo o consola.

5. Configuración y ajustes

    Archivo config.yml con opciones para:

	- Sensibilidad de detección.
	- Sanciones automáticas (kick, ban).
	- Alertas a staff.
	- Exclusiones (por ejemplo, jugadores con permisos especiales).

6. Integraciones extras (opcional)

    Comandos para:

	- Revisar estado de sospecha de un jugador.
	- Listar jugadores bloqueados o en vigilancia.

    Interacción con bases de datos para guardar historial.
    Soporte para permisos y mensajes personalizables.
