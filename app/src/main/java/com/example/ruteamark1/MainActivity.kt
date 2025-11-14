package com.example.ruteamark1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue



// ----------------- PARADAS FIJAS -----------------
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

// ----------------- MAIN ACTIVITY -----------------
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // osmdroid config
        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE))

        setContent {
            RuteaMark1Theme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    // pantallas principales
                    composable("login") { LoginScreen(navController) }
                    composable("menu") { MenuScreen(navController) }
                    composable("rutas") { RutasScreen(navController) }

                    // composables de mapas (cada uno apunta a un archivo KML)
                    composable("mapa/ruta1.kml") { MapaRutaScreen("ruta1.kml") }
                    composable("mapa/ruta2.kml") { MapaRutaScreen("ruta2.kml") }
                    composable("mapa/ruta3.kml") { MapaRutaScreen("ruta3.kml") }
                    composable("mapa/ruta4.kml") { MapaRutaScreen("ruta4.kml") }
                    composable("mapa/ruta5.kml") { MapaRutaScreen("ruta5.kml") }
                    composable("mapa/ruta6.kml") { MapaRutaScreen("ruta6.kml") }
                    composable("mapa/ruta7_upn.kml") { MapaRutaScreen("ruta7_upn.kml") }
                    composable("mapa/ruta7_sanagustin.kml") { MapaRutaScreen("ruta7_sanagustin.kml") }
                    composable("mapa/ruta8.kml") { MapaRutaScreen("ruta8.kml") }
                    composable("mapa/ruta9.kml") { MapaRutaScreen("ruta9.kml") }
                    composable("mapa/ruta10.kml") { MapaRutaScreen("ruta10.kml") }
                    composable("mapa/ruta11.kml") { MapaRutaScreen("ruta11.kml") }
                    composable("mapa/ruta12.kml") { MapaRutaScreen("ruta12.kml") }
                    composable("mapa/ruta13.kml") { MapaRutaScreen("ruta13.kml") }
                    composable("mapa/ruta15.kml") { MapaRutaScreen("ruta15.kml") }
                    composable("mapa/ruta15_frailes.kml") { MapaRutaScreen("ruta15_frailes.kml") }
                    composable("mapa/ruta16.kml") { MapaRutaScreen("ruta16.kml") }
                    composable("mapa/ruta18.kml") { MapaRutaScreen("ruta18.kml") }
                    composable("mapa/ruta19.kml") { MapaRutaScreen("ruta19.kml") }
                    composable("mapa/ruta20.kml") { MapaRutaScreen("ruta20.kml") }
                    composable("mapa/ruta21.kml") { MapaRutaScreen("ruta21.kml") }
                    composable("mapa/ruta22_cardenas.kml") { MapaRutaScreen("ruta22_cardenas.kml") }
                    composable("mapa/ruta22_bellavista.kml") { MapaRutaScreen("ruta22_bellavista.kml") }
                    composable("mapa/ruta22_sanmarcos.kml") { MapaRutaScreen("ruta22_sanmarcos.kml") }
                    composable("mapa/ruta23.kml") { MapaRutaScreen("ruta23.kml") }
                    composable("mapa/ruta23_sanisidro.kml") { MapaRutaScreen("ruta23_sanisidro.kml") }
                    composable("mapa/ruta23_maravillas.kml") { MapaRutaScreen("ruta23_maravillas.kml") }
                    composable("mapa/ruta24.kml") { MapaRutaScreen("ruta24.kml") }
                    composable("mapa/ruta24_bosques.kml") { MapaRutaScreen("ruta24_bosques.kml") }
                    composable("mapa/ruta25_3demayo.kml") { MapaRutaScreen("ruta25_3demayo.kml") }
                    composable("mapa/ruta26_elriego.kml") { MapaRutaScreen("ruta26_elriego.kml") }
                    composable("mapa/ruta27_elriego.kml") { MapaRutaScreen("ruta27_elriego.kml") }
                    composable("mapa/ruta28_sanlorenzo.kml") { MapaRutaScreen("ruta28_sanlorenzo.kml") }
                    composable("mapa/ruta29_sanlorenzo.kml") { MapaRutaScreen("ruta29_sanlorenzo.kml") }
                    composable("mapa/ruta30_sanlorenzo.kml") { MapaRutaScreen("ruta30_sanlorenzo.kml") }
                    composable("mapa/ruta31_fovissste.kml") { MapaRutaScreen("ruta31_fovissste.kml") }
                    composable("mapa/ruta32_cbtis.kml") { MapaRutaScreen("ruta32_cbtis.kml") }
                    composable("mapa/ruta32_sanfrancisco.kml") { MapaRutaScreen("ruta32_sanfrancisco.kml") }
                    composable("mapa/ruta32_colosio.kml") { MapaRutaScreen("ruta32_colosio.kml") }
                    composable("mapa/ruta33_palmas.kml") { MapaRutaScreen("ruta33_palmas.kml") }
                    composable("mapa/ruta34_viveros.kml") { MapaRutaScreen("ruta34_viveros.kml") }
                    composable("mapa/ruta35_sanvicente.kml") { MapaRutaScreen("ruta35_sanvicente.kml") }
                    composable("mapa/ruta36_sanlorenzo.kml") { MapaRutaScreen("ruta36_sanlorenzo.kml") }
                    composable("mapa/ruta37_sanisidro.kml") { MapaRutaScreen("ruta37_sanisidro.kml") }
                    composable("mapa/ruta38_santacruz.kml") { MapaRutaScreen("ruta38_santacruz.kml") }
                    composable("mapa/ruta40.kml") { MapaRutaScreen("ruta40.kml") }
                    composable("mapa/ruta41.kml") { MapaRutaScreen("ruta41.kml") }
                    composable("mapa/ruta42.kml") { MapaRutaScreen("ruta42.kml") }
                    composable("mapa/ruta43.kml") { MapaRutaScreen("ruta43.kml") }
                    composable("mapa/transporte_cuayucatepec.kml") { MapaRutaScreen("transporte_cuayucatepec.kml") }
                    composable("mapa/ruta_rc.kml") { MapaRutaScreen("ruta_rc.kml") }
                    composable("mapa/ruta_rc_va.kml") { MapaRutaScreen("ruta_rc_va.kml") }

                    // pantallas secundarias
                    composable("anuncios") { AnunciosScreen(navController) }
                    composable("quejas") { QuejasScreen(navController) }
                }
            }
        }
    }
}

