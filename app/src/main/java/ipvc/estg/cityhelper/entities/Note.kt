package ipvc.estg.cityhelper.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime


@Entity(tableName = "note_table")
class Note(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val titulo: String,
    val descricao: String
)