package com.example.ruteamark1

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ruteamark1.ui.theme.RuteaMark1Theme
import com.google.android.gms.location.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.osmdroid.config.Configuration as OsmConfig
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream





// @autor : Antony Strange
// @Description: en este proyecto esta la app de rastreo de rutas en la ciudad de tehuacan
//y asi facilitar la ubicacion de nuevas rutas o a los que son nuevos en la ciudad
//
//
//
//  IN MY RESTLESS DREAMS...











// =====================================================================
// PALETA DE COLORES MODERNA
// =====================================================================


object AppColors {
    // Azul principal (inspirado en transporte/ubicación)
    val Primary = ComposeColor(0xFF2196F3) // Azul vibrante
    val PrimaryDark = ComposeColor(0xFF1976D2)
    val PrimaryLight = ComposeColor(0xFFBBDEFB)

    // Colores secundarios
    val Secondary = ComposeColor(0xFF4CAF50) // Verde para acciones positivas
    val SecondaryDark = ComposeColor(0xFF388E3C)

    // Colores de acento
    val Accent = ComposeColor(0xFFFF9800) // Naranja para llamar la atención
    val Danger = ComposeColor(0xFFF44336) // Rojo para alertas/eliminar

    // Colores neutros
    val Background = ComposeColor(0xFFF5F7FA) // Gris azulado claro
    val Surface = ComposeColor(0xFFFFFFFF) // Blanco puro
    val TextPrimary = ComposeColor(0xFF263238) // Gris oscuro
    val TextSecondary = ComposeColor(0xFF546E7A) // Gris medio
    val Divider = ComposeColor(0xFFE0E0E0) // Gris claro

    // Colores para el mapa
    val RouteColor = ComposeColor(0xFF2196F3) // Mismo que Primary
    val LocationPin = ComposeColor(0xFFFF5252) // Rojo brillante para ubicación
}

// =====================================================================
// ESTILOS DE TEXTO
// =====================================================================

object AppTypography {
    val TitleLarge = androidx.compose.ui.text.TextStyle(
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        color = AppColors.TextPrimary
    )

    val TitleMedium = androidx.compose.ui.text.TextStyle(
        fontSize = 22.sp,
        fontWeight = FontWeight.SemiBold,
        color = AppColors.TextPrimary
    )

    val TitleSmall = androidx.compose.ui.text.TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = AppColors.TextPrimary
    )

    val BodyLarge = androidx.compose.ui.text.TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        color = AppColors.TextPrimary
    )

    val BodyMedium = androidx.compose.ui.text.TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = AppColors.TextSecondary
    )

    val ButtonText = androidx.compose.ui.text.TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = ComposeColor.White
    )
}

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
// FUNCIÓN PARA CREAR BITMAP ROJO PERSONALIZADO
// =====================================================================

/**
 * Crea un bitmap personalizado ROJO para la ubicación
 */
fun createRedLocationBitmap(context: Context): Bitmap {
    val size = 100
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    canvas.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR)

    val redPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    val whitePaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    val borderPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 4f
        isAntiAlias = true
    }

    val centerX = size / 2f
    val centerY = size / 2f
    val radius = size / 3f

    canvas.drawCircle(centerX, centerY, radius, redPaint)
    canvas.drawCircle(centerX, centerY, radius, borderPaint)
    canvas.drawCircle(centerX, centerY, radius / 3, whitePaint)

    return bitmap
}

/**
 * Convierte un Drawable a Bitmap
 */
