package com.example.concesionario_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AutoNovaApp()
            }
        }
    }
}

val AzulOscuro = Color(0xFF0A192F)
val Naranja = Color(0xFFF96302)
val Blanco = Color.White
val Antracita = Color(0xFF1E1E1E)
val GrisMetalico = Color(0xFF9AA4AE)
val AzulGrisaceo = Color(0xFF233352)

data class Vehiculo(
    val modelo: String,
    val descripcion: String,
    val precio: Long,
    val imagen: Int
)

val vehiculos = listOf(
    Vehiculo("Toyota Corolla", "Sedán cómodo para el uso diario.", 85000000, R.drawable.toyota_corolla),
    Vehiculo("Mazda 3", "Diseño moderno y conducción confortable.", 95000000, R.drawable.mazda3),
    Vehiculo("Chevrolet Onix", "Práctico y versátil para la ciudad.", 65000000, R.drawable.chevrolet_onix),
    Vehiculo("Kia Sportage", "SUV espaciosa para diferentes recorridos.", 125000000, R.drawable.kia_sportage),
    Vehiculo("Renault Duster", "SUV para diferentes tipos de trayecto.", 90000000, R.drawable.renaul_duster),
    Vehiculo("Mazda CX-5", "SUV de diseño elegante.", 135000000, R.drawable.mazda_cx5),
    Vehiculo("Suzuki Swift", "Compacto y práctico para movilizarse.", 70000000, R.drawable.suzuki_swift),
    Vehiculo("Toyota Corolla Cross", "SUV ideal para viajes y familia.", 115000000, R.drawable.corolla_cross)
)

fun formatoCOP(valor: Long): String =
    "$" + "%,d".format(valor).replace(",", ".") + " COP"