// ----------------- RUTAS SCREEN -----------------
@Composable
fun RutasScreen(navController: NavHostController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Rutas disponibles", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(20.dp))
        }

        // botones independientes en posición fija
        item { RutaButtonGeneral("Ruta 1", "ruta1.kml", navController) }
        item { RutaButtonGeneral("Ruta 2", "ruta2.kml", navController) }
        item { RutaButtonGeneral("Ruta 3", "ruta3.kml", navController) }
        item { RutaButtonGeneral("Ruta 4", "ruta4.kml", navController) }
        item { RutaButtonGeneral("Ruta 5", "ruta5.kml", navController) }
        item { RutaButtonGeneral("Ruta 6", "ruta6.kml", navController) }
        item { RutaButtonGeneral("Ruta 7 - UPN 213 y Normal Superior", "ruta7_upn.kml", navController) }
        item { RutaButtonGeneral("Ruta 7 - San Agustín", "ruta7_sanagustin.kml", navController) }
        item { RutaButtonGeneral("Ruta 8", "ruta8.kml", navController) }
        item { RutaButtonGeneral("Ruta 9", "ruta9.kml", navController) }
        item { RutaButtonGeneral("Ruta 10", "ruta10.kml", navController) }
        item { RutaButtonGeneral("Ruta 11", "ruta11.kml", navController) }
        item { RutaButtonGeneral("Ruta 12", "ruta12.kml", navController) }
        item { RutaButtonGeneral("Ruta 13", "ruta13.kml", navController) }
        item { RutaButtonGeneral("Ruta 15", "ruta15.kml", navController) }
        item { RutaButtonGeneral("Ruta 15 - Frailes", "ruta15_frailes.kml", navController) }
        item { RutaButtonGeneral("Ruta 16", "ruta16.kml", navController) }
        item { RutaButtonGeneral("Ruta 18", "ruta18.kml", navController) }
        item { RutaButtonGeneral("Ruta 19", "ruta19.kml", navController) }
        item { RutaButtonGeneral("Ruta 20", "ruta20.kml", navController) }
        item { RutaButtonGeneral("Ruta 21", "ruta21.kml", navController) }
        item { RutaButtonGeneral("Ruta 22 - Cárdenas", "ruta22_cardenas.kml", navController) }
        item { RutaButtonGeneral("Ruta 22 - Bellavista", "ruta22_bellavista.kml", navController) }
        item { RutaButtonGeneral("Ruta 22 - San Marcos", "ruta22_sanmarcos.kml", navController) }
        item { RutaButtonGeneral("Ruta 23", "ruta23.kml", navController) }
        item { RutaButtonGeneral("Ruta 23 - San Isidro", "ruta23_sanisidro.kml", navController) }
        item { RutaButtonGeneral("Ruta 23 - Maravillas", "ruta23_maravillas.kml", navController) }
        item { RutaButtonGeneral("Ruta 24", "ruta24.kml", navController) }
        item { RutaButtonGeneral("Ruta 24 - Bosques", "ruta24_bosques.kml", navController) }
        item { RutaButtonGeneral("Ruta 25 - 3 de Mayo", "ruta25_3demayo.kml", navController) }
        item { RutaButtonGeneral("Ruta 26 - El Riego", "ruta26_elriego.kml", navController) }
        item { RutaButtonGeneral("Ruta 27 - El Riego", "ruta27_elriego.kml", navController) }
        item { RutaButtonGeneral("Ruta 28 - San Lorenzo", "ruta28_sanlorenzo.kml", navController) }
        item { RutaButtonGeneral("Ruta 29 - San Lorenzo", "ruta29_sanlorenzo.kml", navController) }
        item { RutaButtonGeneral("Ruta 30 - San Lorenzo", "ruta30_sanlorenzo.kml", navController) }
        item { RutaButtonGeneral("Ruta 31 - FOVISSSTE", "ruta31_fovissste.kml", navController) }
        item { RutaButtonGeneral("Ruta 32 - CBTIS", "ruta32_cbtis.kml", navController) }
        item { RutaButtonGeneral("Ruta 32 - San Francisco", "ruta32_sanfrancisco.kml", navController) }
        item { RutaButtonGeneral("Ruta 32 - Colosio", "ruta32_colosio.kml", navController) }
        item { RutaButtonGeneral("Ruta 33 - Palmas", "ruta33_palmas.kml", navController) }
        item { RutaButtonGeneral("Ruta 34 - Viveros", "ruta34_viveros.kml", navController) }
        item { RutaButtonGeneral("Ruta 35 - San Vicente", "ruta35_sanvicente.kml", navController) }
        item { RutaButtonGeneral("Ruta 36 - San Lorenzo", "ruta36_sanlorenzo.kml", navController) }
        item { RutaButtonGeneral("Ruta 37 - San Isidro", "ruta37_sanisidro.kml", navController) }
        item { RutaButtonGeneral("Ruta 38 - Santa Cruz", "ruta38_santacruz.kml", navController) }
        item { RutaButtonGeneral("Ruta 40", "ruta40.kml", navController) }
        item { RutaButtonGeneral("Ruta 41", "ruta41.kml", navController) }
        item { RutaButtonGeneral("Ruta 42", "ruta42.kml", navController) }
        item { RutaButtonGeneral("Ruta 43", "ruta43.kml", navController) }
        item { RutaButtonGeneral("Transporte Cuayucatepec", "transporte_cuayucatepec.kml", navController) }
        item { RutaButtonGeneral("Ruta RC", "ruta_rc.kml", navController) }
        item { RutaButtonGeneral("Ruta RC Del Valle", "ruta_rc_va.kml", navController) }

        // botón volver al menú
        item {
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) { Text("Volver al menú", color = Color.White) }
        }
    }
}

