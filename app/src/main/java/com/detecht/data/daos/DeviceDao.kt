package com.detecht.data.daos

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.DatabaseView
import androidx.room.Query
import androidx.room.Relation
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import com.detecht.data.entities.Device
import com.detecht.data.entities.YoutubeVideo

@Dao
interface DeviceDao {
    
    @Transaction
    @Query("select * from Device where label in (:devices)")
    suspend fun getDevices(devices: List<String>): List<Device>
    
    @Transaction
    @Query("select * from Device where deviceId = :deviceId")
    suspend fun getDetailedDevice(deviceId: Int): DetailedDevice
    
    @RewriteQueriesToDropUnusedColumns
    @DatabaseView(
        """
        select * from Device
        join ShopDevice on Device.deviceId = ShopDevice.deviceId
        join Shop on Shop.shopId = ShopDevice.shopId
        """,
        viewName = "ShopDeviceView"
    )
    data class ShopDeviceView(
            val deviceId: Int,
            val name: String,
            val url: String
    )
    
    data class ShopWithDevice(
            @ColumnInfo(name = "name") val shopName: String,
            @ColumnInfo(name = "url") val deviceUrl: String,
    )
    
    data class DetailedDevice(
            val deviceId: Int,
            val label: String,
            val description: String,
            @Relation(
                parentColumn = "deviceId",
                entityColumn = "deviceId",
                entity = YoutubeVideo::class,
            ) val videos: List<YoutubeVideo>,
            @Relation(
                parentColumn = "deviceId",
                entityColumn = "deviceId",
                entity = ShopDeviceView::class,
            ) val shops: List<ShopWithDevice>
    )
    
    @Transaction
    @Query(
        """
        select * from Device where deviceId = (
            select deviceIdFk from DeviceClassification
            where classificationIdFk = :classificationId
            order by score desc
            limit 1)
    """
    )
    suspend fun getPredictedDevice(classificationId: Int): DetailedDevice
}