@Composable
fun AutoNovaApp() {
    var pantalla by remember { mutableStateOf("Inicio") }
    var vehiculoSeleccionado by remember { mutableStateOf<Vehiculo?>(null) }

    val fondo = when {
        vehiculoSeleccionado != null -> vehiculoSeleccionado!!.imagen
        pantalla == "Inicio" -> R.drawable.principal
        pantalla == "Inventario" -> R.drawable.inventario
        pantalla == "Financiamiento" -> R.drawable.financiamiento
        pantalla == "Registro" -> R.drawable.imagen_decorativa
        else -> R.drawable.decorativa2
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fotografía que ocupa todo el fondo
        Image(
            painter = painterResource(fondo),
            contentDescription = "Fondo de AutoNova",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Oscurece la fotografía para facilitar la lectura
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            AzulOscuro.copy(alpha = 0.82f),
                            AzulOscuro.copy(alpha = 0.93f),
                            AzulOscuro.copy(alpha = 0.98f)
                        )
                    )
                )
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "AutoNova",
                    color = Naranja,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    "Encuentra el vehículo ideal para ti",
                    color = Blanco,
                    fontSize = 14.sp
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Antracita.copy(alpha = 0.85f))
                    .horizontalScroll(rememberScrollState())
                    .padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                listOf(
                    "Inicio", "Inventario", "Login",
                    "Registro", "Ayuda", "Financiamiento"
                ).forEach { nombre ->
                    Button(
                        onClick = {
                            pantalla = nombre
                            vehiculoSeleccionado = null
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (
                                pantalla == nombre && vehiculoSeleccionado == null
                            ) Naranja else AzulGrisaceo
                        ),
                        contentPadding = PaddingValues(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        )
                    ) {
                        Text(nombre, fontSize = 12.sp)
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (vehiculoSeleccionado != null) {
                    PantallaDetalle(
                        vehiculo = vehiculoSeleccionado!!,
                        volver = { vehiculoSeleccionado = null },
                        financiar = {
                            vehiculoSeleccionado = null
                            pantalla = "Financiamiento"
                        }
                    )
                } else {
                    when (pantalla) {
                        "Inicio" -> {
                            Titulo("Tu próximo vehículo comienza aquí")
                            Texto(
                                "Explora nuestro catálogo, conoce los modelos disponibles y consulta las opciones de financiación."
                            )

                            Boton(
                                "Explorar vehículos",
                                { pantalla = "Inventario" }
                            )

                            Boton(
                                "Crear una cuenta",
                                { pantalla = "Registro" }
                            )

                            Titulo("Vehículos destacados")

                            vehiculos.take(4).forEach { auto ->
                                TarjetaVehiculo(auto) {
                                    vehiculoSeleccionado = auto
                                }
                            }
                        }

                        "Inventario" -> {
                            Titulo("Nuestro inventario")
                            Texto(
                                "Selecciona un vehículo para consultar sus detalles."
                            )

                            vehiculos.forEach { auto ->
                                TarjetaVehiculo(auto) {
                                    vehiculoSeleccionado = auto
                                }
                            }
                        }

                        "Login" -> PantallaLogin()

                        "Registro" -> PantallaRegistro()

                        "Ayuda" -> PantallaAyuda()

                        "Financiamiento" -> PantallaFinanciamiento()
                    }
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    "© 2026 AutoNova · Precios ilustrativos",
                    color = GrisMetalico,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun Titulo(texto: String) {
    Text(
        texto,
        color = Blanco,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 32.sp
    )
}

@Composable
fun Texto(texto: String) {
    Text(
        texto,
        color = Blanco,
        fontSize = 15.sp,
        lineHeight = 23.sp
    )
}

@Composable
fun Boton(texto: String, accion: () -> Unit) {
    Button(
        onClick = accion,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Naranja,
            contentColor = Blanco
        )
    ) {
        Text(texto, fontWeight = FontWeight.Bold)
    }
}

// Tarjeta seleccionable del inventario
@Composable
fun TarjetaVehiculo(
    auto: Vehiculo,
    alSeleccionar: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { alSeleccionar() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = AzulGrisaceo.copy(alpha = 0.94f)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Image(
                painter = painterResource(auto.imagen),
                contentDescription = auto.modelo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(12.dp))

            Text(
                auto.modelo,
                color = Blanco,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )

            Text(auto.descripcion, color = GrisMetalico)

            Spacer(Modifier.height(6.dp))

            Text(
                formatoCOP(auto.precio),
                color = Naranja,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Toca para ver detalles →",
                color = Blanco,
                fontSize = 13.sp
            )
        }
    }
}

// Pantalla que aparece al seleccionar un vehículo
@Composable
fun PantallaDetalle(
    vehiculo: Vehiculo,
    volver: () -> Unit,
    financiar: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Boton("← Volver al inventario", volver)

        Image(
            painter = painterResource(vehiculo.imagen),
            contentDescription = vehiculo.modelo,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(18.dp)),
            contentScale = ContentScale.Crop
        )

        Titulo(vehiculo.modelo)

        Text(
            formatoCOP(vehiculo.precio),
            color = Naranja,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Titulo("Descripción")

        Texto(vehiculo.descripcion)

        Titulo("Información del vehículo")

        Texto("Modelo: ${vehiculo.modelo}")
        Texto("Precio de referencia: ${formatoCOP(vehiculo.precio)}")
        Texto("Disponibilidad: consulta con el concesionario.")

        Boton("Calcular financiación", financiar)

        Texto(
            "Los precios y la información son demostrativos y deben confirmarse con el concesionario."
        )
    }
}

@Composable
fun Campo(
    valor: String,
    cambio: (String) -> Unit,
    etiqueta: String,
    contrasena: Boolean = false
) {
    OutlinedTextField(
        value = valor,
        onValueChange = cambio,
        label = { Text(etiqueta) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = if (contrasena) {
            PasswordVisualTransformation()
        } else {
            androidx.compose.ui.text.input.VisualTransformation.None
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Blanco,
            unfocusedTextColor = Blanco,
            focusedBorderColor = Naranja,
            unfocusedBorderColor = GrisMetalico,
            focusedLabelColor = Naranja,
            unfocusedLabelColor = GrisMetalico,
            cursorColor = Naranja
        )
    )
}

@Composable
fun TarjetaInfo(titulo: String, descripcion: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AzulGrisaceo.copy(alpha = 0.94f)
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                titulo,
                color = Naranja,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Texto(descripcion)
        }
    }
}

