package ipvc.estg.cityhelper.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime


@Entity(tableName = "note_table")
class Note(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val titulo: String,
    val descricao: String
)