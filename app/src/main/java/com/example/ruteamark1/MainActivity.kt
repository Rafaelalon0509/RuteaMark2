package com.example.ruteamark1

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Announcement
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Report
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ruteamark1.ui.theme.RuteaMark1Theme
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream

// =====================================================================
// CONSTANTES Y CONFIGURACIÓN
// =====================================================================

/**
 * Paradas fijas del sistema de transporte
 */
val paradasFijas = listOf(
    "Plaza Cristal" to GeoPoint(18.462823164660726, -97.39048813866889),
    "Cuidado con el Perro" to GeoPoint(18.462942592352054, -97.394323066223),
    "Parada Bodega" to GeoPoint(18.463245812573547, -97.40265522671137),
    "Iglesia del Carmen" to GeoPoint(18.464999567231107, -97.39378619213882),
    "El Paseo" to GeoPoint(18.467095241519797, -97.4164854348868),
    "Mercado La Purísima" to GeoPoint(18.458121372017036, -97.38512085973026),
    "CIS" to GeoPoint(18.476899105013377, -97.43865404112017),
    "Callejón de las Flores" to GeoPoint(18.461738310729668, -97.38942712250652)
)

/**
 * Lista de todas las rutas disponibles en la aplicación
 */
val todasLasRutas = listOf(
    "Ruta 1" to "ruta1.kml",
    "Ruta 2" to "ruta2.kml",
    "Ruta 3" to "ruta3.kml",
    "Ruta 4" to "ruta4.kml",
    "Ruta 5" to "ruta5.kml",
    "Ruta 6" to "ruta6.kml",
    "Ruta 7 - UPN 213 y Normal Superior" to "ruta7_upn.kml",
    "Ruta 7 - San Agustín" to "ruta7_sanagustin.kml",
    "Ruta 8" to "ruta8.kml",
    "Ruta 9" to "ruta9.kml",
    "Ruta 10" to "ruta10.kml",
    "Ruta 11" to "ruta11.kml",
    "Ruta 12" to "ruta12.kml",
    "Ruta 13" to "ruta13.kml",
    "Ruta 15" to "ruta15.kml",
    "Ruta 15 - Frailes" to "ruta15_frailes.kml",
    "Ruta 16" to "ruta16.kml",
    "Ruta 18" to "ruta18.kml",
    "Ruta 19" to "ruta19.kml",
    "Ruta 20" to "ruta20.kml",
    "Ruta 21" to "ruta21.kml",
    "Ruta 22 - Cárdenas" to "ruta22_cardenas.kml",
    "Ruta 22 - Bellavista" to "ruta22_bellavista.kml",
    "Ruta 22 - San Marcos" to "ruta22_sanmarcos.kml",
    "Ruta 23" to "ruta23.kml",
    "Ruta 23 - San Isidro" to "ruta23_sanisidro.kml",
    "Ruta 23 - Maravillas" to "ruta23_maravillas.kml",
    "Ruta 24" to "ruta24.kml",
    "Ruta 24 - Bosques" to "ruta24_bosques.kml",
    "Ruta 25 - 3 de Mayo" to "ruta25_3demayo.kml",
    "Ruta 26 - El Riego" to "ruta26_elriego.kml",
    "Ruta 27 - El Riego" to "ruta27_elriego.kml",
    "Ruta 28 - San Lorenzo" to "ruta28_sanlorenzo.kml",
    "Ruta 29 - San Lorenzo" to "ruta29_sanlorenzo.kml",
    "Ruta 30 - San Lorenzo" to "ruta30_sanlorenzo.kml",
    "Ruta 31 - FOVISSSTE" to "ruta31_fovissste.kml",
    "Ruta 32 - CBTIS" to "ruta32_cbtis.kml",
    "Ruta 32 - San Francisco" to "ruta32_sanfrancisco.kml",
    "Ruta 32 - Colosio" to "ruta32_colosio.kml",
    "Ruta 33 - Palmas" to "ruta33_palmas.kml",
    "Ruta 34 - Viveros" to "ruta34_viveros.kml",
    "Ruta 35 - San Vicente" to "ruta35_sanvicente.kml",
    "Ruta 36 - San Lorenzo" to "ruta36_sanlorenzo.kml",
    "Ruta 37 - San Isidro" to "ruta37_sanisidro.kml",
    "Ruta 38 - Santa Cruz" to "ruta38_santacruz.kml",
    "Ruta 40" to "ruta40.kml",
    "Ruta 41" to "ruta41.kml",
    "Ruta 42" to "ruta42.kml",
    "Ruta 43" to "ruta43.kml",
    "Transporte Cuayucatepec" to "transporte_cuayucatepec.kml",
    "Ruta RC" to "ruta_rc.kml",
    "Ruta RC Del Valle" to "ruta_rc_va.kml"
)

