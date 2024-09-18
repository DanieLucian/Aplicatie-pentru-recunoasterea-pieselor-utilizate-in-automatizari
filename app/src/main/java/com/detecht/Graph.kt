package com.detecht

import android.content.Context
import com.detecht.data.DetechtDatabase
import com.detecht.ui.DetechtRepository
import com.detecht.ui.DetechtViewModel

object Graph {
    private lateinit var db:DetechtDatabase
    
    private val repository by lazy {
        DetechtRepository(
            deviceDao = db.deviceDao,
            classifDao = db.classifDao,
            historyDao = db.historyDao
        )
    }
    
    val viewModel: DetechtViewModel by lazy {
        DetechtViewModel(repository = repository)
    }
    
    fun provide(context: Context) {
        db = DetechtDatabase.getInstance(context)
    }
}