import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import ru.netology.nmedia.dao.MarkerPointDao
import ru.netology.nmedia.entity.MarkerPoint

@Database(entities = [MarkerPoint::class], version = 1, exportSchema = false)
abstract class MarkerDatabase : RoomDatabase() {
    abstract fun markerPointDao(): MarkerPointDao

    companion object {
        @Volatile
        private var INSTANCE: MarkerDatabase? = null

        fun getDatabase(context: Context): MarkerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MarkerDatabase::class.java,
                    "marker_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}