// ----------------- FUNCION GENERAL BOTON -----------------
@Composable
fun RutaButtonGeneral(nombre: String, archivoKml: String, navController: NavHostController, habilitada: Boolean = true) {
    Button(
        onClick = { if (habilitada) navController.navigate("mapa/$archivoKml") },
        enabled = habilitada,
        colors = ButtonDefaults.buttonColors(containerColor = if (habilitada) Color.Black else Color.Gray),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().height(55.dp)
    ) { Text(nombre, color = Color.White, fontSize = 18.sp) }
}

// ----------------- MAPA RUTA SCREEN -----------------
@Composable
fun MapaRutaScreen(archivoKml: String) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = archivoKml, fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp), color = Color.Black)
        AndroidView(
            factory = { ctx ->
                MapView(ctx).apply {
                    setMultiTouchControls(true)
                    tileProvider.tileSource = TileSourceFactory.MAPNIK
                    overlays.clear()

                    val puntos = try { parseKml(ctx, archivoKml) } catch (e: Exception) {
                        Log.e("KML", "Error leyendo $archivoKml: ${e.message}")
                        emptyList<GeoPoint>()
                    }

                    if (puntos.isNotEmpty()) {
                        val latitudes = puntos.map { it.latitude }
                        val longitudes = puntos.map { it.longitude }
                        val centerLat = (latitudes.maxOrNull()!! + latitudes.minOrNull()!!) / 2
                        val centerLon = (longitudes.maxOrNull()!! + longitudes.minOrNull()!!) / 2
                        controller.setZoom(14.0)
                        controller.setCenter(GeoPoint(centerLat, centerLon))

                        val polyline = Polyline().apply { setPoints(puntos); width = 8f; color = android.graphics.Color.BLUE }
                        overlays.add(polyline)
                    }

                    paradasFijas.forEach { parada ->
                        val marker = Marker(this)
                        marker.position = parada.second
                        marker.title = parada.first
                        overlays.add(marker)
                    }
                }
            },
            modifier = Modifier.weight(1f).fillMaxWidth()
        )
    }
}

