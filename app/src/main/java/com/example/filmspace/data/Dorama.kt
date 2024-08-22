import java.io.Serializable

data class Dorama(
    val id: Int,
    var isFavorite: Boolean,
    val title: String,
    val posterUrl: String,
    val description: String,
    val year: Int,
    val countSeries: Int,
    val countTracks: Int,
    val listSeries: MutableList<String>,
    val listTracks: MutableList<String>
) : Serializable
