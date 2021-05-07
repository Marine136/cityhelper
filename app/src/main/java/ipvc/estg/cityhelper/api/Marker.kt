package ipvc.estg.cityhelper.api

data class Marker(
    val id: Int,
    val problemType: Int,
    val descricao: String,
    val city: String,
    val address: String,
    val lat: String,
    val lng: String,
    val users_id: Int
)