// ----------------- PARSEAR KML -----------------
fun parseKml(context: android.content.Context, fileName: String): List<GeoPoint> {
    val puntos = mutableListOf<GeoPoint>()
    val inputStream: InputStream = context.assets.open(fileName)
    val factory = XmlPullParserFactory.newInstance()
    val parser = factory.newPullParser()
    parser.setInput(inputStream, null)

    var eventType = parser.eventType
    var insideCoordinates = false

    while (eventType != XmlPullParser.END_DOCUMENT) {
        when (eventType) {
            XmlPullParser.START_TAG -> if (parser.name.equals("coordinates", ignoreCase = true)) insideCoordinates = true
            XmlPullParser.TEXT -> if (insideCoordinates) {
                val coordsText = parser.text.trim().split("\\s+".toRegex())
                coordsText.forEach { coord ->
                    val parts = coord.split(",")
                    if (parts.size >= 2) {
                        val lon = parts[0].toDoubleOrNull()
                        val lat = parts[1].toDoubleOrNull()
                        if (lat != null && lon != null) puntos.add(GeoPoint(lat, lon))
                    }
                }
            }
            XmlPullParser.END_TAG -> if (parser.name.equals("coordinates", ignoreCase = true)) insideCoordinates = false
        }
        eventType = parser.next()
    }

    inputStream.close()
    return puntos
}

// ----------------- LOGIN, MENU Y PANTALLAS SECUNDARIAS -----------------
@Composable
fun LoginScreen(navController: NavHostController) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TEXTO ARRIBA
            Text("Rutea", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.Black)

            Spacer(modifier = Modifier.height(20.dp))

            // IMAGEN
            Image(
                painter = painterResource(id = R.drawable.logo_rutea),
                contentDescription = "Logo Rutea",
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            // BOTON ENTRAR
            Button(
                onClick = { navController.navigate("menu") },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.width(200.dp).height(50.dp)
            ) {
                Text("Entrar", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}

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

@Composable
fun MenuScreen(navController: NavHostController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp)) // Margen superior pequeño

            // ---- CONTENIDO CENTRADO ----
            Column(
                modifier = Modifier.weight(1f), // Esto centra verticalmente
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

                MenuButton("Rutas", Icons.Filled.DirectionsBus) { navController.navigate("rutas") }
                MenuButton("Anuncios", Icons.Filled.Announcement) { navController.navigate("anuncios") }
                MenuButton("Quejas", Icons.Filled.Report) { navController.navigate("quejas") }
            }

            // ---- IMAGEN ABAJO ----
            Image(
                painter = painterResource(id = R.drawable.ubicacion),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom =40.dp)
            )
        }
    }
}


@Composable
fun AnunciosScreen(navController: NavHostController) {

    // Estados de texto
    var texto1 by remember { mutableStateOf("Anuncio importante #1") }
    var texto2 by remember { mutableStateOf("Información sobre rutas actualizada") }
    var texto3 by remember { mutableStateOf("Promociones y avisos generales") }
    var texto4 by remember { mutableStateOf("Más información relevante") }
    var texto5 by remember { mutableStateOf("Aviso temporal de servicio") }
    var texto6 by remember { mutableStateOf("Recordatorio de seguridad") }
    var texto7 by remember { mutableStateOf("Próximos cambios en rutas") }
    var texto8 by remember { mutableStateOf("Eventos especiales") }
    var texto9 by remember { mutableStateOf("Horario extendido") }
    var texto10 by remember { mutableStateOf("Otros anuncios importantes") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        LazyColumn(   // 👉 AGREGO SCROLL
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

            item { EditableCapsule(texto = texto1, onChange = { texto1 = it }) }
            item { EditableCapsule(texto = texto2, onChange = { texto2 = it }) }
            item { EditableCapsule(texto = texto3, onChange = { texto3 = it }) }
            item { EditableCapsule(texto = texto4, onChange = { texto4 = it }) }
            item { EditableCapsule(texto = texto5, onChange = { texto5 = it }) }
            item { EditableCapsule(texto = texto6, onChange = { texto6 = it }) }
            item { EditableCapsule(texto = texto7, onChange = { texto7 = it }) }   // ✔ corregido
            item { EditableCapsule(texto = texto8, onChange = { texto8 = it }) }
            item { EditableCapsule(texto = texto9, onChange = { texto9 = it }) }
            item { EditableCapsule(texto = texto10, onChange = { texto10 = it }) }

            item {
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Volver", color = Color.White, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}




@Composable fun QuejasScreen(navController: NavHostController) { ScreenTemplate("Quejas", navController) }

@Composable
fun ScreenTemplate(title: String, navController: NavHostController) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(modifier = Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = { navController.popBackStack() }, colors = ButtonDefaults.buttonColors(containerColor = Color.Black), shape = RoundedCornerShape(12.dp)) {
                Text("Volver", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}



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
