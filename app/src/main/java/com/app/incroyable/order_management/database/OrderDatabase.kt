package com.app.incroyable.order_management.database

import android.content.Context
import androidx.databinding.adapters.Converters
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.incroyable.order_management.dao.OrderDao
import com.app.incroyable.order_management.model.OrderData

@Database(entities = [OrderData::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class OrderDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: OrderDatabase? = null

        fun getDatabase(context: Context): OrderDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OrderDatabase::class.java,
                    "order_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