fun drawableToBitmap(drawable: Drawable): Bitmap {
    if (drawable is BitmapDrawable && drawable.bitmap != null) {
        return drawable.bitmap
    }

    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

// =====================================================================
// OVERLAY PERSONALIZADO PARA EL TÍTULO
// =====================================================================

/**
 * Overlay personalizado que muestra el título FIJO en el mapa
 */
class TitleOverlay(private val title: String) : org.osmdroid.views.overlay.Overlay() {

    private val titlePaint = Paint().apply {
        color = Color.WHITE
        textSize = 38f
        style = Paint.Style.FILL
        isAntiAlias = true
        isFakeBoldText = true
        textAlign = Paint.Align.CENTER
    }

    private val backgroundPaint = Paint().apply {
        color = Color.parseColor("#2196F3")
        style = Paint.Style.FILL
        isAntiAlias = true
        alpha = 220
    }

    private val shadowPaint = Paint().apply {
        color = Color.argb(80, 0, 0, 0)
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    override fun draw(canvas: Canvas?, mapView: MapView?, shadow: Boolean) {
        if (canvas == null || mapView == null) return

        val x = canvas.width / 2
        val y = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) 120 else 100

        val textBounds = android.graphics.Rect()
        titlePaint.getTextBounds(title, 0, title.length, textBounds)

        val padding = 30
        val rectLeft = x - (textBounds.width() / 2) - padding
        val rectTop = y - textBounds.height() - padding
        val rectRight = x + (textBounds.width() / 2) + padding
        val rectBottom = y + padding

        // Sombra
        val shadowOffset = 3
        canvas.drawRoundRect(
            (rectLeft + shadowOffset).toFloat(),
            (rectTop + shadowOffset).toFloat(),
            (rectRight + shadowOffset).toFloat(),
            (rectBottom + shadowOffset).toFloat(),
            25f,
            25f,
            shadowPaint
        )

        // Fondo
        canvas.drawRoundRect(
            rectLeft.toFloat(),
            rectTop.toFloat(),
            rectRight.toFloat(),
            rectBottom.toFloat(),
            25f,
            25f,
            backgroundPaint
        )

        // Texto
        canvas.drawText(
            title,
            x.toFloat(),
            y.toFloat(),
            titlePaint
        )
    }
}

// =====================================================================
// CLASE PARA GESTIONAR LA UBICACIÓN
// =====================================================================

class LocationHelper(private val context: Context) {
    private var _location = MutableStateFlow<Location?>(null)
    val location = _location.asStateFlow()

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null
    private var myLocationOverlay: MyLocationNewOverlay? = null
    private var mapView: MapView? = null

    init {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    fun setupLocationOverlay(mapView: MapView) {
        this.mapView = mapView

        val locationProvider = GpsMyLocationProvider(context)

        myLocationOverlay = MyLocationNewOverlay(locationProvider, mapView).apply {
            enableMyLocation()
            enableFollowLocation()
            isDrawAccuracyEnabled = true

            try {
                val redBitmap = createRedLocationBitmap(context)
                setPersonIcon(redBitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        mapView.overlays.add(myLocationOverlay)
        mapView.invalidate()
    }

    fun checkPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun startLocationUpdates() {
        try {
            if (!checkPermissions()) {
                return
            }

            val locationRequest = LocationRequest.create().apply {
                interval = 5000
                fastestInterval = 2000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let { location ->
                        _location.update { location }
                        myLocationOverlay?.onLocationChanged(location, null)
                        mapView?.invalidate()
                    }
                }
            }

            fusedLocationClient?.requestLocationUpdates(
                locationRequest,
                locationCallback!!,
                null
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
            Toast.makeText(context, "Error de permisos: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun stopLocationUpdates() {
        try {
            locationCallback?.let {
                fusedLocationClient?.removeLocationUpdates(it)
            }
            locationCallback = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun centerOnUserLocation() {
        try {
            _location.value?.let { location ->
                mapView?.controller?.animateTo(
                    GeoPoint(location.latitude, location.longitude)
                )
            } ?: run {
                getCurrentLocation { location ->
                    location?.let {
                        mapView?.controller?.animateTo(
                            GeoPoint(it.latitude, it.longitude)
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getCurrentLocation(callback: (Location?) -> Unit) {
        try {
            if (!checkPermissions()) {
                callback(null)
                return
            }

            fusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
                callback(location)
            }?.addOnFailureListener {
                callback(null)
            }
        } catch (e: SecurityException) {
            callback(null)
        }
    }

    fun cleanup() {
        stopLocationUpdates()
        try {
            myLocationOverlay?.disableMyLocation()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        myLocationOverlay = null
        mapView = null
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
            containerColor = AppColors.Primary,
            disabledContainerColor = AppColors.Divider
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Filled.DirectionsBus,
                contentDescription = "Ruta",
                tint = ComposeColor.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                nombre,
                style = AppTypography.ButtonText,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Botón reutilizable para el menú principal
 */
@Composable
fun MenuButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = AppColors.Surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onClick)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    icon,
                    contentDescription = text,
                    tint = AppColors.Primary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Text(
                        text = text,
                        style = AppTypography.TitleSmall,
                        color = AppColors.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = when (text) {
                            "Rutas" -> "Ver todas las rutas disponibles"
                            "Anuncios" -> "Información importante"
                            "Quejas" -> "Comparte tus comentarios"
                            else -> ""
                        },
                        style = AppTypography.BodyMedium,
                        color = AppColors.TextSecondary
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    Icons.Filled.ArrowForward,
                    contentDescription = "Ir",
                    tint = AppColors.TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * Cápsula editable para anuncios
 */
@Composable
fun EditableCapsule(texto: String, onChange: (String) -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.Surface
        ),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 70.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            TextField(
                value = texto,
                onValueChange = onChange,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = ComposeColor.Transparent,
                    unfocusedContainerColor = ComposeColor.Transparent,
                    focusedIndicatorColor = ComposeColor.Transparent,
                    unfocusedIndicatorColor = ComposeColor.Transparent,
                    focusedTextColor = AppColors.TextPrimary,
                    unfocusedTextColor = AppColors.TextPrimary,
                    cursorColor = AppColors.Primary
                ),
                textStyle = AppTypography.BodyLarge,
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Botón de ubicación PERSONALIZADO
 */
@Composable
fun LocationButton(
    locationHelper: LocationHelper,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    FloatingActionButton(
        onClick = {
            if (locationHelper.checkPermissions()) {
                locationHelper.centerOnUserLocation()
            } else {
                Toast.makeText(context, "Concede permisos de ubicación primero", Toast.LENGTH_SHORT).show()
                (context as? ComponentActivity)?.let { activity ->
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ),
                        1001
                    )
                }
            }
        },
        containerColor = AppColors.LocationPin,
        modifier = modifier.size(60.dp),
        shape = CircleShape
    ) {
        Icon(
            Icons.Filled.LocationOn,
            contentDescription = "Mi ubicación",
            tint = ComposeColor.White,
            modifier = Modifier.size(28.dp)
        )
    }
}

/**
 * Función para resaltar el texto de búsqueda en los resultados
 */
@Composable
fun highlightSearchText(fullText: String, searchText: String): androidx.compose.ui.text.AnnotatedString {
    val annotatedString = buildAnnotatedString {
        val lowerFullText = fullText.lowercase()
        val lowerSearchText = searchText.lowercase()
        var currentIndex = 0

        while (currentIndex < lowerFullText.length) {
            val startIndex = lowerFullText.indexOf(lowerSearchText, currentIndex)

            if (startIndex == -1) {
                // No más coincidencias, agregar el resto del texto
                append(fullText.substring(currentIndex))
                break
            }

            // Agregar texto antes de la coincidencia
            if (startIndex > currentIndex) {
                append(fullText.substring(currentIndex, startIndex))
            }

            // Agregar texto coincidente con estilo resaltado
            withStyle(
                style = SpanStyle(
                    color = AppColors.Primary,
                    fontWeight = FontWeight.Bold,
                    background = AppColors.PrimaryLight.copy(alpha = 0.3f)
                )
            ) {
                append(fullText.substring(startIndex, startIndex + searchText.length))
            }

            currentIndex = startIndex + searchText.length
        }
    }

    return annotatedString
}

// =====================================================================
// PANTALLA DE RUTAS CON BARRA DE BÚSQUEDA - CORREGIDA
// =====================================================================

@Composable
fun RutasScreen(navController: NavHostController) {
    var searchText by remember { mutableStateOf("") }

    val filteredRoutes = remember(todasLasRutas, searchText) {
        if (searchText.isBlank()) {
            todasLasRutas
        } else {
            todasLasRutas.filter { (nombre, _) ->
                nombre.contains(searchText, ignoreCase = true) ||
                        nombre.replace(" ", "").contains(searchText.replace(" ", ""), ignoreCase = true)
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppColors.Background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Encabezado más compacto
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.Primary
                ),
                shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Rutas Disponibles",
                        style = AppTypography.TitleLarge.copy(
                            color = ComposeColor.White,
                            fontSize = 24.sp
                        ),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Text(
                        "Selecciona una ruta para verla en el mapa",
                        style = AppTypography.BodyLarge.copy(
                            color = ComposeColor.White.copy(alpha = 0.9f),
                            fontSize = 14.sp
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Barra de búsqueda
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = ComposeColor.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp)
                        ) {
                            Icon(
                                Icons.Filled.Search,
                                contentDescription = "Buscar",
                                tint = AppColors.Primary,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))

                            TextField(
                                value = searchText,
                                onValueChange = { searchText = it },
                                placeholder = {
                                    Text(
                                        "Buscar ruta por nombre o número...",
                                        style = AppTypography.BodyMedium.copy(fontSize = 14.sp),
                                        color = AppColors.TextSecondary.copy(alpha = 0.6f)
                                    )
                                },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = ComposeColor.Transparent,
                                    unfocusedContainerColor = ComposeColor.Transparent,
                                    focusedIndicatorColor = ComposeColor.Transparent,
                                    unfocusedIndicatorColor = ComposeColor.Transparent,
                                    focusedTextColor = AppColors.TextPrimary,
                                    unfocusedTextColor = AppColors.TextPrimary,
                                    cursorColor = AppColors.Primary
                                ),
                                textStyle = AppTypography.BodyLarge.copy(fontSize = 15.sp),
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )

                            if (searchText.isNotBlank()) {
                                IconButton(
                                    onClick = { searchText = "" },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Limpiar",
                                        tint = AppColors.TextSecondary
                                    )
                                }
                            }
                        }
                    }

                    Text(
                        "${filteredRoutes.size} de ${todasLasRutas.size} rutas",
                        style = AppTypography.BodyMedium.copy(
                            color = ComposeColor.White.copy(alpha = 0.8f),
                            fontSize = 11.sp
                        ),
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .align(Alignment.End)
                    )
                }
            }

            if (filteredRoutes.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 40.dp, vertical = 40.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Filled.SearchOff,
                        contentDescription = "Sin resultados",
                        tint = AppColors.TextSecondary.copy(alpha = 0.4f),
                        modifier = Modifier.size(70.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        "No se encontraron rutas",
                        style = AppTypography.TitleMedium.copy(fontSize = 18.sp),
                        color = AppColors.TextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Intenta buscar con otras palabras",
                        style = AppTypography.BodyMedium,
                        color = AppColors.TextSecondary.copy(alpha = 0.7f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { searchText = "" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.Primary
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.width(200.dp)
                    ) {
                        Text("Mostrar todas")
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
                ) {
                    itemsIndexed(filteredRoutes) { index, (nombre, archivo) ->
                        val highlightedText = if (searchText.isNotBlank()) {
                            highlightSearchText(nombre, searchText)
                        } else {
                            androidx.compose.ui.text.AnnotatedString(nombre)
                        }

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = AppColors.Surface
                            ),
                            elevation = CardDefaults.cardElevation(2.dp),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { navController.navigate("mapa/$archivo") }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp)
                            ) {
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = AppColors.PrimaryLight
                                    ),
                                    shape = CircleShape,
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            Icons.Filled.DirectionsBus,
                                            contentDescription = "Ruta",
                                            tint = AppColors.Primary,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = highlightedText,
                                        style = AppTypography.BodyLarge.copy(
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        color = AppColors.TextPrimary,
                                        maxLines = 2,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                    if (searchText.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            "Toca para ver en el mapa",
                                            style = AppTypography.BodyMedium.copy(
                                                fontSize = 11.sp
                                            ),
                                            color = AppColors.TextSecondary
                                        )
                                    }
                                }
                                Icon(
                                    Icons.Filled.ArrowForward,
                                    contentDescription = "Ver ruta",
                                    tint = AppColors.TextSecondary.copy(alpha = 0.7f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}

// =====================================================================
// ACTIVIDAD PRINCIPAL - CORREGIDA
// =====================================================================

class MainActivity : ComponentActivity() {
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        OsmConfig.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE))

        setContent {
            RuteaMark1Theme {
                val navController = rememberNavController()
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentScreen = currentBackStackEntry?.destination?.route ?: "login"

                // Contenido principal sin barra de navegación
                NavHost(
                    navController = navController,
                    startDestination = "login",
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable("login") { LoginScreen(navController) }
                    composable("menu") { MenuScreen(navController) }
                    composable("rutas") { RutasScreen(navController) }
                    todasLasRutas.forEach { (_, archivo) ->
                        composable("mapa/$archivo") {
                            MapaRutaScreen(archivo, navController)
                        }
                    }
                    composable("anuncios") { AnunciosScreen(navController) }
                    composable("quejas") { QuejasScreen(navController) }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permiso de ubicación concedido", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        this,
                        "Permiso de ubicación denegado. Algunas funciones estarán limitadas",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}

// =====================================================================
// PANTALLA DE LOGIN - CORREGIDA
// =====================================================================

@Composable
fun LoginScreen(navController: NavHostController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppColors.Background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.Surface
                ),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .width(260.dp)
                    .height(260.dp)
                    .padding(bottom = 30.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_rutea),
                        contentDescription = "Logo Rutea",
                        modifier = Modifier.size(120.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "RUTEA",
                        style = AppTypography.TitleLarge.copy(
                            color = AppColors.Primary,
                            fontSize = 32.sp
                        ),
                        letterSpacing = 1.5.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        "Tu guía de transporte",
                        style = AppTypography.BodyMedium.copy(
                            color = AppColors.TextSecondary,
                            fontSize = 13.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { navController.navigate("menu") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.Primary,
                    contentColor = ComposeColor.White
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "COMENZAR",
                        style = AppTypography.ButtonText.copy(fontSize = 16.sp),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Icon(
                        Icons.Filled.ArrowForward,
                        contentDescription = "Comenzar",
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.PrimaryLight.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "¿Necesitas ayuda?",
                        style = AppTypography.TitleSmall.copy(fontSize = 16.sp),
                        color = AppColors.Primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Usa la barra de búsqueda para encontrar rutas rápidamente",
                        style = AppTypography.BodyMedium.copy(fontSize = 12.sp),
                        color = AppColors.TextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            Text(
                "© 2024 Rutea App",
                style = AppTypography.BodyMedium.copy(
                    color = AppColors.TextSecondary,
                    fontSize = 11.sp
                ),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
    }
}

// =====================================================================
// PANTALLA DEL MENÚ PRINCIPAL - CORREGIDA
// =====================================================================

@Composable
fun MenuScreen(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.Primary
                ),
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Bienvenido a",
                        style = AppTypography.BodyLarge.copy(
                            color = ComposeColor.White.copy(alpha = 0.9f),
                            fontSize = 14.sp
                        )
                    )
                    Text(
                        "RUTEA",
                        style = AppTypography.TitleLarge.copy(
                            color = ComposeColor.White,
                            fontSize = 28.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
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

            // Consejo útil - AHORA SE VE COMPLETO
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.Surface
                ),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Icon(
                            Icons.Filled.Info,
                            contentDescription = "Información",
                            tint = AppColors.Primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Consejo útil",
                            style = AppTypography.TitleSmall.copy(fontSize = 16.sp),
                            color = AppColors.Primary
                        )
                    }
                    Text(
                        "Usa la barra de búsqueda para encontrar rutas rápidamente",
                        style = AppTypography.BodyMedium.copy(fontSize = 13.sp),
                        color = AppColors.TextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }
}

// =====================================================================
// FUNCIÓN PARA PARSEAR KML
// =====================================================================

fun parseKml(context: Context, fileName: String): List<GeoPoint> {
    val puntos = mutableListOf<GeoPoint>()

    try {
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
                            val coordsText = parser.text.trim()
                            val coordenadas = coordsText.split("\\s+".toRegex())

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

                    XmlPullParser.END_TAG ->
                        if (parser.name.equals("coordinates", ignoreCase = true))
                            insideCoordinates = false
                }
                eventType = parser.next()
            }
        }
    } catch (e: Exception) {
        Log.e("KML", "Error leyendo $fileName: ${e.message}")
    }

    return puntos
}

// =====================================================================
// PANTALLA DE MAPA CON RUTA
// =====================================================================

@Composable
fun MapaRutaScreen(archivoKml: String, navController: NavHostController) {
    val context = LocalContext.current
    val locationHelper = remember { LocationHelper(context) }
    var hasLocationPermission by remember { mutableStateOf(false) }

    val nombreRuta = remember(archivoKml) {
        val rawName = archivoKml.substringBeforeLast(".").replace("_", " ").replace(".kml", "")

        val formatted = rawName
            .replace("ruta", "Ruta ")
            .replace("upn", "UPN")
            .replace("san", "San ")
            .replace("el ", "El ")
            .replace("la ", "La ")
            .replace("del ", "Del ")
            .replace("rc", "RC")
            .replace("cbtis", "CBTIS")
            .replace("fovissste", "FOVISSSTE")
            .replace("transporte", "Transporte ")
            .trim()

        if (formatted.length > 35) formatted.substring(0, 32) + "..." else formatted
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    if (hasLocationPermission) {
                        locationHelper.startLocationUpdates()
                    }
                }
                Lifecycle.Event.ON_PAUSE -> {
                    locationHelper.stopLocationUpdates()
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            locationHelper.cleanup()
        }
    }

    LaunchedEffect(Unit) {
        hasLocationPermission = locationHelper.checkPermissions()
        if (hasLocationPermission) {
            locationHelper.startLocationUpdates()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                MapView(ctx).apply {
                    setMultiTouchControls(true)
                    tileProvider.tileSource = TileSourceFactory.MAPNIK
                    overlays.clear()

                    val titleOverlay = TitleOverlay(nombreRuta)
                    overlays.add(titleOverlay)

                    val puntos = parseKml(ctx, archivoKml)

                    if (puntos.isNotEmpty()) {
                        val latitudes = puntos.map { it.latitude }
                        val longitudes = puntos.map { it.longitude }
                        val centerLat = (latitudes.maxOrNull()!! + latitudes.minOrNull()!!) / 2
                        val centerLon = (longitudes.maxOrNull()!! + longitudes.minOrNull()!!) / 2

                        controller.setZoom(14.0)
                        controller.setCenter(GeoPoint(centerLat, centerLon))

                        val polyline = Polyline().apply {
                            setPoints(puntos)
                            width = 10f
                            color = Color.parseColor("#2196F3")
                        }
                        overlays.add(polyline)
                    }

                    paradasFijas.forEach { (nombre, ubicacion) ->
                        val marker = Marker(this).apply {
                            position = ubicacion
                            title = nombre
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        }
                        overlays.add(marker)
                    }

                    if (hasLocationPermission) {
                        locationHelper.setupLocationOverlay(this)
                    }
                }
            },
            update = { mapView ->
                if (hasLocationPermission) {
                    locationHelper.setupLocationOverlay(mapView)
                    locationHelper.startLocationUpdates()
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        LocationButton(
            locationHelper = locationHelper,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 90.dp)
        )

        FloatingActionButton(
            onClick = { navController.popBackStack() },
            containerColor = AppColors.Surface,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 20.dp, top = 100.dp)
                .size(50.dp),
            shape = CircleShape
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = AppColors.Primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// =====================================================================
// PANTALLAS DE ANUNCIOS Y QUEJAS - CORREGIDAS
// =====================================================================

@Composable
fun AnunciosScreen(navController: NavHostController) {
    val textosAnuncios = remember {
        mutableStateListOf(
            "🚌 Nueva ruta express al centro",
            "⏰ Horarios extendidos en fin de semana",
            "🎉 Promoción: 10% descuento con la app",
            "🚧 Desvío temporal en Av. Principal",
            "📢 Mantente informado de cambios",
            "💳 Nuevo sistema de pago digital",
            "🔄 Rutas optimizadas para mejor servicio",
            "🌧️ Servicio garantizado en lluvia",
            "👥 Viaja seguro, respeta el espacio",
            "⭐ Califica nuestro servicio"
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.Accent
                ),
                shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Anuncios Importantes",
                        style = AppTypography.TitleLarge.copy(
                            color = ComposeColor.White,
                            fontSize = 28.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Información actualizada del servicio",
                        style = AppTypography.BodyLarge.copy(
                            color = ComposeColor.White.copy(alpha = 0.9f)
                        )
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 20.dp)
            ) {
                itemsIndexed(textosAnuncios) { index, texto ->
                    EditableCapsule(
                        texto = texto,
                        onChange = { nuevosTexto ->
                            textosAnuncios[index] = nuevosTexto
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(30.dp))

                    Button(
                        onClick = { navController.popBackStack() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.Primary
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = "Volver",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Volver al menú", style = AppTypography.ButtonText)
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
fun QuejasScreen(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences("quejas_app", Context.MODE_PRIVATE)
    }

    var publicaciones by remember {
        mutableStateOf(
            sharedPreferences.getStringSet("publicaciones", setOf())?.toList() ?: emptyList()
        )
    }

    var nuevaPublicacion by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.Surface
                ),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "📢 Comentarios y Sugerencias",
                        style = AppTypography.TitleMedium,
                        color = AppColors.TextPrimary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Tu opinión nos ayuda a mejorar el servicio",
                        style = AppTypography.BodyMedium,
                        color = AppColors.TextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(publicaciones) { index, publicacion ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = AppColors.Surface
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    Icons.Filled.Report,
                                    contentDescription = "Comentario",
                                    tint = AppColors.TextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = publicacion,
                                    style = AppTypography.BodyLarge,
                                    color = AppColors.TextPrimary,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    publicaciones = publicaciones.toMutableList().apply {
                                        removeAt(index)
                                    }.also { updatedList ->
                                        sharedPreferences.edit()
                                            .putStringSet("publicaciones", updatedList.toSet())
                                            .apply()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AppColors.Danger
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Filled.ArrowBack,
                                        contentDescription = "Eliminar",
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Eliminar", style = AppTypography.ButtonText.copy(fontSize = 14.sp))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = AppColors.Surface
                ),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "Escribe tu comentario o sugerencia:",
                        style = AppTypography.BodyLarge,
                        color = AppColors.TextPrimary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = nuevaPublicacion,
                        onValueChange = { nuevaPublicacion = it },
                        label = {
                            Text(
                                "Ej: El conductor fue muy amable",
                                style = AppTypography.BodyMedium
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = ComposeColor.Transparent,
                            unfocusedContainerColor = ComposeColor.Transparent,
                            focusedLabelColor = AppColors.Primary,
                            unfocusedLabelColor = AppColors.TextSecondary,
                            focusedTextColor = AppColors.TextPrimary,
                            unfocusedTextColor = AppColors.TextPrimary,
                            cursorColor = AppColors.Primary,
                            focusedIndicatorColor = AppColors.Primary,
                            unfocusedIndicatorColor = AppColors.Divider
                        ),
                        maxLines = 3,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (nuevaPublicacion.isNotBlank()) {
                                publicaciones = (publicaciones + nuevaPublicacion).also { updatedList ->
                                    sharedPreferences.edit()
                                        .putStringSet("publicaciones", updatedList.toSet())
                                        .apply()
                                }
                                nuevaPublicacion = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.Secondary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Filled.ArrowForward,
                                contentDescription = "Agregar",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Agregar comentario", style = AppTypography.ButtonText)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedButton(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = AppColors.TextSecondary
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 1.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Volver",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Volver al menú", style = AppTypography.ButtonText.copy(color = AppColors.TextSecondary))
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}