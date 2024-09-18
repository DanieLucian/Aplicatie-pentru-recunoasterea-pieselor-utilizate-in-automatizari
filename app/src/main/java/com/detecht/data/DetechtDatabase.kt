package com.detecht.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.detecht.data.daos.ClassificationDao
import com.detecht.data.daos.DeviceDao.ShopDeviceView
import com.detecht.data.daos.DeviceDao
import com.detecht.data.daos.HistoryDao
import com.detecht.data.daos.HistoryDao.DeviceWithScoreView
import com.detecht.data.entities.Classification
import com.detecht.data.entities.Device
import com.detecht.data.entities.DeviceClassification
import com.detecht.data.entities.Shop
import com.detecht.data.entities.ShopDevice
import com.detecht.data.entities.YoutubeVideo

@Database(
    entities = [
        Device::class,
        Classification::class,
        DeviceClassification::class,
        YoutubeVideo::class,
        Shop::class,
        ShopDevice::class
    ],
    views = [DeviceWithScoreView::class, ShopDeviceView::class],
    version = 1
)
abstract class DetechtDatabase : RoomDatabase() {
    abstract val classifDao: ClassificationDao
    abstract val deviceDao: DeviceDao
    abstract val historyDao: HistoryDao
    
    companion object {
        @Volatile
        private var instance: DetechtDatabase? = null
        
        fun getInstance(context: Context): DetechtDatabase {
            synchronized(this) {
                return instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    DetechtDatabase::class.java,
                    "detecht_db"
                ).createFromAsset("DetechtDb.db").build().also { instance = it }
            }
        }
    }
}