@Composable
fun PantallaLogin() {
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Titulo("Iniciar sesión")
        Texto("Ingresa tus datos para acceder a tu cuenta.")

        Campo(correo, { correo = it }, "Correo electrónico")
        Campo(clave, { clave = it }, "Contraseña", true)

        Boton("Ingresar") {
            mensaje = when {
                correo.isBlank() || clave.isBlank() ->
                    "Completa todos los campos."
                !correo.contains("@") ->
                    "Ingresa un correo válido."
                else ->
                    "Validación de demostración. Aún no existe conexión con un sistema de autenticación."
            }
        }

        if (mensaje.isNotBlank()) {
            TarjetaInfo("Resultado", mensaje)
        }
    }
}

@Composable
fun PantallaRegistro() {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Titulo("Crear una cuenta")
        Texto("Completa el formulario para registrarte en AutoNova.")

        Campo(nombre, { nombre = it }, "Nombre completo")
        Campo(correo, { correo = it }, "Correo electrónico")
        Campo(clave, { clave = it }, "Contraseña", true)

        Boton("Registrarme") {
            mensaje = when {
                nombre.isBlank() || correo.isBlank() || clave.isBlank() ->
                    "Completa todos los campos."
                !correo.contains("@") ->
                    "Ingresa un correo válido."
                clave.length < 6 ->
                    "La contraseña debe tener al menos 6 caracteres."
                else ->
                    "Formulario validado. Para crear la cuenta realmente se necesita un servicio de registro."
            }
        }

        if (mensaje.isNotBlank()) {
            TarjetaInfo("Resultado", mensaje)
        }
    }
}

@Composable
fun PantallaAyuda() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Titulo("Ayuda y contacto")

        TarjetaInfo(
            "Consultar vehículos",
            "Entra al inventario y selecciona un automóvil para ver sus detalles."
        )

        TarjetaInfo(
            "Financiación",
            "Utiliza la calculadora para estimar una cuota mensual sin intereses."
        )

        TarjetaInfo(
            "Registro",
            "Completa los campos de la sección Registro. Actualmente es una demostración."
        )

        TarjetaInfo(
            "Contacto",
            "Correo de ejemplo: contacto@autonova.example. No es una dirección comercial verificada."
        )
    }
}

@Composable
fun PantallaFinanciamiento() {
    var valor by remember { mutableStateOf("") }
    var inicial by remember { mutableStateOf("") }
    var meses by remember { mutableStateOf("36") }
    var resultado by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Titulo("Financiamiento")

        Texto(
            "Calcula una cuota mensual aproximada. El resultado no incluye intereses ni otros cargos."
        )

        Campo(valor, { valor = it; resultado = "" }, "Valor del vehículo en COP")
        Campo(inicial, { inicial = it; resultado = "" }, "Cuota inicial en COP")
        Campo(meses, { meses = it; resultado = "" }, "Plazo en meses")

        Boton("Calcular cuota mensual") {
            val precio = valor.toLongOrNull()
            val cuotaInicial = inicial.toLongOrNull()
            val plazo = meses.toIntOrNull()

            resultado = when {
                precio == null || cuotaInicial == null || plazo == null ->
                    "Ingresa números enteros válidos."
                precio <= 0 || cuotaInicial < 0 || plazo <= 0 ->
                    "Verifica los valores ingresados."
                cuotaInicial >= precio ->
                    "La cuota inicial debe ser menor que el precio."
                else -> {
                    val monto = precio - cuotaInicial
                    val cuota = monto / plazo
                    "Monto por financiar: ${formatoCOP(monto)}\n" +
                            "Plazo: $plazo meses\n" +
                            "Cuota mensual aproximada: ${formatoCOP(cuota)}\n\n" +
                            "Estimación sin intereses, seguros ni cargos adicionales."
                }
            }
        }

        if (resultado.isNotBlank()) {
            TarjetaInfo("Resultado de la simulación", resultado)
        }
    }
}