// =====================================================================
// ACTIVIDAD PRINCIPAL
// =====================================================================

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configuración de osmdroid
        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE))

        setContent {
            RuteaMark1Theme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {

                    // Pantallas principales
                    composable("login") { LoginScreen(navController) }
                    composable("menu") { MenuScreen(navController) }
                    composable("rutas") { RutasScreen(navController) }

                    // Pantallas de mapas (generadas dinámicamente)
                    todasLasRutas.forEach { (_, archivo) ->
                        composable("mapa/$archivo") { MapaRutaScreen(archivo) }
                    }

                    // Pantallas secundarias
                    composable("anuncios") { AnunciosScreen(navController) }
                    composable("quejas") { QuejasScreen(navController) }
                }
            }
        }
    }
}

// =====================================================================
// COMPONENTES REUTILIZABLES
// =====================================================================

/**
 * Botón reutilizable para las rutas
 */
@Composable
fun RutaButtonGeneral(
    nombre: String,
    archivoKml: String,
    navController: NavHostController,
    habilitada: Boolean = true
) {
    Button(
        onClick = { if (habilitada) navController.navigate("mapa/$archivoKml") },
        enabled = habilitada,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (habilitada) Color.Black else Color.Gray
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
    ) {
        Text(nombre, color = Color.White, fontSize = 18.sp)
    }
}

/**
 * Botón reutilizable para el menú principal
 */
@Composable
fun MenuButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .height(55.dp)
    ) {
        Icon(icon, contentDescription = text, tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 18.sp, color = Color.White)
    }
}

/**
 * Cápsula editable para anuncios
 */
@Composable
fun EditableCapsule(texto: String, onChange: (String) -> Unit) {
    Card(
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFEFEF)),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 60.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            TextField(
                value = texto,
                onValueChange = onChange,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                maxLines = 3,
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// =====================================================================
// PANTALLAS DE LA APLICACIÓN
// =====================================================================

/**
 * Pantalla de Login
 */
@Composable
fun LoginScreen(navController: NavHostController) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Rutea", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.Black)

            Spacer(modifier = Modifier.height(20.dp))

            Image(
                painter = painterResource(id = R.drawable.logo_rutea),
                contentDescription = "Logo Rutea",
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { navController.navigate("menu") },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp)
            ) {
                Text("Entrar", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}

/**
 * Pantalla del Menú Principal
 */
@Composable
fun MenuScreen(navController: NavHostController) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Contenido centrado
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Menú Principal",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(25.dp))

                MenuButton("Rutas", Icons.Filled.DirectionsBus) {
                    navController.navigate("rutas")
                }
                MenuButton("Anuncios", Icons.Filled.Announcement) {
                    navController.navigate("anuncios")
                }
                MenuButton("Quejas", Icons.Filled.Report) {
                    navController.navigate("quejas")
                }
            }

            // Imagen en la parte inferior
            Image(
                painter = painterResource(id = R.drawable.ubicacion),
                contentDescription = "Ilustración de ubicación",
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 40.dp)
            )
        }
    }
}

/**
 * Pantalla de Rutas Disponibles
 */
@Composable
fun RutasScreen(navController: NavHostController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "Rutas disponibles",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Botones de rutas generados dinámicamente
        items(todasLasRutas.size) { index ->
            val (nombre, archivo) = todasLasRutas[index]
            RutaButtonGeneral(nombre, archivo, navController)
        }

        // Botón volver
        item {
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Volver al menú", color = Color.White)
            }
        }
    }
}

/**
 * Pantalla de Mapa con Ruta
 */
@Composable
fun MapaRutaScreen(archivoKml: String) {
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = archivoKml,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp),
            color = Color.Black
        )

        AndroidView(
            factory = { ctx ->
                MapView(ctx).apply {
                    setMultiTouchControls(true)
                    tileProvider.tileSource = TileSourceFactory.MAPNIK
                    overlays.clear()

                    val puntos = try {
                        parseKml(ctx, archivoKml)
                    } catch (e: Exception) {
                        Log.e("KML", "Error leyendo $archivoKml: ${e.message}")
                        emptyList<GeoPoint>()
                    }

                    if (puntos.isNotEmpty()) {
                        configurarMapa(puntos)
                        agregarRutaAlMapa(puntos)
                    }

                    agregarParadasFijas()
                }
            },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )
    }
}

/**
 * Pantalla de Anuncios
 */
@Composable
fun AnunciosScreen(navController: NavHostController) {
    // Estados para los 10 anuncios editables
    val textosAnuncios = remember {
        mutableStateListOf(
            "Anuncio importante #1",
            "Información sobre rutas actualizada",
            "Promociones y avisos generales",
            "Más información relevante",
            "Aviso temporal de servicio",
            "Recordatorio de seguridad",
            "Próximos cambios en rutas",
            "Eventos especiales",
            "Horario extendido",
            "Otros anuncios importantes"
        )
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Anuncios",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Anuncios editables
            itemsIndexed(textosAnuncios) { index, texto ->
                EditableCapsule(
                    texto = texto,
                    onChange = { nuevosTexto ->
                        textosAnuncios[index] = nuevosTexto
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Volver", color = Color.White, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

/**
 * Pantalla de Quejas con almacenamiento persistente
 */
@Composable
fun QuejasScreen(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences("quejas_app", Context.MODE_PRIVATE)
    }

    // Cargar publicaciones guardadas al iniciar
    var publicaciones by remember {
        mutableStateOf(
            sharedPreferences.getStringSet("publicaciones", setOf())?.toList() ?: emptyList()
        )
    }

    var nuevaPublicacion by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Publicaciones de Quejas",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Lista de publicaciones
            LazyColumn(modifier = Modifier.weight(1f)) {
                itemsIndexed(publicaciones) { index, publicacion ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        elevation = CardDefaults.cardElevation(6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = publicacion,
                                fontSize = 16.sp,
                                color = Color.Black
                            )

                            // Botón para eliminar
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    publicaciones = publicaciones.toMutableList().apply {
                                        removeAt(index)
                                    }.also { updatedList ->
                                        guardarPublicaciones(sharedPreferences, updatedList)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("Eliminar", color = Color.White)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Campo para nueva publicación
            OutlinedTextField(
                value = nuevaPublicacion,
                onValueChange = { nuevaPublicacion = it },
                label = { Text("Escribe una nueva queja") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Gray,
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Botón agregar publicación
            Button(
                onClick = {
                    if (nuevaPublicacion.isNotBlank()) {
                        publicaciones = (publicaciones + nuevaPublicacion).also { updatedList ->
                            guardarPublicaciones(sharedPreferences, updatedList)
                        }
                        nuevaPublicacion = ""
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Agregar publicación", color = Color.White)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botón volver
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Volver al menú", color = Color.White)
            }
        }
    }
}

// =====================================================================
// FUNCIONES DE UTILIDAD
// =====================================================================

/**
 * Parsea archivos KML para obtener coordenadas
 */
fun parseKml(context: Context, fileName: String): List<GeoPoint> {
    val puntos = mutableListOf<GeoPoint>()

    context.assets.open(fileName).use { inputStream ->
        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        parser.setInput(inputStream, null)

        var eventType = parser.eventType
        var insideCoordinates = false

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG ->
                    if (parser.name.equals("coordinates", ignoreCase = true))
                        insideCoordinates = true

                XmlPullParser.TEXT ->
                    if (insideCoordinates) {
                        procesarCoordenadas(parser.text, puntos)
                    }

                XmlPullParser.END_TAG ->
                    if (parser.name.equals("coordinates", ignoreCase = true))
                        insideCoordinates = false
            }
            eventType = parser.next()
        }
    }

    return puntos
}

/**
 * Procesa texto de coordenadas del KML
 */
private fun procesarCoordenadas(coordsText: String, puntos: MutableList<GeoPoint>) {
    val coordenadas = coordsText.trim().split("\\s+".toRegex())

    coordenadas.forEach { coord ->
        val parts = coord.split(",")
        if (parts.size >= 2) {
            val lon = parts[0].toDoubleOrNull()
            val lat = parts[1].toDoubleOrNull()
            if (lat != null && lon != null) {
                puntos.add(GeoPoint(lat, lon))
            }
        }
    }
}

/**
 * Configura el mapa con el centro y zoom apropiados
 */
private fun MapView.configurarMapa(puntos: List<GeoPoint>) {
    val latitudes = puntos.map { it.latitude }
    val longitudes = puntos.map { it.longitude }
    val centerLat = (latitudes.maxOrNull()!! + latitudes.minOrNull()!!) / 2
    val centerLon = (longitudes.maxOrNull()!! + longitudes.minOrNull()!!) / 2

    controller.setZoom(14.0)
    controller.setCenter(GeoPoint(centerLat, centerLon))
}

/**
 * Agrega la ruta como polyline al mapa
 */
private fun MapView.agregarRutaAlMapa(puntos: List<GeoPoint>) {
    val polyline = Polyline().apply {
        setPoints(puntos)
        width = 8f
        color = android.graphics.Color.BLUE
    }
    overlays.add(polyline)
}

/**
 * Agrega marcadores de paradas fijas al mapa
 */
private fun MapView.agregarParadasFijas() {
    paradasFijas.forEach { (nombre, ubicacion) ->
        val marker = Marker(this).apply {
            position = ubicacion
            title = nombre
        }
        overlays.add(marker)
    }
}

/**
 * Guarda las publicaciones en SharedPreferences
 */
private fun guardarPublicaciones(sharedPreferences: android.content.SharedPreferences, publicaciones: List<String>) {
    sharedPreferences.edit()
        .putStringSet("publicaciones", publicaciones.toSet())
        .apply()
}